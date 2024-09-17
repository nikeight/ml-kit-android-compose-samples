package com.example.geminichatapp.features.channel

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChannelListScreen(
    modifier: Modifier = Modifier,
    channelListViewModel: ChannelListViewModel = hiltViewModel(),
    navigateToChatScreen: (UUID) -> Unit,
    createANewChatThread: (UUID) -> Unit,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Channels")
                },
                actions = {
                    IconButton(onClick = { channelListViewModel.sortChatViaDates() }) {
                        Icon(
                            imageVector = Icons.Outlined.Menu,
                            contentDescription = "filter_btn",
                        )
                    }
                }
            )
        },
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            IconButton(
                modifier = modifier
                    .clip(RoundedCornerShape(corner = CornerSize(56.dp)))
                    .background(Color.Gray),
                onClick = {
                    createANewChatThread(
                        UUID.randomUUID()
                    )
                }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "add_content"
                )
            }
        }
    ) {
        val channelList = channelListViewModel.uiState.collectAsState()

        LazyColumn(
            modifier
                .fillMaxSize()
                .padding(it)
        ) {
            channelList.value.channelList?.let { channelList ->
                items(channelList.reversed()) { channel ->
                    println("UNIQUE ID FROM DB : ${channel.channelId}")
                    ChannelListItem(
                        channelWithMessage = channel,
                        navigateToChatScreen = { passedId ->
                            navigateToChatScreen(passedId)
                        }
                    )
                }
            }
        }
    }
}