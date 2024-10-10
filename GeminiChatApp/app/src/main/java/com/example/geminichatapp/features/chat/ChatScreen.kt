package com.example.geminichatapp.features.chat

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import java.util.UUID

@Composable
fun ChatScreen(
    channelId: UUID,
    modifier: Modifier = Modifier,
    chatScreenViewModel: ChatScreenViewModel = hiltViewModel()
) {
    val chatUiState by chatScreenViewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = Unit) {
        chatScreenViewModel.setChannelId(channelId)
    }

    ChatScreenContent(
        modifier,
        channelId = channelId,
        chatUiState = chatUiState,
        sendTextMessageCallBack = {
            chatScreenViewModel.sendMessage(messageEntity = it)
        },
        sendImageMessageCallBack = {
            chatScreenViewModel.sendImagePrompt(messageEntity = it)
        }
    )
}