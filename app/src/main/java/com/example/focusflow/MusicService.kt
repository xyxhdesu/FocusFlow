package com.example.focusflow

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat

class MusicService : Service() {

    private var mediaPlayer: MediaPlayer? = null
    private val CHANNEL_ID = "FocusChannel"

    override fun onCreate() {
        super.onCreate()
        // 1. 初始化播放器
        try {
            // 这里的 R.raw.rain 对应你刚才放入的文件
            mediaPlayer = MediaPlayer.create(this, R.raw.rain)
            mediaPlayer?.isLooping = true // 设置循环播放
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // 2. 创建通知渠道 (Android 8.0+ 必须)
        createNotificationChannel()

        // 3. 构建通知
        val notification: Notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Focus Flow")
            .setContentText("正在专注中...白噪音播放中")
            .setSmallIcon(R.drawable.ic_launcher_foreground) // 使用系统默认图标
            .build()

        // 4. 启动前台服务 (关键！这行代码让 Service 变成“不死之身”)
        startForeground(1, notification)

        // 5. 开始播放
        mediaPlayer?.start()

        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        // 停止播放并释放资源
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null // 我们不需要绑定，只需要启动
    }

    // 创建通知渠道的样板代码
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID,
                "专注服务通知",
                NotificationManager.IMPORTANCE_LOW // 低重要性，不会发出声音打扰专注
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(serviceChannel)
        }
    }
}