package com.example.geminichatapp.features.channel

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import java.util.UUID

@Composable
fun ChannelListScreen(
    modifier: Modifier = Modifier,
    channelListViewModel: ChannelListViewModel = hiltViewModel(),
    navigateToChatScreen: (UUID) -> Unit,
    createANewChatThread: (UUID) -> Unit,
) {
    val state = channelListViewModel.uiState.collectAsStateWithLifecycle()
    ChannelListScreenContent(
        modifier = modifier,
        sortChannelsViaDates = { channelListViewModel.sortChatViaDates() },
        navigateToChatScreen = navigateToChatScreen,
        createANewChatThread = createANewChatThread,
        channelList = state.value.channelList
    )
}