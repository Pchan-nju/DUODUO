package com.pchan.duoduo;

import android.icu.text.SimpleDateFormat;
import android.util.Log;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/*
* 记录用户单个project的日期安排信息
* 日期格式均为 "yyyy-MM-dd"
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
    private int[] sumOfStageTarget = new int[10];
    private String[][] stageTarget = new String[10][10]; // stageTarget[i][j] 表示 第 i + 1 个stage第 j + 1 个目标
    private boolean[][] stageTargetFinish = new boolean[10][10];

    public String getCurrentDateString(){
        return currentDateString;
    }

    public ProjectTimeSchedule(String projectName,String beginningDateString, String deadlineDateString, int sumOfStageDate, String... stageDateStrings) {
        this.projectName = projectName;
        this.beginningDateString = beginningDateString;
        this.deadlineDateString = deadlineDateString;
        this.sumOfStageDate = sumOfStageDate;
        for (int i = 0; i < sumOfStageDate; i++) {
            this.stageDateStrings[i] += stageDateStrings[i];
        }
    }

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

    public ProjectTimeSchedule() {
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
    public void setBeginningDateString(String beginningDateString){
        this.beginningDateString = beginningDateString;
    }
    public String getExpectDateString() {
        return this.expectDateString;
    }
    public void setExpectDateString(String expectDateString){
        this.expectDateString = expectDateString;
    }
    public String getDeadlineDateString() {
        return this.deadlineDateString;
    }
    public void setDeadlineDateString(String deadlineDateString){
        this.deadlineDateString = deadlineDateString;
    }
    public String[] getStageDateStrings() {
        return this.stageDateStrings;
    }
    public void setStageDateStrings(String[] stageDateStrings){
        this.stageDateStrings = stageDateStrings;
    }
    public int[] getSumOfStageTarget() {
        return this.sumOfStageTarget;
    }
    public void setSumOfStageTarget(int[] sumOfStageTarget){
        this.sumOfStageTarget = sumOfStageTarget;
    }
    public String[][] getStageTarget() {
        return this.stageTarget;
    }
    public void setStageTarget(String[][] stageTarget){
        this.stageTarget = stageTarget;
    }
    protected int getSumOfStageDate() {
        return sumOfStageDate;
    }
    public void setSumOfStageDate(int sumOfStageDate){
        this.sumOfStageDate = sumOfStageDate;
    }

    /*******计算两个日期之间的天数******/
    public int daysBetween(String dateString1, String dateString2) {
        try {
            Date date1 = ft.parse(dateString1);
            Date date2 = ft.parse(dateString2);
//            Log.d("date1", date1.toString());
//            Log.d("date2", date2.toString());
            long diff = date2.getTime() - date1.getTime();
            long daysBetween = diff / 86400000;
//            Log.d(dateString1 + " to " + dateString2, "" + daysBetween);
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
    public int numberOfPassedDays() {
        return daysBetween(beginningDateString, currentDateString);
    }

    public float ratioOfExpectedDay() {
        return (float) daysBetween(beginningDateString, expectDateString) / daysBetween(beginningDateString, deadlineDateString);
    }

    /**
     * 计算各阶段时间占总时长的比例
     * @return ratios of every StageDays in the whole schedule
     */
    public float[] ratioOfStageDays() {
        float[] ratios = new float[sumOfStageDate];
//        Log.d("sum of stage date", "" + sumOfStageDate);
        for (int i = 0; i < sumOfStageDate; i++) {
            float x = (float) daysBetween(beginningDateString, stageDateStrings[i]);
            float y = (float) daysBetween(beginningDateString, deadlineDateString);
//            Log.d("ratio of stage[" + i + "]=" + stageDateStrings[i], x + " / " + y + " = " + (x / y));
            ratios[i] = x / y;
//            ratios[i] = (float) daysBetween(beginningDateString, stageDateStrings[i]) / daysBetween(beginningDateString, deadlineDateString);
//            Log.d("ratio id " + i, "" + ratios[i]);
        }
//        Log.d("ratios", ratios.toString());
        return ratios;
    }

    public float[] ratioOfTargetStageFromBeginningToExpected() {
        int tot = 0;
        for (int i = 0; i < sumOfStageDate; i++) {
            tot += sumOfStageTarget[i];
        }
        float[] ans = new float[sumOfStageDate];
        for (int i = 0; i < sumOfStageDate; i++) {
            ans[i] = (float) sumOfStageTarget[i] / tot;
        }
        return ans;
    }

    public float[] ratioOfStageFromBeginningToExpected() {
        float tot = daysBetween(beginningDateString, expectDateString);
        float[] ans = new float[sumOfStageDate];
        for (int i = 0; i < sumOfStageDate; i++) {
            ans[i] = daysBetween(beginningDateString, stageDateStrings[i]) / tot;
        }
        return ans;
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

    public boolean ifExpectedDateOverDue() {
        return (daysBetween(currentDateString, expectDateString) < 0);
    }

    // target完成度
    public int ratioOfCompletedTargets() {
        int tot = 0;
        int completedCnt = 0;
        for (int i = 0; i < sumOfStageDate; i++) {
            tot += sumOfStageTarget[i];
            for (int j = 0; j < sumOfStageTarget[i]; j++) {
                if (stageTargetFinish[i][j]) {
                    completedCnt++;
                }
            }
        }
        if (tot != 0) {
            return completedCnt * 100 / tot;
        } else {
            return 0;
        }
    }

    // 每个阶段target的完成度
    public float ratioOfCompletedTargetsOfStage(int stageIndex) {
        int completedCnt = 0;
        for (int i = 0; i < sumOfStageTarget[stageIndex - 1]; i++) {
            if (stageTargetFinish[stageIndex - 1][i]) {
                completedCnt++;
            }
        }
        if (sumOfStageTarget[stageIndex - 1] != 0) {
            return (float) completedCnt / sumOfStageTarget[stageIndex - 1];
        } else {
            return 1.0f;
        }
    }

    public float ratioOfCurrentDateFromBeginning() {
        return (float) daysBetween(beginningDateString, currentDateString) / daysBetween(beginningDateString, expectDateString);
    }
}
