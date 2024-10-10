package com.example.geminichatapp.data.repo

import com.example.geminichatapp.data.model.ChannelEntity
import com.example.geminichatapp.data.model.ChannelWithMessage
import com.example.geminichatapp.data.model.MessageEntity
import com.example.geminichatapp.features.chat.Message
import kotlinx.coroutines.flow.Flow
import java.util.UUID

interface Repository {
    suspend fun fetchChannels(): Flow<List<ChannelWithMessage>>
    suspend fun createChannel(channelEntity: ChannelEntity)
    suspend fun updateChannel(channelEntity: ChannelEntity)
    suspend fun fetchAllHistory(id: UUID): Flow<List<Message>?>
    suspend fun sendMessage(messageEntity: MessageEntity) : Flow<MessageState>
    suspend fun sendImagePrompt(messageEntity: MessageEntity) : Flow<MessageState>
}