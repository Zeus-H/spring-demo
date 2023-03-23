package com.example.springdemo.entity.task;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@TableName("t_task_param")
public class TaskParam {
    /**
     * ID
     */
    @TableId
    private String id;

    /**
     * 任务编码
     */
    private String taskCode;

    /**
     * 参数名称
     */
    private String paramName;

    /**
     * 参数值
     */
    private String paramValue;

    /**
     * 参数描述
     */
    private String description;

    /**
     * 公共key
     */
    public enum Key {
        /**
         * 单次任务处理数量
         */
        limit,
        /**
         * 延迟确认时间(分钟)
         */
        delayConfirmMinute
    }
}
