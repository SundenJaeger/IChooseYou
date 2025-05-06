package com.android.ichooseyou.model

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sin

class WheelView : View {
    private var items: List<String> = ArrayList()
    private val segmentPaint = Paint()
    private val textPaint = Paint()
    private val arrowPaint = Paint()
    private val centerPaint = Paint()
    private val wheelRect = RectF()
    private var rotationAngle = 0f
    private var selectedIndex = -1
    private var centerX = 0f
    private var centerY = 0f
    private var radius = 0f

    constructor(context: Context?) : super(context) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    private fun init() {
        // Segment paint configuration
        segmentPaint.isAntiAlias = true
        segmentPaint.style = Paint.Style.FILL

        // Text paint configuration
        textPaint.isAntiAlias = true
        textPaint.setColor(Color.WHITE)
        textPaint.textSize = 40f
        textPaint.textAlign = Paint.Align.CENTER

        // Arrow paint configuration
        arrowPaint.setColor(Color.RED)
        arrowPaint.style = Paint.Style.FILL_AND_STROKE
        arrowPaint.strokeWidth = 8f

        // Center circle paint configuration
        centerPaint.setColor(Color.WHITE)
        centerPaint.style = Paint.Style.FILL
    }

    fun setItems(items: List<String>) {
        this.items = items
        invalidate()
    }

    fun setRotationAngle(angle: Float) {
        rotationAngle = angle
        invalidate()
    }

    fun setSelectedIndex(index: Int) {
        selectedIndex = index
        invalidate()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        centerX = w / 2f
        centerY = h / 2f
        radius = min(centerX, centerY) - 40 // Add margin for arrow
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Draw the arrow indicator (always visible)
        drawArrow(canvas)

        // Don't draw wheel if no items
        if (items.isEmpty()) return

        // Set up wheel dimensions
        wheelRect[centerX - radius, centerY - radius, centerX + radius] = centerY + radius

        // Draw the rotating wheel
        drawWheelSegments(canvas)

        // Draw center circle
        canvas.drawCircle(centerX, centerY, radius * 0.08f, centerPaint)
    }

    private fun drawArrow(canvas: Canvas) {
        val arrowLength = radius * 0.2f
        val arrowTipY = centerY - radius - 10

        // Draw arrow line
        canvas.drawLine(
            centerX, arrowTipY,
            centerX, arrowTipY + arrowLength, arrowPaint
        )

        // Draw arrow head
        val arrowHead = Path()
        val headSize = arrowLength * 0.6f
        arrowHead.moveTo(centerX, arrowTipY)
        arrowHead.lineTo(centerX - headSize, arrowTipY + headSize)
        arrowHead.lineTo(centerX + headSize, arrowTipY + headSize)
        arrowHead.close()
        canvas.drawPath(arrowHead, arrowPaint)
    }

    private fun drawWheelSegments(canvas: Canvas) {
        val anglePerSegment = 360f / items.size
        canvas.save()
        canvas.rotate(rotationAngle, centerX, centerY)
        for (i in items.indices) {
            // Draw segment
            drawSegment(canvas, i, anglePerSegment)

            // Draw segment text
            drawSegmentText(canvas, i, anglePerSegment)
        }
        canvas.restore()
    }

    private fun drawSegment(canvas: Canvas, index: Int, anglePerSegment: Float) {
        segmentPaint.setColor(
            if (index == selectedIndex) Color.parseColor("#FF0000") else  // Brighter red for winner
                getSegmentColor(index)
        )
        val startAngle = index * anglePerSegment
        canvas.drawArc(wheelRect, startAngle, anglePerSegment, true, segmentPaint)
    }

    private fun drawSegmentText(canvas: Canvas, index: Int, anglePerSegment: Float) {
        val textAngle = index * anglePerSegment + anglePerSegment / 2
        val textX = (centerX + radius * 0.6 * cos(Math.toRadians(textAngle.toDouble()))).toFloat()
        val textY = (centerY + radius * 0.6 * sin(Math.toRadians(textAngle.toDouble()))).toFloat()
        canvas.save()
        canvas.rotate(textAngle + 90, textX, textY) // Rotate text to be readable
        canvas.drawText(items[index], textX, textY, textPaint)
        canvas.restore()
    }

    private fun getSegmentColor(index: Int): Int {
        val hue = index * 360f / max(1, items.size) % 360
        return Color.HSVToColor(floatArrayOf(hue, 0.8f, 0.9f))
    }
}