package com.example.geminichatapp.data.remote

import com.google.ai.client.generativeai.type.Content
import com.google.ai.client.generativeai.type.GenerateContentResponse
import kotlinx.coroutines.flow.Flow

interface INetworkService {
    suspend fun sendMessage(
        messageContent: String,
        historyContent: List<Content>
    ): GenerateContentResponse

    suspend fun sendImagePrompt(imageContent: Content): GenerateContentResponse
}