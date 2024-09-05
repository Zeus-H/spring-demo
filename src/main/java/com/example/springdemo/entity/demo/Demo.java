package com.example.springdemo.entity.demo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.example.springdemo.common.annotation.EncryptField;
import lombok.Data;

@Data
@TableName("t_demo")
public class Demo {
    private String id;
    // 注解加密
    @EncryptField
    private String name;
    private String age;
    // 注解加密
    @EncryptField
    private String mobile;
    private String encryptedName;
    private String encryptedMobile;
}
