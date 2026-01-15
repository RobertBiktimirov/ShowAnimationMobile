# Быстрый старт

## 1. Откройте проект в Xcode

```bash
cd iosApp/Xcode
open SnowApp.xcodeproj
```

## 2. Соберите Kotlin Multiplatform Framework

Перед сборкой iOS приложения, соберите framework:

```bash
cd iosApp/Xcode
./create_xcframework.sh
```

Или вручную:
```bash
cd ../..
./gradlew :snow:linkDebugFrameworkIosArm64 :snow:linkDebugFrameworkIosSimulatorArm64
```

## 3. Добавьте Framework в Xcode проект

1. Откройте `SnowApp.xcodeproj` в Xcode
2. Выберите проект в навигаторе → Target `SnowApp` → General
3. В разделе "Frameworks, Libraries, and Embedded Content" нажмите "+"
4. Нажмите "Add Other..." → "Add Files..."
5. Выберите `snow/build/XCFrameworks/snowKit.xcframework` (если создали XCFramework)
   ИЛИ выберите `snow/build/bin/iosSimulatorArm64/debugFramework/snowKit.framework` для симулятора
6. Убедитесь, что выбрано "Embed & Sign"

## 4. Добавьте Compose Multiplatform через Swift Package Manager

1. File → Add Packages...
2. Добавьте: `https://github.com/JetBrains/compose-multiplatform-ios`
3. Выберите версию `1.9.3`
4. Добавьте пакет `ComposeUI` в target `SnowApp`

## 5. Соберите и запустите

1. Выберите симулятор iOS
2. Нажмите Run (⌘R)

Готово! 🎉

