package ru.robbik.snow.haptic

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import platform.UIKit.UIImpactFeedbackGenerator
import platform.UIKit.UIImpactFeedbackStyle
import platform.UIKit.UINotificationFeedbackGenerator
import platform.UIKit.UINotificationFeedbackType

@Composable
actual fun rememberHapticFeedback(): HapticFeedback = remember(Unit) {
    object : HapticFeedback {
        override fun performHapticFeedback(type: HapticFeedbackType) {
            when (type) {
                HapticFeedbackType.LongPress -> {
                    val generator = UIImpactFeedbackGenerator(
                        style = UIImpactFeedbackStyle.UIImpactFeedbackStyleMedium
                    )
                    generator.prepare()
                    generator.impactOccurred()
                }

                HapticFeedbackType.TextHandleMove -> {
                    val generator = UINotificationFeedbackGenerator()
                    generator.prepare()
                    generator.notificationOccurred(
                        UINotificationFeedbackType.UINotificationFeedbackTypeSuccess
                    )
                }
            }
        }
    }
}

