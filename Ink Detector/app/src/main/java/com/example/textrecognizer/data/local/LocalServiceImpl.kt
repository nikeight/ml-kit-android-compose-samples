package com.example.textrecognizer.data.local

import com.example.textrecognizer.data.local.db.SavedItemDao
import com.example.textrecognizer.data.model.SavedItemEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalServiceImpl @Inject constructor(
    private val savedItemDao: SavedItemDao
) : LocalService {
    override suspend fun saveNewItem(savedItemEntity: SavedItemEntity) {
        TODO("Not yet implemented")
    }

    override suspend fun fetchAllSavedItem(): Flow<List<SavedItemEntity>> {
        TODO("Not yet implemented")
    }

    override suspend fun recognizeInk(): String {
        TODO("Not yet implemented")
    }

    override suspend fun downloadModel(): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun deleteExistingInkModel(): Boolean {
        TODO("Not yet implemented")
    }

}