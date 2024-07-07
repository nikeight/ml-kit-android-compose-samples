package com.example.textrecognizer.data.remote

import com.example.textrecognizer.data.model.SavedItemEntity

interface NetworkService {
    suspend fun fetchItemMeaning(savedItemEntity: SavedItemEntity)
}