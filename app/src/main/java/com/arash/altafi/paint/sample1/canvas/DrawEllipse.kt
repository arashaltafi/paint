package com.arash.altafi.paint.sample1.canvas

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.arash.altafi.paint.sample1.Sample1Activity
import com.arash.altafi.paint.sample1.Sample1Activity.Companion.currentBrush
import com.arash.altafi.paint.sample1.cons.Ellipse

class DrawEllipse @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val ellipse = mutableListOf<Ellipse>()

    private val paintBrush = Paint().apply {
        strokeWidth = 10f
        color = currentBrush
        isAntiAlias = true
    }

    fun updateColor(newColor: Int) {
        paintBrush.color = newColor
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                Sample1Activity.colorList.add(Sample1Activity.currentBrush)
                ellipse.add(Ellipse(event.x, event.y, event.x, event.y, currentBrush))
                return true
            }

            MotionEvent.ACTION_MOVE -> {
                val current = ellipse[ellipse.size - 1]
                current.stopX = event.x
                current.stopY = event.y
                invalidate()
                return true
            }

            MotionEvent.ACTION_UP -> {
                val current = ellipse[ellipse.size - 1]
                current.stopX = event.x
                current.stopY = event.y
                invalidate()
                return true
            }

            else -> {
                return false
            }
        }
    }

    override fun onDraw(canvas: Canvas) {
        for (e in ellipse) {
            paintBrush.color = e.color
            canvas.drawOval(e.startX, e.startY, e.stopX, e.stopY, paintBrush)
        }
    }

    fun undo() {
        if (ellipse.size != 0) {
            ellipse.removeAt(ellipse.size - 1)
            invalidate()
        }
    }

}