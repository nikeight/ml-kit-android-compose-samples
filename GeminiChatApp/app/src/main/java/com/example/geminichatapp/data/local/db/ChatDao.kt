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
     * includes images
     */
    @Query("SELECT * FROM message WHERE images is NULL")
    suspend fun loadHistory(): List<MessageEntity?>

    @Upsert
    suspend fun addMessage(messageEntity: MessageEntity)
}