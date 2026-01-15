package ru.robbik.snow.haptic

import androidx.compose.runtime.Composable

/**
 * Expect функция для получения HapticFeedback на разных платформах
 */
@Composable
expect fun rememberHapticFeedback(): HapticFeedback

/**
 * Интерфейс для haptic feedback, который работает на всех платформах
 */
interface HapticFeedback {
    fun performHapticFeedback(type: HapticFeedbackType)
}

/**
 * Типы haptic feedback
 */
enum class HapticFeedbackType {
    LongPress,
    TextHandleMove
}

