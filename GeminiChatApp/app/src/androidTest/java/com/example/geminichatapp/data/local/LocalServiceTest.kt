package com.example.geminichatapp.data.local

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import app.cash.turbine.test
import com.example.geminichatapp.data.local.db.ChannelDao
import com.example.geminichatapp.data.local.db.ChatDao
import com.example.geminichatapp.data.local.db.GeminiChatDatabase
import com.example.geminichatapp.data.model.ChannelEntity
import com.example.geminichatapp.data.model.ChannelWithMessage
import com.example.geminichatapp.data.model.MessageEntity
import com.example.geminichatapp.data.model.Participant
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.util.Date
import java.util.UUID

class LocalServiceUnitTest {
    private lateinit var mockChatDao: ChatDao
    private lateinit var mockChannelDao: ChannelDao
    private lateinit var mockDb: GeminiChatDatabase

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        mockDb = Room.inMemoryDatabaseBuilder(
            context = context,
            klass = GeminiChatDatabase::class.java
        ).build()

        mockChatDao = mockDb.provideChatDao()
        mockChannelDao = mockDb.provideChannelDao()
    }


    @Test
    fun whenChannelCreated_findNoMessagesInitially() = runBlocking {
        val fetchHistory = mockChatDao.loadHistory(UUID.randomUUID())
        assertEquals(0, fetchHistory.size)
    }

    @Test
    fun whenMessageSent_verifyDataAddedToDb() = runBlocking {
        val channelId = UUID.randomUUID()
        val fakeMessage = MessageEntity(
            id = 0,
            time = "Current Time",
            date = Date(),
            images = emptyList(),
            message = "Hello, World!",
            byWhom = Participant.USER,
            foreignChannelId = channelId
        )

        mockChatDao.addMessage(fakeMessage)
        val fetchChats = mockChatDao.loadChats(channelId)
        val collectedMessage = fetchChats.first().first().message
        assertEquals(fakeMessage.message, collectedMessage)
    }

    @Test
    fun whenNewMessageAdded_verifyChannelUpdatedWithLastMessageInTheChat() = runBlocking {
        val channelId = UUID.randomUUID()
        val assertDate = Date()
        val fakeMessageByUser = MessageEntity(
            id = 0,
            time = "Current Time",
            date = Date(),
            images = emptyList(),
            message = "Hello, World!",
            byWhom = Participant.USER,
            foreignChannelId = channelId
        )

        mockChatDao.addMessage(fakeMessageByUser)
        mockChannelDao.updateChannel(
            channelEntity = ChannelEntity(
                channelId = channelId,
                messageId = fakeMessageByUser.id,
                lastUpdateDate = fakeMessageByUser.date
            )
        )

        val fakeMessageByModel = MessageEntity(
            id = 1,
            time = "Current Time + 10 sec",
            date = assertDate,
            images = emptyList(),
            message = "Hello, How I can help you?",
            byWhom = Participant.MODEL,
            foreignChannelId = channelId
        )
        mockChatDao.addMessage(fakeMessageByModel)

        mockChannelDao.updateChannel(
            channelEntity = ChannelEntity(
                channelId = channelId,
                messageId = fakeMessageByModel.id,
                lastUpdateDate = fakeMessageByModel.date
            )
        )

        val fetchChannel: ChannelWithMessage = mockChannelDao.fetchAllChannels().first().first()
        assertEquals(channelId, fetchChannel.channelId)
        assertEquals("Hello, How I can help you?", fetchChannel.message)
        assertEquals(assertDate, fetchChannel.date)
        assertEquals(Participant.MODEL, fetchChannel.byWhom)
    }

    @Test
    fun whenMessageAddedInInterval_CheckDbReturnsTheExpectedValue() = runTest {
        val chanelId = UUID.randomUUID()
        fakeMessageEntityList(chanelId).forEach {msgEntity ->
            mockChatDao.addMessage(msgEntity)
        }

        launch {
            delay(500)
            mockChatDao.addMessage(
                MessageEntity(
                    message = "Message 4",
                    time = "Time 4",
                    date = Date(),
                    images = emptyList(),
                    byWhom = Participant.MODEL,
                    foreignChannelId = chanelId
                )
            )
        }

        mockChatDao.loadChats(chanelId).test {
            val initialSize = fakeMessageEntityList(chanelId).size
            val item = awaitItem()
            assertThat(item).hasSize(initialSize)
            val newItemList = awaitItem()
            assertThat(newItemList).hasSize(initialSize.plus(1))
            cancel()
        }
    }

    @After
    fun clearUp() {
        mockDb.close()
    }
}

fun fakeMessageEntityList(channelId: UUID) = listOf(
    MessageEntity(
        message = "Message 1",
        time = "Time 1",
        date = Date(),
        images = emptyList(),
        byWhom = Participant.USER,
        foreignChannelId = channelId
    ),
    MessageEntity(
        message = "Message 2",
        time = "Time 2",
        date = Date(),
        images = emptyList(),
        byWhom = Participant.MODEL,
        foreignChannelId = channelId
    ),
    MessageEntity(
        message = "Message 3",
        time = "Time 3",
        date = Date(),
        images = listOf("uriOne","uriTwo"),
        byWhom = Participant.USER,
        foreignChannelId = channelId
    ),
)