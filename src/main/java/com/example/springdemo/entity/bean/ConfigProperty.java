package com.example.springdemo.entity.bean;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author Mr.Huang
 * @date 2023/2/28 17:25
 **/
@Data
@TableName("t_config_property")
public class ConfigProperty {
    @TableId
    private String id;
    private String propertyKey;
    private String propertyValue;
}

