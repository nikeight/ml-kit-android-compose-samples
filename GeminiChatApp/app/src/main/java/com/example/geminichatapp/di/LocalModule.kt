package com.example.geminichatapp.di

import com.example.geminichatapp.data.local.LocalService
import com.example.geminichatapp.data.local.LocalServiceImpl
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