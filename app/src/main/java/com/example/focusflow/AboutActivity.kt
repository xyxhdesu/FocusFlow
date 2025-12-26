package com.example.focusflow

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class AboutActivity : AppCompatActivity() {

    private lateinit var tvDailyQuote: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        tvDailyQuote = findViewById(R.id.tvDailyQuote)

        // 页面打开时，发起网络请求
        fetchQuote()
    }

    private fun fetchQuote() {
        val client = OkHttpClient()
        // 使用一言 API 获取纯文本 JSON
        val request = Request.Builder()
            .url("https://v1.hitokoto.cn/?c=d&encode=json")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                // 网络失败时，在主线程更新 UI
                runOnUiThread {
                    tvDailyQuote.text = "加载失败，请检查网络"
                }
            }

            override fun onResponse(call: Call, response: Response) {
                response.body?.string()?.let { jsonString ->
                    try {
                        // 解析 JSON
                        val jsonObject = JSONObject(jsonString)
                        val hitokoto = jsonObject.getString("hitokoto") // 获取句子
                        val from = jsonObject.getString("from") // 获取出处

                        // 回到主线程更新 UI
                        runOnUiThread {
                            tvDailyQuote.text = "“$hitokoto”\n—— $from"
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        })
    }
}