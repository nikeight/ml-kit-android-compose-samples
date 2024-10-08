package com.example.geminichatapp.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DispatcherModule {

    @Provides
    @Singleton
    fun provideDispatchers() = DispatcherProvider()
}

sealed class Dispatcher {
    data object Main : Dispatcher()
    data object IO : Dispatcher()
    data object Default : Dispatcher()
    data object UnConfined : Dispatcher()
    data object TestStandard : Dispatcher()
}

class DispatcherProvider {
    val mainDispatcher: CoroutineDispatcher
        get() = Dispatchers.Main

    val ioDispatcher: CoroutineDispatcher
        get() = Dispatchers.IO

    val defaultDispatcher: CoroutineDispatcher
        get() = Dispatchers.Default

    val unConfinedDispatcher: CoroutineDispatcher
        get() = Dispatchers.Unconfined
}
