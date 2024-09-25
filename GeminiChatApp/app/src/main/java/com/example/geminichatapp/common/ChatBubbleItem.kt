package com.example.geminichatapp.common

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.dp
import com.example.geminichatapp.data.model.Participant
import com.example.geminichatapp.features.text_chat.Message

@Composable
fun ChatBubbleItem(
    modifier: Modifier = Modifier,
    messageEntity: Message,
) {
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
                modifier = Modifier.widthIn(0.dp, maxWidth * 0.8f)
            ) {
                ChatBubbleContent(
                    textMsg = messageEntity.message,
                    imagesList = messageEntity.images,
                    imageSize = Size(width = 180f, height = 180f),
                    participant = messageEntity.byWhom,
                    msgDate = messageEntity.time,
                    maxLines = null
                )
            }
        }
    }
}