package com.whf.pan.schedule;

import com.whf.pan.core.exception.FrameworkException;
import com.whf.pan.core.utils.UUIDUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskHolder;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

/**
 * @author whf
 * @className ScheduleManager
 * @description 提供了一套简单但有效的方法来管理定时任务，包括启动、停止和更新任务的执行时间。同时，它还使用了线程安全的缓存来保存任务的信息。
 * @date 2023/10/29 14:23
 */
@Component
@Slf4j
public class ScheduleManager {

    /**
     * 用于管理定时任务的调度器
     */
    @Resource
    private ThreadPoolTaskScheduler taskScheduler;

    /**
     * 它是一个线程安全的Map实例，用于缓存定时任务的信息。
     * 键是唯一标识（UUID），值是一个 ScheduleTaskHolder 对象，用于保存定时任务的信息
     */
    private final Map<String, ScheduleTaskHolder> cache = new ConcurrentHashMap<>();

    /**
     * 启动一个定时任务
     * 参数： ScheduleTask 对象（表示要执行的任务）和一个 cron 表达式（表示任务的执行时间）
     */
    public String startTask(ScheduleTask scheduleTask,String cron){
        // 通过调用 taskScheduler.schedule() 方法来安排任务的执行。
        // 这个方法接受一个 Runnable 对象（在这里是 scheduleTask）和一个 Trigger 对象（在这里是使用 CronTrigger 来指定任务的执行时间）。
        ScheduledFuture<?> scheduledFuture = taskScheduler.schedule(scheduleTask, new CronTrigger(cron));
        // 生成一个唯一的标识符（UUID）作为任务的唯一标识
        String key = UUIDUtil.getUUID();
        ScheduleTaskHolder holder = new ScheduleTaskHolder(scheduleTask,scheduledFuture);
        // 将任务信息保存在缓存中
        cache.put(key,holder);
        log.info("{} 启动成功！ 唯一标识：{}",scheduleTask.getName(),key);
        return key;
    }

    /**
     * 停止一个定时任务
     * 参数： 一个任务的唯一标识符
     */
    public void stopTask(String key){
        // 判断传入的标识符是否为空，如果是则直接返回
        if (StringUtils.isBlank(key)){
            return ;
        }
        // 从缓存中获取与标识符相关联的 ScheduleTaskHolder 对象，如果找不到就返回。
        ScheduleTaskHolder holder = cache.get(key);
        if (Objects.isNull(holder)){
            return;
        }
        // 从 ScheduleTaskHolder 对象中获取与任务关联的 ScheduledFuture 对象，然后调用 cancel() 方法来尝试取消任务的执行。
        ScheduledFuture scheduledFuture = holder.getScheduledFuture();
        boolean cancel = scheduledFuture.cancel(true);
        // 根据 cancel() 方法的返回值，记录相应的日志
        if (cancel){
            log.info("{} 停止成功！ 唯一标识：{}",holder.getScheduleTask().getName(),key);
        }else{
            log.error("{} 停止失败！ 唯一标识：{}",holder.getScheduleTask().getName(),key);
        }
    }

    /**
     * 更新一个定时任务的执行时间
     * 参数：任务的唯一标识符和一个 cron 表达式
     */
    public String updateTask(String key,String cron){
        // 首先检查传入的标识符和 cron 表达式是否为空或空白，如果是则抛出一个自定义异常。
        if(StringUtils.isAnyBlank(key,cron)){
            throw new FrameworkException("定时任务的唯一标识以及新的执行表达式不能为空");
        }
        // 从缓存中获取与标识符相关联的 ScheduleTaskHolder 对象，如果找不到就抛出一个异常
        ScheduleTaskHolder holder = cache.get(key);
        if (Objects.isNull(holder)){
            throw new FrameworkException(key+"唯一标识不存在！");
        }
        // 调用 stopTask() 方法来停止当前的定时任务
        stopTask(key);
        // 调用 startTask() 方法来启动一个新的定时任务，并返回新任务的唯一标识符
        return startTask(holder.getScheduleTask(), cron);
    }
}
