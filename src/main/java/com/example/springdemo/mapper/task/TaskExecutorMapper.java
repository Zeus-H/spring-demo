package com.example.springdemo.mapper.task;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.springdemo.entity.task.TaskExecutor;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface TaskExecutorMapper extends BaseMapper<TaskExecutor> {
    int deleteByPrimaryKey(String id);

    int insert(TaskExecutor record);

    int insertSelective(TaskExecutor record);

    TaskExecutor selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(TaskExecutor record);

    int updateByPrimaryKey(TaskExecutor record);

    List<TaskExecutor> selectAll();
}