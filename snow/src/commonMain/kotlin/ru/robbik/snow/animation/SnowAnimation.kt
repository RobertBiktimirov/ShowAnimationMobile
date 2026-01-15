package ru.robbik.snow.animation

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.input.pointer.pointerInput
import kotlinx.coroutines.delay
import ru.robbik.snow.haptic.HapticFeedbackType
import ru.robbik.snow.haptic.rememberHapticFeedback
import kotlin.math.abs
import kotlin.math.sqrt
import kotlin.random.Random

data class Snowflake(
    var x: Float,
    var y: Float,
    val size: Float,
    var speed: Float,
    val windSpeed: Float,
    val opacity: Float,
    val baseSpeed: Float,
    var bounceVelocityX: Float = 0f,
    var bounceVelocityY: Float = 0f,
    var bounceDecay: Float = 0.95f
)

private fun createSnowflake(
    width: Float,
    height: Float,
    speedMultiplier: Float,
    startY: Float? = null,
): Snowflake {
    val random = Random.Default
    val baseSpeed = 1.5f + random.nextFloat() * 2.0f
    val actualSpeed = baseSpeed * speedMultiplier.coerceIn(0.1f, 5.0f)
    
    return Snowflake(
        x = random.nextFloat() * width,
        y = startY ?: (-random.nextFloat() * height * 0.5f - 50f),
        size = 2f + random.nextFloat() * 6f,
        speed = actualSpeed,
        windSpeed = -0.5f + random.nextFloat() * 1.0f,
        opacity = 0.5f + random.nextFloat() * 0.5f,
        baseSpeed = baseSpeed,
        bounceVelocityX = 0f,
        bounceVelocityY = 0f,
        bounceDecay = 0.95f
    )
}

/**
 * Composable функция для анимации снега на весь экран
 * 
 * @param modifier Модификатор для настройки размера и позиции
 * @param density Плотность снега (0.0 - 1.0), влияет на количество снежинок
 * @param speed Скорость падения снега (0.0 - 2.0)
 * @param snowflakeCount Количество снежинок (переопределяет density если указано)
 * @param color Цвет снежинок
 */

fun Modifier.showSnow() = Modifier
    .drawWithContent {

    }

@Composable
fun SnowAnimation(
    modifier: Modifier = Modifier,
    density: Float = 0.5f,
    speed: Float = 0.5f,
    snowflakeCount: Int? = null,
    color: Color = Color.White
) {
    val hapticFeedback = rememberHapticFeedback()
    var canvasSize by remember { mutableStateOf(Size.Zero) }
    var animationFrame by remember { mutableIntStateOf(0) }
    
    val count = remember(density, canvasSize, snowflakeCount) {
        if (canvasSize.width == 0f || canvasSize.height == 0f) {
            100
        } else {
            snowflakeCount ?: (canvasSize.width * canvasSize.height * density / 5000).toInt().coerceIn(50, 1000)
        }
    }

    val snowflakes = remember { mutableStateListOf<Snowflake>() }
    
    var isInitialized by remember { mutableStateOf(false) }
    
    var targetSnowflakeCount by remember { mutableIntStateOf(0) }

    LaunchedEffect(canvasSize.width, canvasSize.height) {
        if (canvasSize.width > 0f && canvasSize.height > 0f && !isInitialized) {
            val targetCount = count.coerceIn(50, 1000)
            targetSnowflakeCount = targetCount
            snowflakes.clear()
            repeat(targetCount) { index ->
                val startY = -canvasSize.height * 0.5f - (index % 10) * 50f
                snowflakes.add(createSnowflake(canvasSize.width, canvasSize.height, speed, startY))
            }
            isInitialized = true
        }
    }
    
    LaunchedEffect(count) {
        if (isInitialized && canvasSize.width > 0f && canvasSize.height > 0f) {
            val newTargetCount = count.coerceIn(50, 1000)
            targetSnowflakeCount = newTargetCount
            val currentCount = snowflakes.size
            
            if (newTargetCount > currentCount) {
                repeat(newTargetCount - currentCount) {
                    snowflakes.add(createSnowflake(canvasSize.width, canvasSize.height, speed))
                }
            }
        }
    }
    
    LaunchedEffect(speed) {
        if (isInitialized) {
            snowflakes.forEach { snowflake ->
                snowflake.speed = snowflake.baseSpeed * speed.coerceIn(0.1f, 5.0f)
            }
        }
    }

    LaunchedEffect(Unit) {
        while (true) {
            delay(16) // ~60 FPS
            if (canvasSize.width > 0 && canvasSize.height > 0 && isInitialized) {
                val targetCount = if (targetSnowflakeCount > 0) {
                    targetSnowflakeCount.coerceIn(50, 1000)
                } else {
                    count.coerceIn(50, 1000)
                }
                
                if (snowflakes.isEmpty()) {
                    repeat(targetCount) {
                        snowflakes.add(createSnowflake(canvasSize.width, canvasSize.height, speed))
                    }
                }
                
                if (snowflakes.size > targetCount) {
                    snowflakes.removeAll { 
                        it.y > canvasSize.height + 200f || it.y < -200f
                    }
                }
                
                if (snowflakes.size < targetCount) {
                    repeat(targetCount - snowflakes.size) {
                        snowflakes.add(createSnowflake(canvasSize.width, canvasSize.height, speed))
                    }
                }
                
                snowflakes.forEach { snowflake ->
                    updateSnowflake(
                        snowflake, 
                        canvasSize.width, 
                        canvasSize.height,
                        targetCount
                    )
                }
                animationFrame++
            }
        }
    }

    val touchRadius = 100f
    val bounceForce = 8f
    
    fun bounceSnowflakesAtPosition(offset: Offset) {
        var hasBounced = false
        snowflakes.forEach { snowflake ->
            val dx = snowflake.x - offset.x
            val dy = snowflake.y - offset.y
            val distance = sqrt(dx * dx + dy * dy)
            
            if (distance < touchRadius && distance > 0f) {
                val normalizedDx = dx / distance
                val normalizedDy = dy / distance
                
                val force = bounceForce * (1f - distance / touchRadius)
                snowflake.bounceVelocityX += normalizedDx * force
                snowflake.bounceVelocityY += normalizedDy * force
                hasBounced = true
            }
        }
        if (hasBounced) {
            hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
        }
    }
    
    Box(
        modifier = modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures { offset ->
                    bounceSnowflakesAtPosition(offset)
                }
            }
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = { offset ->
                        bounceSnowflakesAtPosition(offset)
                    },
                    onDrag = { change, _ ->
                        bounceSnowflakesAtPosition(change.position)
                    },
                )
            }
    ) {
        key(animationFrame) {
            Canvas(
                modifier = Modifier.fillMaxSize()
            ) {
                val currentSize = size
                if (canvasSize.width == 0f || canvasSize.height == 0f) {
                    canvasSize = currentSize
                } else if (abs(canvasSize.width - currentSize.width) > 1f ||
                           abs(canvasSize.height - currentSize.height) > 1f) {
                    canvasSize = currentSize
                }
                
                if (canvasSize.width > 0f && canvasSize.height > 0f) {
                    snowflakes.forEach { snowflake ->
                        drawSnowflake(snowflake, color)
                    }
                }
            }
        }
    }
}

