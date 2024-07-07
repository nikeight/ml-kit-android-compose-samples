package com.example.cameraview.di

import com.example.cameraview.data.FirebaseStorageService
import com.example.cameraview.data.StorageService
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class StorageServiceModule {

    @Singleton
    @Binds
    abstract fun bindFirebaseStorageService(
        firebaseStorageService: FirebaseStorageService
    ): StorageService
}