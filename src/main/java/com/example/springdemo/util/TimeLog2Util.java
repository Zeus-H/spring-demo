package com.example.springdemo.util;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Mr.Huang
 * @date 2024/3/19 10:18
 **/
public class TimeLog2Util {
    private final StringBuilder log = new StringBuilder();
    private final long startTime;
    private long lastTime;
    private final Lock lock = new ReentrantLock();

    public interface LogOutput {
        void output(String log);
    }

    // 默认的日志输出接口实现，输出到控制台
    private LogOutput logOutput = System.out::println;

    public TimeLog2Util(String title) {
        startTime = lastTime = System.nanoTime();
        listen(title + " -> start");
    }

    public void setLogOutput(LogOutput logOutput) {
        this.logOutput = logOutput;
    }

    public void listen(String action) {
        lock.lock();
        try {
            long currentTime = System.nanoTime();
            log.append(action).append(": ").append((currentTime - lastTime) / 1_000_000.0).append("ms # ");
            lastTime = currentTime;
        } finally {
            lock.unlock();
        }
    }

    public String end() {
        listen("end");
        long totalDuration = System.nanoTime() - startTime;
        log.append("总耗时: ").append(totalDuration / 1_000_000.0).append("ms");
        String finalLog = log.toString();
        if (logOutput != null) {
            logOutput.output(finalLog);
        }
        return finalLog;
    }

    // 测试主方法
    public static void main(String[] args) throws InterruptedException {
        TimeLog2Util logUtil = new TimeLog2Util("Test");
        Thread.sleep(100); // 模拟延时
        logUtil.listen("Action 1 completed");
        Thread.sleep(200); // 模拟延时
        logUtil.listen("Action 2 completed");
        logUtil.end();
    }
}
