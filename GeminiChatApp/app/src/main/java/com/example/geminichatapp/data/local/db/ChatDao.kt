package com.example.geminichatapp.data.local.db

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.geminichatapp.data.model.MessageEntity
import kotlinx.coroutines.flow.Flow
import java.util.UUID

@Dao
interface ChatDao {

    /**
     * For the application level feature, we wanted to showcase
     * everything
     * Chats, Images and outputs.
     */
    @Query("SELECT * FROM message WHERE foreignChannelId = :id")
    fun loadChats(id: UUID): Flow<List<MessageEntity>>

    /**
     * For Chat feature we don't want history which
     * includes images.
     * Also belongs to the same channel id
     */
    @Query("SELECT * FROM message WHERE images is NULL AND foreignChannelId =:channelId")
    suspend fun loadHistory(channelId : UUID): List<MessageEntity?>

    @Upsert
    suspend fun addMessage(messageEntity: MessageEntity)
}