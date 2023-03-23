package com.example.springdemo.service.datasource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 *
 * @author Mr.Huang
 * @date 2022/10/28 17:46
 **/
public class DynamicDataSource extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
        return DynamicDataSourceContextHolder.getDataSourceType();
    }
}
