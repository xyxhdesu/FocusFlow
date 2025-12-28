# FocusFlow

FocusFlow 是一款帮助用户提升专注力的 Android 应用，通过白噪音播放功能，为用户创造沉浸式的专注环境，辅助高效工作与学习。


## 功能特点

- **白噪音播放**：支持后台持续播放白噪音，帮助用户隔绝干扰、保持专注
- **前台服务保障**：通过前台服务机制，确保白噪音在应用后台运行时不被系统轻易终止
- **状态通知**：实时在通知栏显示专注状态与播放信息，方便用户快速了解应用状态
- **辅助功能页**：包含历史记录（HistoryActivity）、关于应用（AboutActivity）、帮助指南（HelpActivity）等辅助页面，提升用户体验
- **启动引导**：通过 SplashActivity 提供流畅的应用启动体验


## 权限说明

应用运行需要以下权限，均用于保障核心功能正常运行：

- `android.permission.VIBRATE`：支持振动反馈（如专注计时结束提醒等）
- `android.permission.FOREGROUND_SERVICE`、`android.permission.FOREGROUND_SERVICE_MEDIA_PLAYBACK`：保障白噪音后台持续播放的前台服务权限
- `android.permission.POST_NOTIFICATIONS`：在通知栏显示专注状态与播放信息
- `android.permission.INTERNET`：可能用于在线白噪音资源加载（如适用）


## 技术栈

- 开发语言：Kotlin
- 基础框架：Android SDK
- 核心组件：Service（前台服务）、Notification（通知）、MediaPlayer（音频播放）
- 适配版本：支持 Android 8.0（API 26）及以上（因使用通知渠道特性）


## 安装与运行

1. 克隆或下载项目代码到本地
2. 使用 Android Studio 打开项目
3. 连接 Android 设备或启动模拟器
4. 点击运行按钮（Run），等待应用安装完成


## 注意事项

- 为确保白噪音持续播放，请勿手动关闭应用的前台通知
- 部分功能可能需要授予对应的权限（如通知权限）后才能正常使用