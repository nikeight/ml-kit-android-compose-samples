@file:OptIn(ExperimentalMaterial3Api::class)

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.geminichatapp.data.model.ChannelWithMessage
import com.example.geminichatapp.data.model.Participant
import java.util.Date
import java.util.UUID

@Composable
fun ChannelListScreenContent(
    modifier: Modifier = Modifier,
    sortChannelsViaDates: () -> Unit,
    navigateToChatScreen: (UUID) -> Unit,
    createANewChatThread: (UUID) -> Unit,
    channelList: List<ChannelWithMessage>? = null
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Channels")
                },
                actions = {
                    IconButton(
                        modifier = modifier.testTag("sort_icon_button"),
                        onClick = sortChannelsViaDates) {
                        Icon(
                            imageVector = Icons.Outlined.Menu,
                            contentDescription = "filter_btn",
                        )
                    }
                }
            )
        },
        floatingActionButtonPosition = FabPosition.EndOverlay,
        floatingActionButton = {
            IconButton(
                modifier = modifier
                    .clip(RoundedCornerShape(corner = CornerSize(56.dp)))
                    .background(Color.Gray)
                    .testTag("fab_icon_button"),
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
        LazyColumn(
            modifier
                .fillMaxSize()
                .padding(it)
        ) {
            channelList?.let { channelList ->
                items(channelList.reversed()) { channel ->
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

@Preview(
    showSystemUi = true,
    showBackground = true,
)
@Composable
private fun ChannelListScreenContentPreview() {
    ChannelListScreenContent(
        sortChannelsViaDates = { /*TODO*/ },
        navigateToChatScreen = {},
        createANewChatThread = {},
        channelList = listOf(
            ChannelWithMessage(
                channelId = UUID.randomUUID(),
                message = "Hello",
                date = Date(),
                images = emptyList(),
                byWhom = Participant.USER
            ),
        )
    )
}