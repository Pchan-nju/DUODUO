package com.pchan.duoduo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class StageEditActivity extends AppCompatActivity {

    public int sumOfEditText;
    public String projectName;
    public int stageIndex;
    public ProjectTimeSchedule projectTimeSchedule;
    public EditText stageDueEditText;
    public String[] stageDateStrings;
    public int[] sumOfTarget;
    public String[][] stageTargetStrings;
    public boolean[][] ifTargetFinish;
    LinearLayout targetLinearLayout;
    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stage_edit);

        Intent messageIntent = getIntent();
        projectName = messageIntent.getStringExtra("Project name");
        stageIndex = messageIntent.getIntExtra("stageIndex", 0);
        Log.d("stage index", "" + stageIndex);
        projectTimeSchedule = ProjectTimeScheduleFileIO.getScheduleFromFile(this, projectName);
        sumOfEditText = projectTimeSchedule.getSumOfStageTarget()[stageIndex - 1];
        Log.d("sum of target", "" + sumOfEditText);
        stageDateStrings = projectTimeSchedule.getStageDateStrings();
        sumOfTarget = projectTimeSchedule.getSumOfStageTarget();
        stageTargetStrings = projectTimeSchedule.getStageTarget();
        ifTargetFinish = projectTimeSchedule.getStageTargetFinish();

        linearLayout = findViewById(R.id.StageTargetLinearLayout);// 获取当前LinearLayout页面布局

        /*
         * editText, index, deleteButton 的合体*
         * 将targetLinearLayout加入linearLayout*/
        targetLinearLayout = findViewById(R.id.SingleTargetLayout);
        linearLayout.removeView(targetLinearLayout);
        for (int i = 0; i < sumOfEditText; i++) {
            LinearLayout newLinearLayout = newLinearLayoutOfTarget(stageTargetStrings[stageIndex - 1][i], ifTargetFinish[stageIndex - 1][i]);
//            LinearLayout newLinearLayout = newLinearLayoutOfTarget("" + i);
            linearLayout.addView(newLinearLayout, linearLayout.getChildCount() - 2);
        }

        /***Cancel按钮的功能实现***/
        Button cancelButton = findViewById(R.id.cancleButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /****回到MainActivity****/
                Intent intent = new Intent(StageEditActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finishAffinity();
                System.exit(0);
            }
        });

        /**呈现当前阶段序号**/
        TextView stageTitle = findViewById(R.id.stageNumber);
        stageTitle.setText("" + stageIndex);

        // 获取stageDueEditText
        stageDueEditText = findViewById(R.id.stageDueTime);
        stageDueEditText.setText(projectTimeSchedule.getStageDateStrings()[stageIndex - 1]);

        /***Done按钮功能实现***/
        Button nextButton = findViewById(R.id.doneBotton); // 设置为Done按钮
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 记录该阶段结束日期
                if (TextUtils.isEmpty(stageDueEditText.getText().toString()) || !FirstSetActivity.isDateFormatValid(stageDueEditText.getText().toString())) {
                    Toast.makeText(StageEditActivity.this, "Please input date correctly", Toast.LENGTH_SHORT).show();
                    return;
                }
                stageDateStrings[stageIndex - 1] = stageDueEditText.getText().toString();
                // 记录当前页面输入的Target内容
                sumOfTarget[stageIndex - 1] = sumOfEditText;
                for (int i = 1 ; i <= sumOfEditText; i++){
                    View child = linearLayout.getChildAt(1 + 3);
                    if (child instanceof LinearLayout) {
                        AppCompatEditText editText = (AppCompatEditText) ((LinearLayout) child).getChildAt(1);
                        CheckBox checkBox = (CheckBox) ((LinearLayout) child).getChildAt(0);
                        if (editText.getText()!= null && !TextUtils.isEmpty(editText.getText().toString())) {
                            Log.d("stageIndex, i", stageIndex + ", " + i);
                            Log.d("length", "" + stageTargetStrings.length + ", " + stageTargetStrings[0].length);
                            stageTargetStrings[stageIndex - 1][i - 1] = editText.getText().toString();
                            Log.d("Stage Target " + (stageIndex - 1) + ", " + (i - 1), stageTargetStrings[stageIndex - 1][i - 1]);
                        }
                        ifTargetFinish[stageIndex - 1][i - 1] = checkBox.isChecked();
                    }
                    linearLayout.removeView(child);
                }
                /********存储用户Project信息********/
                projectTimeSchedule = new ProjectTimeSchedule(projectTimeSchedule.getProjectName(),
                        projectTimeSchedule.getBeginningDateString(),
                        projectTimeSchedule.getExpectDateString(),
                        projectTimeSchedule.getDeadlineDateString(),
                        projectTimeSchedule.getSumOfStageDate(),
                        stageDateStrings,
                        sumOfTarget,
                        stageTargetStrings);

                projectTimeSchedule.setStageTargetFinish(ifTargetFinish);

                ProjectTimeScheduleFileIO.removeScheduleFile(StageEditActivity.this, projectName);
                ProjectTimeScheduleFileIO.createNewScheduleFile(StageEditActivity.this, projectTimeSchedule);
                Intent intent = new Intent(StageEditActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        /**Plus按钮的功能实现**/
        Button plusButton = findViewById(R.id.addTarget); // 设置为Plus按钮
        plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sumOfEditText >= 9) {
                    Toast.makeText(StageEditActivity.this, "Too much Targets", Toast.LENGTH_SHORT).show();
                    return;
                }
                sumOfEditText++;
                Log.d("Press Add", "success");
                LinearLayout newLinearLayout = newLinearLayoutOfTarget("", false);
                linearLayout.addView(newLinearLayout, linearLayout.getChildCount() - 2);
            }
        });
    }

    public LinearLayout newLinearLayoutOfTarget(String str, boolean isChecked) {
        LinearLayout newLinearLayout = new LinearLayout(StageEditActivity.this);
        newLinearLayout.setLayoutParams(targetLinearLayout.getLayoutParams());
        newLinearLayout.setOrientation(targetLinearLayout.getOrientation());
        for (int i = 0; i < targetLinearLayout.getChildCount(); i++) {
            View child = targetLinearLayout.getChildAt(i);
            if (child instanceof AppCompatEditText) {
                AppCompatEditText originalEditText = (AppCompatEditText) child;

                AppCompatEditText newEditText = new AppCompatEditText(StageEditActivity.this);
                newEditText.setLayoutParams(originalEditText.getLayoutParams());
                newEditText.setBackground(originalEditText.getBackground());
                newEditText.setTextColor(originalEditText.getTextColors());
                newEditText.setTextSize(20);
                newEditText.setText(str);
                newEditText.setMaxLines(1);
                newEditText.setPadding(38,16,8,16);
                newEditText.setEms(10);
                newEditText.setInputType(InputType.TYPE_CLASS_TEXT);
                // 可以根据需要设置其他属性

                newLinearLayout.addView(newEditText); // 将新的 EditText 添加到新的 LinearLayout
            } else if (child instanceof CheckBox && !(child instanceof AppCompatButton)) {
                CheckBox originalCheckBox = (CheckBox) child;
                CheckBox newCheckBox = new CheckBox(StageEditActivity.this);
                newCheckBox.setLayoutParams(originalCheckBox.getLayoutParams());
                newCheckBox.setChecked(isChecked);
                newLinearLayout.addView(newCheckBox);
            } else if (child instanceof AppCompatButton) {
                // 复制 Button
                AppCompatButton originalButton = (AppCompatButton) child;
                AppCompatButton newButton = new AppCompatButton(StageEditActivity.this);
                newButton.setLayoutParams(originalButton.getLayoutParams());
                newButton.setText(originalButton.getText());
                newButton.setBackground(originalButton.getBackground());
                newButton.setTextColor(originalButton.getTextColors());
                newButton.setTextSize(50);
                // 根据需要设置其他属性
                newButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        linearLayout.removeView(newLinearLayout);
                        sumOfEditText--;
                        for(int i = 0; i < linearLayout.getChildCount(); i++) {
                            View targetChild = linearLayout.getChildAt(i);
                            if (targetChild instanceof LinearLayout) {
                                TextView numberTextView = (TextView) ((LinearLayout) targetChild).getChildAt(0);
                                numberTextView.setText("0" + (i - 3));
                            }
                        }
                    }
                });
                newLinearLayout.addView(newButton);
            }
        }
        return newLinearLayout;
    }
}