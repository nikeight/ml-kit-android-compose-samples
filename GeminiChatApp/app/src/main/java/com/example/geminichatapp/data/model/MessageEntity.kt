package com.example.geminichatapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date
import java.util.UUID

@Entity(tableName = "Message")
data class MessageEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val time: String,
    val date: Date,
    val images: List<String>? = null,
    val message: String,
    val byWhom: Participant,
    val foreignChannelId: UUID,
)

enum class Participant {
    USER, MODEL, ERROR
}