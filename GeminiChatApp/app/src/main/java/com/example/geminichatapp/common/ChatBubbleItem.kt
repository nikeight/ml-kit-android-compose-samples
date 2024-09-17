package com.example.geminichatapp.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.geminichatapp.R
import com.example.geminichatapp.data.model.MessageEntity
import com.example.geminichatapp.data.model.Participant
import com.example.geminichatapp.ui.components.AvatarsImage
import com.example.geminichatapp.ui.components.LocalImageView
import com.example.geminichatapp.ui.components.MessageGeneratingAnimation

@Composable
fun ChatBubbleItem(
    modifier: Modifier = Modifier,
    messageEntity: MessageEntity,
    isChatLoading: Boolean,
    isError: Boolean,
    errorMessage: String,
) {
    var imageSize by remember { mutableStateOf<Size>(Size(width = 180f, height = 90f)) }

    LaunchedEffect(key1 = Unit) {
        val numberOfImages = messageEntity.images ?: 0
        imageSize = when (numberOfImages) {
            1 -> Size(width = 360f, height = 240f)
            2 -> Size(width = 180f, height = 180f)
            3 -> Size(width = 120f, height = 120f)
            else -> Size(width = 90f, height = 90f)
        }
    }

    val isModelMessage = messageEntity.byWhom == Participant.MODEL ||
            messageEntity.byWhom == Participant.ERROR

    val backgroundColor = when (messageEntity.byWhom) {
        Participant.MODEL -> MaterialTheme.colorScheme.primaryContainer
        Participant.USER -> MaterialTheme.colorScheme.tertiaryContainer
        Participant.ERROR -> MaterialTheme.colorScheme.errorContainer
    }

    val bubbleShape = if (isModelMessage) {
        RoundedCornerShape(4.dp, 20.dp, 20.dp, 20.dp)
    } else {
        RoundedCornerShape(20.dp, 4.dp, 20.dp, 20.dp)
    }

    val horizontalAlignment = if (isModelMessage) {
        Alignment.Start
    } else {
        Alignment.End
    }

    Column(
        horizontalAlignment = horizontalAlignment,
        modifier = modifier
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .fillMaxWidth()
    ) {
        BoxWithConstraints {
            Card(
                colors = CardDefaults.cardColors(containerColor = backgroundColor),
                shape = bubbleShape,
                modifier = Modifier.widthIn(0.dp, maxWidth * 0.9f)
            ) {
                // Upper Row
                Row(
                    modifier = modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    AvatarsImage(
                        modifier = modifier,
                        resources = R.drawable.gemini_logo,
                        imageVector = Icons.Default.Person,
                        contentDescription = "avatars_logo",
                        isModel = messageEntity.byWhom == Participant.MODEL
                    )

                    Text(
                        text = messageEntity.time,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp)
                    )
                }

                Divider(Modifier.width(2.dp))

                // Show Images when its from user only
                if (messageEntity.byWhom == Participant.USER) {
                    messageEntity.images?.let { imagesList ->
                        LazyRow {
                            items(imagesList) { imageUri ->
                                LocalImageView(imageSize = imageSize, filePath = imageUri)
                            }
                        }
                    }
                    Divider(Modifier.width(2.dp))
                }

                if (isChatLoading) {
                    MessageGeneratingAnimation()
                } else {
                    // Chat Msg
                    Text(
                        text = messageEntity.message,
                        modifier
                            .fillMaxWidth()
                            .padding(all = 24.dp),
                        style = TextStyle(
                            fontSize = 15.sp,
                        ),
                        textAlign = TextAlign.Start
                    )
                }

            }
        }
    }
}