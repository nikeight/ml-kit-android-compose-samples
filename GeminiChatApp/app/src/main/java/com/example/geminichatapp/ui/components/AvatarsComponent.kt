package com.example.geminichatapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun AvatarsImage(
    modifier: Modifier,
    resources: Int,
    imageVector: ImageVector,
    contentDescription: String,
    backgroundColor: Color = Color.White,
    isModel: Boolean,
) {
    Box(
        modifier = modifier
            .size(32.dp)
            .clip(CircleShape)
            .background(backgroundColor),
        contentAlignment = Alignment.Center,
    ) {
        if (isModel) {
            Icon(
                modifier = Modifier.size(24.dp),
                painter = painterResource(id = resources),
                contentDescription = contentDescription,
                tint = Color.Gray
            )
        } else {
            Icon(
                modifier = Modifier.size(24.dp),
                imageVector = imageVector,
                contentDescription = contentDescription,
                tint = Color.Gray
            )
        }
    }
}