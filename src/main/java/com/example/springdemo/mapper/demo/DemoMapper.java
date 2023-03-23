package com.example.springdemo.mapper.demo;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.springdemo.entity.demo.Demo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DemoMapper extends BaseMapper<Demo> {

    Demo selectByPrimaryKey(String id);

    List<Demo> selectAll();

    int deleteByPrimaryKey(String id);
}
