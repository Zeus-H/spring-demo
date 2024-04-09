package com.example.springdemo.util;

import java.util.Date;

/** 方法执行耗时跟踪工具 */
public class TimeLogUtil {

    private final StringBuilder log;
    private final Date startTime;
    private Date time;

    public TimeLogUtil(String title){
        startTime = new Date();
        time = new Date();
        log = new StringBuilder();
        listen(title+"->start");
    }

    public void listen(String action){
        Date currentTime = new Date();
        log.append(action).append(":").append(currentTime.getTime() - time.getTime()).append("#");
        time =  currentTime;
    }

    public String end(){
        listen("end");
        log.append("总耗时:").append(System.currentTimeMillis()-startTime.getTime());
        return log.toString();
    }

    public static void main(String[] args) throws InterruptedException {
        TimeLogUtil log = new TimeLogUtil("test");
        log.listen("第一个动作!");
        log.listen("第x个动作!");
        Thread.sleep(1000);
        log.listen("第x个动作!");
        log.listen("第x个动作!");
        log.listen("第x个动作!");
        log.listen("第x个动作!");
        log.listen("第x个动作!");
        log.listen("第x个动作!");
        log.listen("第x个动作!");
        log.listen("第x个动作!");
        System.err.println(log.end());
    }
}