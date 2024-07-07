package com.example.textrecognizer.di

import com.example.textrecognizer.data.local.LocalService
import com.example.textrecognizer.data.local.LocalServiceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class LocalModule {

    @Binds
    @Singleton
    abstract fun bindLocalServiceImpl(localServiceImpl: LocalServiceImpl) : LocalService
}