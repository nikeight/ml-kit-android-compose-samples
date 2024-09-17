package com.example.geminichatapp.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.geminichatapp.R

@Composable
fun LocalImageView(
    modifier: Modifier = Modifier,
    imageSize: Size,
    filePath: String
) {
    Image(
        painter = rememberAsyncImagePainter(
            filePath,
            placeholder = painterResource(
                R.drawable.gemini_img,
            )
        ),
        contentDescription = "chat_user_input_image",
        contentScale = ContentScale.Fit,
        modifier = modifier
            .padding(4.dp)
            .requiredSize(
                width = imageSize.width.dp,
                height = imageSize.height.dp,
            )
            .clip(
                RoundedCornerShape(
                    corner = CornerSize(
                        size = 4.dp,
                    ),
                ),
            )
    )
}