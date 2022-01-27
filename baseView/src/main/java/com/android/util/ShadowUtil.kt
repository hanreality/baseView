package com.android.util

import android.content.res.TypedArray
import android.graphics.*
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import com.android.widget.AdvancedParams
import com.android.widget.ShadowBitmapDrawable
import kotlin.math.abs

/**
 * Author: han.chen
 * Time: 2022/1/27 15:40
 */
object ShadowUtil {

    @JvmStatic
     fun addShadowToView(@NonNull widget: View, @NonNull a: TypedArray, viewWidth: Int, viewHeight: Int): Boolean {
        val params = AdvancedParams.newShadowParams(a)
        if (!params.mShadowEnable) {
            return false
        }
        if (viewWidth <= 0 || viewHeight <= 0) {
            return false
        }
        widget.overlay.clear()
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
            widget.resources, output,
            Point(xIncrement, yIncrement), Rect(0, 0, viewWidth, viewHeight), radii
        )
        val parent = widget.parent
        if (parent is ViewGroup) {
            parent.clipChildren = false
            parent.clipToPadding = false
        }
        widget.overlay.add(mShadowBitmapDrawable)
        //Relayout to ensure the shadows are fully drawn
        if (parent != null) {
            parent.requestLayout()
            if (parent is ViewGroup) {
                parent.invalidate()
            }
        }
        return true
    }
}