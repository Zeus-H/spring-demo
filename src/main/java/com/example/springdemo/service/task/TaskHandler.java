package com.example.springdemo.service.task;

import org.quartz.JobDataMap;

import java.time.Duration;

/**
 * 任务处理器。
 * 先在t_task_config表中添加一个job记录，
 * 再在t_task_executor表中添加一台用于执行job的机器，
 * 任务只会在t_task_executor表中指定的那台机器上执行。
 */
public interface TaskHandler {

    /**
     * 执行任务,根据task_config中的定义执行,
     *
     * @param params 任务参数,在task_params中指定(请考虑并发情况下参数的一致性)
     * @return 任务执行处理业务数量(若不清楚请返回0)
     */
    int execute(JobDataMap params);

    default Duration timeout() {
        return Duration.ofSeconds(120);
    };

}
