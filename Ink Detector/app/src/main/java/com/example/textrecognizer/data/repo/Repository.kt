package com.example.textrecognizer.data.repo

import com.example.textrecognizer.data.model.SavedItemEntity
import kotlinx.coroutines.flow.Flow

interface Repository {
    suspend fun saveItemToDb(entity: SavedItemEntity)
    suspend fun fetchAllItemsFromDb(): Flow<List<SavedItemEntity>>
    suspend fun fetchItemDetailsFromWeb(): SavedItemEntity
    suspend fun recognizeInk(): String
    suspend fun downloadModel(): Boolean
    suspend fun deleteExistingInkModel(): Boolean
}