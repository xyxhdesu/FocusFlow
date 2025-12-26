package com.example.focusflow

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // 隐藏顶部的标题栏 (全屏体验)
        supportActionBar?.hide()

        // 延迟 3秒 (3000毫秒) 后跳转
        Handler(Looper.getMainLooper()).postDelayed({
            // 跳转到 MainActivity
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            // 关闭当前页面，防止用户按返回键回到启动页
            finish()
        }, 3000)
    }
}