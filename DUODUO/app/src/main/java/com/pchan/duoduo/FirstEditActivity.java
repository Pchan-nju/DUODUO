package com.pchan.duoduo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class FirstEditActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_edit);

        // 获取要更改的project
        Intent intent = getIntent();
        String projectName = intent.getStringExtra("Project name");
        int projectIndex = intent.getIntExtra("Project index", 0);
        ProjectTimeSchedule projectTimeSchedule = ProjectTimeScheduleFileIO.getScheduleFromFile(this, projectName);

        EditText projectNameEditTextView = findViewById(R.id.projectNameEditTextView);
        EditText startTimeEditTextView = findViewById(R.id.startTimeEditTextView);
        EditText expectTimeEditTextView = findViewById(R.id.expectTimeEditTextView);
        EditText deadlineEditTextView = findViewById(R.id.deadlineEditTextView);

        projectNameEditTextView.setText(projectTimeSchedule.getProjectName());
        startTimeEditTextView.setText(projectTimeSchedule.getBeginningDateString());
        expectTimeEditTextView.setText(projectTimeSchedule.getExpectDateString());
        deadlineEditTextView.setText(projectTimeSchedule.getDeadlineDateString());

        AppCompatButton cancelButton = findViewById(R.id.cancleButton);
        AppCompatButton doneButton = findViewById(R.id.doneBotton);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent mainIntent = new Intent(FirstEditActivity.this, MainActivity.class);
                startActivity(mainIntent);
            }
        });

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                projectTimeSchedule.setProjectName(projectNameEditTextView.getText().toString());
                projectTimeSchedule.setBeginningDateString(startTimeEditTextView.getText().toString());
                projectTimeSchedule.setExpectDateString(expectTimeEditTextView.getText().toString());
                projectTimeSchedule.setDeadlineDateString(deadlineEditTextView.getText().toString());
                //MainActivity.projectLog(projectTimeSchedule);
                ProjectTimeScheduleFileIO.removeScheduleFile(FirstEditActivity.this, projectName);
                ProjectTimeScheduleFileIO.createNewScheduleFile(FirstEditActivity.this, projectTimeSchedule);
                final SharedPreferences sp = getSharedPreferences("user_project", MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("Project" + projectIndex,projectTimeSchedule.getProjectName());
                editor.apply();
                finish();
                Intent mainIntent = new Intent(FirstEditActivity.this, MainActivity.class);
                startActivity(mainIntent);
            }
        });
    }
}