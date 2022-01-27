package com.android.util

import android.content.res.TypedArray
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.StateListDrawable
import android.view.View
import android.widget.TextView
import androidx.annotation.IntRange
import androidx.annotation.NonNull
import com.android.widget.AdvancedParams
import com.android.widget.R

/**
 * Author: han.chen
 * Time: 2022/1/26 23:40
 */
object DrawableUtil {

    @JvmStatic
    val STATE_PRESSED =
        intArrayOf(android.R.attr.state_pressed, android.R.attr.state_enabled)

    @JvmStatic
    val STATE_SELECTED =
        intArrayOf(android.R.attr.state_selected, android.R.attr.state_enabled)

    @JvmStatic
    val STATE_DISABLED =
        intArrayOf(-android.R.attr.state_enabled)

    @JvmStatic
    val STATE_EMPTY =
        intArrayOf()

    private fun generateDrawable(
        @NonNull widget: View,
        @NonNull params: AdvancedParams
    ): GradientDrawable? {
        if (params.mBorderWidth == 0
            && params.mBorderRadius == 0
            && params.mBorderRadiusBottomLeft == 0
            && params.mBorderRadiusBottomRight == 0
            && params.mBorderRadiusTopLeft == 0
            && params.mBorderRadiusTopRight == 0
            && params.mGradientDirection == 0
        ) {
            return null
        }
        val drawable = GradientDrawable()
        if (params.mBorderWidth > 0) {
            drawable.setStroke(params.mBorderWidth, params.mBorderColor)
        }
        if (0 != params.mBorderRadius) {
            drawable.cornerRadius = params.mBorderRadius.toFloat()
        } else if (params.mBorderRadiusTopLeft != 0
            || params.mBorderRadiusTopRight != 0
            || params.mBorderRadiusBottomLeft != 0
            || params.mBorderRadiusBottomRight != 0
        ) {
            drawable.cornerRadii = floatArrayOf(
                params.mBorderRadiusTopLeft.toFloat(),
                params.mBorderRadiusTopLeft.toFloat(),
                params.mBorderRadiusTopRight.toFloat(),
                params.mBorderRadiusTopRight.toFloat(),
                params.mBorderRadiusBottomRight.toFloat(),
                params.mBorderRadiusBottomRight.toFloat(),
                params.mBorderRadiusBottomLeft.toFloat(),
                params.mBorderRadiusBottomLeft.toFloat()
            )
        }
        when {
            params.orientation != null -> {
                drawable.orientation = params.orientation
                drawable.colors = intArrayOf(params.mGradientStartColor, params.mGradientEndColor)
            }
            widget.background is ColorDrawable -> {
                drawable.setColor((widget.background as ColorDrawable).color)
            }
            else -> {
                drawable.setColor(Color.TRANSPARENT)
            }
        }
        return drawable
    }

    @JvmStatic
    fun generateDrawable(@NonNull widget: View, @NonNull a: TypedArray): Drawable? {
        val params = AdvancedParams.newParams(a)
        return generateDrawable(widget, params)
    }

    @JvmStatic
    fun generateDisableDrawable(@NonNull widget: View, @NonNull a: TypedArray): Drawable? {
        val params = AdvancedParams.newDisableParams(a)
        val drawable = generateDrawable(widget, params)
        return if (drawable != null) {
            if (params.mDisableColor != 1) {
                drawable.setColor(params.mDisableColor)
            }
            drawable
        } else {
            if (params.mDisableColor != 1) {
                ColorDrawable(params.mDisableColor)
            } else {
                null
            }
        }
    }

    @JvmStatic
    fun generateSelectedDrawable(@NonNull widget: View, @NonNull a: TypedArray): Drawable? {
        val params = AdvancedParams.newSelectedParams(a)
        val drawable = generateDrawable(widget, params)
        return if (drawable != null) {
            if (params.mSelectedColor != 1) {
                drawable.setColor(params.mSelectedColor)
            }
            drawable
        } else {
            if (params.mSelectedColor != 1) {
                ColorDrawable(params.mSelectedColor)
            } else {
                null
            }
        }
    }

