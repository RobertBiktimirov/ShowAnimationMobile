package ru.robbik.snow.iosApp

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.cValue
import platform.CoreGraphics.CGRectZero
import platform.UIKit.UIApplication
import platform.UIKit.UIApplicationDelegateProtocol
import platform.UIKit.UIApplicationDelegateProtocolMeta
import platform.UIKit.UIResponder
import platform.UIKit.UIWindow

@Suppress("CONSTANT_UPPER_BOUND")
class AppDelegate : UIResponder(), UIApplicationDelegateProtocol {
    companion object : UIResponder(), UIApplicationDelegateProtocolMeta

    var windowUi: UIWindow? = null

    @OptIn(ExperimentalForeignApi::class)
    override fun application(
        application: UIApplication,
        didFinishLaunchingWithOptions: Map<Any?, *>?
    ): Boolean {
        windowUi = UIWindow(frame = cValue<platform.CoreGraphics.CGRect> { platform.CoreGraphics.CGRectZero })
        windowUi?.rootViewController = MainViewController()
        windowUi?.makeKeyAndVisible()
        return true
    }
}

@OptIn(ExperimentalForeignApi::class)
fun main() {
    platform.UIKit.UIApplicationMain(0, null, null, "ru.robbik.snow.iosApp.AppDelegate")
}

