package com.example.textrecognizer.di

import com.example.textrecognizer.data.remote.NetworkService
import com.example.textrecognizer.data.remote.NetworkServiceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class NetworkModule {

    @Binds
    @Singleton
    abstract fun bindNetworkServiceImpl(networkServiceImpl: NetworkServiceImpl): NetworkService
}