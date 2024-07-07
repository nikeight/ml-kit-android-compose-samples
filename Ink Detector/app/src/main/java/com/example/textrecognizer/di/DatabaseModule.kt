package com.example.textrecognizer.di

import android.content.Context
import androidx.room.Room
import com.example.textrecognizer.data.local.db.SavedItemDatabase
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
        SavedItemDatabase::class.java, "chat_db"
    ).build()

    @Provides
    @Singleton
    fun provideSavedItemDao(savedItemDatabase: SavedItemDatabase) =
        savedItemDatabase.provideSavedItemDao()
}