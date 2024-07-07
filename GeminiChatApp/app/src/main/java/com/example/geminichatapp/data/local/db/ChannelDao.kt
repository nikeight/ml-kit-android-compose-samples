package com.example.geminichatapp.data.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Upsert
import com.example.geminichatapp.data.model.ChannelEntity
import com.example.geminichatapp.data.model.ChannelWithMessage
import kotlinx.coroutines.flow.Flow

@Dao
interface ChannelDao {

    @Query(
        "SELECT channelId, message, date, images, byWhom FROM channel, message " +
                "WHERE channelId = foreignChannelId AND messageId = id"
    )
    fun fetchAllChannels(): Flow<List<ChannelWithMessage>>

    @Insert
    suspend fun createChannel(channelEntity: ChannelEntity)

    @Upsert
    suspend fun updateChannel(channelEntity: ChannelEntity)
}