package com.android.widget

import android.content.res.TypedArray
import android.graphics.drawable.GradientDrawable

/**
 * Author: han.chen
 * Time: 2022/1/27 09:39
 */
class AdvancedParams {
    var mBorderWidth = 0
    var mBorderRadius = 0
    var mBorderRadiusTopLeft = 0
    var mBorderRadiusTopRight = 0
    var mBorderRadiusBottomLeft = 0
    var mBorderRadiusBottomRight = 0
    var mBorderColor = 0
    var mGradientDirection = 0
    var mGradientStartColor = 0
    var mGradientEndColor = 0
    var mDisableColor = 0
    var mSelectedColor = 0
    var mPressedColor = 0
    var mShadowEnable = false
    var mShadowXOffset = 0
    var mShadowYOffset = 0
    var mShadowBlur = 0
    var mShadowSpread = 0
    var mShadowColor = 0
    var mShadowRadius = 0
    val orientation: GradientDrawable.Orientation?
        get() = when (mGradientDirection) {
            1 -> GradientDrawable.Orientation.LEFT_RIGHT
            2 -> GradientDrawable.Orientation.RIGHT_LEFT
            3 -> GradientDrawable.Orientation.TOP_BOTTOM
            4 -> GradientDrawable.Orientation.BOTTOM_TOP
            5 -> GradientDrawable.Orientation.TL_BR
            6 -> GradientDrawable.Orientation.BR_TL
            7 -> GradientDrawable.Orientation.TR_BL
            8 -> GradientDrawable.Orientation.BL_TR
            else -> null
        }

