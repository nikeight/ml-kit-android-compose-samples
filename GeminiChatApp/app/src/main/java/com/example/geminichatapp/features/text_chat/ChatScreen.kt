package com.example.geminichatapp.features.text_chat

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.geminichatapp.common.ChatBubbleItem
import com.example.geminichatapp.common.ChatStickyHeader
import com.example.geminichatapp.common.MessageTextField
import com.example.geminichatapp.ui.components.ErrorMessage
import com.example.geminichatapp.ui.components.MessageGeneratingAnimation
import java.util.UUID

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ChatScreen(
    channelId: UUID,
    modifier: Modifier = Modifier,
    chatScreenViewModel: ChatScreenViewModel = hiltViewModel()
) {
    val chatUiState by chatScreenViewModel.uiState.collectAsState()
    val listState = rememberLazyListState()

    LaunchedEffect(chatUiState.chatListWithDate?.values?.size) {
        chatUiState.chatListWithDate?.values?.size?.let { newScrollPosition ->
            listState.animateScrollToItem(newScrollPosition)
        }
    }

    LaunchedEffect(key1 = Unit) {
        chatScreenViewModel.setChannelId(channelId)
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = {
            MessageTextField(
                onSendMessage = { message ->
                    chatScreenViewModel.sendMessage(
                        messageEntity = message,
                    )
                },
                onSendImagePrompt = { imagePrompt ->
                    chatScreenViewModel.sendImagePrompt(
                        messageEntity = imagePrompt
                    )
                },
                channelId = channelId
            )
        }
    ) {
        LazyColumn(
            reverseLayout = false,
            state = listState,
            modifier = modifier.padding(it),
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
                    ErrorMessage()
                }
                AnimatedVisibility(visible = chatUiState.loading) {
                    MessageGeneratingAnimation()
                }
            }
        }
    }
}