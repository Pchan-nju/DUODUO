package com.pchan.duoduo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private static Context appContext;
    public ProjectTimeSchedule projectTimeSchedule;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //获取上下文对象
        appContext = getApplicationContext();


        /*判断是否存在已有的project
        * 如果是，则进入新建project页面的前导页面
        * 如果不是，则显示已有的project进度
        * */
        final SharedPreferences sp = getSharedPreferences("user_project", MODE_PRIVATE);
        int sumOfUserProjects = sp.getInt("sum",0);
        String[] projectNameStrings = new String[sumOfUserProjects + 2];
        for (int i = 0; i < sumOfUserProjects + 2; i++) {
            projectNameStrings[i] = "";
        }
        if (sumOfUserProjects == 0) {
            Log.d("Start", "A new project");
            Intent intent = new Intent(this, StartActivity.class);
            startActivity(intent);
        } else {
            Log.d("Start", "already have " + sumOfUserProjects + " projects");
            for (int i = 1; i <= sumOfUserProjects; i++) {
                projectNameStrings[i - 1] = sp.getString("Project" + i, "Error");
                Log.d("Project" + i, projectNameStrings[i - 1]);
            }
        }

        /*获取屏幕中心坐标*/
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int centerX = displayMetrics.widthPixels / 2;
        int centerY = displayMetrics.heightPixels / 2;

        /*在屏幕中央绘制圆形*/
//        CircleView circleView = new CircleView(this);
//        circleView.setCircle(0xFFFFB6C1, Paint.Style.FILL, 400.0f, centerX,centerY);    // 设置圆形参数
        ConstraintLayout constraintLayout = (ConstraintLayout) findViewById(R.id.activity_main);
