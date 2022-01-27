package com.android.widget

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.TextView
import com.android.util.DrawableUtil
import com.android.util.ShadowUtil
import com.android.util.TextUtil
import java.lang.Exception

/**
 * Author: han.chen
 * Time: 2022/1/1 16:13
 * 1. shape 已支持，可以在xml直接用。
 * 2. html
 * 3. ImageSpan
 * 4. shadow 已支持，xml直接用，且xoffset、yoffset、blur、spread、color缺一不可，宽高必须是exactly，api18以上
 * 5. 暂不支持主动设置
 */

@SuppressLint("AppCompatCustomView")
class AdvancedTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : TextView(context, attrs, defStyleAttr) {
    private var mShadowEnable = false
    init {
        initStyle(context, attrs)
    }

    @SuppressLint("ResourceType", "CustomViewStyleable")
    fun initStyle(context: Context, attrs: AttributeSet?) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.AdvancedTextView)
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
        val compoundDrawables = DrawableUtil.generateCompoundDrawables(this, a)
        val colorStateList: ColorStateList = TextUtil.generateTextColors(this, a)
        mShadowEnable = ShadowUtil.addShadowToView(this, shadowAttr, layoutWidth, layoutHeight)
        TextUtil.setFontWeight(this, a)
        TextUtil.setTextShadow(this, a)
        a.recycle()
        backgroundAttr.recycle()
        shadowAttr.recycle()

        setCompoundDrawablesWithIntrinsicBounds(
            compoundDrawables[0],
            compoundDrawables[1],
            compoundDrawables[2],
            compoundDrawables[3]
        )
        setTextColor(colorStateList)
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