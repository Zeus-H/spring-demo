package com.example.springdemo.entity.log;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @author Mr.Huang
 * @date 2024/3/19 10:32
 **/
@Data
@TableName("t_logs")
public class Logs {
    @TableId
    private long id;
    private String log;
    private Date createdAt;
}