//        constraintLayout.addView(circleView);
//        circleView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d("circleView", "HaHaHa");
//            }
//        });

        /*****初始化一个ProjectTimeSchedule实例，并应用于timeScheduleCircleView*****/
        projectTimeSchedule = ProjectTimeScheduleFileIO.getScheduleFromFile(this, projectNameStrings[0]);
        Log.d("Read Schedule Files", "Success");
        Log.d("Project name", projectTimeSchedule.getProjectName());
        Log.d("Project beginning time", projectTimeSchedule.getBeginningDateString());
        Log.d("Project expected end time", projectTimeSchedule.getExpectDateString());
        Log.d("Project deadline", projectTimeSchedule.getDeadlineDateString());
        Log.d("Project sum of stages", "" + projectTimeSchedule.getSumOfStageDate());
        TimeScheduleCircleView timeScheduleCircleView = new TimeScheduleCircleView(this);
        timeScheduleCircleView.setProjectTimeSchedule(projectTimeSchedule);
        timeScheduleCircleView.setCenterPosition(centerX, centerY);

        /********/
        TextView projectNameTextView = findViewById(R.id.textView7);
        projectNameTextView.setText(projectTimeSchedule.getProjectName());

        /*********设置动画*********/
        timeScheduleCircleView.setAlpha(0f);
        ScaleAnimation scaleAnimation = new ScaleAnimation(0f, 1f, 0f, 1f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(500);
        timeScheduleCircleView.startAnimation(scaleAnimation);
        timeScheduleCircleView.animate().alpha(1f).setDuration(500);

        constraintLayout.addView(timeScheduleCircleView);

        /***增加project***/
        LayoutInflater inflater = getLayoutInflater();
        View rootView = inflater.inflate(R.layout.activity_start, null);
        MaterialButton originAddProjectButton = (MaterialButton) rootView.findViewById(R.id.startButton);
        MaterialButton addProjectButton = new MaterialButton(this);
        addProjectButton.setBackground(originAddProjectButton.getBackground());
        addProjectButton.setText(originAddProjectButton.getText());
        addProjectButton.setLayoutParams(originAddProjectButton.getLayoutParams());
        addProjectButton.setTextSize(40);
        addProjectButton.setTypeface(Typeface.DEFAULT_BOLD);
        addProjectButton.setStrokeColor(originAddProjectButton.getStrokeColor());
        addProjectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addProjectIntent = new Intent(MainActivity.this, FirstSetActivity.class);
                startActivity(addProjectIntent);
            }
        });

        /***切换project1***/
        /***TODO:切换显示有问题！***/
        AppCompatButton secondProjectButton = (AppCompatButton) findViewById(R.id.button1);
        secondProjectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                constraintLayout.removeView(addProjectButton);
                projectNameTextView.setText(projectNameStrings[1]);
                if (sumOfUserProjects < 2) {
                    constraintLayout.removeView(timeScheduleCircleView);
                    constraintLayout.addView(addProjectButton);
                    return;
                }
                try {
                    ProjectTimeSchedule newProjectTimeSchedule = ProjectTimeScheduleFileIO.getScheduleFromFile(MainActivity.this, projectNameStrings[1]);
                    timeScheduleCircleView.setProjectTimeSchedule(newProjectTimeSchedule);
                    constraintLayout.removeView(timeScheduleCircleView);

                    /****重新增加动画****/
                    timeScheduleCircleView.setAlpha(0f);
                    timeScheduleCircleView.startAnimation(scaleAnimation);
                    timeScheduleCircleView.animate().alpha(1f).setDuration(500);

                    constraintLayout.addView(timeScheduleCircleView);
                    projectTimeSchedule = newProjectTimeSchedule;
                    Log.d("Switch Project", "Success");
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("Switch Project", "ERROR");
                }
            }
        });

        /***切换project2***/
        AppCompatButton thirdProjectButton = (AppCompatButton) findViewById(R.id.button2);
        secondProjectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                constraintLayout.removeView(addProjectButton);
                if (sumOfUserProjects < 3) {
                    constraintLayout.removeView(timeScheduleCircleView);
                    constraintLayout.addView(addProjectButton);
                    return;
                }
                projectNameTextView.setText(projectNameStrings[2]);
                try {
                    ProjectTimeSchedule newProjectTimeSchedule = ProjectTimeScheduleFileIO.getScheduleFromFile(MainActivity.this, projectNameStrings[1]);
                    timeScheduleCircleView.setProjectTimeSchedule(newProjectTimeSchedule);
                    constraintLayout.removeView(timeScheduleCircleView);

                    /****重新增加动画****/
                    timeScheduleCircleView.setAlpha(0f);
                    timeScheduleCircleView.startAnimation(scaleAnimation);
                    timeScheduleCircleView.animate().alpha(1f).setDuration(500);

                    constraintLayout.addView(timeScheduleCircleView);
                    projectTimeSchedule = newProjectTimeSchedule;
                    Log.d("Switch Project", "Success");
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("Switch Project", "ERROR");
                }
            }
        });

        /***切换project3***/
        AppCompatButton fourthProjectButton = (AppCompatButton) findViewById(R.id.button3);
        secondProjectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                constraintLayout.removeView(addProjectButton);
                if (sumOfUserProjects < 4) {
                    constraintLayout.removeView(timeScheduleCircleView);
                    constraintLayout.addView(addProjectButton);
                    return;
                }
                projectNameTextView.setText(projectNameStrings[3]);
                try {
                    ProjectTimeSchedule newProjectTimeSchedule = ProjectTimeScheduleFileIO.getScheduleFromFile(MainActivity.this, projectNameStrings[1]);
                    timeScheduleCircleView.setProjectTimeSchedule(newProjectTimeSchedule);
                    constraintLayout.removeView(timeScheduleCircleView);

                    /****重新增加动画****/
                    timeScheduleCircleView.setAlpha(0f);
                    timeScheduleCircleView.startAnimation(scaleAnimation);
                    timeScheduleCircleView.animate().alpha(1f).setDuration(500);

                    constraintLayout.addView(timeScheduleCircleView);
                    projectTimeSchedule = newProjectTimeSchedule;
                    Log.d("Switch Project", "Success");
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("Switch Project", "ERROR");
                }
            }
        });
    }

    public static Context getAppContext(){
        return appContext;
    }
}