package com.example.springdemo.config;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.springdemo.entity.bean.ConfigProperty;
import com.example.springdemo.mapper.bean.ConfigPropertyMapper;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.DefaultSingletonBeanRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.GenericApplicationContext;

import java.util.List;

/**
 * 实例化属性Bean
 *
 * @author Mr.Huang
 * @date 2023/3/1 11:50
 **/
@Configuration
public abstract class TestConfig implements InitializingBean, ApplicationContextAware {
    private GenericApplicationContext applicationContext;
    @Autowired
    private ConfigPropertyMapper configPropertyMapper;

    @Override
    public abstract void afterPropertiesSet();

    public void registerSingleton(String beanName, ConfigProperty bean) {
        DefaultSingletonBeanRegistry registry = (DefaultSingletonBeanRegistry) applicationContext.getBeanFactory();
        registry.destroySingleton(beanName);
        if (!applicationContext.isBeanNameInUse(beanName)) {
            applicationContext.getBeanFactory().registerSingleton(beanName, bean);
        }
    }

    public List<ConfigProperty> getConfigProperties() {
        QueryWrapper<ConfigProperty> wrapper = new QueryWrapper<>();
        wrapper.last("limit 1000");
        return configPropertyMapper.selectList(wrapper);
    }

    @Override
    public void setApplicationContext(@NotNull ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = (GenericApplicationContext) applicationContext;
    }
}
