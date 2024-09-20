package com.example.geminichatapp.features.channel

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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

    var imageSize by remember { mutableStateOf<Size>(Size(width = 180f, height = 90f)) }

    LaunchedEffect(key1 = Unit) {
        val numberOfImages = channelWithMessage.images ?: 0
        imageSize = when (numberOfImages) {
            1 -> Size(width = 360f, height = 240f)
            2 -> Size(width = 180f, height = 180f)
            3 -> Size(width = 120f, height = 120f)
            else -> Size(width = 90f, height = 90f)
        }
    }

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
            imageSize = imageSize,
            participant = channelWithMessage.byWhom,
            msgDate = channelWithMessage.date
                .getDateAsDayDateAndMonth(),
            maxLines = 2
        )
    }
}