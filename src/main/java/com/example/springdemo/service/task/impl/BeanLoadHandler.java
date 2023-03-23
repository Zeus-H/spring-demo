package com.example.springdemo.service.task.impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.springdemo.entity.bean.ConfigProperty;
import com.example.springdemo.mapper.bean.ConfigPropertyMapper;
import com.example.springdemo.service.task.TaskHandler;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobDataMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.DefaultSingletonBeanRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 定时匹配Bean差异进行销毁与创建
 *
 * @author Mr.Huang
 * @date 2023/2/21 14:53
 **/
@Slf4j
@Service
public class BeanLoadHandler implements TaskHandler {
    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private ConfigPropertyMapper configPropertyMapper;
    @Autowired
    private DefaultListableBeanFactory beanFactory;
    @Autowired
    private GenericApplicationContext genericApplicationContext;

    @Override
    public int execute(JobDataMap params) {
        QueryWrapper<ConfigProperty> wrapper = new QueryWrapper<>();
        wrapper.last("limit 1000");
        List<ConfigProperty> configPropertyList = configPropertyMapper.selectList(wrapper);
        for (ConfigProperty configProperty : configPropertyList) {
            String refreshedBeanName = configProperty.getPropertyKey() + configProperty.getId();
            if (beanFactory.isBeanNameInUse(refreshedBeanName)) {
                ConfigProperty property = applicationContext.getBean(refreshedBeanName, ConfigProperty.class);
                if (!configProperty.getPropertyValue().equals(property.getPropertyValue())) {
                    refresh(refreshedBeanName, configProperty);
                }
            } else {
                refresh(refreshedBeanName, configProperty);
            }
        }
        return 0;
    }

    /**
     * 刷新Client bean。
     */
    public synchronized void refresh(String name, Object client) {
        if (beanFactory.isBeanNameInUse(name)) {
            beanFactory.destroySingleton(name);
        }
        beanFactory.registerSingleton(name, client);
    }

}
