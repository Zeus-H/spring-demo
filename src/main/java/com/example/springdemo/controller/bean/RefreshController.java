package com.example.springdemo.controller.bean;

import com.example.springdemo.entity.bean.ConfigProperty;
import com.example.springdemo.mapper.bean.ConfigPropertyMapper;
import com.example.springdemo.service.bean.TestBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RefreshScope
@RequestMapping(path = "/refresh")
public class RefreshController {
    @Autowired
    private ConfigPropertyMapper configPropertyRepository;
    @Autowired
    private TestBean testBean;

    @PostMapping("/refresh")
    public String refresh() {
        ConfigProperty configProperty = configPropertyRepository.selectById("1");
        return configProperty.getPropertyValue();
    }

    @GetMapping("/getTest")
    public String getTest() {
        return testBean.getValue();
    }
}
           
