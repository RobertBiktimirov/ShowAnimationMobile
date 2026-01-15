# iOS Xcode Application

Это нативное iOS приложение, использующее Kotlin Multiplatform библиотеку `snow` для отображения анимации снега.

## Структура проекта

- `SnowApp/` - Основные файлы приложения
  - `AppDelegate.swift` - Делегат приложения (UIKit)
  - `MainViewController.swift` - Главный View Controller с Compose UI
  - `SnowApp.swift` - Точка входа для SwiftUI версии
  - `ContentView.swift` - SwiftUI View с Compose
  - `Info.plist` - Конфигурация приложения

## Настройка Xcode проекта

### 1. Открытие готового проекта

Xcode проект уже создан! Просто откройте его:

```bash
cd iosApp/Xcode
open SnowApp.xcodeproj
```

Или через Finder: найдите файл `SnowApp.xcodeproj` и дважды кликните на него.

### 2. Сборка Kotlin Multiplatform Framework

Перед настройкой Xcode проекта, соберите framework:

**Вариант 1: Сборка отдельных frameworks**
```bash
cd /path/to/SnowAniimationMobile
./gradlew :snow:linkDebugFrameworkIosX64 :snow:linkDebugFrameworkIosArm64 :snow:linkDebugFrameworkIosSimulatorArm64
```

**Вариант 2: Использование скрипта для сборки frameworks**
```bash
cd iosApp/Xcode
./build_framework.sh
```

**Вариант 3: Создание XCFramework (рекомендуется)**
```bash
cd iosApp/Xcode
./create_xcframework.sh
```

XCFramework будет создан в: `snow/build/XCFrameworks/snowKit.xcframework`

### 3. Добавление Framework в Xcode проект

1. Откройте проект в Xcode
2. Выберите проект в навигаторе
3. Выберите target `SnowApp`
4. Перейдите на вкладку "General"
5. В разделе "Frameworks, Libraries, and Embedded Content" нажмите "+"
6. Нажмите "Add Other..." → "Add Files..."
7. Выберите `snow/build/XCFrameworks/snowKit.xcframework`
8. Убедитесь, что выбрано "Embed & Sign"

### 4. Настройка Build Settings

1. Выберите проект → Target `SnowApp` → Build Settings
2. Найдите "Framework Search Paths"
3. Добавьте путь: `$(SRCROOT)/../../../snow/build/XCFrameworks`
4. Убедитесь, что "Always Search User Paths" = Yes

### 5. Добавление Swift файлов

Скопируйте Swift файлы из этой директории в ваш Xcode проект:
- `AppDelegate.swift` (если используете UIKit)
- `MainViewController.swift` (если используете UIKit)
- `SnowApp.swift` (если используете SwiftUI)
- `ContentView.swift` (если используете SwiftUI)

### 6. Настройка Info.plist

Замените `Info.plist` в проекте на файл из этой директории или скопируйте необходимые ключи.

### 7. Добавление зависимостей Compose Multiplatform

В Xcode проекте нужно добавить зависимости Compose Multiplatform через Swift Package Manager.

**Через Swift Package Manager:**
1. File → Add Packages...
2. Добавьте репозиторий: `https://github.com/JetBrains/compose-multiplatform-ios`
3. Выберите версию, соответствующую `composeMultiplatform = "1.9.3"` из вашего `libs.versions.toml`
4. Добавьте пакет `ComposeUI` в target `SnowApp`

**Альтернатива:** Если вы используете готовый executable из модуля `iosApp`, зависимости Compose уже включены в framework.

### 8. Сборка и запуск

1. Выберите симулятор или устройство
2. Нажмите Run (⌘R)

## Альтернативный вариант: Использование готового executable

Если вы хотите использовать уже скомпилированный executable из модуля `iosApp`:

1. Соберите executable:
```bash
./gradlew :iosApp:linkDebugExecutableIosSimulatorArm64
```

2. Найденный executable будет в:
   `iosApp/build/bin/iosSimulatorArm64/debugExecutable/iosApp.kexe`

3. Вы можете интегрировать его в Xcode проект или запустить напрямую на симуляторе.

## Важно: Имена функций в Swift

После компиляции Kotlin кода, функции будут доступны в Swift с суффиксом `Kt`:
- `fun App()` → `AppKt.App()` в Swift
- `fun MainViewController()` → `MainViewControllerKt.MainViewController()` в Swift

Убедитесь, что framework `snowKit` и модуль `iosApp` правильно экспортируют эти функции.

## Примечания

- Framework должен быть пересобран после изменений в модуле `snow`
- Убедитесь, что версии Compose Multiplatform совпадают в Gradle и Xcode проекте
- Для production сборки используйте release конфигурацию framework

