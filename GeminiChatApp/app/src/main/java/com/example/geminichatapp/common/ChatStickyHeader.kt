package com.example.geminichatapp.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ChatStickyHeader(modifier: Modifier = Modifier, heading: String) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.LightGray) // Replace with your desired background color
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = heading,
            modifier = Modifier.align(Alignment.Center),
            style = TextStyle(
                fontSize = 18.sp, // Adjust font size as needed
                fontWeight = FontWeight.Bold
            )
        )
    }
}