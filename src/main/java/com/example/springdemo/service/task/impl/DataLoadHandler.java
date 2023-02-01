package com.example.springdemo.service.task.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.example.springdemo.service.task.exception.CommonException;
import com.example.springdemo.service.task.manager.TaskScheduler;
import com.example.springdemo.entity.task.TaskConfig;
import com.example.springdemo.entity.task.TaskExecutor;
import com.example.springdemo.entity.task.TaskParam;
import com.example.springdemo.mapper.task.TaskConfigMapper;
import com.example.springdemo.mapper.task.TaskExecutorMapper;
import com.example.springdemo.mapper.task.TaskParamMapper;
import com.example.springdemo.service.task.TaskHandler;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.quartz.CronExpression;
import org.quartz.JobDataMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Slf4j
@Service
public class DataLoadHandler implements TaskHandler {
    private final AtomicInteger itemSn = new AtomicInteger(1);
    @Autowired
    private TaskConfigMapper taskConfigMapper;
    @Autowired
    private TaskExecutorMapper taskExecutorMapper;
    @Autowired
    private TaskParamMapper taskParamMapper;
    @Autowired
    private TaskScheduler taskScheduler;

    @Override
    public int execute(JobDataMap params) {
        List<TaskExecutor> executors = getValidExecutors();
        log.info("{}:{}", itemSn.getAndIncrement(), JSON.toJSONString(executors));
        if (CollectionUtils.isNotEmpty(executors)) {
            Map<String, TaskConfig> configMap = getValidConfig(taskConfigMapper.selectAll(), executors);
            log.info("config:{}", JSON.toJSONString(configMap));

            if (ObjectUtils.isNotEmpty(configMap)) {
                for (Map.Entry<String, TaskConfig> configEntry : configMap.entrySet()) {
                    configEntry.getValue().setTaskParams(Lists.newArrayList());
                }

                List<TaskParam> paramList = taskParamMapper.selectAll();
                log.info("param:{}", JSON.toJSONString(paramList));
                paramList.forEach(param -> {
                    if (configMap.containsKey(param.getTaskCode())) {
                        configMap.get(param.getTaskCode()).getTaskParams().add(param);
                    }
                });
            }
//            taskScheduler.refreshTaskConfig(configMap);
        } else {
//            taskScheduler.refreshTaskConfig(Maps.newHashMap());
        }

        return 0;
    }

    /**
     * 获取有效的执行者(一个机器可能同时是多个任务的执行者)
     */
    private List<TaskExecutor> getValidExecutors() {
        return taskExecutorMapper.selectAll()
                .stream()
                .filter(taskExecutor -> TaskScheduler.getMachine().equals(taskExecutor.getMachine()))
                .collect(Collectors.toList());
    }

    /**
     * 获取有效的任务
     *
     * @param configs            所有任务
     * @param validTaskExecutors 有效的执行者
     * @return 有效的任务 key:任务编码 value:任务
     */
    private Map<String, TaskConfig> getValidConfig(List<TaskConfig> configs, List<TaskExecutor> validTaskExecutors) {
        return configs.stream()
                .filter(config -> config.getIsEnabled() == 1 && config.fieldValid())
                .filter(config -> validTaskExecutors
                        .stream()
                        .anyMatch(executor -> executor.getTaskCode().equals(config.getTaskCode()))
                )
                .filter(config -> {
                    try {
                        new CronExpression(config.getRunRule());
                        return true;
                    } catch (Exception e) {
                        throw new CommonException(90900000,
                                "加载任务[" + config.getTaskCode() + "]失败:cron表达式不正确!");
                    }
                })
                .filter(config -> {
                    if (config.getIsAsynchronous() == 0) {
                        boolean handlerMultiMatch = configs.stream()
                                .filter(that -> !that.equals(config))
                                .anyMatch(that -> that.getTaskHandlerBean().equals(config.getTaskHandlerBean()));
                        if (handlerMultiMatch) {
                            throw new CommonException(90900000, "加载任务[" + config.getTaskCode()
                                    + "]失败:同步任务与其他任务共用同一个handler,会导致并发修改数据的问题!");
                        }
                    }
                    return true;
                })
                .collect(Collectors.toMap(TaskConfig::getTaskCode, taskConfig -> taskConfig));
    }
}
