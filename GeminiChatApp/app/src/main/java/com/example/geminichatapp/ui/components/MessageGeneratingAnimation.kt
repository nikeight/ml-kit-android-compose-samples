package com.example.geminichatapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun MessageGeneratingAnimation() {
    val dotSize = 10.dp
    val dotSpacing = 4.dp
    val animationDuration = 800L

    val dotsState = remember { mutableStateOf(listOf(0f, 0f, 0f)) }
    LaunchedEffect(key1 = true) {
        while (true) {
            dotsState.value = dotsState.value.map { it + 1f }
            delay(animationDuration)
            dotsState.value = dotsState.value.map { if (it >= 1f) 0f else it }
            delay(animationDuration)
        }
    }

    Row(horizontalArrangement = Arrangement.spacedBy(dotSpacing)) {
        dotsState.value.forEachIndexed { index, progress ->
            Box(
                modifier = Modifier
                    .size(dotSize)
                    .clip(CircleShape)
                    .background(
                        shape = CircleShape,
                        brush = Brush.radialGradient(
                            colors = listOf(
                                Color.Gray,
                                Color.Transparent
                            ),
                            center = Offset(0f, 0f),
                            radius = dotSize.value * progress
                        )
                    )
            )
        }
    }
}