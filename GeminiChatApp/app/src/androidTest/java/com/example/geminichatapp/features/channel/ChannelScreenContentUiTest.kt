package com.example.geminichatapp.features.channel

import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import com.example.geminichatapp.data.model.ChannelWithMessage
import com.example.geminichatapp.data.model.Participant
import org.junit.Rule
import org.junit.Test
import java.util.Date
import java.util.UUID

class ChannelScreenContentUiTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val channelText = composeTestRule.onNodeWithText("Channels")
    private val iconButton = composeTestRule.onNodeWithTag("sort_icon_button")
    private val fabIconButton = composeTestRule.onNodeWithTag("fab_icon_button")

    @Test
    fun channelListScreenContentUiComponentTest() {
        composeTestRule.setContent {
            ChannelListScreenContent(
                sortChannelsViaDates = {},
                navigateToChatScreen = {},
                createANewChatThread = {},
                channelList = emptyList()
            )
        }

        listOf(
            channelText,
            iconButton,
            fabIconButton
        ).forEach { node ->
            node.assertExists()
        }
    }

    @Test
    fun checkChannelComponentIconClickableNature() {
        composeTestRule.setContent {
            ChannelListScreenContent(
                sortChannelsViaDates = {},
                navigateToChatScreen = {},
                createANewChatThread = {},
                channelList = fakeChannelWithMsg()
            )
        }

        listOf(
            iconButton,
            fabIconButton
        ).forEach {node ->
            node.assertHasClickAction()
            node.assertIsEnabled()
        }
    }

    @Test
    fun whenChannelContentContainsOneMessage_FromUser_CheckComponentExists() {
        composeTestRule.setContent {
            ChannelListScreenContent(
                sortChannelsViaDates = {},
                navigateToChatScreen = {},
                createANewChatThread = {},
                channelList = fakeChannelWithMsg()
            )
        }

        composeTestRule.onNodeWithText(
            fakeChannelWithMsg()[0].message
        ).assertExists()

        composeTestRule.onNode(
            hasTestTag("avatar_image"),
            useUnmergedTree = true
        ).assertExists()

        composeTestRule.onNodeWithTag("images_row").assertDoesNotExist()
    }
}

fun fakeChannelWithMsg() = listOf(
    ChannelWithMessage(
        channelId = UUID.randomUUID(),
        message = "Hello",
        date = Date(),
        images = emptyList(),
        byWhom = Participant.USER
    ),
)