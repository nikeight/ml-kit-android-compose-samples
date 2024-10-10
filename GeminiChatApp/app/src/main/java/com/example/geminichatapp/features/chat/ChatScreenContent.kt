package com.example.geminichatapp.features.chat

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.geminichatapp.common.ChatBubbleItem
import com.example.geminichatapp.common.ChatStickyHeader
import com.example.geminichatapp.common.MessageTextField
import com.example.geminichatapp.common.UiState
import com.example.geminichatapp.data.model.MessageEntity
import com.example.geminichatapp.ui.components.ErrorMessage
import com.example.geminichatapp.ui.components.MessageGeneratingAnimation
import java.util.UUID

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ChatScreenContent(
    modifier: Modifier = Modifier,
    channelId: UUID,
    chatUiState: UiState,
    sendTextMessageCallBack: (MessageEntity) -> Unit,
    sendImageMessageCallBack: (MessageEntity) -> Unit,
) {
    val listState = rememberLazyListState()

    LaunchedEffect(chatUiState.chatListWithDate) {
        listState.animateScrollToItem(
            listState.layoutInfo.totalItemsCount,
        )
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = {
            MessageTextField(
                onSendMessage = sendTextMessageCallBack,
                onSendImagePrompt = sendImageMessageCallBack,
                channelId = channelId
            )
        }
    ) {
        LazyColumn(
            modifier = modifier
                .padding(it)
                .testTag("chat_list"),
            reverseLayout = false,
            state = listState,
            verticalArrangement = Arrangement.Center
        ) {
            chatUiState.chatListWithDate?.forEach { (key, list) ->
                stickyHeader {
                    ChatStickyHeader(heading = key)
                }
                items(
                    count = list.size,
                    key = { index ->
                        list[index].id
                    },
                    itemContent = { index ->
                        ChatBubbleItem(
                            modifier = Modifier.animateItemPlacement(),
                            messageEntity = list[index],
                        )
                    }
                )
            }
            item {
                AnimatedVisibility(visible = chatUiState.isError) {
                    ErrorMessage(
                        modifier = modifier,
                        errorMessage = "Something Went Wrong"
                    )
                }
                AnimatedVisibility(visible = chatUiState.loading) {
                    Card(
                        modifier = modifier
                            .wrapContentSize(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primary
                                .copy(alpha = 0.6f)
                        )
                    ) {
                        Box(modifier = Modifier.padding(24.dp)) {
                            MessageGeneratingAnimation()
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun ChatScreenContentPreview() {
    ChatScreenContent(
        channelId = UUID.randomUUID(),
        chatUiState = UiState(),
        sendTextMessageCallBack = {},
        sendImageMessageCallBack = {}
    )
}