    companion object {
        fun newParams(a: TypedArray): AdvancedParams {
            val params = AdvancedParams()
            params.mBorderWidth =
                a.getDimensionPixelSize(R.styleable.BackgroundAttr_border_width, 0)
            params.mBorderRadius =
                a.getDimensionPixelSize(R.styleable.BackgroundAttr_border_radius, 0)
            params.mBorderColor = a.getColor(R.styleable.BackgroundAttr_border_color, -0x1)
            params.mGradientDirection =
                a.getInt(R.styleable.BackgroundAttr_gradient_direction, 0)
            params.mGradientStartColor =
                a.getColor(R.styleable.BackgroundAttr_gradient_startColor, -0x1)
            params.mGradientEndColor =
                a.getColor(R.styleable.BackgroundAttr_gradient_endColor, -0x1)
            setRadii(params, a)
            return params
        }

        fun newDisableParams(a: TypedArray): AdvancedParams {
            val params = AdvancedParams()
            params.mBorderWidth =
                a.getDimensionPixelSize(R.styleable.BackgroundAttr_disable_border_width, 0)
            params.mBorderRadius =
                a.getDimensionPixelSize(R.styleable.BackgroundAttr_disable_border_radius, 0)
            params.mBorderColor =
                a.getColor(R.styleable.BackgroundAttr_disable_border_color, -0x1)
            params.mGradientDirection =
                a.getInt(R.styleable.BackgroundAttr_disable_gradient_direction, 0)
            params.mGradientStartColor =
                a.getColor(R.styleable.BackgroundAttr_disable_gradient_startColor, -0x1)
            params.mGradientEndColor =
                a.getColor(R.styleable.BackgroundAttr_disable_gradient_endColor, -0x1)
            params.mDisableColor = a.getColor(R.styleable.BackgroundAttr_disable_color, 1)
            params.mBorderRadiusTopLeft = a.getDimensionPixelSize(
                R.styleable.BackgroundAttr_disable_border_radius_top_left,
                0
            )
            params.mBorderRadiusTopRight = a.getDimensionPixelSize(
                R.styleable.BackgroundAttr_disable_border_radius_top_right,
                0
            )
            params.mBorderRadiusBottomLeft = a.getDimensionPixelSize(
                R.styleable.BackgroundAttr_disable_border_radius_bottom_left,
                0
            )
            params.mBorderRadiusBottomRight = a.getDimensionPixelSize(
                R.styleable.BackgroundAttr_disable_border_radius_bottom_right,
                0
            )
            return params
        }

        fun newSelectedParams(a: TypedArray): AdvancedParams {
            val params = AdvancedParams()
            params.mBorderWidth =
                a.getDimensionPixelSize(R.styleable.BackgroundAttr_selected_border_width, 0)
            params.mBorderRadius =
                a.getDimensionPixelSize(R.styleable.BackgroundAttr_selected_border_radius, 0)
            params.mBorderColor =
                a.getColor(R.styleable.BackgroundAttr_selected_border_color, -0x1)
            params.mGradientDirection =
                a.getInt(R.styleable.BackgroundAttr_selected_gradient_direction, 0)
            params.mGradientStartColor =
                a.getColor(R.styleable.BackgroundAttr_selected_gradient_startColor, -0x1)
            params.mGradientEndColor =
                a.getColor(R.styleable.BackgroundAttr_selected_gradient_endColor, -0x1)
            params.mSelectedColor = a.getColor(R.styleable.BackgroundAttr_selected_color, 1)
            params.mBorderRadiusTopLeft = a.getDimensionPixelSize(
                R.styleable.BackgroundAttr_selected_border_radius_top_left,
                0
            )
            params.mBorderRadiusTopRight = a.getDimensionPixelSize(
                R.styleable.BackgroundAttr_selected_border_radius_top_right,
                0
            )
            params.mBorderRadiusBottomLeft = a.getDimensionPixelSize(
                R.styleable.BackgroundAttr_selected_border_radius_bottom_left,
                0
            )
            params.mBorderRadiusBottomRight = a.getDimensionPixelSize(
                R.styleable.BackgroundAttr_selected_border_radius_bottom_right,
                0
            )
            return params
        }

        fun newPressedParams(a: TypedArray): AdvancedParams {
            val params = AdvancedParams()
            params.mBorderWidth =
                a.getDimensionPixelSize(R.styleable.BackgroundAttr_pressed_border_width, 0)
            params.mBorderRadius =
                a.getDimensionPixelSize(R.styleable.BackgroundAttr_pressed_border_radius, 0)
            params.mBorderColor =
                a.getColor(R.styleable.BackgroundAttr_pressed_border_color, -0x1)
            params.mGradientDirection =
                a.getInt(R.styleable.BackgroundAttr_pressed_gradient_direction, 0)
            params.mGradientStartColor =
                a.getColor(R.styleable.BackgroundAttr_pressed_gradient_startColor, -0x1)
            params.mGradientEndColor =
                a.getColor(R.styleable.BackgroundAttr_pressed_gradient_endColor, -0x1)
            params.mPressedColor = a.getColor(R.styleable.BackgroundAttr_pressed_color, 1)
            params.mBorderRadiusTopLeft = a.getDimensionPixelSize(
                R.styleable.BackgroundAttr_pressed_border_radius_top_left,
                0
            )
            params.mBorderRadiusTopRight = a.getDimensionPixelSize(
                R.styleable.BackgroundAttr_pressed_border_radius_top_right,
                0
            )
            params.mBorderRadiusBottomLeft = a.getDimensionPixelSize(
                R.styleable.BackgroundAttr_pressed_border_radius_bottom_left,
                0
            )
            params.mBorderRadiusBottomRight = a.getDimensionPixelSize(
                R.styleable.BackgroundAttr_pressed_border_radius_bottom_right,
                0
            )
            return params
        }

        private fun setRadii(params: AdvancedParams, a: TypedArray) {
            params.mBorderRadiusTopLeft =
                a.getDimensionPixelSize(R.styleable.BackgroundAttr_border_radius_top_left, 0)
            params.mBorderRadiusTopRight =
                a.getDimensionPixelSize(R.styleable.BackgroundAttr_border_radius_top_right, 0)
            params.mBorderRadiusBottomLeft = a.getDimensionPixelSize(
                R.styleable.BackgroundAttr_border_radius_bottom_left,
                0
            )
            params.mBorderRadiusBottomRight = a.getDimensionPixelSize(
                R.styleable.BackgroundAttr_border_radius_bottom_right,
                0
            )
        }

        fun newShadowParams(a: TypedArray): AdvancedParams {
            val params = AdvancedParams()
            params.mShadowEnable = (a.hasValue(R.styleable.ShadowAttr_shadow_xoffset)
                    && a.hasValue(R.styleable.ShadowAttr_shadow_yoffset)
                    && a.hasValue(R.styleable.ShadowAttr_shadow_blur)
                    && a.hasValue(R.styleable.ShadowAttr_shadow_spread)
                    && a.hasValue(R.styleable.ShadowAttr_shadow_color))
            params.mShadowXOffset =
                a.getDimensionPixelSize(R.styleable.ShadowAttr_shadow_xoffset, 0)
            params.mShadowYOffset =
                a.getDimensionPixelSize(R.styleable.ShadowAttr_shadow_yoffset, 0)
            params.mShadowBlur =
                a.getDimensionPixelSize(R.styleable.ShadowAttr_shadow_blur, 0)
            params.mShadowSpread =
                a.getDimensionPixelSize(R.styleable.ShadowAttr_shadow_spread, 0)
            params.mShadowColor = a.getColor(R.styleable.ShadowAttr_shadow_color, 0)
            params.mShadowRadius =
                a.getDimensionPixelSize(R.styleable.ShadowAttr_shadow_radius, 0)
            return params
        }
    }
}