package ru.russia.snowaniimationmobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import ru.robbik.snow.animation.SnowAnimation
import ru.russia.snowaniimationmobile.ui.theme.SnowAniimationMobileTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SnowAniimationMobileTheme {
                App()
            }
        }
    }
}

@Composable
private fun App(
    modifier: Modifier = Modifier
) {

    Scaffold(
        modifier = modifier
            .fillMaxSize()
    ) { paddingValues ->
        SnowScreen(
            modifier = Modifier.padding(paddingValues)
        )
    }
}

@Composable
fun SnowScreen(
    modifier: Modifier = Modifier,
    density: Float = 1f,
    speed: Float = 1f,
    snowflakeCount: Int? = null
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        SnowAnimation(
            modifier = Modifier.fillMaxSize(),
            density = density,
            speed = speed,
            snowflakeCount = snowflakeCount,
            color = Color.White
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SnowScreenPreview() {
    SnowAniimationMobileTheme {
        SnowScreen(
            density = 1f,
            speed = 1f
        )
    }
}