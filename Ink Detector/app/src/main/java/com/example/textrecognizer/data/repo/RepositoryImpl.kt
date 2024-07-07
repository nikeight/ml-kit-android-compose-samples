package com.example.textrecognizer.data.repo

import android.content.Context
import com.example.textrecognizer.data.local.LocalService
import com.example.textrecognizer.data.model.SavedItemEntity
import com.example.textrecognizer.data.remote.NetworkService
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val networkService: NetworkService,
    private val localService: LocalService,
    @ApplicationContext private val activityContext: Context,
) : Repository {
    override suspend fun saveItemToDb(entity: SavedItemEntity) {
        TODO("Not yet implemented")
    }

    override suspend fun fetchAllItemsFromDb(): Flow<List<SavedItemEntity>> {
        TODO("Not yet implemented")
    }

    override suspend fun fetchItemDetailsFromWeb(): SavedItemEntity {
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