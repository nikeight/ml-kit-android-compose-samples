package com.example.geminichatapp.features.channel

import app.cash.turbine.test
import com.example.geminichatapp.data.model.ChannelWithMessage
import com.example.geminichatapp.data.model.Participant
import com.example.geminichatapp.data.repo.Repository
import com.example.geminichatapp.rules.TestDispatcherRule
import com.google.common.truth.Truth.assertThat
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.Date
import java.util.UUID

@OptIn(ExperimentalCoroutinesApi::class)
class ChannelListViewModelTest {

    private lateinit var repository: Repository

    @get:Rule
    val instantTaskExecutorRule = TestDispatcherRule()

    @Before
    fun setup(){
        repository = mockk<Repository>(relaxed = true)
    }

    @Test
    fun whenFetchChannels_UpdateStateFlowWithChannelList() = runTest {

        coEvery {
            repository.fetchChannels()
        } returns flowOf(fakeChannelWithMsgList())

        val sut = ChannelListViewModel(repository)
        advanceUntilIdle()

        sut.uiState.test {
            val initialState = awaitItem()
            assertThat(initialState.channelList).isNotEmpty()
            assertThat(initialState.channelList).hasSize(fakeChannelWithMsgList().size)
            val finalState = awaitItem()
            assertThat(finalState.channelList).isNotEmpty()
            assertThat(finalState.channelList).hasSize(fakeChannelWithMsgList().size)
            awaitComplete()
        }
    }

    @After
    fun setUp(){
        clearAllMocks()
    }
}

fun fakeChannelWithMsgList() = listOf(
    ChannelWithMessage(
        channelId = UUID.randomUUID(),
        message = "Message 1",
        date = Date(),
        images = emptyList(),
        byWhom = Participant.USER,
    ),
    ChannelWithMessage(
        channelId = UUID.randomUUID(),
        message = "Message 2",
        date = Date(),
        images = emptyList(),
        byWhom = Participant.MODEL,
    ),
)