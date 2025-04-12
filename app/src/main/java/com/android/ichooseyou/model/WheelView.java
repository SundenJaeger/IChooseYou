package com.android.ichooseyou.model;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import java.util.ArrayList;
import java.util.List;

public class WheelView extends View {
    private List<String> items = new ArrayList<>();
    private Paint segmentPaint = new Paint();
    private Paint textPaint = new Paint();
    private Paint arrowPaint = new Paint();
    private Paint centerPaint = new Paint();
    private RectF wheelRect = new RectF();
    private float rotationAngle = 0;
    private int selectedIndex = -1;
    private float centerX, centerY, radius;

    public WheelView(Context context) {
        super(context);
        init();
    }

    public WheelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        // Segment paint configuration
        segmentPaint.setAntiAlias(true);
        segmentPaint.setStyle(Paint.Style.FILL);

        // Text paint configuration
        textPaint.setAntiAlias(true);
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(40);
        textPaint.setTextAlign(Paint.Align.CENTER);

        // Arrow paint configuration
        arrowPaint.setColor(Color.RED);
        arrowPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        arrowPaint.setStrokeWidth(8);

        // Center circle paint configuration
        centerPaint.setColor(Color.WHITE);
        centerPaint.setStyle(Paint.Style.FILL);
    }

    public void setItems(List<String> items) {
        this.items = items;
        invalidate();
    }

    public void setRotationAngle(float angle) {
        this.rotationAngle = angle;
        invalidate();
    }

    public void setSelectedIndex(int index) {
        this.selectedIndex = index;
        invalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        centerX = w / 2f;
        centerY = h / 2f;
        radius = Math.min(centerX, centerY) - 40; // Add margin for arrow
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Draw the arrow indicator (always visible)
        drawArrow(canvas);

        // Don't draw wheel if no items
        if (items.isEmpty()) return;

        // Set up wheel dimensions
        wheelRect.set(centerX - radius, centerY - radius,
                centerX + radius, centerY + radius);

        // Draw the rotating wheel
        drawWheelSegments(canvas);

        // Draw center circle
        canvas.drawCircle(centerX, centerY, radius * 0.08f, centerPaint);
    }

    private void drawArrow(Canvas canvas) {
        float arrowLength = radius * 0.2f;
        float arrowTipY = centerY - radius - 10;

        // Draw arrow line
        canvas.drawLine(centerX, arrowTipY,
                centerX, arrowTipY + arrowLength, arrowPaint);

        // Draw arrow head
        Path arrowHead = new Path();
        float headSize = arrowLength * 0.6f;
        arrowHead.moveTo(centerX, arrowTipY);
        arrowHead.lineTo(centerX - headSize, arrowTipY + headSize);
        arrowHead.lineTo(centerX + headSize, arrowTipY + headSize);
        arrowHead.close();
        canvas.drawPath(arrowHead, arrowPaint);
    }

    private void drawWheelSegments(Canvas canvas) {
        float anglePerSegment = 360f / items.size();

        canvas.save();
        canvas.rotate(rotationAngle, centerX, centerY);

        for (int i = 0; i < items.size(); i++) {
            // Draw segment
            drawSegment(canvas, i, anglePerSegment);

            // Draw segment text
            drawSegmentText(canvas, i, anglePerSegment);
        }

        canvas.restore();
    }

    private void drawSegment(Canvas canvas, int index, float anglePerSegment) {
        segmentPaint.setColor(index == selectedIndex ?
                Color.parseColor("#FF0000") : // Brighter red for winner
                getSegmentColor(index));
        float startAngle = index * anglePerSegment;
        canvas.drawArc(wheelRect, startAngle, anglePerSegment, true, segmentPaint);
    }

    private void drawSegmentText(Canvas canvas, int index, float anglePerSegment) {
        float textAngle = index * anglePerSegment + anglePerSegment / 2;
        float textX = (float) (centerX + radius * 0.6 * Math.cos(Math.toRadians(textAngle)));
        float textY = (float) (centerY + radius * 0.6 * Math.sin(Math.toRadians(textAngle)));

        canvas.save();
        canvas.rotate(textAngle + 90, textX, textY); // Rotate text to be readable
        canvas.drawText(items.get(index), textX, textY, textPaint);
        canvas.restore();
    }

    private int getSegmentColor(int index) {
        float hue = (index * 360f / Math.max(1, items.size())) % 360;
        return Color.HSVToColor(new float[]{hue, 0.8f, 0.9f});
    }
}