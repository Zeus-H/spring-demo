package com.example.springdemo.mapper.task;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.springdemo.entity.task.TaskParam;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Mybatis Generator on 2019/09/14
 */
@Repository
public interface TaskParamMapper extends BaseMapper<TaskParam> {
    int deleteByPrimaryKey(String id);

    int insert(TaskParam record);

    int insertSelective(TaskParam record);

    TaskParam selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(TaskParam record);

    int updateByPrimaryKey(TaskParam record);

    List<TaskParam> selectAll();

    @Select("select * from t_task_param where PARAM_VALUE = 'unconverted'")
    TaskParam selectByUnconverted();

    @Update("update t_task_param set PARAM_VALUE = 'converted' where ID= ${id} ")
    void updateConverted(@Param("id") String id);

    @Select("select * from t_task_param where task_code = #{taskCode}")
    TaskParam findByTaskCode(@Param("taskCode") String taskCode);

    @Select("select * from t_task_param where PARAM_NAME = #{name}")
    TaskParam findByName(@Param("name") String name);
}