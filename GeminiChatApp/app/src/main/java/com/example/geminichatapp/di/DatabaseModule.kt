package com.example.geminichatapp.di

import android.content.Context
import androidx.room.Room
import com.example.geminichatapp.data.local.db.GeminiChatDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext applicationContext: Context
    ) = Room.databaseBuilder(
        applicationContext,
        GeminiChatDatabase::class.java, "chat_db"
    ).build()

    @Provides
    @Singleton
    fun provideChatDao(geminiChatDatabase: GeminiChatDatabase) =
        geminiChatDatabase.provideChatDao()

    @Provides
    @Singleton
    fun provideChannelDao(geminiChatDatabase: GeminiChatDatabase) =
        geminiChatDatabase.provideChannelDao()
}