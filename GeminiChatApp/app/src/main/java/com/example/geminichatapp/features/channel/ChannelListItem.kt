package com.example.geminichatapp.features.channel

import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.geminichatapp.R
import com.example.geminichatapp.data.model.ChannelWithMessage
import com.example.geminichatapp.data.model.Participant
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
                navigateToChatScreen(channelWithMessage.channelId)
            }
    ) {
        Row(
            modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (channelWithMessage.byWhom == Participant.MODEL) {
                Icon(
                    modifier = Modifier.size(24.dp),
                    painter = painterResource(id = R.drawable.gemini_logo),
                    contentDescription = "gemini_logo"
                )
            } else {
                Icon(
                    modifier = Modifier.size(24.dp),
                    imageVector = Icons.Default.Person,
                    contentDescription = "gemini_logo"
                )
            }

            Text(
                modifier = modifier.padding(horizontal = 8.dp),
                text = channelWithMessage.byWhom.name, style = TextStyle(
                    fontSize = 14.sp,
                    textAlign = TextAlign.Start,
                    color = Color.Gray
                )
            )

            Text(
                modifier = modifier
                    .weight(1f),
                text = channelWithMessage.date.toString(), style = TextStyle(
                    fontSize = 14.sp,
                    textAlign = TextAlign.End,
                    color = Color.Gray
                )
            )
        }

        if (channelWithMessage.images?.isNotEmpty() == true) {
            AsyncImage(
                model = Uri.parse(channelWithMessage.images.first()),
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .padding(4.dp)
                    .requiredSize(84.dp)
            )
        }

        Text(
            text = channelWithMessage.message,
            modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            style = TextStyle(
                fontSize = 15.sp,
            ),
            maxLines = 4,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Start
        )
    }
}