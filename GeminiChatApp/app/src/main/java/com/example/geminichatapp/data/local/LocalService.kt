package com.example.geminichatapp.data.local

import com.example.geminichatapp.data.model.ChannelEntity
import com.example.geminichatapp.data.model.ChannelWithMessage
import com.example.geminichatapp.data.model.MessageEntity
import kotlinx.coroutines.flow.Flow
import java.util.UUID

// DATABASE : CACHE
// LOCAL DATA
interface LocalService {
    suspend fun fetchChannels() : Flow<List<ChannelWithMessage>>
    suspend fun loadChats(id : UUID) : Flow<List<MessageEntity>>
    suspend fun loadHistory(channelId : UUID) : List<MessageEntity?>
    suspend fun addMessage(messageEntity: MessageEntity)
    suspend fun createChannel(channelEntity: ChannelEntity)
    suspend fun updateChannel(channelEntity: ChannelEntity)
}