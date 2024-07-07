package com.example.geminichatapp.features.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.geminichatapp.R

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    onProceedClicked: () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Hello,\nWelcome",
            style = TextStyle(
                fontSize = 54.sp,
                textAlign = TextAlign.Center,
            )
        )

        Image(
            painter = painterResource(id = R.drawable.gemini_img),
            contentDescription = "gemini_img",
            modifier.size(94.dp)
        )

        Text(
            text = "Gemini Chat App",
            style = TextStyle(
                fontSize = 20.sp,
                textAlign = TextAlign.Start,
            )
        )

        Text(
            text = "Provide your own API Key at Local config.\nGo through `Docs`",
            style = TextStyle(
                fontSize = 14.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )
        )

        Button(
            modifier = modifier.fillMaxWidth(0.8f),
            onClick = {
                onProceedClicked()
            }) {
            Text(text = "Proceed")
        }

        Text(
            text = "Terms and Condition", style = TextStyle(
                fontSize = 14.sp,
                color = Color.Gray
            )
        )
    }
}