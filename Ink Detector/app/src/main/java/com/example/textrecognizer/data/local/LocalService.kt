package com.example.textrecognizer.data.local

import com.example.textrecognizer.data.model.SavedItemEntity
import kotlinx.coroutines.flow.Flow

interface LocalService {
    suspend fun saveNewItem(savedItemEntity: SavedItemEntity)
    suspend fun fetchAllSavedItem(): Flow<List<SavedItemEntity>>
    suspend fun recognizeInk(): String
    suspend fun downloadModel(): Boolean
    suspend fun deleteExistingInkModel(): Boolean
}