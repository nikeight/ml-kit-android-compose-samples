package com.example.geminichatapp.data.repo

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.geminichatapp.data.local.LocalService
import com.example.geminichatapp.data.model.ChannelEntity
import com.example.geminichatapp.data.model.ChannelWithMessage
import com.example.geminichatapp.data.model.MessageEntity
import com.example.geminichatapp.data.model.Participant
import com.example.geminichatapp.data.model.toEntity
import com.example.geminichatapp.data.model.toMessage
import com.example.geminichatapp.data.remote.INetworkService
import com.example.geminichatapp.data.util.UriToBitmapConverter
import com.example.geminichatapp.features.text_chat.Message
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.util.UUID
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val networkService: INetworkService,
    private val localService: LocalService,
    private val uriToBitmapConverter: UriToBitmapConverter
) : Repository {

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
    override suspend fun sendMessage(messageEntity: MessageEntity) = flow {
        // Cache the User Message to the history as well
        localService.addMessage(messageEntity)

        emit(MessageState.Loading)

        val historyUpTo = localService.loadHistory(messageEntity.foreignChannelId)
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
                emit(MessageState.Success)
                localService.addMessage(
                    Message(
                        time = messageEntity.time,
                        date = messageEntity.date,
                        images = emptyList(),
                        byWhom = Participant.MODEL,
                        message = successResponse,
                        foreignChannelId = messageEntity.foreignChannelId,
                    ).toEntity()
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
    override suspend fun sendImagePrompt(
        messageEntity: MessageEntity,
    ) = flow {
        // Cache the User Message to the history as well
        localService.addMessage(
            messageEntity,
        )

        emit(MessageState.Loading)

        val bitmaps = withContext(Dispatchers.Default) {
            uriToBitmapConverter.convert(
                messageEntity.images ?: emptyList(),
            )
        }

        val content = content {
            bitmaps?.forEach { bitmap ->
                bitmap?.let {
                    image(it)
                }
            }
            text(messageEntity.message)
        }

        try {
            val imageGptResponse = networkService.sendImagePrompt(content)

            /**
             * Avoiding to save images to the Model Response,
             * Not so required not only in UI but for
             * Fetching history as well.
             */
            imageGptResponse.text?.let { response ->
                emit(MessageState.Success)
                localService.addMessage(
                    messageEntity.copy(
                        message = response,
                        byWhom = Participant.MODEL,
                        images = emptyList()
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
}