private fun updateSnowflake(
    snowflake: Snowflake,
    width: Float,
    height: Float,
    targetCount: Int
) {
    snowflake.x += snowflake.bounceVelocityX
    snowflake.y += snowflake.bounceVelocityY
    
    snowflake.bounceVelocityX *= snowflake.bounceDecay
    snowflake.bounceVelocityY *= snowflake.bounceDecay
    
    if (abs(snowflake.bounceVelocityX) < 0.1f) {
        snowflake.bounceVelocityX = 0f
    }
    if (abs(snowflake.bounceVelocityY) < 0.1f) {
        snowflake.bounceVelocityY = 0f
    }
    
    snowflake.y += snowflake.speed * 1.2f
    
    snowflake.x += snowflake.windSpeed * 0.3f
    
    if (snowflake.y > height) {
        snowflake.y = -snowflake.size
        snowflake.x = Random.Default.nextFloat() * width
        snowflake.bounceVelocityX = 0f
        snowflake.bounceVelocityY = 0f
    }
    
    if (snowflake.x < 0) {
        snowflake.x = width
    } else if (snowflake.x > width) {
        snowflake.x = 0f
    }
}

private fun DrawScope.drawSnowflake(
    snowflake: Snowflake,
    color: Color
) {
    val center = Offset(snowflake.x, snowflake.y)
    val radius = snowflake.size / 2f
    
    drawCircle(
        color = color.copy(alpha = snowflake.opacity),
        radius = radius,
        center = center
    )
    
    val rayLength = radius * 1.5f
    val rayWidth = radius * 0.3f
    
    drawCircle(
        color = color.copy(alpha = snowflake.opacity * 0.7f),
        radius = rayWidth,
        center = Offset(center.x - rayLength, center.y)
    )
    drawCircle(
        color = color.copy(alpha = snowflake.opacity * 0.7f),
        radius = rayWidth,
        center = Offset(center.x + rayLength, center.y)
    )
    
    drawCircle(
        color = color.copy(alpha = snowflake.opacity * 0.7f),
        radius = rayWidth,
        center = Offset(center.x, center.y - rayLength)
    )
    drawCircle(
        color = color.copy(alpha = snowflake.opacity * 0.7f),
        radius = rayWidth,
        center = Offset(center.x, center.y + rayLength)
    )
}

