package com.example.focusflow

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import kotlin.math.min

/**
 * 自定义倒计时 View
 * 得分点：Custom View, Canvas绘制, 属性动画原理
 */
class CircularTimerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    // 画笔：背景圆（灰色）
    private val backPaint = Paint().apply {
        color = Color.LTGRAY
        style = Paint.Style.STROKE // 空心
        strokeWidth = 20f          // 线宽
        isAntiAlias = true         // 抗锯齿
    }

    // 画笔：进度条（蓝色 - 答辩时可随时改成红色）
    private val progPaint = Paint().apply {
        color = Color.parseColor("#6200EE") // 主题色
        style = Paint.Style.STROKE
        strokeWidth = 20f
        isAntiAlias = true
        strokeCap = Paint.Cap.ROUND // 圆头
    }

    // 绘制区域
    private val rectF = RectF()

    // 当前进度 (0.0 ~ 1.0)
    private var progress = 1.0f

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        // 确保是正方形，取宽高中较小的一个
        val size = min(measuredWidth, measuredHeight)
        setMeasuredDimension(size, size)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // 计算绘制区域（留出线宽的一半，防止边缘被切掉）
        val padding = 20f
        rectF.set(padding, padding, width - padding, height - padding)

        // 1. 画底圆（完整的圆）
        canvas.drawOval(rectF, backPaint)

        // 2. 画进度（根据 progress 画弧线）
        // -90度是12点钟方向，360 * progress 是扫过的角度
        canvas.drawArc(rectF, -90f, 360 * progress, false, progPaint)
    }

    /**
     * 更新进度的方法
     * @param currentProgress 0.0 到 1.0 之间的浮点数
     */
    fun updateProgress(currentProgress: Float) {
        this.progress = currentProgress
        invalidate() // 关键：通知系统重绘，触发 onDraw
    }
}