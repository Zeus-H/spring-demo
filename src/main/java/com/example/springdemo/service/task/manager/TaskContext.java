package com.example.springdemo.service.task.manager;

import com.example.springdemo.entity.task.TaskParam;
import com.example.springdemo.service.task.TaskHandler;
import com.example.springdemo.entity.task.TaskConfig;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Slf4j
public class TaskContext {

    private TaskConfig config;
    private Trigger trigger;
    private JobDetail jobDetail;
    private TaskScheduler manager;
    private TaskHandler handler;
    TaskContext(TaskConfig config, TaskHandler handler, TaskScheduler manager) {
        this.config = config;
        this.handler = handler;
        this.manager = manager;
        this.trigger = initTrigger(config);
        this.jobDetail = initJobDetail(config);
    }

    private CronTrigger initTrigger(TaskConfig config) {
        CronScheduleBuilder schedler;
        if (config.getIsMisfireDoNothing() == 1) {
            // 时间错过则跳过执行,建议循环的任务设置
            schedler = CronScheduleBuilder.cronSchedule(config.getRunRule()).withMisfireHandlingInstructionDoNothing();
        } else {
            // 一直等待任务执行完成后立即执行, 建议每天一次的任务设置
            schedler = CronScheduleBuilder.cronSchedule(config.getRunRule()).withMisfireHandlingInstructionIgnoreMisfires();
        }
        return TriggerBuilder.newTrigger()
                .withIdentity(getCode())
                .withSchedule(schedler)
                .build();
    }

    private JobDetail initJobDetail(TaskConfig config) {
        JobDataMap newJobDataMap = new JobDataMap(config.getTaskParamMap());
        TaskUtil.setTaskContext(newJobDataMap, this);
        if (config.getIsAsynchronous() == 1) {
            return JobBuilder.newJob(AsynchronousJob.class)
                    .withIdentity(getCode())
                    .setJobData(newJobDataMap)
                    .build();
        } else {
            return JobBuilder.newJob(SynchronizationJob.class)
                    .withIdentity(getCode())
                    .setJobData(newJobDataMap)
                    .build();
        }
    }

    TaskConfig getConfig() {
        return this.config;
    }
    String getCode() {
        return this.config.getTaskCode();
    }

    JobDetail getJobDetail() {
        return this.jobDetail;
    }

    Trigger getTrigger() {
        return this.trigger;
    }

    /**
     * config配置是否发生变化
     */
    boolean dataIsUpdated(TaskConfig config) {
        boolean enableStatusIsUpdated = !Objects.equals(this.config.getIsEnabled(), config.getIsEnabled());
        boolean ruleIsUpdated = !Objects.equals(this.config.getRunRule(), config.getRunRule());
        boolean continuousStatusIsUpdated = !Objects.equals(this.config.getIsContinuous(), config.getIsContinuous());
        boolean loggingStatusIsUpdated = !Objects.equals(this.config.getIsLogging(), config.getIsLogging());
        boolean statisticsStatusIsUpdated = !Objects.equals(this.config.getIsStatistics(), config.getIsStatistics());
        boolean paramUpdated = false;
        for (TaskParam newTaskParam : config.getTaskParams()) {
            Object oldParamValue = this.getJobDetail().getJobDataMap().get(newTaskParam.getParamName());
            if (!Objects.equals(oldParamValue, newTaskParam.getParamValue())) {
                paramUpdated = true;
            }
        }
        return enableStatusIsUpdated || ruleIsUpdated || continuousStatusIsUpdated
                || loggingStatusIsUpdated || statisticsStatusIsUpdated || paramUpdated;
    }

    /**
     * 更新任务
     */
    public void update(TaskConfig updatedConfig) {
        this.config = updatedConfig;
        this.trigger = updateTrigger(updatedConfig, this.trigger);
    }

