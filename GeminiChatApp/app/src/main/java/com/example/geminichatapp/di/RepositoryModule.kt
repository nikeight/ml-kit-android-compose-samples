package com.example.geminichatapp.di

import com.example.geminichatapp.data.repo.Repository
import com.example.geminichatapp.data.repo.RepositoryImpl
import com.example.geminichatapp.data.util.UriToBitmapConverter
import com.example.geminichatapp.data.util.UriToBitmapConverterImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindRepositoryImpl(repositoryImpl: RepositoryImpl): Repository

    @Binds
    @Singleton
    abstract fun bindUriToBitmapConverterImpl(
        uriToBitmapConverterImpl: UriToBitmapConverterImpl,
    ): UriToBitmapConverter
}