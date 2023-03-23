package com.example.springdemo.service.bean;

import com.example.springdemo.entity.bean.ConfigProperty;
import com.example.springdemo.mapper.bean.ConfigPropertyMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author Mr.Huang
 * @date 2023/2/28 17:46
 **/
@Component
@RefreshScope
public class TestBean {
    @Autowired
    private ConfigPropertyMapper configPropertyMapper;
    private String value;

    @PostConstruct
    private void init() {
        ConfigProperty configProperty = configPropertyMapper.selectById("1");
        if (configProperty != null) {
            value = configProperty.getPropertyValue();
        }
    }

    public String getValue() {
        return value;
    }
}
