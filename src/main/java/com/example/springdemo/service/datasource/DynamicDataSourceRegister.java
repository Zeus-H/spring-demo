package com.example.springdemo.service.datasource;

import com.example.springdemo.entity.datasource.DataSourceEnum;
import com.google.common.collect.Maps;
import com.zaxxer.hikari.HikariDataSource;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;

import javax.sql.DataSource;
import java.util.Map;

/**
 *
 * @author Mr.Huang
 * @date 2022/10/28 17:46
 **/
public class DynamicDataSourceRegister implements ImportBeanDefinitionRegistrar, EnvironmentAware {

    private final Map<DataSourceEnum, DataSource> dataSourceMap = Maps.newHashMap();

    @Override
    public void setEnvironment(@NotNull Environment environment) {
        initDataSource(environment);
    }

    /**
     * 初始化数据源
     */
    private void initDataSource(Environment env) {
        dataSourceMap.put(DataSourceEnum.DATASOURCE_01, dataSource01(env));
        dataSourceMap.put(DataSourceEnum.DATASOURCE_02, dataSource02(env));
    }

    @Override
    public void registerBeanDefinitions(@NotNull AnnotationMetadata annotationMetadata, BeanDefinitionRegistry beanDefinitionRegistry) {
        DynamicDataSourceContextHolder.dataSourceIds.addAll(dataSourceMap.keySet());

        //创建DynamicDataSource
        GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
        beanDefinition.setBeanClass(DynamicDataSource.class);
        beanDefinition.setSynthetic(true);
        MutablePropertyValues mpv = beanDefinition.getPropertyValues();
        //AbstractRoutingDataSource  抽象类中参数
        mpv.addPropertyValue("defaultTargetDataSource", dataSourceMap.get(DataSourceEnum.DATASOURCE_01));
        mpv.addPropertyValue("targetDataSources", dataSourceMap);
        //注册 - BeanDefinitionRegistry
        beanDefinitionRegistry.registerBeanDefinition("dataSource", beanDefinition);
    }

    /**
     * demo01
     *
     * @param env 环境
     * @return ds
     */
    private DataSource dataSource01(Environment env) {
        return buildDataSource(
                env.getProperty("hikari-jdbc-driver-class-name"),
                env.getProperty("hikari-jdbc-url-ils_01"),
                env.getProperty("hikari-jdbc-username"),
                env.getProperty("hikari-jdbc-password"),
                env.getProperty("hikari-jdbc-pool-size"));
    }

    /**
     * demo02
     *
     * @param env 环境
     * @return ds
     */
    private DataSource dataSource02(Environment env) {
        return buildDataSource(
                env.getProperty("hikari-jdbc-driver-class-name"),
                env.getProperty("hikari-jdbc-url-ils_02"),
                env.getProperty("hikari-jdbc-username"),
                env.getProperty("hikari-jdbc-password"),
                env.getProperty("hikari-jdbc-pool-size"));
    }

    private DataSource buildDataSource(String driver, String url, String username, String password, String poolSize) {
        HikariDataSource ds = new HikariDataSource();
        ds.setDriverClassName(driver);
        ds.setJdbcUrl(url);
        ds.setUsername(username);
        ds.setPassword(password);
        ds.setMaximumPoolSize(Integer.parseInt(poolSize));
        return ds;
    }
}
