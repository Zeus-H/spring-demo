package com.example.springdemo.entity.task;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.example.springdemo.service.task.exception.CommonException;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.task.TaskExecutor;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Slf4j
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_task_config")
public class TaskConfig {
    /** ID */
    private String id;

    /** 任务编码(重启JVM生效) */
    private String taskCode;

    /** 任务名称 */
    private String taskName;

    /** 任务处理器(spring bean name)(异步状态下才允许一个处理器分配给多个任务编码)(重启JVM生效) */
    private String taskHandlerBean;

    /** 任务描述 */
    private String taskDescription;

    /** 执行规则连续执行状态下会跳过当前执行指令 */
    private String runRule;

    /** 是否启用  */
    private Integer isEnabled;

    /**固定使用同步:若使用异步可能会出现失控(无法停止)的现象
     * 是否异步执行,(重启JVM生效) */
    private Integer isAsynchronous = 1;

    /** 是否连续执行 只要有数据就不停止执行 */
    private Integer isContinuous;

    /** misfire策略,固定使用:CronTrigger.MISFIRE_INSTRUCTION_DO_NOTHING
     * 若错过执行时间,则等到下一次执行时间再继续执行*/
    private Integer isMisfireDoNothing = 1;

    /** 是否记录日志 */
    private Integer isLogging;

    /** 是否统计状态  */
    private Integer isStatistics;

    /** 乐观锁 */
    private Integer revision;

    /** 创建人 */
    private String createdBy;

    /** 创建时间 */
    private Date createdTime;

    /** 更新人 */
    private String updatedBy;

    /** 更新时间 */
    private Date updatedTime;

    private List<TaskParam> taskParams;

    private List<TaskExecutor> taskExecutors;

    public Map<String, String> getTaskParamMap() {
        Map<String, String> result = new HashMap<>();
        if (CollectionUtils.isNotEmpty(taskParams)) {
            for (TaskParam taskParam : taskParams) {
                result.put(taskParam.getParamName(), taskParam.getParamValue());
            }
        }
        return result;

    }

    public boolean fieldValid() {
        StringBuilder sb = new StringBuilder();
        if (StringUtils.isEmpty(taskCode)) {
            sb.append(" taskCode 为空!");
        }
        if (StringUtils.isEmpty(taskHandlerBean)) {
            sb.append(" taskHandlerBean 为空!");
        }
        if (StringUtils.isEmpty(runRule)) {
            sb.append(" runRule 为空!");
        }
        if (isStatistics == null) {
            sb.append(" isStatistics 为空!");
        }
        if (isContinuous == null) {
            sb.append(" isContinuous 为空!");
        }
        if (isAsynchronous == null) {
            sb.append(" isAsynchronous 为空!");
        }
        if (isLogging == null) {
            sb.append(" isLogging 为空!");
        }
        if (isEnabled == null) {
            sb.append(" isEnabled 为空!");
        }
        if (StringUtils.isNotEmpty(sb)) {
            throw new CommonException(90900000, "加载任务["+taskCode+"]失败: "+sb);
        }
        return true;
    }

    public enum EnableEnum {
        /** 开启 */
        start(1),
        /** 关闭 */
        stop(0);

        private final Integer isEnable;

        EnableEnum(Integer isEnable) {
            this.isEnable = isEnable;
        }

        public Integer isEnable() {
            return this.isEnable;
        }
    }

    /** 任务是否启用状态 */
    public boolean taskIsEnable(){
        return isEnabled!=null && isEnabled==1;
    }
}
