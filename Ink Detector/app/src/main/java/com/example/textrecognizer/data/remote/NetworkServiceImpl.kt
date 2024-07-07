package com.example.textrecognizer.data.remote

import com.example.textrecognizer.data.model.SavedItemEntity
import javax.inject.Inject

class NetworkServiceImpl @Inject constructor(
) : NetworkService {
    override suspend fun fetchItemMeaning(savedItemEntity: SavedItemEntity) {
        TODO("Not yet implemented")
    }
}