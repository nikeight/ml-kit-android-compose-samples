package com.example.geminichatapp.data.repo

import com.example.geminichatapp.data.model.ChannelEntity
import com.example.geminichatapp.data.model.ChannelWithMessage
import com.example.geminichatapp.data.model.MessageEntity
import kotlinx.coroutines.flow.Flow
import java.util.UUID

interface Repository {
    suspend fun fetchChannels(): Flow<List<ChannelWithMessage>>
    suspend fun createChannel(channelEntity: ChannelEntity)
    suspend fun updateChannel(channelEntity: ChannelEntity)
    suspend fun fetchAllHistory(id: UUID): Flow<List<MessageEntity>?>
    suspend fun sendMessage(messageEntity: MessageEntity)
    suspend fun sendImagePrompt(messageEntity: MessageEntity)
}