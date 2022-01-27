package com.android.widget

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import android.graphics.*
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.StateListDrawable
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.FrameLayout
import com.android.util.DrawableUtil
import com.android.util.ShadowUtil
import java.lang.Exception
import kotlin.math.abs

/**
 * Author: han.chen
 * Time: 2022/1/1 16:13
 * 1. shape 已支持，可以在xml直接用。
 * 2. html
 * 3. ImageSpan
 * 4. shadow 已支持，xml直接用，且xoffset、yoffset、blur、spread、color缺一不可，宽高必须是exactly，api18以上
 */

class AdvancedFrameLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    private var mShadowEnable = false
    init {
        initStyle(context, attrs)
    }
    @SuppressLint("ResourceType", "CustomViewStyleable")
    fun initStyle(context: Context, attrs: AttributeSet?) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.AdvancedViewGroup)
        val backgroundAttr = context.obtainStyledAttributes(attrs, R.styleable.BackgroundAttr)
        val shadowAttr = context.obtainStyledAttributes(attrs, R.styleable.ShadowAttr)
        var layoutHeight = 0
        var layoutWidth = 0
        try {
            layoutWidth = a.getDimensionPixelSize(
                R.styleable.AdvancedTextView_android_layout_width,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            layoutHeight =
                a.getDimensionPixelSize(
                    R.styleable.AdvancedTextView_android_layout_height,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
        } catch (e: Exception) {
        }
        val backgroundDrawable = DrawableUtil.generateBackgroundDrawable(this, backgroundAttr)
        mShadowEnable = ShadowUtil.addShadowToView(this, shadowAttr, layoutWidth, layoutHeight)
        a.recycle()
        backgroundAttr.recycle()
        shadowAttr.recycle()

        background = backgroundDrawable
    }

    override fun setBackgroundColor(color: Int) {
        if (background is GradientDrawable) {
            (background as GradientDrawable).setColor(color)
        } else {
            super.setBackgroundColor(color)
        }
    }

    fun setBackgroundColors(colors: IntArray) {
        if (background is GradientDrawable) {
            (background as GradientDrawable).colors = colors
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if (mShadowEnable) {
            val parent = parent
            if (parent is ViewGroup) {
                parent.clipChildren = false
                parent.clipToPadding = false
                parent.invalidate()
            }
        }
    }
}