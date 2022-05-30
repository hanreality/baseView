package com.android.widget

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorInt
import java.lang.Exception

class LiveColumnView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var columnNum = 3
    private var rectFs: Array<RectF>
    private var columnHeightScale: Array<Float>
    private var paint: Paint = Paint()
    private var layoutWidth: Int = 0
    private var layoutHeight: Int = 0
    private var drawWidth: Int = 0
    private var drawHeight: Int = 0
    private var columnWidth: Int = 0
    private var columnHeight: Int = 0
    private var valueAnimators: Array<ValueAnimator>

    var isStart = false

    @ColorInt
    private var color: Int = Color.RED

    init {
        val a = context.obtainStyledAttributes(attrs, R.styleable.LiveColumnView)
        try {
            layoutWidth = a.getLayoutDimension(
                R.styleable.LiveColumnView_android_layout_width,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            layoutHeight =
                a.getLayoutDimension(
                    R.styleable.LiveColumnView_android_layout_height,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
        } catch (e: Exception) {}
        columnNum = a.getInt(R.styleable.LiveColumnView_column_count, 3)
        color = a.getColor(R.styleable.LiveColumnView_column_color, Color.WHITE)
        columnWidth = a.getDimensionPixelSize(R.styleable.LiveColumnView_column_width, dp2px(2f))
        columnHeight = a.getDimensionPixelSize(R.styleable.LiveColumnView_column_height, dp2px(12f))
        a.recycle()
        drawWidth = columnWidth * (2 * columnNum + 1)
        drawHeight = columnHeight
        rectFs = Array(columnNum) { RectF() }
        columnHeightScale = Array(columnNum) {
            1f
        }
        paint.color = color
        paint.style = Paint.Style.FILL
        valueAnimators = Array(columnNum) { index ->
            val valueAnimator = ValueAnimator.ofFloat(1f, 1f / columnNum, 1f)
            valueAnimator.duration = 800
            valueAnimator.addUpdateListener {
                columnHeightScale[index] = it.animatedValue as Float
                invalidate()
            }
            valueAnimator.repeatCount = ValueAnimator.INFINITE
            valueAnimator
        }

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val viewWidth = when (layoutWidth) {
            -1 -> MeasureSpec.getSize(widthMeasureSpec)
            -2 -> drawWidth
            else -> layoutWidth
        }
        val viewHeight = when (layoutHeight) {
            -1 -> MeasureSpec.getSize(heightMeasureSpec)
            -2 -> drawHeight
            else -> layoutHeight
        }
        setMeasuredDimension(viewWidth, viewHeight)
    }

    fun start() {
        isStart = true
        valueAnimators.forEachIndexed { index, valueAnimator ->
            valueAnimator.startDelay = 300L * index
            valueAnimator.start()
        }
        invalidate()
    }

    fun stop() {
        isStart = false
        valueAnimators.forEach {
            it.cancel()
        }
        invalidate()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val drawLeft = (right - left) / 2 - drawWidth / 2
        val drawTop = (bottom - top) / 2 - drawHeight / 2
        rectFs.forEachIndexed { index, rectF ->
            rectF.set(
                drawLeft + columnWidth * (2f * index + 1f),
                drawTop + columnHeight * (1 - columnHeightScale[index]),
                drawLeft + columnWidth * 2f * (index + 1),
                drawTop + columnHeight.toFloat()
            )
            canvas?.drawRoundRect(rectF, columnWidth.toFloat(), columnWidth.toFloat(), paint)
            rectF.set(
                drawLeft+ columnWidth * (2f * index + 1f),
                drawTop + columnHeight * (1 - 1f / columnNum) + columnWidth.toFloat(),
                drawLeft + columnWidth * 2f * (index + 1),
                drawTop + columnHeight.toFloat()
            )
            canvas?.drawRect(rectF, paint)
        }
    }

    private fun dp2px(dp: Float): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.displayMetrics)
            .toInt()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        start()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        stop()
    }

}