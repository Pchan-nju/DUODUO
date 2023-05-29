package com.pchan.duoduo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Date;
import java.util.regex.Pattern;

public class FirstSetActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_set);

        EditText projectNameEditText = findViewById(R.id.projectNameEditTextView);
        EditText startTimeEditText = findViewById(R.id.startTimeEditTextView);
        EditText expectTimeEditText = findViewById(R.id.expectTimeEditTextView);
        EditText deadlineEditText = findViewById(R.id.deadlineEditTextView);
        EditText stageNumberEditText = findViewById(R.id.stageNumberEditTextView);

        /***默认开始日期为当前日期***/
        SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd");
        Date currentDate = new Date();
        String currentDateString = ft.format(currentDate);
        startTimeEditText.setText(currentDateString);

        Button nextButton = findViewById(R.id.nextButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                 * 对于开始时间，夏日称其为start time, 而我称其为beginning time
                 * 故这里做一个生硬的名称转换，意思其实是相同的
                 * @author Pchan-nju
                 * **/
                String projectName = projectNameEditText.getText().toString();
                String beginningDate = startTimeEditText.getText().toString();
                String expectDate = expectTimeEditText.getText().toString();
                String deadlineDate = deadlineEditText.getText().toString();
                String sumOfStage = stageNumberEditText.getText().toString();
                // 做个日志
                Log.d("project name", projectName);
                Log.d("beginning date", beginningDate);
                Log.d("expect date", expectDate);
                Log.d("deadline", deadlineDate);
                Log.d("sum of stages", sumOfStage);

                // 判断projectName是否重复
                final SharedPreferences sp = getSharedPreferences("user_project", MODE_PRIVATE);
                int sumOfUserProjects = sp.getInt("sum",0);
                for (int i = 1; i <= sumOfUserProjects; i++) {
                    String tempProjectName = sp.getString("Project" + i, "Error");
                    if (tempProjectName.equals("Error")) {
                        Log.d("Read Project Name Error", "" + i + " of " + sumOfUserProjects);
                    }
                    if (tempProjectName.equals(projectName)) {
                        Log.d("Already have this name", tempProjectName);
                        Toast.makeText(FirstSetActivity.this, "Already have this name", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                // 判断用户输入是否合法
                if (TextUtils.isEmpty(projectName)) {
                    Toast.makeText(FirstSetActivity.this, "Please input your project name", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(expectDate) || !isDateFormatValid(expectDate)) {
                    Toast.makeText(FirstSetActivity.this, "Please input your expected end time correctly", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(deadlineDate) || !isDateFormatValid(deadlineDate)) {
                    Toast.makeText(FirstSetActivity.this, "Please input your deadline correctly", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(sumOfStage)) {
                    Toast.makeText(FirstSetActivity.this, "Please input your number of stages", Toast.LENGTH_SHORT).show();
                } else if (Integer.parseInt(sumOfStage) >= 6 || Integer.parseInt(sumOfStage) < 0) {
                    Toast.makeText(FirstSetActivity.this, "Number of stages should be less than 5 and more than 0", Toast.LENGTH_SHORT).show();
                } else {
                    if (TextUtils.isEmpty(beginningDate)) {
                        Toast.makeText(FirstSetActivity.this, "Set today as your start time", Toast.LENGTH_SHORT).show();
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        Date currentDate = new Date();
                        beginningDate = dateFormat.format(currentDate);
                    }
                    // 切换页面并传递信息
                    Intent intent = new Intent(FirstSetActivity.this, StageSetActivity.class);
                    String[] firstSetMessage = {projectName, beginningDate, expectDate, deadlineDate, sumOfStage};
                    intent.putExtra("FirstSetMessage", firstSetMessage);
                    startActivity(intent);
                }
            }
        });
    }

    /***判断输入字符串是否符合“yyyy-MM-dd”格式***/
    public static boolean isDateFormatValid(String dateString) {
        String regex = "\\d{4}-\\d{2}-\\d{2}";
        return Pattern.matches(regex, dateString);
    }

    public void cancel(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}