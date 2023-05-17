package com.pchan.duoduo;

import android.icu.text.SimpleDateFormat;
import android.util.Log;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/*
* 记录用户单个project的日期安排信息
* 日期格式均为 "yyyy-MM-dd"
* TODO() {
*   ProjectTimeSchedule();
*   不同阶段的期望时间;
*   是否超过了预定的时间：阶段期望时间或者deadline;
*   到各个期望时间的天数;
*  }
* */
public class ProjectTimeSchedule {
    private String projectName;
    final private SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd"); // 不用存储
    final private Date currentDate = new Date(); // 不用存储
    final private String currentDateString = ft.format(currentDate); // 不用存储
    private String beginningDateString;
    private String expectDateString;
    private String deadlineDateString;
    private int sumOfStageDate = 0;
    private String[] stageDateStrings = {"", "", "", "", ""}; // 最多设置5个中间阶段时间点
    private int[] sumOfStageTarget = new int[5];
    private String[][] stageTarget = new String[5][5]; // stageTarget[i][j] 表示 第 i + 1 个stage第 j + 1 个目标
    private boolean[][] stageTargetFinish = new boolean[5][5];

    public ProjectTimeSchedule(String projectName,String beginningDateString, String deadlineDateString, int sumOfStageDate, String... stageDateStrings) {
        this.projectName = projectName;
        this.beginningDateString = beginningDateString;
        this.deadlineDateString = deadlineDateString;
        this.sumOfStageDate = sumOfStageDate;
        for (int i = 0; i < sumOfStageDate; i++) {
            this.stageDateStrings[i] += stageDateStrings[i];
        }
    }
    public ProjectTimeSchedule() {}

    public ProjectTimeSchedule(String projectName, String beginningDateString, String expectDateString, String deadlineDateString, int sumOfStageDate, String[] stageDateStrings, int[] sumOfStageTarget, String[][] stageTarget) {
        this.projectName = projectName;
        this.beginningDateString = beginningDateString;
        this.expectDateString = expectDateString;
        this.deadlineDateString = deadlineDateString;
        this.sumOfStageDate = sumOfStageDate;
        this.stageDateStrings = stageDateStrings;
        this.sumOfStageTarget = sumOfStageTarget;
        this.stageTarget = stageTarget;
    }

    public ProjectTimeSchedule(String projectName, String beginningDateString, String expectDateString, String deadlineDateString, int sumOfStageDate) {
        this.projectName = projectName;
        this.beginningDateString = beginningDateString;
        this.expectDateString = expectDateString;
        this.deadlineDateString = deadlineDateString;
        this.sumOfStageDate = sumOfStageDate;
    }

    public ProjectTimeSchedule(String beginningDateString, String deadlineDateString) {
        this.beginningDateString = beginningDateString;
        this.deadlineDateString = deadlineDateString;
    }

    public void setProjectName(String name) {
        this.projectName = name;
    }

    public String getProjectName() {
        return this.projectName;
    }

    public String getBeginningDateString() {
        return this.beginningDateString;
    }

    public String getExpectDateString() {
        return this.expectDateString;
    }

    public String getDeadlineDateString() {
        return this.deadlineDateString;
    }

    public String[] getStageDateStrings() {
        return this.stageDateStrings;
    }

    public int[] getSumOfStageTarget() {
        return this.sumOfStageTarget;
    }

    public String[][] getStageTarget() {
        return this.stageTarget;
    }

    protected int getSumOfStageDate() {
        return sumOfStageDate;
    }

    public void setDeadline(String deadlineDateString) {
        this.deadlineDateString = deadlineDateString;
    }

    /*******计算两个日期之间的天数******/
    public int daysBetween(String dateString1, String dateString2) {
        try {
            Date date1 = ft.parse(dateString1);
            Date date2 = ft.parse(dateString2);
            Log.d("date1", date1.toString());
            Log.d("date2", date2.toString());
            long diff = date2.getTime() - date1.getTime();
            long daysBetween = diff / 86400000;
            Log.d(dateString1 + " to " + dateString2, "" + daysBetween);
            return (int)daysBetween;
        } catch (Exception e) {
            Log.d("daysBetween", "ERROR");
            e.printStackTrace();
            return -1;
        }
    }
    /*******还剩下多少天******/
    public int daysLeft() {
        return daysBetween(currentDateString, deadlineDateString);
    }

    /*******已经过去的天数占总计划时长的比例********/
    public float ratioOfPassedDays() {
        return (float) daysBetween(beginningDateString, currentDateString) / daysBetween(beginningDateString, deadlineDateString);
    }

    /**
     * 计算各阶段时间占总时长的比例
     * @return ratios of every StageDays in the whole schedule
     */
    public float[] ratioOfStageDays() {
        float[] ratios = new float[sumOfStageDate];
        Log.d("sum of stage date", "" + sumOfStageDate);
        for (int i = 0; i < sumOfStageDate; i++) {
            float x = (float) daysBetween(beginningDateString, stageDateStrings[i]);
            float y = (float) daysBetween(beginningDateString, deadlineDateString);
            Log.d("ratio of stage[" + i + "]=" + stageDateStrings[i], x + " / " + y + " = " + (x / y));
            ratios[i] = x / y;
//            ratios[i] = (float) daysBetween(beginningDateString, stageDateStrings[i]) / daysBetween(beginningDateString, deadlineDateString);
            Log.d("ratio id " + i, "" + ratios[i]);
        }
        Log.d("ratios", ratios.toString());
        return ratios;
    }

    /***判断是否已经逾期***/
    public boolean ifOverDue() {
        return daysBetween(currentDateString, deadlineDateString) < 0;
    }

    /***判断现在时间超过第几段***/
    public int stagesOver() {
        for (int i = sumOfStageDate - 1; i >= 0; i--) {
            if (daysBetween(currentDateString, stageDateStrings[i]) < 0) {
                return  i + 1;
            }
        }
        return 0;
    }

    public boolean[][] getStageTargetFinish() {
        return stageTargetFinish;
    }

    public void setStageTargetFinish(boolean[][] stageTargetFinish) {
        this.stageTargetFinish = stageTargetFinish;
    }
}
