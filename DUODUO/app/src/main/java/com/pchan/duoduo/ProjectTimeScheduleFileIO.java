package com.pchan.duoduo;

import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class ProjectTimeScheduleFileIO {

    /*******创建新的文件储存新的项目安排信息*******/
    public static void createNewScheduleFile(ProjectTimeSchedule projectTimeSchedule){
        Context context = MainActivity.getAppContext();
        File file = new File(context.getFilesDir(), projectTimeSchedule.getProjectName());
        try{
            FileWriter writer = new FileWriter(file);
            //获取对象的数据
            String ProjectName = projectTimeSchedule.getProjectName();
            String beginningDateString = projectTimeSchedule.getBeginningDateString();
            String expectDateString = projectTimeSchedule.getExpectDateString();
            String deadlineDateString = projectTimeSchedule.getDeadlineDateString();
            int sumOfStageDate = projectTimeSchedule.getSumOfStageDate();
            String[] stageDateStrings = projectTimeSchedule.getStageDateStrings();
            int[] sumOfStageTarget = projectTimeSchedule.getSumOfStageTarget();
            String[][] stageTarget = projectTimeSchedule.getStageTarget();
            boolean[][] stageTargetFinish = projectTimeSchedule.getStageTargetFinish();
            writer.append(ProjectName).append("\n");             //存ProjectName
            writer.append(beginningDateString).append("\n");     //存beginningDateString
            writer.append(expectDateString).append("\n");        //存expectDateString
            writer.append(deadlineDateString).append("\n");      //存deadlineDateString
            writer.append(String.valueOf(sumOfStageDate));
            writer.append("\n");                                 //存sumOfStageDate
            //存stageDateStrings
            for(String str : stageDateStrings){
                writer.append(str);
                writer.append(",");
            }
            writer.append("\n");
            //存sumOfStageTarget
            StringBuilder sb = new StringBuilder();
            for(int i = 0; i < sumOfStageTarget.length; i++){
                sb.append(sumOfStageTarget[i]);
                if(i != sumOfStageTarget.length - 1){
                    sb.append(",");
                }
            }
            writer.append(sb.toString()).append("\n");
            //存stageTarget
            int s_lines = stageTarget.length;
            int s_cols = stageTarget[0].length;
            writer.append(String.valueOf(s_lines)).append(",").append(String.valueOf(s_cols)).append("\n"); //第一行存储：行数，列数；接下来存lines行
            StringBuilder Sb = new StringBuilder();
            for(int i = 0; i < s_lines; i++){
                for(int j = 0; j < s_cols; j++){
                    Sb.append(stageTarget[i][j]);
                    if(j != s_cols - 1){
                        sb.append(",");
                    }
                }
                if(i != s_lines - 1){
                    Sb.append("\n");
                }
            }
            writer.append(Sb.toString());
            //存stageTargetFinish
            int b_lines = stageTargetFinish.length;
            int b_cols = stageTargetFinish[0].length;
            writer.append(String.valueOf(b_lines)).append(",").append(String.valueOf(b_cols)).append("\n"); ////第一行存储：行数，列数；接下来存lines行
            StringBuilder SB = new StringBuilder();
            for(int i = 0; i < b_lines; i++){
                for(int j = 0; j < b_cols; j++){
                    if(stageTargetFinish[i][j]){
                        SB.append("1");
                    }else {
                        SB.append("0");
                    }
                }
                SB.append("\n");
            }
            writer.append(SB.toString());
            //关闭写入器
            writer.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    /*******从本地文件读取已有项目安排信息********/
    public static ProjectTimeSchedule getScheduleFromFile(String ProjectName) {
        ProjectTimeSchedule projectTimeSchedule = new ProjectTimeSchedule();
        Context context = MainActivity.getAppContext();
        File file = new File(context.getFilesDir(), ProjectName);
        try {
            FileInputStream fis = new FileInputStream(file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
            String line;
            ArrayList<String> lines = new ArrayList<>();
            while ((line = reader.readLine()) != null){
                line = line.trim();
                lines.add(line);
            }
            reader.close();
            fis.close();
            String[] linesArray = lines.toArray(new String[0]); //将ArrayList转换为字符串数组
            int len = linesArray.length;
            projectTimeSchedule.setProjectName(linesArray[0]);
            projectTimeSchedule.setBeginningDateString(linesArray[1]);
            projectTimeSchedule.setExpectDateString(linesArray[2]);
            projectTimeSchedule.setDeadlineDateString(linesArray[3]);
            projectTimeSchedule.setSumOfStageDate(Integer.parseInt(linesArray[4]));
            String[] stageDateStrings = linesArray[5].split(",");
            projectTimeSchedule.setStageDateStrings(stageDateStrings);
            String[] sumOfStageTarget_s = linesArray[6].split(",");
            int[] sumOfStageTarget = new int[sumOfStageTarget_s.length];
            for(int i = 0; i < sumOfStageTarget_s.length; i++){
                sumOfStageTarget[i] = Integer.parseInt(sumOfStageTarget_s[i]);
            }
            projectTimeSchedule.setSumOfStageTarget(sumOfStageTarget);
            String[] lines_and_cols = linesArray[7].split(",");
            int l = Integer.parseInt(lines_and_cols[0]);
            int c = Integer.parseInt(lines_and_cols[1]);
            String[][] stageTarget = new String[l][c];
            for(int i = 8; i < 8 + l; i++){
                stageTarget[i - 8] = linesArray[i].split(",");
            }
            projectTimeSchedule.setStageTarget(stageTarget);
            String[] lines_and_cols_2 = linesArray[8 + l].split(",");
            int l2 = Integer.parseInt(lines_and_cols[0]);
            int c2 = Integer.parseInt(lines_and_cols[1]);
            boolean[][] stageTargetFinish = new boolean[l2][c2];
            for(int i = 9 + l; i < 9 + l + l2; i++){
                for(int j = 0; j < c2; j++){
                    if(linesArray[i].charAt(j) == '0'){
                        stageTargetFinish[i][j] = false;
                    }else {
                        stageTargetFinish[i][j] = true;
                    }
                }
            }
            projectTimeSchedule.setStageTargetFinish(stageTargetFinish);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return projectTimeSchedule;
    }

    /*******删除本地已有项目安排信息文件********/
    public static void removeScheduleFile(String ProjectName) {
        Context context = MainActivity.getAppContext();
        File file = new File(context.getFilesDir(), ProjectName);
        if(file.exists()){
            file.delete();
        }
    }
}
