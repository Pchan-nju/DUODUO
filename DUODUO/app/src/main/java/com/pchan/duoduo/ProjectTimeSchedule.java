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
*   setDeadline();
*   不同阶段的期望时间;
*   是否超过了预定的时间：阶段期望时间或者deadline;
*   到各个期望时间的天数;
*  }
* */
public class ProjectTimeSchedule {
    final private SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd");
    final private Date currentDate = new Date();
    final private String currentDateString = ft.format(currentDate);

    private String deadlineDateString = "";
    private String beginningDateString = "";  // 从本地文件中读取或者读入
    private String[] stageDateStrings = {"", "", "", "", ""}; // 最多设置5个中间阶段时间点
    private int sumOfStageDate = 0;

    public ProjectTimeSchedule(String beginningDateString, String deadlineDateString, String... stageDateStrings) {
        this.beginningDateString = beginningDateString;
        this.deadlineDateString = deadlineDateString;
        sumOfStageDate = stageDateStrings.length;
        for (int i = 0; i < sumOfStageDate; i++) {
            this.stageDateStrings[i] += stageDateStrings[i];
        }
    }

    public ProjectTimeSchedule(String beginningDateString, String deadlineDateString) {
        this.beginningDateString = beginningDateString;
        this.deadlineDateString = deadlineDateString;
    }

    public ProjectTimeSchedule() {
        // TODO();
    }

    protected int getSumOfStageDate() {
        return sumOfStageDate;
    }

    public void setDeadline() {
        // TODO();
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
}
