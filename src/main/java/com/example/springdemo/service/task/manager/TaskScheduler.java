package com.example.springdemo.service.task.manager;


import com.example.springdemo.service.task.exception.CommonException;
import com.example.springdemo.entity.task.TaskConfig;
import com.example.springdemo.service.task.TaskHandler;
import com.example.springdemo.service.task.impl.BeanLoadHandler;
import com.example.springdemo.service.task.impl.DataLoadHandler;
import com.example.springdemo.common.util.NetUtil;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;

import java.util.*;

@Slf4j
@Component
public class TaskScheduler implements ApplicationRunner, Ordered {

    @Autowired
    private ApplicationContext applicationContext;

    private Scheduler scheduler;
    private Set<TaskContext> tasks;
    private final Set<String> configLoadTaskCodes = new HashSet<>();
    /**
     * 项目中默认启用任务调度
     */
    @Value(value = "${task.is-disable-scheduler:false}")
    private Boolean isDisableScheduler;

    @Value(value = "${server.port:}")
    private String machinePort;

    private static String machine;

    /**
     * 获取本服务的IP地址
     */
    public static String getMachine() {
        if (machine == null) {
            machine = NetUtil.getLocalHostAddress();
        }
        return machine;
    }

    private String getMachinePort() {
        return machinePort;
    }

    @Override
    public void run(ApplicationArguments args) {
        log.info("========= 最先启动 ==========");

        configLoadTaskCodes.add(applicationContext.getBeanNamesForType(DataLoadHandler.class)[0]);
        configLoadTaskCodes.add(applicationContext.getBeanNamesForType(BeanLoadHandler.class)[0]);

        // 初始化调度任务管理器
        try {
            Properties properties = new Properties();
            // 线程数
            int threadCount = Runtime.getRuntime().availableProcessors() * 16;
            properties.setProperty("org.quartz.threadPool.threadCount", String.valueOf(threadCount));
            // misfire超时时间(毫秒)
            properties.setProperty("org.quartz.jobStore.misfireThreshold", "1000");
            scheduler = new StdSchedulerFactory(properties).getScheduler();
            scheduler.start();
        } catch (Exception e) {
            log.error("无法加载Quartz", e);
            throw new CommonException(90900000, "无法加载Quartz.");
        }
        // 初始化可用任务
        this.tasks = Sets.newHashSet();
        for (String taskCode : configLoadTaskCodes) {
            TaskContext configLoadTask = buildDataLoadTask(taskCode);
            appendTask(configLoadTask, "初始化");
            tasks.add(configLoadTask);
        }
    }

    private TaskContext buildDataLoadTask(String configLoadTaskCode) {
        TaskConfig dataLoadTaskConfig = new TaskConfig();
        dataLoadTaskConfig.setTaskCode(configLoadTaskCode);
        dataLoadTaskConfig.setTaskHandlerBean(configLoadTaskCode);
        dataLoadTaskConfig.setRunRule("0/10 * * ? * *");
        dataLoadTaskConfig.setIsLogging(1);
        dataLoadTaskConfig.setIsAsynchronous(0);
        dataLoadTaskConfig.setIsStatistics(0);
        dataLoadTaskConfig.setIsContinuous(0);
        dataLoadTaskConfig.setTaskParams(new ArrayList<>());

        return buildTask(dataLoadTaskConfig, getHandler(dataLoadTaskConfig.getTaskHandlerBean()));
    }

    /**
     * 获取任务处理器
     *
     * @param taskHandlerBeanName 任务处理器class bean name
     */
    private TaskHandler getHandler(String taskHandlerBeanName) {
        taskHandlerBeanName = WordUtils.uncapitalize(StringUtils.trim(taskHandlerBeanName));
        Map<String, TaskHandler> taskServiceMap = applicationContext.getBeansOfType(TaskHandler.class);
        return taskServiceMap.getOrDefault(taskHandlerBeanName, null);
    }

