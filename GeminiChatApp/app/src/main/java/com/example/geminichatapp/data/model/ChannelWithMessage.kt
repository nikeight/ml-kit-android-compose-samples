package com.example.geminichatapp.data.model

import java.util.Date
import java.util.UUID

data class ChannelWithMessage(
    val channelId: UUID,
    val message: String,
    val date: Date,
    val images: List<String>? = null,
    val byWhom: Participant = Participant.MODEL
)
