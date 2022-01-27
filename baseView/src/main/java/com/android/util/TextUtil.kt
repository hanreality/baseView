package com.android.util

import android.content.res.ColorStateList
import android.content.res.TypedArray
import android.graphics.Paint
import android.widget.TextView
import androidx.annotation.NonNull
import com.android.widget.R

/**
 * Author: han.chen
 * Time: 2022/1/27 15:43
 */
object TextUtil {

    @JvmStatic
    fun setFontWeight(@NonNull widget: TextView, @NonNull a: TypedArray) {
        val weight = a.getInt(R.styleable.AdvancedTextView_font_weight, 0)
        val boldType = a.getInt(R.styleable.AdvancedTextView_bold_style, 0)
        if (weight > 0) {
            val textPaint = widget.paint
            textPaint.style = Paint.Style.FILL_AND_STROKE
            textPaint.strokeWidth = weight / 1000f
        } else if (boldType > 0) {
            val textPaint = widget.paint
            textPaint.style = Paint.Style.FILL_AND_STROKE
            textPaint.strokeWidth = boldType / 1000f
        }
    }

    @JvmStatic
    fun setTextShadow(@NonNull widget: TextView, @NonNull a: TypedArray) {
        val radius = a.getFloat(R.styleable.AdvancedTextView_text_shadow_layer_radius, 0f)
        val dx = a.getFloat(R.styleable.AdvancedTextView_text_shadow_layer_dx, 0f)
        val dy = a.getFloat(R.styleable.AdvancedTextView_text_shadow_layer_dy, 0f)
        val color = a.getColor(R.styleable.AdvancedTextView_text_shadow_layer_color, 0)
        if (radius != 0f || dx != 0f || dy != 0f || color != 0) {
            val textPaint = widget.paint
            textPaint.setShadowLayer(radius, dx, dy, color)
        }
    }

    @JvmStatic
    fun generateTextColors(@NonNull widget: TextView, @NonNull a: TypedArray): ColorStateList {
        val textColors = widget.textColors
        if (textColors?.isStateful == true) {
            return textColors
        }
        val states = arrayListOf<IntArray>()
        val colors = arrayListOf<Int>()
        if (a.hasValue(R.styleable.AdvancedTextView_pressed_text_color)) {
            states.add(DrawableUtil.STATE_PRESSED)
            colors.add(
                a.getColor(
                    R.styleable.AdvancedTextView_pressed_text_color,
                    textColors.defaultColor
                )
            )
        }
        if (a.hasValue(R.styleable.AdvancedTextView_selected_text_color)) {
            states.add(DrawableUtil.STATE_SELECTED)
            colors.add(
                a.getColor(
                    R.styleable.AdvancedTextView_selected_text_color,
                    textColors.defaultColor
                )
            )
        }
        if (a.hasValue(R.styleable.AdvancedTextView_disable_text_color)) {
            states.add(DrawableUtil.STATE_DISABLED)
            colors.add(
                a.getColor(
                    R.styleable.AdvancedTextView_disable_text_color,
                    textColors.defaultColor
                )
            )
        }
        states.add(DrawableUtil.STATE_EMPTY)
        colors.add(textColors.defaultColor)
        return ColorStateList(states.toTypedArray(), colors.toIntArray())
    }
}