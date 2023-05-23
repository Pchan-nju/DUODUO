package com.pchan.duoduo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class StageSetActivity extends AppCompatActivity {
    Context context = this;
    private int stageIndex = 1;
    ProjectTimeSchedule projectTimeSchedule;
    String[] stageDateStrings = new String[10];
    int[] sumOfTarget = new int[10];
    String[][] stageTargetStrings = new String[10][10];
    ArrayList<EditText> editTextArrayList = new ArrayList<>();
    int sumOfEditText = 0;
    EditText stageDueEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stage_set);

        LinearLayout linearLayout = findViewById(R.id.StageTargetLinearLayout);// 获取当前LinearLayout页面布局

        /*
         * editText, index, deleteButton 的合体*
         * 将targetLinearLayout加入linearLayout*/
        LinearLayout targetLinearLayout = findViewById(R.id.SingleTargetLayout);
        linearLayout.removeView(targetLinearLayout);

        /**得到FirstSetActivity传递的信息**/
        Intent messageIntent = getIntent();
        String[] firstSetMessage = messageIntent.getStringArrayExtra("FirstSetMessage");
        for (String message : firstSetMessage) {
            Log.d("test intent", message);
        }

        int sumOfStage = Integer.parseInt(firstSetMessage[4]);

        /***Cancel按钮的功能实现***/
        Button cancelButton = findViewById(R.id.cancelButton);
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

        /**呈现当前阶段序号**/
        TextView stageTitle = findViewById(R.id.textView12);
        stageTitle.setText("" + stageIndex);

        // 获取stageDueEditText
        stageDueEditText = findViewById(R.id.stageDueTimeEditTextView);

        /***Next按钮功能实现***/
        Button nextButton = findViewById(R.id.nextButton1); // 设置为NEXT按钮
        if (sumOfStage == stageIndex) {
            nextButton.setText("Finish");
        }
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 记录该阶段结束日期
                if (TextUtils.isEmpty(stageDueEditText.getText().toString()) || !FirstSetActivity.isDateFormatValid(stageDueEditText.getText().toString())) {
                    Toast.makeText(StageSetActivity.this, "Please input date correctly", Toast.LENGTH_SHORT).show();
                    return;
                }
                stageDateStrings[stageIndex - 1] = stageDueEditText.getText().toString();
                stageDueEditText.setText("");
                // 记录当前页面输入的Target内容
                sumOfTarget[stageIndex - 1] = sumOfEditText;
                for (int i = 1 ; i <= sumOfEditText; i++){
                    View child = linearLayout.getChildAt(1 + 3);
                    if (child instanceof LinearLayout) {
                        AppCompatEditText editText = (AppCompatEditText) ((LinearLayout) child).getChildAt(1);
                        if (editText.getText()!= null && !TextUtils.isEmpty(editText.getText().toString())) {
                            stageTargetStrings[stageIndex - 1][i - 1] = editText.getText().toString();
                            Log.d("Stage Target " + (stageIndex - 1) + ", " + (i - 1), stageTargetStrings[stageIndex - 1][i - 1]);
                        }
                    }
                    linearLayout.removeView(child);
                }
                sumOfEditText = 0;

                stageIndex++;
                if (sumOfStage < stageIndex) {
                    /****用户Project数量喜加一****/
                    final SharedPreferences sp = getSharedPreferences("user_project", MODE_PRIVATE);
                    int sumOfUserProjects = sp.getInt("sum",0);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putInt("sum", sumOfUserProjects + 1);
                    editor.putString("Project" + (sumOfUserProjects + 1), firstSetMessage[0]);
                    Log.d("Add Project " + firstSetMessage[0], "Success");
                    editor.apply();
                    /********存储用户Project信息********/
                    projectTimeSchedule = new ProjectTimeSchedule(firstSetMessage[0], firstSetMessage[1], firstSetMessage[2], firstSetMessage[3], Integer.parseInt(firstSetMessage[4]), stageDateStrings, sumOfTarget, stageTargetStrings);

                    ProjectTimeScheduleFileIO.createNewScheduleFile(context, projectTimeSchedule);
                    Intent intent = new Intent(StageSetActivity.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    if (sumOfStage == stageIndex) {
                        nextButton.setText("Finish");
                        // 默认最后截止时间为 expected end time
                        stageDueEditText.setText(firstSetMessage[2]);
                    }
                    stageTitle.setText("" + stageIndex);
                }
            }
        });

        /**Plus按钮的功能实现**/
        Button plusButton = findViewById(R.id.addButton); // 设置为Plus按钮
        plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sumOfEditText >= 9) {
                    Toast.makeText(StageSetActivity.this, "Too much Targets", Toast.LENGTH_SHORT).show();
                    return;
                }
                sumOfEditText++;
                Log.d("Press Add", "success");
                LinearLayout newLinearLayout = new LinearLayout(StageSetActivity.this);
                newLinearLayout.setLayoutParams(targetLinearLayout.getLayoutParams());
                newLinearLayout.setOrientation(targetLinearLayout.getOrientation());
                for (int i = 0; i < targetLinearLayout.getChildCount(); i++) {
                    View child = targetLinearLayout.getChildAt(i);
                    Log.d("ChildType", "Child " + i + ": " + child.getClass().getSimpleName());
                    if (child instanceof AppCompatEditText) {
                        AppCompatEditText originalEditText = (AppCompatEditText) child;

                        AppCompatEditText newEditText = new AppCompatEditText(StageSetActivity.this);
                        newEditText.setLayoutParams(originalEditText.getLayoutParams());
                        newEditText.setBackground(originalEditText.getBackground());
                        newEditText.setTextColor(originalEditText.getTextColors());
                        newEditText.setTextSize(20);
                        newEditText.setText("");
                        newEditText.setMaxLines(1);
                        newEditText.setPadding(originalEditText.getPaddingLeft(),16,8,16);
                        newEditText.setEms(10);
                        newEditText.setInputType(InputType.TYPE_CLASS_TEXT);
                        // 可以根据需要设置其他属性

                        newLinearLayout.addView(newEditText); // 将新的 EditText 添加到新的 LinearLayout
                    } else if (child instanceof TextView && !(child instanceof AppCompatButton)) {
                        TextView originalTextView = (TextView) child;

                        TextView newTextView = new TextView(StageSetActivity.this);
                        newTextView.setLayoutParams(originalTextView.getLayoutParams());
                        newTextView.setText("0" + sumOfEditText);
                        newTextView.setBackground(originalTextView.getBackground());
                        newTextView.setGravity(Gravity.CENTER);
                        newTextView.setTextColor(originalTextView.getTextColors());
                        newTextView.setTextSize(22);
                        newTextView.setTypeface(null, Typeface.BOLD);
                        newLinearLayout.addView(newTextView);
                    } else if (child instanceof AppCompatButton) {
                        // 复制 Button
                        AppCompatButton originalButton = (AppCompatButton) child;
                        AppCompatButton newButton = new AppCompatButton(StageSetActivity.this);
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
                linearLayout.addView(newLinearLayout, linearLayout.getChildCount() - 2);
            }
        });
    }
}