package com.example.geminichatapp.di

import com.example.geminichatapp.BuildConfig
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.GenerationConfig
import com.google.ai.client.generativeai.type.generationConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object GeminiModule {

    @Provides
    @Singleton
    fun provideGeminiConfig() = generationConfig {
          temperature = 0.7f
    }

    @Provides
    @Singleton
    @Named("ChatGenerativeModel")
    fun provideChatGeminiModel(
        config: GenerationConfig
    ) = GenerativeModel(
        // The Gemini 1.5 models are versatile and work with most use cases
        modelName = "gemini-1.0-pro",
        apiKey = BuildConfig.GEMINI_API_KEY,
        generationConfig = config
    )

    @Provides
    @Singleton
    @Named("ImageGenerativeModel")
    fun provideImageGeminiModel(
        config: GenerationConfig
    ) = GenerativeModel(
        // The Gemini 1.5 models are versatile and work with most use cases
        modelName = "gemini-1.0-pro-vision-latest",
        apiKey = BuildConfig.GEMINI_API_KEY,
        generationConfig = config
    )
}