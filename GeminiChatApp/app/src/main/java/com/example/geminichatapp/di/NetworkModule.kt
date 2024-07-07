package com.example.geminichatapp.di

import com.example.geminichatapp.data.remote.INetworkService
import com.example.geminichatapp.data.remote.NetworkServiceImpl
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
    abstract fun bindNetworkServiceImpl(networkServiceImpl: NetworkServiceImpl): INetworkService
}