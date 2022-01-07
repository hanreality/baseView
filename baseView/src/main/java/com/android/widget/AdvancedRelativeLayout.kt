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
import android.widget.RelativeLayout
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

class AdvancedRelativeLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {
    private var mShadowEnable = false
    init {
        initStyle(context, attrs)
    }
    @SuppressLint("ResourceType", "CustomViewStyleable")
    fun initStyle(context: Context, attrs: AttributeSet?) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.AdvancedViewGroup)
        val layoutWidth = a.getDimensionPixelSize(R.styleable.AdvancedTextView_android_layout_width, ViewGroup.LayoutParams.WRAP_CONTENT)
        val layoutHeight = a.getDimensionPixelSize(R.styleable.AdvancedTextView_android_layout_height, ViewGroup.LayoutParams.WRAP_CONTENT)
        val normal: Drawable? = generateDrawable(a)
        val disable: Drawable? = generateDisableDrawable(a)
        val selected: Drawable? = generateSelectedDrawable(a)
        mShadowEnable = addShadowToView(a, layoutWidth, layoutHeight)
        a.recycle()
        var stateDrawable: StateListDrawable? = null
        if (disable != null || selected != null) {
            stateDrawable = StateListDrawable()
            if (disable != null) {
                stateDrawable.addState(intArrayOf(-android.R.attr.state_enabled), disable)
            }
            if (selected != null) {
                stateDrawable.addState(intArrayOf(android.R.attr.state_selected), selected)
            }
        }
        var bgDrawable: Drawable? = null
        if (normal != null) {
            bgDrawable = if (stateDrawable != null) {
                stateDrawable.addState(intArrayOf(), normal)
                stateDrawable
            } else {
                normal
            }
        } else {
            if (stateDrawable != null) {
                val main = if (background != null) background else ColorDrawable(Color.TRANSPARENT)
                stateDrawable.addState(intArrayOf(), main)
            } else {
                bgDrawable = null
            }
        }
        if (bgDrawable != null) {
            background = bgDrawable
        }
    }

    private fun generateDrawable(params: AdvancedParams): GradientDrawable? {
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
            background is ColorDrawable -> {
                drawable.setColor((background as ColorDrawable).color)
            }
            else -> {
                drawable.setColor(Color.TRANSPARENT)
            }
        }
        return drawable
    }

    private fun generateDrawable(a: TypedArray): Drawable? {
        val params = AdvancedParams.newParams(a)
        return generateDrawable(params)
    }

    private fun generateDisableDrawable(a: TypedArray): Drawable? {
        val params = AdvancedParams.newDisableParams(a)
        val drawable = generateDrawable(params)
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

    private fun generateSelectedDrawable(a: TypedArray): Drawable? {
        val params = AdvancedParams.newSelectedParams(a)
        val drawable = generateDrawable(params)
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

    private fun addShadowToView(a: TypedArray, viewWidth: Int, viewHeight: Int): Boolean {
        val params = AdvancedParams.newShadowParams(a)
        if (!params.mShadowEnable) {
            return false
        }
        if (viewWidth <= 0 || viewHeight <= 0) {
            return false
        }
        this.overlay.clear()
        val blur = params.mShadowBlur
        val spread = params.mShadowSpread
        val xOffset: Int = params.mShadowXOffset
        val yOffset: Int = params.mShadowYOffset
        val shadowColor = params.mShadowColor
        val radius = params.mShadowRadius
        val xIncrement = blur + spread + abs(xOffset)
        val yIncrement = blur + spread + abs(yOffset)
        val maxWidth = viewWidth + 2 * xIncrement
        val maxHeight = viewHeight + 2 * yIncrement
        val output = Bitmap.createBitmap(maxWidth, maxHeight, Bitmap.Config.ARGB_4444)
        val canvas = Canvas(output)
        val shadowRect = RectF(
            0f, 0f,
            viewWidth + 2f * spread, viewHeight + 2f * spread
        )
        val shadowDx = blur + 2f * xOffset
        val shadowDy = blur + 2f * yOffset
        shadowRect.offset(shadowDx, shadowDy)
        val shadowPaint = Paint()
        shadowPaint.isAntiAlias = true
        shadowPaint.color = shadowColor
        shadowPaint.style = Paint.Style.FILL
        if (blur > 0) {
            shadowPaint.maskFilter = BlurMaskFilter(blur.toFloat(), BlurMaskFilter.Blur.NORMAL)
        }
        val shadowPath = Path()
        val shadowRadii = FloatArray(8)
        val radii = FloatArray(8)
        for (i in shadowRadii.indices) {
            shadowRadii[i] = if (radius.toFloat() == 0f) 0f else (radius + spread).toFloat()
            radii[i] = radius.toFloat()
        }
        shadowPath.addRoundRect(shadowRect, shadowRadii, Path.Direction.CCW)
        canvas.drawPath(shadowPath, shadowPaint)
        val mShadowBitmapDrawable = ShadowBitmapDrawable(
            resources, output,
            Point(xIncrement, yIncrement), Rect(0, 0, viewWidth, viewHeight), radii
        )
        val parent = parent
        if (parent is ViewGroup) {
            parent.clipChildren = false
            parent.clipToPadding = false
        }
        this.overlay.add(mShadowBitmapDrawable)
        //Relayout to ensure the shadows are fully drawn
        if (parent != null) {
            parent.requestLayout()
            if (parent is ViewGroup) {
                parent.invalidate()
            }
        }
        return true
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

    internal class AdvancedParams {
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
                    a.getDimensionPixelSize(R.styleable.AdvancedViewGroup_border_width, 0)
                params.mBorderRadius =
                    a.getDimensionPixelSize(R.styleable.AdvancedViewGroup_border_radius, 0)
                params.mBorderColor = a.getColor(R.styleable.AdvancedViewGroup_border_color, -0x1)
                params.mGradientDirection =
                    a.getInt(R.styleable.AdvancedViewGroup_gradient_direction, 0)
                params.mGradientStartColor =
                    a.getColor(R.styleable.AdvancedViewGroup_gradient_startColor, -0x1)
                params.mGradientEndColor =
                    a.getColor(R.styleable.AdvancedViewGroup_gradient_endColor, -0x1)
                setRadii(params, a)
                return params
            }

            fun newDisableParams(a: TypedArray): AdvancedParams {
                val params = AdvancedParams()
                params.mBorderWidth =
                    a.getDimensionPixelSize(R.styleable.AdvancedViewGroup_disable_border_width, 0)
                params.mBorderRadius =
                    a.getDimensionPixelSize(R.styleable.AdvancedViewGroup_disable_border_radius, 0)
                params.mBorderColor =
                    a.getColor(R.styleable.AdvancedViewGroup_disable_border_color, -0x1)
                params.mGradientDirection =
                    a.getInt(R.styleable.AdvancedViewGroup_disable_gradient_direction, 0)
                params.mGradientStartColor =
                    a.getColor(R.styleable.AdvancedViewGroup_disable_gradient_startColor, -0x1)
                params.mGradientEndColor =
                    a.getColor(R.styleable.AdvancedViewGroup_disable_gradient_endColor, -0x1)
                params.mDisableColor = a.getColor(R.styleable.AdvancedViewGroup_disable_color, 1)
                params.mBorderRadiusTopLeft = a.getDimensionPixelSize(
                    R.styleable.AdvancedViewGroup_disable_border_radius_top_left,
                    0
                )
                params.mBorderRadiusTopRight = a.getDimensionPixelSize(
                    R.styleable.AdvancedViewGroup_disable_border_radius_top_right,
                    0
                )
                params.mBorderRadiusBottomLeft = a.getDimensionPixelSize(
                    R.styleable.AdvancedViewGroup_disable_border_radius_bottom_left,
                    0
                )
                params.mBorderRadiusBottomRight = a.getDimensionPixelSize(
                    R.styleable.AdvancedViewGroup_disable_border_radius_bottom_right,
                    0
                )
                return params
            }

            fun newSelectedParams(a: TypedArray): AdvancedParams {
                val params = AdvancedParams()
                params.mBorderWidth =
                    a.getDimensionPixelSize(R.styleable.AdvancedViewGroup_selected_border_width, 0)
                params.mBorderRadius =
                    a.getDimensionPixelSize(R.styleable.AdvancedViewGroup_selected_border_radius, 0)
                params.mBorderColor =
                    a.getColor(R.styleable.AdvancedViewGroup_selected_border_color, -0x1)
                params.mGradientDirection =
                    a.getInt(R.styleable.AdvancedViewGroup_selected_gradient_direction, 0)
                params.mGradientStartColor =
                    a.getColor(R.styleable.AdvancedViewGroup_selected_gradient_startColor, -0x1)
                params.mGradientEndColor =
                    a.getColor(R.styleable.AdvancedViewGroup_selected_gradient_endColor, -0x1)
                params.mSelectedColor = a.getColor(R.styleable.AdvancedViewGroup_selected_color, 1)
                params.mBorderRadiusTopLeft = a.getDimensionPixelSize(
                    R.styleable.AdvancedViewGroup_selected_border_radius_top_left,
                    0
                )
                params.mBorderRadiusTopRight = a.getDimensionPixelSize(
                    R.styleable.AdvancedViewGroup_selected_border_radius_top_right,
                    0
                )
                params.mBorderRadiusBottomLeft = a.getDimensionPixelSize(
                    R.styleable.AdvancedViewGroup_selected_border_radius_bottom_left,
                    0
                )
                params.mBorderRadiusBottomRight = a.getDimensionPixelSize(
                    R.styleable.AdvancedViewGroup_selected_border_radius_bottom_right,
                    0
                )
                return params
            }

            private fun setRadii(params: AdvancedParams, a: TypedArray) {
                params.mBorderRadiusTopLeft =
                    a.getDimensionPixelSize(R.styleable.AdvancedViewGroup_border_radius_top_left, 0)
                params.mBorderRadiusTopRight =
                    a.getDimensionPixelSize(R.styleable.AdvancedViewGroup_border_radius_top_right, 0)
                params.mBorderRadiusBottomLeft = a.getDimensionPixelSize(
                    R.styleable.AdvancedViewGroup_border_radius_bottom_left,
                    0
                )
                params.mBorderRadiusBottomRight = a.getDimensionPixelSize(
                    R.styleable.AdvancedViewGroup_border_radius_bottom_right,
                    0
                )
            }

            fun newShadowParams(a: TypedArray): AdvancedParams {
                val params = AdvancedParams()
                params.mShadowEnable = (a.hasValue(R.styleable.AdvancedViewGroup_shadow_xoffset)
                        && a.hasValue(R.styleable.AdvancedViewGroup_shadow_yoffset)
                        && a.hasValue(R.styleable.AdvancedViewGroup_shadow_blur)
                        && a.hasValue(R.styleable.AdvancedViewGroup_shadow_spread)
                        && a.hasValue(R.styleable.AdvancedViewGroup_shadow_color))
                params.mShadowXOffset =
                    a.getDimensionPixelSize(R.styleable.AdvancedViewGroup_shadow_xoffset, 0)
                params.mShadowYOffset =
                    a.getDimensionPixelSize(R.styleable.AdvancedViewGroup_shadow_yoffset, 0)
                params.mShadowBlur =
                    a.getDimensionPixelSize(R.styleable.AdvancedViewGroup_shadow_blur, 0)
                params.mShadowSpread =
                    a.getDimensionPixelSize(R.styleable.AdvancedViewGroup_shadow_spread, 0)
                params.mShadowColor = a.getColor(R.styleable.AdvancedViewGroup_shadow_color, 0)
                params.mShadowRadius =
                    a.getDimensionPixelSize(R.styleable.AdvancedViewGroup_shadow_radius, 0)
                return params
            }
        }
    }
}