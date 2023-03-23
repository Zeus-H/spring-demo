package com.example.springdemo.mapper.bean;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.springdemo.entity.bean.ConfigProperty;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Mr.Huang
 * @date 2023/2/28 17:33
 **/
@Mapper
public interface ConfigPropertyMapper extends BaseMapper<ConfigProperty> {

}
