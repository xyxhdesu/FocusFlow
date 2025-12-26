package com.example.focusflow

import android.content.Context
import android.content.Intent
import android.os.*
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
class MainActivity : AppCompatActivity() {

    private lateinit var timerView: CircularTimerView
    private lateinit var tvTime: TextView
    private lateinit var btnStart: Button
    private lateinit var sbTime: SeekBar // 新增

    private var timer: CountDownTimer? = null
    private var isRunning = false

    // 当前设定的时长（默认为25分钟，单位毫秒）
    private var currentDuration: Long = 25 * 60 * 1000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.btnHelp).setOnClickListener {
            startActivity(Intent(this, HelpActivity::class.java))
        }

        findViewById<Button>(R.id.btnAbout).setOnClickListener {
            startActivity(Intent(this, AboutActivity::class.java))
        }

        findViewById<Button>(R.id.btnHistory).setOnClickListener {
            startActivity(Intent(this, HistoryActivity::class.java))
        }

        timerView = findViewById(R.id.timerView)
        tvTime = findViewById(R.id.tvTime)
        btnStart = findViewById(R.id.btnStart)
        sbTime = findViewById(R.id.sbTime) // 绑定 SeekBar

        // 1. 设置 SeekBar 的监听器
        sbTime.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    // 防止设置为0分钟，最少1分钟
                    val minutes = if (progress < 1) 1 else progress

                    // 更新全局时长变量
                    currentDuration = minutes * 60 * 1000L

                    // 更新界面文字显示
                    tvTime.text = String.format("%02d:00", minutes)
                }
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        // 2. 按钮点击
        btnStart.setOnClickListener {
            if (isRunning) {
                stopTimer()
            } else {
                startTimer(currentDuration) // 使用 SeekBar 设定的时间
            }
        }
    }

    private fun startTimer(totalTimeInMillis: Long) {
        isRunning = true
        btnStart.text = "放弃"
        sbTime.isEnabled = false // 专注期间禁止调节时间

        startForegroundService(Intent(this, MusicService::class.java))

        timer = object : CountDownTimer(totalTimeInMillis, 100) {
            override fun onTick(millisUntilFinished: Long) {
                // 更新进度条
                val progress = millisUntilFinished.toFloat() / totalTimeInMillis
                timerView.updateProgress(progress)

                // 更新时间文字
                val minutes = (millisUntilFinished / 1000) / 60
                val seconds = (millisUntilFinished / 1000) % 60
                tvTime.text = String.format("%02d:%02d", minutes, seconds)
            }

            override fun onFinish() {
                finishFocus() // 封装成一个方法，显得代码整洁
            }
        }.start()
    }

    private fun stopTimer() {
        timer?.cancel()
        isRunning = false
        timerView.updateProgress(1.0f)

        // 恢复时间显示为 SeekBar 当前选中的值
        val minutes = currentDuration / 1000 / 60
        tvTime.text = String.format("%02d:00", minutes)

        stopService(Intent(this, MusicService::class.java))

        btnStart.text = "开始专注"
        sbTime.isEnabled = true // 恢复滑动
    }

    private fun finishFocus() {
        isRunning = false
        timerView.updateProgress(0f)
        tvTime.text = "00:00"
        btnStart.text = "开始专注"
        sbTime.isEnabled = true

        // ⬇️⬇️⬇️ 新增：保存到数据库 ⬇️⬇️⬇️
        val dao = AppDatabase.getDatabase(this).focusDao()
        val currentTime = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Date())

        // 计算专注时长 (根据 currentDuration 算出分钟数)
        val minutes = currentDuration / 1000 / 60

        val record = FocusRecord(date = currentTime, duration = "${minutes}分钟")
        dao.insert(record)
        // ⬆️⬆️⬆️ 新增结束 ⬆️⬆️⬆️

        // ⬇️⬇️⬇️ 新增：停止音乐服务 ⬇️⬇️⬇️
        stopService(Intent(this, MusicService::class.java))

        // 震动反馈 (保持原有代码)
        val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (vibrator.hasVibrator()) {
            // ... (保持震动代码不变)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(1000, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                vibrator.vibrate(1000)
            }
        }
        Toast.makeText(this, "专注完成！", Toast.LENGTH_SHORT).show()
    }
}