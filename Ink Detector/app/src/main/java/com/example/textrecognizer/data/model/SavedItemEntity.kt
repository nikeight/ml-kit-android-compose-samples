package com.example.textrecognizer.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity
data class SavedItemEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val data: String,
    val date: Date
)
