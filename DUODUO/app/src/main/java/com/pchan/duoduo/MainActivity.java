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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private static Context appContext;
    public ProjectTimeSchedule projectTimeSchedule;
    public TextView projectNameTextView;
    public String[] projectNameStrings;
    int sumOfUserProjects;
    public ConstraintLayout constraintLayout;
    public TimeScheduleCircleView timeScheduleCircleView;
    public MaterialButton originAddProjectButton;
    public ScaleAnimation scaleAnimation;
    public LinearLayout projectButtonsLinearLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        projectNameTextView = findViewById(R.id.textView7);
        final SharedPreferences sp = getSharedPreferences("user_project", MODE_PRIVATE);
        sumOfUserProjects = sp.getInt("sum",0);
        projectNameStrings = new String[sumOfUserProjects + 2];

        // 获取底下一排按钮的LinearLayout
        projectButtonsLinearLayout = findViewById(R.id.projectButtonsLinearLayout);

        //获取上下文对象
        appContext = getApplicationContext();


        /*判断是否存在已有的project
        * 如果是，则进入新建project页面的前导页面
        * 如果不是，则显示已有的project进度
        * */
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

        constraintLayout = (ConstraintLayout) findViewById(R.id.activity_main);

        /*****初始化一个ProjectTimeSchedule实例，并应用于timeScheduleCircleView*****/
        projectTimeSchedule = ProjectTimeScheduleFileIO.getScheduleFromFile(this, projectNameStrings[0]);
/*
        Log.d("Read Schedule Files", "Success");
        Log.d("Project name", projectTimeSchedule.getProjectName());
        Log.d("Project beginning time", projectTimeSchedule.getBeginningDateString());
        Log.d("Project expected end time", projectTimeSchedule.getExpectDateString());
        Log.d("Project deadline", projectTimeSchedule.getDeadlineDateString());
        Log.d("Project sum of stages", "" + projectTimeSchedule.getSumOfStageDate());
        */
        timeScheduleCircleView = new TimeScheduleCircleView(this);
        timeScheduleCircleView.setProjectTimeSchedule(projectTimeSchedule);
        timeScheduleCircleView.setCenterPosition(centerX, centerY);

        projectNameTextView.setText(projectTimeSchedule.getProjectName());

        /*********设置动画*********/
        timeScheduleCircleView.setAlpha(0f);
        scaleAnimation = new ScaleAnimation(0f, 1f, 0f, 1f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(500);
        timeScheduleCircleView.startAnimation(scaleAnimation);
        timeScheduleCircleView.animate().alpha(1f).setDuration(500);

        // 应用timeScheduleCircleView
        constraintLayout.addView(timeScheduleCircleView);

        // 增加project
        LayoutInflater inflater = getLayoutInflater();
        View rootView = inflater.inflate(R.layout.activity_start, null);
        originAddProjectButton = (MaterialButton) rootView.findViewById(R.id.startButton);

        // 默认打开界面为 project1
        // 记录上一个打开的Project, 初始值为1
        final int[] lastProjectIndex = {1};
        // 设置最大 Project 数量
        final int MAX_PROJECT_NUM = 4 + 1;
        // 切换project1
        AppCompatButton[] projectButtons = new AppCompatButton[MAX_PROJECT_NUM];
        projectButtons[1] = (AppCompatButton) findViewById(R.id.firstProjectButton);
        projectButtons[2] = (AppCompatButton) findViewById(R.id.button1);
        projectButtons[3] = (AppCompatButton) findViewById(R.id.button2);
        projectButtons[4] = (AppCompatButton) findViewById(R.id.button3);
        projectButtonsLinearLayout.removeView(projectButtons[1]);
        // 获取 Edit 按钮
        AppCompatButton editButton = (AppCompatButton) findViewById(R.id.button);

        for (int i = 1; i < MAX_PROJECT_NUM; i++) {
            int finalI = i;
            projectButtons[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    projectButtonsLinearLayout.removeView(editButton);
                    projectButtonsLinearLayout.addView(projectButtons[lastProjectIndex[0]], lastProjectIndex[0] - 1);
                    projectButtonsLinearLayout.removeView(projectButtons[finalI]);
                    projectButtonsLinearLayout.addView(editButton, finalI - 1);
                    lastProjectIndex[0] = finalI;
                    Log.d("Press Project", "" + finalI);
                    changeProject(finalI);
                }
            });
        }
    }

    public static Context getAppContext(){
        return appContext;
    }

    public MaterialButton newAddProjectButton() {
        ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(700,700);
        // 设置水平居中和垂直居中的约束条件
        layoutParams.leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID;
        layoutParams.rightToRight = ConstraintLayout.LayoutParams.PARENT_ID;
        layoutParams.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
        layoutParams.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID;

        //复制一个新建Project按钮
        MaterialButton addProjectButton = new MaterialButton(this);
        addProjectButton.setBackground(originAddProjectButton.getBackground());
        addProjectButton.setText(originAddProjectButton.getText());
        addProjectButton.setLayoutParams(layoutParams);
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
        return addProjectButton;
    }

    public void changeProject(int index) {
        // 去除页面已有的addProjectButton或TimeScheduleCircleView
        for (int i = 0; i < constraintLayout.getChildCount(); i++) {
            View child = constraintLayout.getChildAt(i);
            if (child instanceof MaterialButton || child instanceof TimeScheduleCircleView) {
                try {
                    constraintLayout.removeView(child);
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                    Log.d("Error", "remove view failed " + index);
                }

            }
        }
        // 判断这个Project是否存在
        if (sumOfUserProjects >= index) {
            // Project存在
            try {
                projectNameTextView.setText(projectNameStrings[index - 1]);
                ProjectTimeSchedule newProjectTimeSchedule = ProjectTimeScheduleFileIO.getScheduleFromFile(MainActivity.this, projectNameStrings[index - 1]);
                timeScheduleCircleView.setProjectTimeSchedule(newProjectTimeSchedule);
                constraintLayout.removeView(timeScheduleCircleView);

                /****重新增加动画****/
                timeScheduleCircleView.setAlpha(0f);
                timeScheduleCircleView.startAnimation(scaleAnimation);
                timeScheduleCircleView.animate().alpha(1f).setDuration(500);

                constraintLayout.addView(timeScheduleCircleView);
                projectTimeSchedule = newProjectTimeSchedule;
                Log.d("Switch Project", "Success id " + index);
            } catch (Exception e) {
                e.printStackTrace();
                Log.d("Switch Project", "ERROR id " + index);
            }
        } else {
            // Project不存在
            // 增添新建Project按钮
            projectNameTextView.setText("A New Project!");
            MaterialButton materialButton = newAddProjectButton();
            constraintLayout.addView(materialButton);
        }
    }
}