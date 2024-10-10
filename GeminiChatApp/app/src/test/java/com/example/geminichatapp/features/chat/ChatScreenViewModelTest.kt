package com.example.geminichatapp.features.chat

import app.cash.turbine.test
import com.example.geminichatapp.data.model.MessageEntity
import com.example.geminichatapp.data.model.Participant
import com.example.geminichatapp.data.model.toMessage
import com.example.geminichatapp.data.repo.MessageState
import com.example.geminichatapp.data.repo.Repository
import com.example.geminichatapp.di.DispatcherProvider
import com.example.geminichatapp.rules.TestDispatcherRule
import com.google.common.truth.Truth.assertThat
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.Date
import java.util.UUID

class ChatScreenViewModelTest {

    private lateinit var repository: Repository
    private lateinit var dispatcherProvider: DispatcherProvider
    private val sut: ChatScreenViewModel by lazy {
        ChatScreenViewModel(repository, dispatcherProvider)
    }
    private val randomChannelId = UUID.randomUUID()

    @get:Rule
    val instantTaskExecutorRule = TestDispatcherRule()

    @Before
    fun setup() {
        dispatcherProvider = DispatcherProvider()
        repository = mockk<Repository>(relaxed = true)
    }

    /**
     * SendMessage and SendImageMessage have same behaviour
     */
    @Test
    fun whenSendMessageSuccessStateEmitted_thenUpdateRespectiveState() = runTest {
        assertThat(sut.uiState.value.chatListWithDate).isEmpty()

        coEvery {
            repository.sendMessage(any())
        } returns flowOf(MessageState.Success)

        sut.sendMessage(
            fakeMessageEntityList(channelId = randomChannelId).first(),
        )

        sut.uiState.test {
            val newState = awaitItem()
            assertThat(newState.loading).isFalse()
            assertThat(newState.isError).isFalse()
            cancel()
        }
    }

    @Test
    fun whenSendMessageFailureStateEmitted_thenUpdateRespectiveState() = runTest {
        assertThat(sut.uiState.value.chatListWithDate).isEmpty()

        coEvery {
            repository.sendMessage(any())
        } returns flowOf(MessageState.Failure)

        sut.sendMessage(
            fakeMessageEntityList(channelId = randomChannelId).first(),
        )

        sut.uiState.test {
            val newState = awaitItem()
            assertThat(newState.loading).isFalse()
            assertThat(newState.isError).isTrue()
            cancel()
        }
    }

    @Test
    fun whenSendMessageLoadingStateEmitted_thenUpdateRespectiveState() = runTest {
        assertThat(sut.uiState.value.chatListWithDate).isEmpty()

        coEvery {
            repository.sendMessage(any())
        } returns flowOf(MessageState.Loading)

        sut.sendMessage(
            fakeMessageEntityList(channelId = randomChannelId).first(),
        )

        sut.uiState.test {
            val newState = awaitItem()
            assertThat(newState.loading).isTrue()
            assertThat(newState.isError).isFalse()
            cancel()
        }
    }

    @Test
    fun whenCollectingChatFlow_thenUpdateChatWithDateState() = runTest {
        sut.setChannelId(randomChannelId)

        coEvery {
            repository.updateChannel(any())
        } returns Unit

        coEvery {
            repository.fetchAllHistory(randomChannelId)
        } returns flowOf(
            fakeMessageEntityList(channelId = randomChannelId).map {
                it.toMessage()
            },
        )

        sut.uiState.test {
            val newEmittedItem = awaitItem()
            assertThat(newEmittedItem.loading).isFalse()
            assertThat(newEmittedItem.isError).isFalse()
            assertThat(newEmittedItem.chatListWithDate).isNotEmpty()
            cancel()
        }
    }

    @After
    fun setUp() {
        unmockkAll()
        clearAllMocks()
    }
}

fun fakeMessageEntityList(channelId: UUID) = listOf(
    MessageEntity(
        id = 0,
        message = "Message 1",
        time = "Time 1",
        date = Date(),
        images = emptyList(),
        byWhom = Participant.USER,
        foreignChannelId = channelId
    ),
    MessageEntity(
        id = 1,
        message = "Message 2",
        time = "Time 2",
        date = Date(),
        images = emptyList(),
        byWhom = Participant.MODEL,
        foreignChannelId = channelId
    ),
    MessageEntity(
        id = 2,
        message = "Message 3",
        time = "Time 3",
        date = Date(),
        images = listOf("uriOne", "uriTwo"),
        byWhom = Participant.USER,
        foreignChannelId = channelId
    ),
)
