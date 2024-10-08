package com.example.geminichatapp.features.channel

import app.cash.turbine.test
import com.example.geminichatapp.data.model.ChannelWithMessage
import com.example.geminichatapp.data.model.Participant
import com.example.geminichatapp.data.repo.Repository
import com.example.geminichatapp.di.DispatcherProvider
import com.example.geminichatapp.rules.TestDispatcherRule
import com.google.common.truth.Truth.assertThat
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.Date
import java.util.UUID

class ChannelListViewModelTest {

    private lateinit var repository: Repository
    private lateinit var dispatcherProvider: DispatcherProvider

    private val sut: ChannelListViewModel by lazy {
        ChannelListViewModel(repository, dispatcherProvider)
    }

    @get:Rule
    val instantTaskExecutorRule = TestDispatcherRule()

    @Before
    fun setup() {
        dispatcherProvider = DispatcherProvider()
        repository = mockk<Repository>(relaxed = true)
    }

    @Test
    fun whenFetchChannels_UpdateStateFlowWithChannelList() = runTest {
        assertThat(sut.uiState.value.channelList).isEmpty()
        assertThat(sut.uiState.value.channelList).hasSize(0)

        coEvery {
            repository.fetchChannels()
        } returns flowOf(fakeChannelWithMsgList())

        sut.uiState.test {
            val newStateEmitted = awaitItem()
            assertThat(newStateEmitted.channelList).isNotEmpty()
            assertThat(newStateEmitted.channelList).hasSize(fakeChannelWithMsgList().size)
            cancel()
        }

        coVerify(exactly = 1) {
            repository.fetchChannels()
        }
    }

    @After
    fun setUp() {
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