package com.example.springdemo.service.demo.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.springdemo.common.annotation.EncryptField;
import com.example.springdemo.entity.demo.Demo;
import com.example.springdemo.mapper.demo.DemoMapper;
import com.example.springdemo.service.cache.CacheService;
import com.example.springdemo.service.cache.CacheType;
import com.example.springdemo.service.demo.DemoService;
import com.example.springdemo.common.annotation.Encrypt;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collections;

/**
 * DemoServiceImpl
 *
 * @author generator
 * @date 2022年09月02日
 */
@Slf4j
@Service
public class DemoServiceImpl implements DemoService {

    @Autowired
    private DemoMapper repo;
    @Autowired
    private CacheService cacheService;
    @Autowired
    private ApplicationContext applicationContext;

    /**
     * 保存
     */
    @Override
    @EncryptField
    public void save(Demo entity) {
        this.setDefaultValues(entity);
        this.checkEntity(entity);
        cacheService.cache(entity.getId(), CacheType.delivery_back_channel_order_sn);
        repo.insert(entity);
    }

    /**
     * 修改
     */
    @Encrypt
    public void update(Demo entity) {
        this.setDefaultValues(entity);
        this.checkEntity(entity);
        QueryWrapper<Demo> updateWrapper = new QueryWrapper<>();
        repo.update(entity, updateWrapper.eq("id", entity.getId()));
    }

    @Override
    @Encrypt
    public Demo selectById(String id) {
        boolean a = cacheService.contains(id, CacheType.delivery_back_channel_order_sn);
        log.info("是否存在:" + a);
        try {
            demo();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return repo.selectById(id);
    }

    /**
     * 设置实体默认值
     */
    private void setDefaultValues(Demo entity) {
        entity.setName(entity.getName());
    }

    /**
     * 较验实体
     */
    public void checkEntity(Demo entity) {

    }

    private void demo() throws IOException {
        // 获取 ConfigurableEnvironment 对象
        ConfigurableEnvironment env = (ConfigurableEnvironment) applicationContext.getEnvironment();

        // 获取 PropertySources 对象
        MutablePropertySources propertySources = env.getPropertySources();

        // 创建一个新的 PropertySource 对象，并添加到 PropertySources 中
        PropertySource myPropertySource = new MapPropertySource("myPropertySource", Collections.singletonMap("myProperty", "myValue"));
        propertySources.addFirst(myPropertySource);

        // 保存修改后的配置到 application.yml 文件中
        YamlPropertySourceLoader loader = new YamlPropertySourceLoader();
        Resource resource = new ClassPathResource("application.yml");
        PropertySource<?> yamlPropertySource = loader.load("custom-yaml", resource).get(0);
        propertySources.addLast(yamlPropertySource);
    }

}
