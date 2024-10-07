package com.example.geminichatapp.di

import android.content.Context
import coil.ImageLoader
import coil.request.ImageRequest
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ImageBuilderModule {

    @Provides
    @Singleton
    fun provideImageBuilder(
        @ApplicationContext activityContext: Context,
    ): ImageRequest.Builder {
        return ImageRequest.Builder(activityContext)
    }

    @Provides
    @Singleton
    fun provideImageLoader(
        @ApplicationContext activityContext: Context,
    ): ImageLoader {
        return ImageLoader.Builder(activityContext).build()
    }
}