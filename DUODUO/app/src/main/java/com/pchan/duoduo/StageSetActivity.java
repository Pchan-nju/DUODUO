package com.pchan.duoduo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class StageSetActivity extends AppCompatActivity {
    private int stageIndex = 1;
    ProjectTimeSchedule projectTimeSchedule;
    String[] stageDateStrings = new String[5];
    int[] sumOfTarget = new int[5];
    String[][] stageTargetStrings = new String[5][5];
    LinearLayout linearLayout = null;// TODO():获取当前页面布局
    ArrayList<EditText> editTextArrayList = new ArrayList<>();
    int sumOfEditText = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stage_set);
        Intent messageIntent = getIntent();
        String[] firstSetMessage = messageIntent.getStringArrayExtra("FirstSetMessage");
        for (String message : firstSetMessage) {
            Log.d("test intent", message);
        }
        int sumOfStage = Integer.parseInt(firstSetMessage[4]);

        /***Cancel按钮的功能实现***/
        Button cancelButton = null; // TODO():设置为Cancel按钮
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /****回到MainActivity****/
                Intent intent = new Intent(StageSetActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finishAffinity();
                System.exit(0);
            }
        });

        TextView stageTitle = null; // TODO():设置为标题TextView
        stageTitle.setText("Stage" + stageIndex);

        /***Next按钮功能实现***/
        Button nextButton = null; // TODO():设置为NEXT按钮
        if (sumOfStage == stageIndex) {
            nextButton.setText("Finish");
        }
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO():记录当前页面输入的Target内容
                stageIndex++;
                if (sumOfStage < stageIndex) {
                    projectTimeSchedule = new ProjectTimeSchedule(firstSetMessage[0], firstSetMessage[1], firstSetMessage[2], firstSetMessage[3], Integer.parseInt(firstSetMessage[4]), stageDateStrings, sumOfTarget, stageTargetStrings);
                    ProjectTimeScheduleFileIO.createNewScheduleFile(projectTimeSchedule);
                    Intent intent = new Intent(StageSetActivity.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    if (sumOfStage == stageIndex) {
                        nextButton.setText("Finish");
                    }
                    stageTitle.setText("Stage" + stageIndex);
                    // TODO():清空EditText中的文本内容
                }
            }
        });

        /**Plus按钮的功能实现**/
        Button plusButton = null; // TODO():设置为Plus按钮
        plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sumOfEditText++;
                EditText editText = new EditText(StageSetActivity.this);
                // TODO():设置EditText属性
                editTextArrayList.add(editText);
                linearLayout.addView(editText);
            }
        });
    }
}