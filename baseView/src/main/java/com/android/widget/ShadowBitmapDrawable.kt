package com.android.widget

import android.content.res.Resources
import android.graphics.*
import android.graphics.drawable.BitmapDrawable

/**
 * Author: han.chen
 * Time: 2022/1/1 15:58
 */
class ShadowBitmapDrawable(
    resources: Resources,
    bitmap: Bitmap,
    topLeft: Point,
    viewRect: Rect,
    radii: FloatArray
) : BitmapDrawable(resources, bitmap) {

    private var paddingX = topLeft.x
    private var paddingY = topLeft.y
    private var mContentPath: Path = Path()
    private var mDx = 0f
    private var mDy = 0f

    init {
        val rectF = RectF(0f, 0f, viewRect.width().toFloat(), viewRect.height().toFloat())
        mContentPath.addRoundRect(rectF, radii, Path.Direction.CCW)
        setBounds(-paddingX, -paddingY, viewRect.width() + paddingX, viewRect.height() + paddingY)
    }

    fun translate(dx: Float, dy: Float) {
        mDx = dx
        mDy = dy
    }

    override fun draw(canvas: Canvas) {
        canvas.save()
        canvas.translate(mDx, mDy)
        val newRect = canvas.clipBounds
        newRect.inset(-paddingX, -paddingY)
        canvas.clipRect(newRect, Region.Op.INTERSECT)
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            canvas.clipOutPath(mContentPath)
//        } else {
            canvas.clipPath(mContentPath, Region.Op.DIFFERENCE)
//        }
        super.draw(canvas)
        canvas.restore()
    }

}