    private CronTrigger updateTrigger(TaskConfig config, Trigger trigger) {
        CronScheduleBuilder schedler;
//        if (config.getIsMisfireDoNothing() == 1) {
//            // 时间错过则跳过执行,建议循环的任务设置
//            schedler = CronScheduleBuilder.cronSchedule(config.getRunRule()).withMisfireHandlingInstructionDoNothing();
//        }else{
//            // 一直等待任务执行完成后立即执行, 建议每天一次的任务设置
//            schedler = CronScheduleBuilder.cronSchedule(config.getRunRule()).withMisfireHandlingInstructionFireAndProceed();
//        }
        // 统一使用跳过策略,避免任务堆积造成隐患
        schedler = CronScheduleBuilder.cronSchedule(config.getRunRule()).withMisfireHandlingInstructionDoNothing();
        return TriggerBuilder.newTrigger()
                .withIdentity(getCode())
                .withSchedule(schedler)
                .usingJobData(trigger.getJobDataMap())
                .build();
    }

    /**
     * 同步任务
     * 请勿调整public为private,否则会导致无法实例化
     */
    @SuppressWarnings("all")
    @DisallowConcurrentExecution
    public static class SynchronizationJob extends BaseJob {
    }

    /**
     * 异步任务
     * 请勿调整public为private,否则会导致无法实例化
     */
    @SuppressWarnings("all")
    public static class AsynchronousJob extends BaseJob {
    }


    public static abstract class BaseJob implements Job {

        /**
         * 单次最大处理数量
         */
        private static final int MAX_PROCESSING_NUMBER = 100000;

        @Override
        public void execute(JobExecutionContext context) {

            long executeSn = System.currentTimeMillis();
            JobKey key = context.getJobDetail().getKey();

            if (log.isDebugEnabled()) {
                if (!"dataLoadHandler".equalsIgnoreCase(key.getName())) {
                    log.debug("task[" + key.getName() + "][" + executeSn + "] start");
                }
            }


            TaskContext taskContext = TaskUtil.getTaskContext(context.getMergedJobDataMap());

            long startTime = System.currentTimeMillis();
            int totalCount = 0;
            try {

                int count;
                do {
                    count = taskContext.handler.execute(context.getMergedJobDataMap());
                    totalCount += count;
                } while (isContinue(taskContext, totalCount, count));

            } catch (NullPointerException e) {
                log.error(e.getMessage());
//                totalCount += e.getHandleCount();
//                taskContext.manager.logToDb(taskContext.config, TaskLog.LogType.executer_fail, e.getErrorMessage(), "任务继续");

            } catch (Exception e) {

                log.error("任务暂停:" + taskContext.getCode(), e);
//                taskContext.manager.logToDb(taskContext.config, TaskLog.LogType.executer_fail, "任务暂停!" + e.getMessage(), "任务暂停");
                try {
                    TimeUnit.SECONDS.sleep(taskContext.handler.timeout().getSeconds());
                } catch (InterruptedException ignored) {
                }
            } finally {
                TaskUtil.clearnProcessed(context.getMergedJobDataMap());
//                taskContext.manager.statisticToDb(taskContext, startTime, totalCount);
            }

            if (log.isDebugEnabled()) {
                if (!"dataLoadHandler".equalsIgnoreCase(key.getName())) {
                    log.debug("task[" + key.getName() + "][" + executeSn + "] end");
                }
            }
        }

        /**
         * <pre>
         *  任务是否可以继续执行.
         *  主要为了解决,单次任务可能执行不完所有业务,
         * </pre>
         *
         * @param taskContext 任务上下文
         * @param totalCount  任务处理业务数(子循环次数)
         * @param count       最后一次执行业务数
         * @return 若可以继续执行则返回true
         */
        private boolean isContinue(TaskContext taskContext, int totalCount, int count) {
            return taskContext.config.taskIsEnable() &&
                    taskContext.config.getIsContinuous() == 1 &&
                    count > 0 &&
                    totalCount < MAX_PROCESSING_NUMBER;
        }
    }
}
