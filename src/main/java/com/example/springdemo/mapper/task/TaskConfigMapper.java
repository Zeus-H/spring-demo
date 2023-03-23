package com.example.springdemo.mapper.task;

import com.example.springdemo.entity.task.TaskConfig;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**  Created by Mybatis Generator on 2019/09/14 */
@Repository
public interface TaskConfigMapper {
    int deleteByPrimaryKey(String id);

    int insert(TaskConfig record);

    int insertSelective(TaskConfig record);

    TaskConfig selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(TaskConfig record);

    int updateByPrimaryKey(TaskConfig record);

    List<TaskConfig> selectAll();

    List<TaskConfig> queryByCondition(TaskConfig condition);

    /**开启或关闭定时任务*/
    void startOrStopTask(@Param("id") String id, @Param("isEnabled") Integer isEnabled,@Param("operator") String operator);
}