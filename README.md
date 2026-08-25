# Muho Launcher

Mobile-first Android launcher foundation with a custom home screen, app drawer, gesture navigation and System Lab.

## Build

Requirements: JDK 17, Android SDK 35, Gradle 8.9.

```bash
gradle assembleDebug --no-daemon
```

APK output:

`app/build/outputs/apk/debug/app-debug.apk`

GitHub Actions also builds the debug APK on every push to `main` and exposes it as an Actions artifact.

## Install

Install the generated `app-debug.apk` on an Android 8.0+ device. After installation, choose **Muho Launcher** as the Home app when Android asks which launcher to use.

## Current features

- Custom HOME activity
- Installed-app discovery and launcher
- Swipe up: app drawer
- Swipe down: System Lab
- Tap System Lab / Apps panels
- Plugin-ready architecture target for future versions
