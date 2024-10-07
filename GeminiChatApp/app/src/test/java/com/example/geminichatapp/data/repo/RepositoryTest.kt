package com.example.geminichatapp.data.repo

import com.example.geminichatapp.data.local.LocalService
import com.example.geminichatapp.data.model.ChannelWithMessage
import com.example.geminichatapp.data.model.MessageEntity
import com.example.geminichatapp.data.model.Participant
import com.example.geminichatapp.data.remote.INetworkService
import com.example.geminichatapp.data.util.UriToBitmapConverter
import com.google.ai.client.generativeai.type.GenerateContentResponse
import com.google.common.truth.Truth.assertThat
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.coVerifyOrder
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.util.Date
import java.util.UUID

class RepositoryTest {

    private lateinit var sut: Repository
    private lateinit var networkService: INetworkService
    private lateinit var localService: LocalService
    private lateinit var imageConverterService: UriToBitmapConverter

    @Before
    fun setUp() {
        networkService = mockk<INetworkService>(relaxed = true)
        localService = mockk<LocalService>(relaxed = true)
        imageConverterService = mockk(relaxed = true)
        sut = RepositoryImpl(networkService, localService, imageConverterService)
    }

    @Test
    fun whenAppLaunches_FetchAllChannels() = runBlocking {
        coEvery {
            localService.fetchChannels()
        } returns flowOf(fakeChannelsWithMessageList())

        val result = sut.fetchChannels()
        assertThat(result.first().size).isEqualTo(2)

        coVerify {
            localService.fetchChannels()
        }
    }

    @Test
    fun whenNewMessageIsSent_FetchHistory() = runBlocking {
        val channelId = UUID.randomUUID()

        coEvery {
            localService.loadChats(channelId)
        } returns flowOf(fakeMessageList())

        val history = sut.fetchAllHistory(id = channelId)
        assertThat(history.first()?.size).isEqualTo(3)

        coVerify {
            localService.loadChats(channelId)
        }
    }

    @Test
    fun whenTextMessageSent_VerifyDbDataSyncAndSuccessResponse() = runTest {
        val channelId = UUID.randomUUID()
        val result = sut.sendMessage(fakeMessageList().first()).toList()

        coEvery {
            localService.addMessage(any())
        } returns Unit

        coEvery {
            localService.loadHistory(channelId)
        } returns fakeMessageList()

        coEvery {
            networkService.sendMessage(fakeMessageList().first().message, emptyList())
        } returns fakeGenerateContentResponse()

        coEvery {
            localService.addMessage(any())
        } returns Unit

        assertThat(result).containsExactly(
            MessageState.Loading,
            MessageState.Success,
        )

        coVerifyOrder {
            localService.addMessage(any())
            localService.loadHistory(any())
            networkService.sendMessage(any(), any())
            localService.addMessage(any())
        }

        coVerify(exactly = 2) {
            localService.addMessage(any())
        }

        coVerify(exactly = 1) {
            localService.loadHistory(any())
        }
    }

    @Test
    fun whenImageMessageSent_VerifyDbDataSyncAndSuccessResponse() = runBlocking {
        val result = sut.sendImagePrompt(fakeMessageList()[2]).toList()

        coEvery {
            localService.addMessage(any())
        } returns Unit

        coEvery {
            imageConverterService.convert(any())
        } returns listOf()

        coEvery {
            networkService.sendImagePrompt(any())
        } returns fakeGenerateContentResponse()

        coEvery {
            localService.addMessage(any())
        } returns Unit

        assertThat(result).containsExactly(
            MessageState.Loading,
            MessageState.Success,
        )

        coVerifyOrder {
            localService.addMessage(any())
            imageConverterService.convert(any())
            networkService.sendImagePrompt(any())
            localService.addMessage(any())
        }

        coVerify(exactly = 2) {
            localService.addMessage(any())
        }

        coVerify(exactly = 1) {
            imageConverterService.convert(any())
        }

        coVerify(exactly = 1) {
            networkService.sendImagePrompt(any())
        }
    }

    @Test
    fun whenSendMessageTaskFailed_verifyFailureState() = runTest {
        val channelId = UUID.randomUUID()

        coEvery {
            localService.addMessage(any())
        } returns Unit

        coEvery {
            localService.loadHistory(channelId)
        } throws IllegalStateException("Database Error")

        coEvery {
            networkService.sendMessage(fakeMessageList().first().message, emptyList())
        } throws IllegalStateException("Network Error")

        val result = sut.sendMessage(fakeMessageList().first()).toList()

        assertThat(result).containsExactly(
            MessageState.Loading,
            MessageState.Failure,
        )

        coVerifyOrder {
            localService.addMessage(any())
            localService.loadHistory(any())
            networkService.sendMessage(any(), any())
        }

        coVerify(exactly = 1) {
            localService.addMessage(any())
        }

        coVerify(exactly = 1) {
            localService.loadHistory(any())
        }

        coVerify(exactly = 1) {
            networkService.sendMessage(any(), any())
        }
    }

    @After
    fun clear(){
        clearAllMocks()
    }
}

fun fakeChannelsWithMessageList() = listOf(
    ChannelWithMessage(
        channelId = UUID.randomUUID(),
        message = "Message 1",
        date = Date(),
        images = emptyList(),
        byWhom = Participant.USER
    ),
    ChannelWithMessage(
        channelId = UUID.randomUUID(),
        message = "Message 2",
        date = Date(),
        images = emptyList(),
        byWhom = Participant.MODEL
    )
)

fun fakeMessageList() = listOf(
    MessageEntity(
        message = "Message 1",
        time = "Time 1",
        date = Date(),
        images = emptyList(),
        byWhom = Participant.USER,
        foreignChannelId = UUID.randomUUID()
    ),
    MessageEntity(
        message = "Message 2",
        time = "Time 2",
        date = Date(),
        images = emptyList(),
        byWhom = Participant.MODEL,
        foreignChannelId = UUID.randomUUID()
    ),
    MessageEntity(
        message = "Message 3",
        time = "Time 3",
        date = Date(),
        images = listOf("uriOne","uriTwo"),
        byWhom = Participant.MODEL,
        foreignChannelId = UUID.randomUUID()
    ),
)

fun fakeGenerateContentResponse() = GenerateContentResponse(
    candidates = emptyList(),
    promptFeedback = null,
    usageMetadata = null
)