    @JvmStatic
    fun generatePressedDrawable(@NonNull widget: View, @NonNull a: TypedArray): Drawable? {
        val params = AdvancedParams.newPressedParams(a)
        val drawable = generateDrawable(widget, params)
        return if (drawable != null) {
            if (params.mPressedColor != 1) {
                drawable.setColor(params.mPressedColor)
            }
            drawable
        } else {
            if (params.mPressedColor != 1) {
                ColorDrawable(params.mPressedColor)
            } else {
                null
            }
        }
    }

    @JvmStatic
    fun generateCompoundDrawable(
        @NonNull widget: TextView,
        @NonNull a: TypedArray,
        @IntRange(from = 0, to = 3) direction: Int
    ): Drawable? {
        val compoundDrawable = widget.compoundDrawables[direction]
        if (compoundDrawable?.isStateful == true) {
            return compoundDrawable
        }
        if (direction < 0 || direction > 3) {
            return null
        }
        val pressedDrawable: Drawable?
        val selectedDrawable: Drawable?
        val disableDrawable: Drawable?
        when (direction) {
            0 -> {
                pressedDrawable = a.getDrawable(R.styleable.AdvancedTextView_pressed_drawableLeft)
                selectedDrawable = a.getDrawable(R.styleable.AdvancedTextView_selected_drawableLeft)
                disableDrawable = a.getDrawable(R.styleable.AdvancedTextView_disable_drawableLeft)
            }
            1 -> {
                pressedDrawable = a.getDrawable(R.styleable.AdvancedTextView_pressed_drawableTop)
                selectedDrawable = a.getDrawable(R.styleable.AdvancedTextView_selected_drawableTop)
                disableDrawable = a.getDrawable(R.styleable.AdvancedTextView_disable_drawableTop)
            }
            2 -> {
                pressedDrawable = a.getDrawable(R.styleable.AdvancedTextView_pressed_drawableRight)
                selectedDrawable =
                    a.getDrawable(R.styleable.AdvancedTextView_selected_drawableRight)
                disableDrawable = a.getDrawable(R.styleable.AdvancedTextView_disable_drawableRight)
            }
            else -> {
                pressedDrawable = a.getDrawable(R.styleable.AdvancedTextView_pressed_drawableBottom)
                selectedDrawable =
                    a.getDrawable(R.styleable.AdvancedTextView_selected_drawableBottom)
                disableDrawable = a.getDrawable(R.styleable.AdvancedTextView_disable_drawableBottom)
            }
        }

        val drawable =
            if (pressedDrawable != null || selectedDrawable != null || disableDrawable != null) {
                val drawable = StateListDrawable()
                drawable.addState(STATE_PRESSED, pressedDrawable)
                drawable.addState(STATE_SELECTED, selectedDrawable)
                drawable.addState(STATE_DISABLED, disableDrawable)
                drawable.addState(STATE_EMPTY, compoundDrawable)
                drawable
            } else {
                compoundDrawable
            }
        return drawable
    }

    @JvmStatic
    fun generateCompoundDrawables(@NonNull widget: TextView,
                                  @NonNull a: TypedArray) : Array<Drawable?> {
        return arrayOf(
            generateCompoundDrawable(widget, a, 0),
            generateCompoundDrawable(widget, a, 1),
            generateCompoundDrawable(widget, a, 2),
            generateCompoundDrawable(widget, a, 3)
        )
    }

    @JvmStatic
    fun generateBackgroundDrawable(@NonNull widget: View, @NonNull backgroundAttr: TypedArray) : Drawable {
        val backgroundDrawable = widget.background
        if (backgroundDrawable?.isStateful == true) {
            return backgroundDrawable
        }
        val normal: Drawable? = generateDrawable(widget, backgroundAttr)
        val disabled: Drawable? = generateDisableDrawable(widget, backgroundAttr)
        val selected: Drawable? = generateSelectedDrawable(widget, backgroundAttr)
        val pressed: Drawable? = generatePressedDrawable(widget, backgroundAttr)
        val drawable = StateListDrawable()
        if (pressed != null) {
            drawable.addState(
                STATE_PRESSED, pressed
            )
        }
        if (selected != null) {
            drawable.addState(
                STATE_SELECTED, selected
            )
        }
        if (disabled != null) {
            drawable.addState(
                STATE_DISABLED, disabled
            )
        }
        drawable.addState(STATE_EMPTY, normal)
        return drawable
    }
}