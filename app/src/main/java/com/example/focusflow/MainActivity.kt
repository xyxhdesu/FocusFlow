package com.example.focusflow

import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

import android.os.Vibrator
import android.os.VibrationEffect
import android.content.Context
import android.os.Build

class MainActivity : AppCompatActivity() {

    // 定义控件变量
    private lateinit var timerView: CircularTimerView
    private lateinit var tvTime: TextView
    private lateinit var btnStart: Button

    // 倒计时工具
    private var timer: CountDownTimer? = null
    private var isRunning = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 1. 确保这里是 R.layout.activity_main (对应你的xml文件名)
        setContentView(R.layout.activity_main)

        // 2. 初始化控件 (关联 XML 里的 ID)
        timerView = findViewById(R.id.timerView)
        tvTime = findViewById(R.id.tvTime)
        btnStart = findViewById(R.id.btnStart)

        // 3. 设置按钮点击事件
        btnStart.setOnClickListener {
            if (isRunning) {
                stopTimer()
            } else {
                startTimer(25 * 60 * 1000) // 25分钟
            }
        }
    }

    // 开始倒计时逻辑
    private fun startTimer(totalTimeInMillis: Long) {
        isRunning = true
        btnStart.text = "放弃专注" // 改变按钮文字

        timer = object : CountDownTimer(totalTimeInMillis, 100) { // 每100毫秒更新一次
            override fun onTick(millisUntilFinished: Long) {
                // 1. 更新圆环进度 (剩余时间 / 总时间)
                val progress = millisUntilFinished.toFloat() / totalTimeInMillis
                timerView.updateProgress(progress)

                // 2. 更新文字 (格式化为 mm:ss)
                val minutes = (millisUntilFinished / 1000) / 60
                val seconds = (millisUntilFinished / 1000) % 60
                tvTime.text = String.format("%02d:%02d", minutes, seconds)
            }

            override fun onFinish() {
                // 倒计时结束
                isRunning = false
                timerView.updateProgress(0f)
                tvTime.text = "00:00"
                btnStart.text = "开始专注"
                Toast.makeText(this@MainActivity, "专注完成！", Toast.LENGTH_LONG).show()
                // 下一步我们会在这里加上震动和停止音乐
                override fun onFinish() {
                    // 1. UI 归位
                    isRunning = false
                    timerView.updateProgress(0f)
                    tvTime.text = "00:00"
                    btnStart.text = "开始专注"

                    // 2. 触发震动 (核心硬件调用代码)
                    val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                    if (vibrator.hasVibrator()) {
                        // 兼容不同版本的震动 API
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            // 震动 1秒 (1000毫秒)，强度默认
                            vibrator.vibrate(VibrationEffect.createOneShot(1000, VibrationEffect.DEFAULT_AMPLITUDE))
                        } else {
                            // 旧版本写法
                            vibrator.vibrate(1000)
                        }
                    }

                    Toast.makeText(this@MainActivity, "专注完成！", Toast.LENGTH_LONG).show()
                }
            }
        }.start()
    }

    // 停止倒计时
    private fun stopTimer() {
        timer?.cancel()
        isRunning = false
        timerView.updateProgress(1.0f) // 恢复满圈
        tvTime.text = "25:00"
        btnStart.text = "开始专注"
    }
}