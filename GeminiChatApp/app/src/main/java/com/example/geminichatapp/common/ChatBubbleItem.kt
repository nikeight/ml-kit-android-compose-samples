package com.example.geminichatapp.common

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.geminichatapp.R
import com.example.geminichatapp.data.model.MessageEntity
import com.example.geminichatapp.data.model.Participant

@Composable
fun ChatBubbleItem(
    modifier: Modifier = Modifier,
    messageEntity: MessageEntity,
    isChatLoading: Boolean,
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
        Row {
            if (isChatLoading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(all = 8.dp)
                )
            }

        }

        BoxWithConstraints {
            Card(
                colors = CardDefaults.cardColors(containerColor = backgroundColor),
                shape = bubbleShape,
                modifier = Modifier.widthIn(0.dp, maxWidth * 0.9f)
            ) {
                Row(
                    modifier = modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    if (isModelMessage) {
                        Icon(
                            modifier = Modifier.size(16.dp),
                            painter = painterResource(id = R.drawable.gemini_logo),
                            contentDescription = "gemini_logo"
                        )
                    } else {
                        Icon(
                            modifier = Modifier.size(16.dp),
                            imageVector = Icons.Default.Person,
                            contentDescription = "gemini_logo"
                        )
                    }

                    Text(
                        text = messageEntity.message,
                        modifier = Modifier.padding(16.dp)
                    )

                    // Show Images when its from user only
                    if (messageEntity.byWhom == Participant.USER) {
                        messageEntity.images?.let { uriList ->
                            uriList.forEach { uri ->
                                AsyncImage(
                                    model = Uri.parse(uri),
                                    contentDescription = null,
                                    contentScale = ContentScale.Fit,
                                    modifier = Modifier
                                        .padding(4.dp)
                                        .requiredSize(240.dp)
                                )
                            }
                        }
                    }
                }

                Box(
                    contentAlignment = Alignment.BottomEnd
                ) {
                    Text(
                        text = messageEntity.time,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp)
                    )
                }
            }
        }
    }
}