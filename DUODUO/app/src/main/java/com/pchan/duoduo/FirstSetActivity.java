package com.pchan.duoduo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

                /**切换页面并传递信息**/
                Intent intent = new Intent(FirstSetActivity.this, StageSetActivity.class);

//                intent.putExtra("project name", projectName);
//                intent.putExtra("beginning date", beginningDate);
//                intent.putExtra("expect date", expectDate);
//                intent.putExtra("deadline", deadlineDate);
//                intent.putExtra("sum of stages", sumOfStage);
                /***改为合成字符串数组传递***/
                String[] firstSetMessage = {projectName, beginningDate, expectDate, deadlineDate, sumOfStage};
                intent.putExtra("FirstSetMessage", firstSetMessage);
                startActivity(intent);
            }
        });
    }

    public void cancel(View view) {
        finish();
    }
}