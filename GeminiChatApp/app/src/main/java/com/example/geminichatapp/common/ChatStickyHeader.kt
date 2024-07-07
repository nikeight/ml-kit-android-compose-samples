package com.example.geminichatapp.common

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp

@Composable
fun ChatStickyHeader(modifier: Modifier = Modifier, heading: String) {
    Text(text = heading, style = TextStyle(fontSize = 12.sp, color = Color.Gray,))
}