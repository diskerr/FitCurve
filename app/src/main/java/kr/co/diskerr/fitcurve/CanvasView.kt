package kr.co.diskerr.fitcurve

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

@SuppressLint("ClickableViewAccessibility")
class CanvasView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRes: Int = 0)
    : View(context, attrs, defStyleAttr, defStyleRes)
{
    private val path = Path()

    private val paint = Paint().apply {
        style = Paint.Style.STROKE
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
        strokeWidth = 16f
        isAntiAlias = true
        color = Color.argb(64, 0, 0, 255)
    }

    private val points = ArrayList<PointF>()

    init {
        setOnTouchListener { _, event ->
            when(event.actionMasked) {
                MotionEvent.ACTION_DOWN -> {
                    path.reset()
                    points.clear()
                    path.moveTo(event.x, event.y)
                    points.add(PointF(event.x, event.y))
                    invalidate()
                }
                MotionEvent.ACTION_MOVE -> {
                    path.lineTo(event.x, event.y)
                    points.add(PointF(event.x, event.y))
                    invalidate()
                }
                MotionEvent.ACTION_UP -> {
                    if (points.size >= 2) {
                        path.set(Path().apply {
                            for ((index, bc) in CurveFitting.fitCurve(points, 1.0).withIndex()) {
                                if (index == 0) moveTo(bc.first.x.toFloat(), bc.first.y.toFloat())

                                cubicTo(
                                    bc.cp0.x.toFloat(),
                                    bc.cp0.y.toFloat(),
                                    bc.cp1.x.toFloat(),
                                    bc.cp1.y.toFloat(),
                                    bc.last.x.toFloat(),
                                    bc.last.y.toFloat()
                                )
                            }
                        })

                        invalidate()
                    }
                }
            }

            true
        }
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawPath(path, paint)
    }
}
