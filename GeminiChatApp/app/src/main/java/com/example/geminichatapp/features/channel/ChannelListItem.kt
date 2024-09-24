package com.example.geminichatapp.features.channel

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.dp
import com.example.geminichatapp.common.ChatBubbleContent
import com.example.geminichatapp.data.model.ChannelWithMessage
import com.example.geminichatapp.util.getDateAsDayDateAndMonth
import java.util.UUID

@Composable
fun ChannelListItem(
    modifier: Modifier = Modifier,
    channelWithMessage: ChannelWithMessage,
    navigateToChatScreen: (UUID) -> Unit
) {
    Card(
        modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable {
                navigateToChatScreen(
                    channelWithMessage.channelId,
                )
            }
    ) {
        ChatBubbleContent(
            textMsg = channelWithMessage.message,
            imagesList = channelWithMessage.images,
            imageSize = Size(width = 180f, height = 180f),
            participant = channelWithMessage.byWhom,
            msgDate = channelWithMessage.date
                .getDateAsDayDateAndMonth(),
            maxLines = 2
        )
    }
}