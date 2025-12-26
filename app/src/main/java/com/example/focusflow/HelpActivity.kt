package com.example.focusflow

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class HelpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ❌ 错误写法: setContentView(R.layout.main)
        // ✅ 正确写法: 对应你的 activity_help.xml 文件名
        setContentView(R.layout.activity_help)

        // 设置标题（可选）
        title = "帮助说明"

        // 显示返回按钮（可选，显得更像个二级页面）
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    // 处理左上角返回按钮点击事件
    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}