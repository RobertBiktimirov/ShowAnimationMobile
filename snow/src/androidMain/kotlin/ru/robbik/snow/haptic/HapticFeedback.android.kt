package ru.robbik.snow.haptic

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType as ComposeHapticFeedbackType

@Composable
actual fun rememberHapticFeedback(): HapticFeedback {
    val hapticFeedback = LocalHapticFeedback.current
    return object : HapticFeedback {
        override fun performHapticFeedback(type: HapticFeedbackType) {
            val composeType = when (type) {
                HapticFeedbackType.LongPress -> ComposeHapticFeedbackType.LongPress
                HapticFeedbackType.TextHandleMove -> ComposeHapticFeedbackType.TextHandleMove
            }
            hapticFeedback.performHapticFeedback(composeType)
        }
    }
}

