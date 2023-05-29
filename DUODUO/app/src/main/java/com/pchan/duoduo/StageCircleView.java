package com.pchan.duoduo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.View;

public class StageCircleView extends View {
    private int stageIndex;
    private ProjectTimeSchedule projectTimeSchedule;

    private float centerPositionX = 80;
    private float centerPositionY = 80;

    private RectF rectF;

    public StageCircleView(Context context) {
        super(context);
    }

    public void setCenterPosition(float centerPositionX, float centerPositionY) {
        this.centerPositionX = centerPositionX;
        this.centerPositionY = centerPositionY;
    }

    /**
     * @param stageIndex 第几阶段
     * @param projectTimeSchedule 目标Project
     */
    public void setStage(int stageIndex, ProjectTimeSchedule projectTimeSchedule) {
        this.stageIndex = stageIndex;
        this.projectTimeSchedule = projectTimeSchedule;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // 调用父类的onMeasure方法，以确保测量规范的正确性
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        // 设置自定义 View 的宽度和高度
        int desiredWidth = 160;  // 设置宽度像素
        int desiredHeight = 160; // 设置高度像素

        // 根据测量规范计算实际宽度和高度
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width;
        int height;

        // 根据测量规范和所需大小计算实际宽度
        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            width = Math.min(desiredWidth, widthSize);
        } else {
            width = desiredWidth;
        }

        // 根据测量规范和所需大小计算实际高度
        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            height = Math.min(desiredHeight, heightSize);
        } else {
            height = desiredHeight;
        }

        // 设置测量结果
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        Paint paint = new Paint();
        paint.setAntiAlias(true); // 采用抗锯齿功能

        float radius = 80.0f;
        // 先画底座圆
        paint.setColor(0x3FE6E4F6);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(centerPositionX, centerPositionY, radius, paint);
        // 再画扇形
        paint.setColor(0xFFE6E4F6);
        int startAngle = -90;           // 扇形的起始角度
        int sweepAngle = (int) ((int) 360 * projectTimeSchedule.ratioOfCompletedTargetsOfStage(stageIndex));           // 扇形的扫描角度

        rectF = new RectF(centerPositionX - radius, centerPositionY - radius, centerPositionX + radius, centerPositionY + radius);
        canvas.drawArc(centerPositionX - radius, centerPositionY - radius, centerPositionX + radius, centerPositionY + radius,
                startAngle, sweepAngle, true, paint);
    }
}
