package com.android.widget

import android.animation.Animator
import android.animation.Animator.AnimatorListener
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Shader
import android.os.Build.VERSION_CODES.R
import android.os.Handler
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import androidx.annotation.ColorInt
import java.util.concurrent.atomic.AtomicInteger


/**
 * Author: han.chen
 * Date: 2024/1/30
 */
class AudioWaveView@JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var circleNum = 5
    private var circleRadiusScale: Array<Float>
    private var paint: Paint = Paint()
    private var circleRadius: Int = 0
    private var valueAnimators: Array<ValueAnimator>
    private var linearGradient: LinearGradient? = null
    private var layoutWidth: Int = 0
    private var layoutHeight: Int = 0
    private var circleX = 0f
    private var circleY = 0f
    private var circleExpWidth:Int = dp2px(10f)
    private var circleLineWidth:Int = dp2px(2f)
    @ColorInt
    private var color: Int = Color.WHITE
    @ColorInt
    private var beginColor: Int = Color.WHITE
    @ColorInt
    private var endColor: Int = Color.WHITE
    private var colorChanged :Boolean = false
    var isRunning = false

    init {
        val a = context.obtainStyledAttributes(attrs, R.styleable.AudioWaveView)
        try {
            layoutWidth = a.getLayoutDimension(
                R.styleable.AudioWaveView_android_layout_width,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            layoutHeight =
                a.getLayoutDimension(
                    R.styleable.AudioWaveView_android_layout_height,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
        } catch (e: Exception) {
        }
        circleNum = a.getInt(R.styleable.AudioWaveView_circle_count, 5)
        color = a.getColor(R.styleable.AudioWaveView_circle_color, Color.RED)
        beginColor = a.getColor(R.styleable.AudioWaveView_circle_begin_color, color)
        endColor = a.getColor(R.styleable.AudioWaveView_circle_end_color, color)
        circleRadius = a.getDimensionPixelSize(R.styleable.AudioWaveView_circle_radius, dp2px(25f))
        circleExpWidth = a.getDimensionPixelSize(R.styleable.AudioWaveView_circle_exp_width, dp2px(10f))
        circleLineWidth = a.getDimensionPixelSize(R.styleable.AudioWaveView_circle_line_width, dp2px(2f))
        a.recycle()
        circleRadiusScale = Array(circleNum) {
            1f
        }
        valueAnimators = Array(circleNum) { index ->
            val valueAnimator = ValueAnimator.ofFloat(0f, 1f)
            valueAnimator.duration = 1200
            valueAnimator.addUpdateListener {
                circleRadiusScale[index] = it.animatedValue as Float
                invalidate()
            }
            valueAnimator.repeatCount = 0
            valueAnimator
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        circleX = w / 2f
        circleY = h / 2f
    }

    private fun dp2px(dp: Float): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.displayMetrics)
            .toInt()
    }

    fun setColor(@ColorInt color: Int) {
        this.color = color
        this.beginColor = color
        this.endColor = color
        colorChanged = true
        invalidate()
    }

    fun setGradientColors(@ColorInt beginColor: Int, @ColorInt endColor: Int) {
        this.beginColor = beginColor
        this.endColor = endColor
        colorChanged = true
        invalidate()
    }

    fun start() {
        if (!isAttachedToWindow) {
            return
        }
        if (isRunning) {
            return
        }
        isRunning = true
        valueAnimators.forEachIndexed { index, valueAnimator ->
            valueAnimator.startDelay = 200L * index
            valueAnimator.interpolator = DecelerateInterpolator()
            if (index == 0) {
                valueAnimator.addListener(listener)
            }
            valueAnimator.start()
        }
        invalidate()
    }

    private val listener = object :AnimatorListener {
        override fun onAnimationStart(animation: Animator?) {

        }

        override fun onAnimationEnd(animation: Animator?) {
            stopImmediately()
        }

        override fun onAnimationCancel(animation: Animator?) {

        }

        override fun onAnimationRepeat(animation: Animator?) {

        }
    }

    fun stopImmediately() {
        isRunning = false
        valueAnimators.forEach {
            it.cancel()
        }
        circleRadiusScale = Array(circleNum) {
            1f
        }
        invalidate()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (linearGradient == null || colorChanged) {
            linearGradient = LinearGradient(
                0f,
                0f,
                0f,
                measuredHeight.toFloat(),
                beginColor,
                endColor,
                Shader.TileMode.CLAMP
            )
            colorChanged = false
        }
        paint.style = Paint.Style.STROKE
        paint.shader = linearGradient
        paint.strokeWidth = circleLineWidth.toFloat()
        //绘制扩散圆
        for (i in 0 until circleNum) {
            paint.alpha = (255 * (1 - circleRadiusScale[i])).toInt()
            canvas?.drawCircle(circleX, circleY, circleRadius + circleExpWidth * circleRadiusScale[i], paint)
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        stopImmediately()
    }
}