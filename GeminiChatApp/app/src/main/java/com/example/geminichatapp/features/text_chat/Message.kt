package com.example.geminichatapp.features.text_chat

import com.example.geminichatapp.data.model.Participant
import java.util.Date
import java.util.UUID

data class Message(
    val id: Int = 0,
    val time: String,
    val date: Date,
    val images: List<String>? = null,
    val message: String,
    val byWhom: Participant,
    val foreignChannelId: UUID
)


