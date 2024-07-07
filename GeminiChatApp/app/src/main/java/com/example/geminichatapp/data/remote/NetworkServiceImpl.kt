package com.example.geminichatapp.data.remote

import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.Content
import com.google.ai.client.generativeai.type.GenerateContentResponse
import javax.inject.Inject
import javax.inject.Named

class NetworkServiceImpl @Inject constructor(
    @Named("ChatGenerativeModel") private val chatGenerativeModel: GenerativeModel,
    @Named("ImageGenerativeModel") private val imageGenerativeModel: GenerativeModel,
) : INetworkService {

    override suspend fun sendMessage(
        messageContent: String,
        historyContent: List<Content>
    ): GenerateContentResponse {
        val generativeChat = chatGenerativeModel.startChat(historyContent)
        return generativeChat.sendMessage(messageContent)
    }

    override suspend fun sendImagePrompt(imageContent: Content): GenerateContentResponse {
        return imageGenerativeModel.generateContent(imageContent)
    }
}