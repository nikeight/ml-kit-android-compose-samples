package com.example.geminichatapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date
import java.util.UUID

@Entity(tableName = "Channel")
data class ChannelEntity(
    @PrimaryKey
    val channelId: UUID,
    val messageId: Int? = null,
    val lastUpdateDate: Date? = null,
)

