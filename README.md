# DUODUO

## 项目介绍

### 本项目是基于Android的一款Project进度助手，实现通过图形化界面显示和设定时间提醒，帮助使用者更好地进行项目时间规划。

## 项目功能

### 秉持着提高工作效率、可视化程度高、易于着手使用的原则，本项目目前实现以下功能：

1. 进度显示。在主页面中呈现以实际时间、期望完成时间、deadline时间组成的同心圆。随着时间的流逝，实际时间会以半透明实心圆的形式慢慢扩大，可以清晰地看出时间进度和期望值以及deadline之间的关系。
2. 进度提醒。项目按阶段完成，每个阶段若超时未完成将会发送信息提醒使用者加快完成项目建设。
3. 进度完成度设置。在项目初始化时，会向使用者询问项目有哪些阶段以及阶段预期时间占比，设置每个阶段的划分，每完成一个划分形成对应的百分比形成完成度。当阶段完成度尚未到100％且已经到达期望阶段完成时间时，将会提供提醒帮助。
4. 形象化展示期望完成时间和deadline时间。当超出期望时间时，实际时间的半透明实心圆会转红，提醒使用者加快进度。

## 环境要求
### 最低Android版本： Android 11(API级别30)
### 目标Android版本： Android 12.1(API级别33)

## 下载和安装
### 在我们的GitHub存储库中下载APK文件并在设备上手动安装
下载链接：https://github.com/Pchan-nju/DUODUO.git

## 目录结构

### 详细目录见DUODUO/list.txt

### 主体结构如下
```
main
│  AndroidManifest.xml
│  ic_launcher-playstore.png
│  list.txt
│  
├─java
│  └─com
│      └─pchan
│          └─duoduo
│                  CircleView.java
│                  DeleteProjectActivity.java
│                  FirstEditActivity.java
│                  FirstSetActivity.java
│                  MainActivity.java
│                  NotificationService.java
│                  ProjectTimeSchedule.java
│                  ProjectTimeScheduleFileIO.java
│                  StageCircleView.java
│                  StageEditActivity.java
│                  StageSetActivity.java
│                  StartActivity.java
│                  TimeScheduleCircleView.java
│                  WelcomeActivity.java
│                  
└─res
    ├─anim
    │      slide_in_left.xml
    │      slide_in_right.xml
    │      slide_out_left.xml
    │      slide_out_right.xml
    │      
    ├─drawable
    │      background_add_target.xml
    │      background_edit_target.xml
    │      background_edit_text.xml
    │      background_next_button.xml
    │      background_number.xml
    │      background_quit_button.xml
    │      background_top_login.xml
    │      background_top_text.xml
    │      ic_launcher_background.xml
    │      image1.png
    │      image10.png
    │      image11.png
    │      image12.png
    │      image2.png
    │      image3.png
    │      image4.png
    │      image5.png
    │      image6.png
    │      image7.png
    │      image8.png
    │      image9.png
    │      notification.png
    │      start_background_top.xml
    │      time_schedule_background.xml
    │      time_schedule_bottom_button.xml
    │      time_schedule_bottom_otherproj_button.xml
    │      
    ├─drawable-v24
    │      ic_launcher_foreground.xml
    │      
    ├─layout
    │      activity_delete_project.xml
    │      activity_first_edit.xml
    │      activity_first_set.xml
    │      activity_main.xml
    │      activity_stage_edit.xml
    │      activity_stage_set.xml
    │      activity_start.xml
    │      activity_welcome.xml
    │      
    ├─mipmap-anydpi-v26
    │      ic_launcher.xml
    │      ic_launcher_round.xml
    │      
    ├─mipmap-hdpi
    │      ic_launcher.png
    │      ic_launcher_foreground.png
    │      ic_launcher_round.png
    │      
    ├─mipmap-mdpi
    │      ic_launcher.png
    │      ic_launcher_foreground.png
    │      ic_launcher_round.png
    │      
    ├─mipmap-xhdpi
    │      ic_launcher.png
    │      ic_launcher_foreground.png
    │      ic_launcher_round.png
    │      
    ├─mipmap-xxhdpi
    │      ic_launcher.png
    │      ic_launcher_foreground.png
    │      ic_launcher_round.png
    │      
    ├─mipmap-xxxhdpi
    │      ic_launcher.png
    │      ic_launcher_foreground.png
    │      ic_launcher_round.png
    │      
    ├─values
    │      colors.xml
    │      ic_launcher_background.xml
    │      strings.xml
    │      themes.xml
    │      
    ├─values-night
    │      themes.xml
    │      
    └─xml
            backup_rules.xml
            data_extraction_rules.xml
```

## 版本内容更新
### **v1.0.0**：
**初始版本**  完成上述功能：进度显示、进度提醒、完成度显示、进度完成度设置、形象化展示期望完成时间和deadline时间

## 联系方式 (投喂Duouo)
DuoDuo 2537831239@qq.com  
Pchan  1612351216@qq.com  
夏日  fancy.xfx@qq.com  
chai.avi 2498489904@qq.com
