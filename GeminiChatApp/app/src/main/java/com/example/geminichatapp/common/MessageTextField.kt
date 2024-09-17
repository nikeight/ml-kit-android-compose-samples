package com.example.geminichatapp.common

import android.icu.util.Calendar
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.SaverScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import com.example.geminichatapp.R
import com.example.geminichatapp.data.model.MessageEntity
import com.example.geminichatapp.data.model.Participant
import com.example.geminichatapp.ui.components.LocalImageView
import com.example.geminichatapp.util.decodeUriToFilePath
import com.example.geminichatapp.util.getCurrentDate
import com.example.geminichatapp.util.getCurrentTime
import java.util.UUID

@Composable
fun MessageTextField(
    modifier: Modifier = Modifier,
    onSendMessage: (MessageEntity) -> Unit,
    onSendImagePrompt: (MessageEntity) -> Unit,
    channelId: UUID,
) {
    val context = LocalContext.current
    var userMessage by rememberSaveable { mutableStateOf("") }
    var imageUris = rememberSaveable(saver = FilePathSaver()) { mutableStateListOf() }

//        ActivityResultContracts.PickVisualMedia()
    val pickMedia = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { imageUri ->
        imageUri?.let { uri ->
            val filePath = decodeUriToFilePath(context, uri)
            filePath?.let { path ->
                imageUris.add(path)
            }
        }
    }

//    val pickMedia = rememberLauncherForActivityResult(
//        ActivityResultContracts.StartActivityForResult()
//    ) { result ->
//        if (result.resultCode == RESULT_OK) {
//            val imageData = result.data
//            if (imageData != null) {
//                val imageFilePath = imageData.data
//                imageFilePath?.let { pathUri ->
//                    imageUris.add(pathUri)
//                }
//            }
//        }
//    }

    ElevatedCard(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Card {
            if (imageUris.isNotEmpty()) {
                LazyRow(
                    modifier = Modifier.padding(all = 8.dp)
                ) {
                    items(imageUris) { imageUri ->
                        LocalImageView(
                            imageSize = Size(width = 72f, height = 72f),
                            filePath = imageUri,
                        )
                    }
                }
            }
        }

        Row(
            modifier = modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            IconButton(
                onClick = {
//                    pickMedia.launch(
//                        PickVisualMediaRequest(
//                            ActivityResultContracts.PickVisualMedia.ImageOnly
//                        )
//                    )

                    pickMedia.launch(
                        arrayOf("image/*")
                    )
                },
                modifier = Modifier
                    .padding(all = 4.dp)
                    .align(Alignment.CenterVertically)
            ) {
                Icon(
                    Icons.Rounded.Add,
                    contentDescription = stringResource(R.string.add_image),
                )
            }

            OutlinedTextField(
                value = userMessage,
                label = { Text(stringResource(R.string.chat_label)) },
                onValueChange = { userMessage = it },
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                ),
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .fillMaxWidth()
                    .weight(0.85f)
            )
            IconButton(
                onClick = {
                    if (imageUris.isNotEmpty() && userMessage.isNotBlank()) {
                        onSendImagePrompt(
                            MessageEntity(
                                time = Calendar.getInstance().time.toString(),
                                date = Calendar.getInstance().time,
                                message = userMessage,
                                byWhom = Participant.USER,
                                foreignChannelId = channelId,
                                images = imageUris
                            )
                        )
                        userMessage = ""
                        imageUris = mutableListOf()
                    } else
                        if (userMessage.isNotBlank()) {
                            val currentDate = getCurrentDate()
                            onSendMessage(
                                MessageEntity(
                                    time = currentDate.getCurrentTime(),
                                    date = currentDate,
                                    message = userMessage,
                                    byWhom = Participant.USER,
                                    foreignChannelId = channelId
                                )
                            )
                            userMessage = ""
                        }
                },
                modifier = Modifier
                    .padding(start = 16.dp)
                    .align(Alignment.CenterVertically)
                    .fillMaxWidth()
                    .weight(0.15f)
            ) {
                Icon(
                    Icons.Default.Send,
                    contentDescription = stringResource(R.string.action_send),
                    modifier = Modifier
                )
            }
        }
    }
}

class FilePathSaver : Saver<MutableList<String>, List<String>> {
    override fun restore(value: List<String>): MutableList<String> = value.toMutableList()

    override fun SaverScope.save(value: MutableList<String>): List<String> =
        value.map { it.toString() }
}