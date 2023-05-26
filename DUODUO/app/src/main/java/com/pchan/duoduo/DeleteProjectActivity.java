package com.pchan.duoduo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

public class DeleteProjectActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_project);
        // 获取要删除的project
        Intent intent = getIntent();
        String projectName = intent.getStringExtra("Project name");
        ProjectTimeSchedule projectTimeSchedule = ProjectTimeScheduleFileIO.getScheduleFromFile(MainActivity.getAppContext(), projectName);
        // 设置 Project 标题
        TextView projNameView = findViewById(R.id.projName);
        projNameView.setText(projectName);
        // 设置 dateNumber
        TextView dateNumberView = findViewById(R.id.dateNumber);
        dateNumberView.setText("" + projectTimeSchedule.numberOfPassedDays());
        // 设置 rateNumber
        TextView rateNumberTextView = findViewById(R.id.rateNumber);
        rateNumberTextView.setText(projectTimeSchedule.ratioOfCompletedTargets() + "%");
        // 设置 EndButton ——— 删除项目
        MaterialButton endButton = findViewById(R.id.endButton);
        endButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProjectTimeScheduleFileIO.removeScheduleFile(MainActivity.getAppContext(), projectName);

                // 用户 project 数量喜减一
                final SharedPreferences sp = getSharedPreferences("user_project", MODE_PRIVATE);
                int sumOfUserProjects = sp.getInt("sum",0);
                SharedPreferences.Editor editor = sp.edit();
                editor.putInt("sum", sumOfUserProjects - 1);

                // 删除储存的 Project name
                String[] projectNameStrings = new String[sumOfUserProjects];
                boolean indexToRemove = false;
                for (int i = 1; i <= sumOfUserProjects; i++) {
                    projectNameStrings[i - 1] = sp.getString("Project" + i, "Error");
                    Log.d("Project" + i, projectNameStrings[i - 1]);
                    if (indexToRemove) {
                        editor.putString("Project" + (i - 1), projectNameStrings[i - 1]);
                    }
                    if (projectNameStrings[i - 1].equals(projectName)) {
                        indexToRemove = true;
                    }
                }
                editor.apply();
                finish();
                Intent mainIntent = new Intent(DeleteProjectActivity.this, MainActivity.class);
                startActivity(mainIntent);
            }
        });
    }

    public void Back(View view) {
        finish();
        Intent mainIntent = new Intent(DeleteProjectActivity.this, MainActivity.class);
        startActivity(mainIntent);
    }
}