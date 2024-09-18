package com.example.geminichatapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun MessageGeneratingAnimation(
    dotSize: Dp = 10.dp,
    dotSpacing: Dp = 4.dp,
    animationDuration: Long = 800L
) {
    val offsetValues = remember { mutableStateOf(listOf(0f, 0f, 0f)) }
    LaunchedEffect(key1 = true) {
        while (true) {
            offsetValues.value.forEachIndexed { offsetValueIndex, fl ->
                offsetValues.value = offsetValues.value.mapIndexed { index, offset ->
                    if (index == offsetValueIndex) (offset - 3f) else fl
                }
                delay(animationDuration / 3)
            }
            // reset things
            offsetValues.value = offsetValues.value.map { it ->
                it.times(0)
            }
        }
    }

    Row(horizontalArrangement = Arrangement.spacedBy(dotSpacing)) {
        offsetValues.value.forEach { offset ->
            Box(
                modifier = Modifier
                    .size(dotSize)
                    .offset(y = offset.dp)
                    .clip(CircleShape)
                    .background(
                        color = Color.Gray,
                        shape = CircleShape
                    )
            )
        }
    }
}