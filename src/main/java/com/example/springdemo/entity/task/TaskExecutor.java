package com.example.springdemo.entity.task;

import lombok.*;

/**
 * t_task_executor
 * Created by Mybatis Generator on 2019/09/14
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TaskExecutor {
    /** ID */
    private String id;

    /** 任务编码 */
    private String taskCode;

    /** 执行设备 */
    private String machine;
}