package com.pchan.duoduo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.graphics.Paint;
import android.os.Bundle;
import android.util.DisplayMetrics;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*获取屏幕中心坐标*/
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int centerX = displayMetrics.widthPixels / 2;
        int centerY = displayMetrics.heightPixels / 2;

        /*在屏幕中央绘制圆形*/
        CircleView circleView = new CircleView(this);
        circleView.setCircle(0xFFFFB6C1, Paint.Style.FILL, 400.0f, centerX,centerY);    // 设置圆形参数
        ConstraintLayout constraintLayout = (ConstraintLayout) findViewById(R.id.activity_main);
        constraintLayout.addView(circleView);
    }
}