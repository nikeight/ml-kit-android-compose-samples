package com.example.geminichatapp.features.chat

import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performScrollToIndex
import androidx.compose.ui.test.printToLog
import com.example.geminichatapp.common.UiState
import com.example.geminichatapp.data.model.Participant
import org.junit.Rule
import org.junit.Test
import java.util.Date
import java.util.UUID

class ChatScreenContentUiTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun checkChatComponent_AtInitialStage() {
        composeTestRule.setContent {
            ChatScreenContent(
                channelId = UUID.randomUUID(),
                chatUiState = UiState(),
                sendTextMessageCallBack = {},
                sendImageMessageCallBack = {}
            )
        }

        composeTestRule.onRoot(
            useUnmergedTree = true
        ).printToLog("chat_component_tree")

        composeTestRule.onNode(
            hasTestTag("message_text_field") and hasClickAction(),
            useUnmergedTree = true
        )

        composeTestRule.onNode(
            hasTestTag("add_image_icon") and hasClickAction(),
            useUnmergedTree = true
        )

        composeTestRule.onNode(
            hasTestTag("send_msg_icon") and hasClickAction(),
            useUnmergedTree = true
        )
    }

    @Test
    fun scrollToTop_andCheckFirstStickHeaderExistsOrNot() {
        composeTestRule.setContent {
            ChatScreenContent(
                channelId = UUID.randomUUID(),
                chatUiState = UiState(
                    chatListWithDate = fakeChatHistory().toMutableMap()
                ),
                sendTextMessageCallBack = {},
                sendImageMessageCallBack = {}
            )
        }

        composeTestRule.onRoot(
            useUnmergedTree = true
        ).printToLog("scroll_chat_component")

        val chatListComponent = composeTestRule.onNode(
            hasTestTag("chat_list"), useUnmergedTree = true
        )

        chatListComponent.performScrollToIndex(0)

        composeTestRule.onNode(
            hasTestTag("12-12-2023"), useUnmergedTree = true
        ).assertExists()
    }
}

fun fakeChatHistory() = mapOf<String, MutableList<Message>>(
    "12-12-2023" to fakeConversationOne(),
    "13-12-2023" to fakeConversationThree(),
    "14-12-2023" to fakeConversationTwo(),
)

fun fakeConversationOne() = mutableListOf<Message>(
    Message(
        id = 1,
        time = "12:34",
        date = Date(),
        images = emptyList(),
        byWhom = Participant.USER,
        message = "Hello, how are you?",
        foreignChannelId = UUID.randomUUID()
    ),
    Message(
        id = 2,
        time = "12:34",
        date = Date(),
        images = emptyList(),
        byWhom = Participant.MODEL,
        message = "Hello, I am good, please take this Loreum from me, this is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum",
        foreignChannelId = UUID.randomUUID()
    )
)

fun fakeConversationTwo() = mutableListOf<Message>(
    Message(
        id = 3,
        time = "12:34",
        date = Date(),
        images = emptyList(),
        byWhom = Participant.USER,
        message = "Hello, how are you?",
        foreignChannelId = UUID.randomUUID()
    ),
    Message(
        id = 4,
        time = "12:34",
        date = Date(),
        images = emptyList(),
        byWhom = Participant.MODEL,
        message = "Hello, I am good, please take this Loreum from me, this is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum",
        foreignChannelId = UUID.randomUUID()
    )
)

fun fakeConversationThree() = mutableListOf<Message>(
    Message(
        id = 5,
        time = "12:34",
        date = Date(),
        images = emptyList(),
        byWhom = Participant.USER,
        message = "Hello, how are you?",
        foreignChannelId = UUID.randomUUID()
    ),
    Message(
        id = 6,
        time = "12:34",
        date = Date(),
        images = emptyList(),
        byWhom = Participant.MODEL,
        message = "Hello, I am good, please take this Loreum from me, this is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum",
        foreignChannelId = UUID.randomUUID()
    )
)


