package com.example.springdemo.service.task.manager;

import com.google.common.collect.Sets;
import org.quartz.JobDataMap;

import java.util.Set;

/**
 * 任务辅助类
 * @author fong on 2020/3/17.
 */
public class TaskUtil {

    /** 当前任务被启用 */
    public static boolean taskIsEnabled(JobDataMap dataMap){
        return getTaskContext(dataMap).getConfig().taskIsEnable();
    }

    /** 当前任务被被禁用 */
    public static boolean taskIsDisabled(JobDataMap dataMap){
        return !taskIsEnabled(dataMap);
    }

    /** 当前 context key */
    private final static String JOB_DATA_MAP_TASK_CONTEXT_KEY = "JOB_DATA_MAP_TASK_CONTEXT_KEY";

    /** 设置当前任务的 context */
    static void setTaskContext(JobDataMap dataMap, TaskContext taskContext) {
        dataMap.put(JOB_DATA_MAP_TASK_CONTEXT_KEY, taskContext);
    }

    /** 获取当前任务的 context */
    static TaskContext getTaskContext(JobDataMap dataMap) {
        return (TaskContext) dataMap.get(JOB_DATA_MAP_TASK_CONTEXT_KEY);
    }

    /** 当前 context key */
    private final static String JOB_DATA_MAP_PROCESSED_KEY = "JOB_DATA_MAP_PROCESSED_KEY";

    /**
     * 当前单据已被处理
     * @param dataMap  任务参数
     * @param key      key
     * @return 如果已处理,返回true, 否则加入到处理列表中,且返回false
     */
    @SuppressWarnings("unchecked")
    public static boolean orderIsProcessed(JobDataMap dataMap, String key){
        synchronized (getTaskContext(dataMap).getCode()){
            Set<String> processeds = (Set<String>) dataMap.get(JOB_DATA_MAP_PROCESSED_KEY);
            if (processeds != null) {
                return !processeds.add(key);
            }else{
                dataMap.put(JOB_DATA_MAP_PROCESSED_KEY, Sets.newHashSet(key));
                return false;
            }
        }
    }

    /**
     * 当前单据已被处理
     * @param dataMap  任务参数
     * @param key      key
     * @return 如果已处理,返回true, 否则加入到处理列表中,且返回false
     */
    @SuppressWarnings("unchecked")
    public static boolean orderIsProcessed(JobDataMap dataMap, Long key){
        synchronized (getTaskContext(dataMap).getCode()){
            Set<Long> processeds = (Set<Long>) dataMap.get(JOB_DATA_MAP_PROCESSED_KEY);
            if (processeds != null) {
                return !processeds.add(key);
            }else{
                dataMap.put(JOB_DATA_MAP_PROCESSED_KEY, Sets.newHashSet(key));
                return false;
            }
        }
    }

    /**
     * 清理已处理列表,在任务完成时
     * @param dataMap 任务参数
     */
    static void clearnProcessed(JobDataMap dataMap){
        if (dataMap.containsKey(JOB_DATA_MAP_PROCESSED_KEY)) {
            dataMap.put(JOB_DATA_MAP_PROCESSED_KEY,null);
        }
    }

    /** 获取任务编码 */
    public static String getTaskCode(JobDataMap dataMap) {
        return getTaskContext(dataMap).getConfig().getTaskCode();
    }
}
