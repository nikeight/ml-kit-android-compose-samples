package com.example.geminichatapp.data.repo

import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import coil.size.Precision
import com.example.geminichatapp.data.local.LocalService
import com.example.geminichatapp.data.model.ChannelEntity
import com.example.geminichatapp.data.model.ChannelWithMessage
import com.example.geminichatapp.data.model.MessageEntity
import com.example.geminichatapp.data.model.Participant
import com.example.geminichatapp.data.model.toEntity
import com.example.geminichatapp.data.model.toMessage
import com.example.geminichatapp.data.remote.INetworkService
import com.example.geminichatapp.features.text_chat.Message
import com.google.ai.client.generativeai.type.content
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import java.util.UUID
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val networkService: INetworkService,
    private val localService: LocalService,
    @ApplicationContext private val activityContext: Context,
) : Repository {

    private val imageRequestBuilder = ImageRequest.Builder(activityContext)
    private val imageLoader = ImageLoader.Builder(activityContext).build()

    override suspend fun fetchChannels(): Flow<List<ChannelWithMessage>> {
        return localService.fetchChannels().map {
            val uniqueItem = it.distinctBy { channelMsg ->
                channelMsg.channelId
            }
            uniqueItem
        }.distinctUntilChanged()
    }

    override suspend fun createChannel(channelEntity: ChannelEntity) {
        return localService.createChannel(channelEntity)
    }

    override suspend fun updateChannel(channelEntity: ChannelEntity) {
        return localService.updateChannel(channelEntity)
    }

    override suspend fun fetchAllHistory(id: UUID): Flow<List<Message>?> {
        return localService.loadChats(id).map { msgEntityList ->
            msgEntityList.map { msgEntity ->
                msgEntity.toMessage()
            }
        }
    }

    /**
     * Fetches the local history first
     * Get the response from the GPT
     * Save to the DB the new message
     */
    override suspend fun sendMessage(messageEntity: MessageEntity): Flow<MessageState> = flow {
        // Cache the User Message to the history as well
        localService.addMessage(messageEntity)

        // Creating the Loading effect First
        val newGeneratedMessage = Message(
            time = messageEntity.time,
            date = messageEntity.date,
            images = messageEntity.images,
            byWhom = Participant.MODEL,
            message = "Generating",
            foreignChannelId = messageEntity.foreignChannelId,
        )

        emit(MessageState.Loading)


        // Todo : Take History based on the channels only
        // Not all the data
        val historyUpTo = localService.loadHistory()
        try {
            val gptResponseWithContext = networkService.sendMessage(
                messageContent = messageEntity.message,
                historyContent = historyUpTo.mapNotNull { newMessage ->
                    newMessage?.let {
                        return@mapNotNull content(
                            role = if (it.byWhom == Participant.USER) "user" else "model"
                        ) {
                            text(it.message)
                        }
                    }
                }
            )

            // Cache the Model message to the local DB
            gptResponseWithContext.text?.let { successResponse ->
                emit(
                    MessageState.Success,
                )

                localService.addMessage(
                    newGeneratedMessage.toEntity().copy(
                        images = emptyList(),
                        message = successResponse
                    )
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emit(
                MessageState.Failure,
            )
        }
    }

    /**
     * Fetches the local history first
     * Get the response from the GPT
     * Save to the DB the new message
     */
    @RequiresApi(Build.VERSION_CODES.P)
    override suspend fun sendImagePrompt(messageEntity: MessageEntity) {
        // Cache the User Message to the history as well
        localService.addMessage(
            messageEntity,
        )

        val bitmaps = messageEntity.images?.map {
            val imageRequest = imageRequestBuilder
                .data(Uri.parse(it))
                .size(size = 768)
                .precision(Precision.EXACT)
                .build()
            try {
                val result = imageLoader.execute(imageRequest)
                if (result is SuccessResult) {
                    return@map (result.drawable as BitmapDrawable).bitmap
                } else {
                    return@map null
                }
            } catch (e: Exception) {
                return@map null
            }
        }
        val content = content {
            bitmaps?.forEach { bitmap ->
                bitmap?.let {
                    image(it)
                }
            }
            text(messageEntity.message)
        }
        val imageGptResponse = networkService.sendImagePrompt(content)

        /**
         * We want to add images to the Model response as well, to filter it out later
         * As when we provide a history to the TextChat Model, we will exclude the things which was
         * related to images.
         * Filter is happening in the UI Layer
         */
        imageGptResponse.text?.let { response ->
            localService.addMessage(
                messageEntity.copy(
                    message = response,
                    byWhom = Participant.MODEL
                )
            )
        }
    }
}