    /**
     * 编译任务,得到任务上下文
     */
    private TaskContext buildTask(TaskConfig config, TaskHandler handler) {
        return new TaskContext(config, handler, this);
    }

    /**
     * 新增任务 (启动任务)
     */
    private void appendTask(TaskContext task, String operator) {
        try {
            if (!scheduler.checkExists(task.getJobDetail().getKey())) {
                log.info("启动任务: {}", task.getCode());
                scheduler.scheduleJob(task.getJobDetail(), task.getTrigger());
//                logToDb(task.getConfig(), LogType.start_task, new Gson().toJson(task.getConfig()), operator);
                task.getConfig().setIsEnabled(1);
            }
        } catch (SchedulerException e) {
            log.error("加入任务调度失败:" + task.getCode(), e);
        }
    }

    /** 任务优先级最高 */
    @Override
    public int getOrder() {
        return Integer.MAX_VALUE;
    }


    /**
     * 更新任务
     *
     * @param updatedTaskMap 需要运行的任务(包含新增) key:任务编码 value:任务
     */
    public void refreshTaskConfig(Map<String, TaskConfig> updatedTaskMap) {

        String operator = "任务更新";
        Iterator<TaskContext> iterator = tasks.iterator();
        while (iterator.hasNext()) {

            TaskContext task = iterator.next();
            if (updatedTaskMap.containsKey(task.getCode())) {
                // update
                if (task.dataIsUpdated(updatedTaskMap.get(task.getCode()))) {
                    task.update(updatedTaskMap.get(task.getCode()));
                    restartTask(task, operator);
                }
            } else {
                // delete
                if (!configLoadTaskCodes.contains(task.getCode())) {
                    removeTask(task, operator);
                    iterator.remove();
                }
            }
        }

        // append
        for (TaskConfig config : updatedTaskMap.values()) {
            if (tasks.stream().noneMatch(task -> task.getCode().equals(config.getTaskCode()))) {
                TaskHandler handler = getHandler(config.getTaskHandlerBean());
                if (handler != null) {
                    TaskContext task = buildTask(config, handler);
                    appendTask(task, operator);
                    tasks.add(task);
                } else {
                    printSpringBeanNotFindLog(config.getTaskCode(), config.getTaskHandlerBean());
                }
            }
        }
    }

    private final Set<String> springNotFindBean = Sets.newHashSet();

    /**
     * 日志输出bean不存在
     * 同一个错误只输出一次
     */
    private void printSpringBeanNotFindLog(String taskCode, String taskHandlerBean) {
        if (!springNotFindBean.contains(taskHandlerBean)) {
            log.warn("跳过加载task[" + taskCode + "],因为未在服务[" + getMachine() + ":" + getMachinePort() + "]的spring中找到name为[" + taskHandlerBean + "]的bean!");
            springNotFindBean.add(taskHandlerBean);
        }
    }

    /**
     * 重启任务
     *
     * @param task     任务
     * @param operator 操作人
     */
    private void restartTask(TaskContext task, String operator) {
        removeTask(task, operator);
        try {
            Thread.sleep(100L);
        } catch (InterruptedException ignore) {
        }
        appendTask(task, operator);
    }

    /**
     * 将任务从当前执行队列中移除
     *
     * @param task     任务
     * @param operator 操作人
     */
    private void removeTask(TaskContext task, String operator) {
        try {
            log.info("停止任务: {}", task.getCode());
            if (scheduler.checkExists(task.getJobDetail().getKey())) {
                scheduler.deleteJob(task.getJobDetail().getKey());
//                logToDb(task.getConfig(), LogType.stop_task, new Gson().toJson(task.getConfig()), operator);
                task.getConfig().setIsEnabled(0);
            }
        } catch (Exception e) {
            log.error("终止任务失败!taskCode:" + task.getCode(), e);
        }
    }

}
