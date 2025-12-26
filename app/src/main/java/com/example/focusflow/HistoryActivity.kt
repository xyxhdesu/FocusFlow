package com.example.focusflow

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class HistoryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        val tvList = findViewById<TextView>(R.id.tvHistoryList)

        // 1. 获取数据库实例
        val dao = AppDatabase.getDatabase(this).focusDao()

        // 2. 查询所有记录
        val records = dao.getAll()

        // 3. 如果有数据，拼接成字符串显示
        if (records.isNotEmpty()) {
            val sb = StringBuilder()
            for (record in records) {
                sb.append("📅 ${record.date}\n")
                sb.append("⏱ 专注时长: ${record.duration}\n")
                sb.append("-----------------------\n")
            }
            tvList.text = sb.toString()
        }
    }
}