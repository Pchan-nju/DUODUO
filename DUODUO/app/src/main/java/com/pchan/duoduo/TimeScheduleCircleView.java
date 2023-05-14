package com.pchan.duoduo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

/***根据已有的ProjectTimeSchedule确定展示***/
public class TimeScheduleCircleView extends View {
    private ProjectTimeSchedule projectTimeSchedule = new ProjectTimeSchedule();
    private float centerPositionX;
    private float centerPositionY;
    public TimeScheduleCircleView(Context context) {
        super(context);
    }

    public void setCenterPosition(float x, float y) {
        this.centerPositionX = x;
        this.centerPositionY = y;
    }

    public void setProjectTimeSchedule(ProjectTimeSchedule projectTimeSchedule) {
        this.projectTimeSchedule = projectTimeSchedule;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        paint.setAntiAlias(true); // 采用抗锯齿功能
        final float radius = 400.0f;

        /**绘制不同大小的圆**/
        /**先画deadline的圆**/
        paint.setColor(0x4FFFB6C1);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(centerPositionX, centerPositionY, radius, paint);

        /**画currentDate的圆**/
        paint.setColor(0x6FE6E6FA);
        paint.setStrokeWidth(5);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        canvas.drawCircle(centerPositionX, centerPositionY, radius * projectTimeSchedule.ratioOfPassedDays(), paint);

        /**画各个阶段的圆**/
        float[] ratiosOfStages = projectTimeSchedule.ratioOfStageDays();
        paint.setStyle(Paint.Style.STROKE);
        for (int i = projectTimeSchedule.getSumOfStageDate() - 1; i >= 0 ; i--) {
            Log.d("LoadStageCircle", "success id " + ratiosOfStages[i]);
            paint.setColor(0xFFC1BED5);
            canvas.drawCircle(centerPositionX, centerPositionY, radius * ratiosOfStages[i], paint);
        }
    }
}