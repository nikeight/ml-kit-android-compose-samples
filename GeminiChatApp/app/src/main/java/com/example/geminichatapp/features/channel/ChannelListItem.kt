package com.example.geminichatapp.features.channel

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.geminichatapp.R
import com.example.geminichatapp.data.model.ChannelWithMessage
import com.example.geminichatapp.data.model.Participant
import com.example.geminichatapp.ui.components.AvatarsImage
import com.example.geminichatapp.ui.components.LocalImageView
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
        Row(
            modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            AvatarsImage(
                modifier = modifier,
                resources = R.drawable.gemini_logo,
                imageVector = Icons.Default.Person,
                contentDescription = "avatars_logo",
                isModel = channelWithMessage.byWhom == Participant.MODEL
            )

            Text(
                modifier = modifier
                    .weight(1f),
                text = channelWithMessage.date
                    .getDateAsDayDateAndMonth(),
                style = TextStyle(
                    fontSize = 14.sp,
                    textAlign = TextAlign.End,
                    color = Color.Gray
                )
            )
        }

        Divider(
            thickness = 2.dp,
            color = Color.Black,
        )

        channelWithMessage.images?.let { imagesList ->
            LazyRow {
                items(imagesList) { imageUri ->
                    LocalImageView(imageSize = imageSize, filePath = imageUri)
                }
            }
        }

        Text(
            text = channelWithMessage.message,
            modifier
                .fillMaxWidth()
                .padding(all = 24.dp),
            style = TextStyle(
                fontSize = 15.sp,
            ),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Start
        )
    }
}