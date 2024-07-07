package com.example.textrecognizer.di

import com.example.textrecognizer.data.repo.Repository
import com.example.textrecognizer.data.repo.RepositoryImpl
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
}