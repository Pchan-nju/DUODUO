package com.pchan.duoduo;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;

import java.util.ArrayList;
import java.util.Calendar;

public class NotificationService extends Service {
    private static final String CHANNEL_ID = "notification_channel_id";
    private static final String CHANNEL_NAME = "my_channel";
    private static final long INTERVAL_MILLS = 24 * 60 * 60 * 1000;
    private static final int EXECUTION_HOUR = 8; //执行任务的小时
    private static final int EXECUTION_MINUTE = 0; //执行任务的分钟

    private Handler mHandler;
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            checkProjectProgress();
            scheduleNextExecution();
        }
    };

    public NotificationService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mHandler = new Handler();
        scheduleNextExecution();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacks(mRunnable);
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void checkProjectProgress(){
        // 在这里读取共享首选项和内部存储中的项目信息，并进行项目进度的检查
        final SharedPreferences sp = getSharedPreferences("user_project", MODE_PRIVATE);
        int sumOfUserProjects = sp.getInt("sum",0);
        String[] projectNameStrings = new String[sumOfUserProjects + 2];

        for (int i = 1; i <= sumOfUserProjects; i++) {
            projectNameStrings[i - 1] = sp.getString("Project" + i, "Error");
        }
        for(int i = 1; i <= sumOfUserProjects; i++){
            ProjectTimeSchedule projectTimeSchedule = ProjectTimeScheduleFileIO.getScheduleFromFile(this, projectNameStrings[i - 1]);
            // 如果检测到项目某阶段超时未完成，构建通知并发送
            if(isProjectOverdue(projectTimeSchedule)){
                buildAndSendNotification(i, projectTimeSchedule);
            }
        }
    }

    /**根据存储的项目信息，判断项目是否存在某阶段超时未完成,返回一个布尔值表示项目是否超时*/
    private boolean isProjectOverdue(ProjectTimeSchedule projectTimeSchedule){
        for(int i = 0; i < projectTimeSchedule.getSumOfStageDate(); i++){
            for(int j = 0; j < projectTimeSchedule.getSumOfStageTarget()[i]; j++){
                if(!projectTimeSchedule.getStageTargetFinish()[i][j]
                        && projectTimeSchedule.daysBetween(projectTimeSchedule.getCurrentDateString(), projectTimeSchedule.getStageDateStrings()[i]) <= 0){
                    return true;
                }
            }
        }
        return false;
    }

    private void buildAndSendNotification(int notification_id, ProjectTimeSchedule projectTimeSchedule){
        String ContentText = "";
        for(int i = 0; i < projectTimeSchedule.getSumOfStageDate(); i++){
            if(projectTimeSchedule.daysBetween(projectTimeSchedule.getCurrentDateString(), projectTimeSchedule.getStageDateStrings()[i]) <= 0){
                ArrayList<Integer> list = new ArrayList<Integer>();
                for(int j = 0; j < projectTimeSchedule.getSumOfStageTarget()[i]; j++){
                    if(!projectTimeSchedule.getStageTargetFinish()[i][j]){
                        list.add(j);
                    }
                }
                if(list.size() != 0){
                    ContentText += ("Stage" + (i + 1) + " : \n");
                    for(int k = 0; k < list.size(); k++){
                        if(k != list.size() - 1){
                            ContentText = ContentText.concat("目标：" + projectTimeSchedule.getStageTarget()[i][list.get(k)] + "\n");
                        }else {
                            ContentText = ContentText.concat("目标：" + projectTimeSchedule.getStageTarget()[i][list.get(k)] + "\n未完成，请抓紧时间尽快完成！\n");
                        }
                    }
                }
            }
        }

        //创建通知渠道
        NotificationManager notificationManager = (NotificationManager) getSystemService(Service.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            channel.canShowBadge();
            notificationManager.createNotificationChannel(channel);
        }

        // 创建一个跳转到 MainActivity 的 Intent
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        // 构建通知对象，设置标题、内容、图标等相关信息
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.notification)
                .setContentTitle(projectTimeSchedule.getProjectName() + " 进度提醒")
                .setStyle(new NotificationCompat.BigTextStyle().bigText(ContentText))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        //发送通知到手机状态栏
        notificationManager.notify(notification_id, builder.build());
    }

    private void scheduleNextExecution(){
        //创建一个‘Calendar’实例,设置通知任务执行的时间
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY , EXECUTION_HOUR);
        calendar.set(Calendar.MINUTE, EXECUTION_MINUTE);
        calendar.set(Calendar.SECOND, 0);

        long currentTimeMillis = System.currentTimeMillis();
        long scheduledTimeMillis = calendar.getTimeInMillis();

        if(scheduledTimeMillis <= currentTimeMillis){
            scheduledTimeMillis += INTERVAL_MILLS;
        }

        long delayMillis = scheduledTimeMillis - currentTimeMillis;
        mHandler.postDelayed(mRunnable, delayMillis);
    }
}