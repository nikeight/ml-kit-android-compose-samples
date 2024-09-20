package com.example.geminichatapp.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.geminichatapp.data.model.Participant
import com.example.geminichatapp.ui.components.AvatarsImage
import com.example.geminichatapp.ui.components.LocalImageView

@Composable
fun ChatBubbleContent(
    modifier: Modifier = Modifier,
    textMsg: String,
    imagesList: List<String>?,
    imageSize: Size,
    participant: Participant,
    msgDate: String,
    maxLines: Int?
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
            isModel = participant == Participant.MODEL
        )

        Text(
            modifier = modifier
                .weight(1f),
            text = msgDate,
            style = TextStyle(
                fontSize = 14.sp,
                textAlign = TextAlign.End,
                color = Color.Gray
            )
        )
    }

    Divider(
        thickness = 1.dp,
        color = Color.Black,
    )

    imagesList?.let { list ->
        LazyRow {
            items(list) { imageUri ->
                LocalImageView(imageSize = imageSize, filePath = imageUri)
            }
        }

        Divider(
            thickness = 1.dp,
            color = Color.Black,
        )
    }

    maxLines?.let { numberOfLines ->
        Text(
            text = textMsg,
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
    } ?: Text(
        text = textMsg,
        modifier
            .fillMaxWidth()
            .padding(all = 24.dp),
        style = TextStyle(
            fontSize = 15.sp,
        ),
        textAlign = TextAlign.Start
    )

}