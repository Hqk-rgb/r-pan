package com.whf.pan.schedule.test.task;

import com.whf.pan.schedule.ScheduleTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author whf
 * @className SimpleScheduleTask
 * @description 简单的定时任务执行逻辑
 * @date 2023/10/29 14:48
 */
@Component
@Slf4j
public class SimpleScheduleTask implements ScheduleTask {
    @Override
    public String getName() {
        return "测试定时任务";
    }

    @Override
    public void run() {
        log.info(getName()+"is running...");
    }
}
