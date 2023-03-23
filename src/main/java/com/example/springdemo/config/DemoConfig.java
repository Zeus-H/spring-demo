package com.example.springdemo.config;

import com.example.springdemo.entity.bean.ConfigProperty;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author Mr.Huang
 * @date 2023/3/7 15:53
 **/
@Configuration
public class DemoConfig extends TestConfig {
    @Override
    public void afterPropertiesSet() {
        synchronized (this) {
            List<ConfigProperty> configPropertyList = getConfigProperties();
            for (ConfigProperty configProperty : configPropertyList) {
                String beanName = configProperty.getPropertyKey() + configProperty.getId();
                registerSingleton(beanName, configProperty);
            }
        }
    }
}
