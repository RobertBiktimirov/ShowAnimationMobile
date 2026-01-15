package ru.robbik.snow.iosApp

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import ru.robbik.snow.animation.SnowAnimation

@Composable
fun App() {
    MaterialTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color.Black
        ) {
            SnowAnimation(
                modifier = Modifier.fillMaxSize(),
                density = 1f,
                speed = 1f,
                color = Color.White
            )
        }
    }
}

