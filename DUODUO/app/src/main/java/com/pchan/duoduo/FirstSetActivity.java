package com.pchan.duoduo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Date;

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

        Button nextButton = findViewById(R.id.nextButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * 对于开始时间，夏日称其为start time, 而我称其为beginning time
                 * 故这里做一个生硬的名称转换，意思其实是相同的
                 * @author Pchan-nju
                 * **/
                String projectName = projectNameEditText.getText().toString();
                String beginningDate = startTimeEditText.getText().toString();
                String expectDate = expectTimeEditText.getText().toString();
                String deadlineDate = deadlineEditText.getText().toString();
                String sumOfStage = stageNumberEditText.getText().toString();
                /**做个日志**/
                Log.d("project name", projectName);
                Log.d("beginning date", beginningDate);
                Log.d("expect date", expectDate);
                Log.d("deadline", deadlineDate);
                Log.d("sum of stages", sumOfStage);

                if (TextUtils.isEmpty(projectName)) {
                    Toast.makeText(FirstSetActivity.this, "Please input your project name", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(expectDate)) {
                    Toast.makeText(FirstSetActivity.this, "Please input your expected end time", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(deadlineDate)) {
                    Toast.makeText(FirstSetActivity.this, "Please input your deadline", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(sumOfStage)) {
                    Toast.makeText(FirstSetActivity.this, "Please input your number of stages", Toast.LENGTH_SHORT).show();
                } else if (Integer.parseInt(sumOfStage) >= 4 || Integer.parseInt(sumOfStage) < 0) {
                    Toast.makeText(FirstSetActivity.this, "Number of stages should be less than 5 and no less than 0", Toast.LENGTH_SHORT).show();
                } else if (Integer.parseInt(sumOfStage) == 0) {
                    // TODO()
                } else {
                    if (TextUtils.isEmpty(beginningDate)) {
                        Toast.makeText(FirstSetActivity.this, "Set today as your start time", Toast.LENGTH_SHORT).show();
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        Date currentDate = new Date();
                        beginningDate = dateFormat.format(currentDate);
                    }
                    /**切换页面并传递信息**/
                    Intent intent = new Intent(FirstSetActivity.this, StageSetActivity.class);
                    String[] firstSetMessage = {projectName, beginningDate, expectDate, deadlineDate, sumOfStage};
                    intent.putExtra("FirstSetMessage", firstSetMessage);
                    startActivity(intent);
                }
            }
        });
    }

    public void cancel(View view) {
        finish();
    }
}