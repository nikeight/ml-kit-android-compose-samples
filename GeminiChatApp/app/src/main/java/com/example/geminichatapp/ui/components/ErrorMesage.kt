package com.example.geminichatapp.ui.components

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ErrorMessage(
    modifier: Modifier = Modifier,
    errorMessage: String,
    linkText: String = "TRY ONLINE TOOL",
    geminiUrl: String = "https://gemini.google.com/"
) {
    val context = LocalContext.current
    Card(
        modifier = modifier
            .wrapContentSize(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.error.copy(alpha = 0.5f)),
    ) {
        Text(
            modifier = modifier.padding(horizontal = 24.dp, vertical = 8.dp),
            text = "Error : $errorMessage", style = TextStyle(
                fontWeight = FontWeight.Normal,
                fontSize = 20.sp,
                textAlign = TextAlign.Start

            )
        )
        Text(
            text = linkText,
            modifier = modifier
                .padding(horizontal = 24.dp, vertical = 8.dp)
                .clickable {
                    // Open the link in a browser
                    val uri = Uri.parse(geminiUrl)
                    val intent = Intent(Intent.ACTION_VIEW, uri)
                    context.startActivity(intent)
                },
            style = TextStyle(
                textDecoration = TextDecoration.Underline,
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp,
                textAlign = TextAlign.Start,
                color = Color.Blue
            )
        )
    }
}