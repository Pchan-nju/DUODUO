package com.pchan.duoduo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

/*绘制进度圈*/
public class CircleView extends View {
    protected int circleColor = 0xFF0000;
    protected Paint.Style circleStyle = Paint.Style.FILL;
    protected float circleRadius = 1.0f;
    protected float circleCenterPositionX = 0.0f;
    protected float circleCenterPositionY = 0.0f;

    public CircleView(Context context) {
        super(context);
    }

    public void setCircleColor(int color) {
        this.circleColor = color;
    }

    public void setCircleStyle(Paint.Style style) {
        this.circleStyle = style;
    }

    public void setCircleRadius(float radius) {
        this.circleRadius = radius;
    }

    public void setCircleCenterPosition(float x, float y) {
        this.circleCenterPositionX = x;
        this.circleCenterPositionY = y;
    }

    public void setCircle(int color, Paint.Style style, float radius, float x, float y) {
        setCircleColor(color);
        setCircleRadius(radius);
        setCircleStyle(style);
        setCircleCenterPosition(x, y);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        paint.setColor(circleColor);
        paint.setStyle(circleStyle);
        paint.setAntiAlias(true); // 采用抗锯齿功能
        canvas.drawCircle(circleCenterPositionX,circleCenterPositionY,circleRadius,paint);
    }
}
