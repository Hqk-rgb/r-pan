package com.whf.pan.schedule;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.concurrent.ScheduledFuture;

/**
 * @author whf
 * @className ScheduleTaskHolder
 * @description 存储一个定时任务及其关联的结果实体。通过注解，它自动生成了常用的方法，使得在使用这个类时更加方便。
 * 同时，实现了 Serializable 接口，使得这个类的对象可以在网络上传输或者进行持久化存储。
 * @date 2023/10/29 14:24
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
// implements Serializable 表示该类的对象可以被序列化和反序列化
public class ScheduleTaskHolder implements Serializable {
    /**
     * 执行任务实体
     */
    private ScheduleTask scheduleTask;
    /**
     * 执行任务的结果实体
     */
    private ScheduledFuture scheduledFuture;
}
