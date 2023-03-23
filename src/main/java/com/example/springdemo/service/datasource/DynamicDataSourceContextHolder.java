package com.example.springdemo.service.datasource;

import com.example.springdemo.entity.datasource.DataSourceEnum;
import com.google.common.collect.Sets;

import java.util.Set;

/**
 * 动态数据源上下文管理
 * @author Mr.Huang
 * @date 2022/10/28 17:46
 **/
public class DynamicDataSourceContextHolder {

    //存放当前线程使用的数据源类型信息
    private static final ThreadLocal<DataSourceEnum> CONTEXT_HOLDER = new ThreadLocal<>();
    //存放数据源id
    static Set<DataSourceEnum> dataSourceIds = Sets.newHashSet();

    //设置数据源
    public static void setDataSourceType(DataSourceEnum dataSourceType) {
        CONTEXT_HOLDER.set(dataSourceType);
    }

    //获取数据源
    public static DataSourceEnum getDataSourceType() {
        return CONTEXT_HOLDER.get();
    }

    //清除数据源
    public static void clearDataSourceType() {
        CONTEXT_HOLDER.remove();
    }

    //判断当前数据源是否存在
    public static boolean isContainsDataSource(DataSourceEnum dataSourceId) {
        return dataSourceIds.contains(dataSourceId);
    }
}
