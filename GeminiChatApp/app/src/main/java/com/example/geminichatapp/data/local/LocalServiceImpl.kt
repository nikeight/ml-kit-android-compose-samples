package com.example.geminichatapp.data.local

import com.example.geminichatapp.data.local.db.ChannelDao
import com.example.geminichatapp.data.local.db.ChatDao
import com.example.geminichatapp.data.model.ChannelEntity
import com.example.geminichatapp.data.model.ChannelWithMessage
import com.example.geminichatapp.data.model.MessageEntity
import kotlinx.coroutines.flow.Flow
import java.util.UUID
import javax.inject.Inject

class LocalServiceImpl @Inject constructor(
    private val channelDao: ChannelDao,
    private val chatDao: ChatDao,
) : LocalService {

    override suspend fun fetchChannels(): Flow<List<ChannelWithMessage>> {
        return channelDao.fetchAllChannels()
    }

    override suspend fun loadChats(id : UUID): Flow<List<MessageEntity>> {
        return chatDao.loadChats(id)
    }

    override suspend fun loadHistory(): List<MessageEntity?> {
        return chatDao.loadHistory()
    }

    override suspend fun addMessage(messageEntity: MessageEntity) {
        return chatDao.addMessage(messageEntity)
    }

    override suspend fun createChannel(channelEntity: ChannelEntity) {
        return channelDao.createChannel(channelEntity)
    }

    override suspend fun updateChannel(channelEntity: ChannelEntity) {
        return channelDao.updateChannel(channelEntity)
    }
}