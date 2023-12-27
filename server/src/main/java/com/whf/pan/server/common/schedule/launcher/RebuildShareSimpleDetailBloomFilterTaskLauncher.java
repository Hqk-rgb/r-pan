package com.whf.pan.server.common.schedule.launcher;

import com.whf.pan.schedule.ScheduleManager;
import com.whf.pan.server.common.schedule.task.RebuildShareSimpleDetailBloomFilterTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * @author whf
 * @className RebuildShareSimpleDetailBloomFilterTaskLauncher
 * @description 定时重建简单分享详情布隆过滤器任务触发器
 * @date 2023/12/27 08:20
 */

@Slf4j
@Component
public class RebuildShareSimpleDetailBloomFilterTaskLauncher implements CommandLineRunner {
    private final static String CRON = "1 0 0 * * ? ";

    @Autowired
    private RebuildShareSimpleDetailBloomFilterTask task;

    @Autowired
    private ScheduleManager scheduleManager;

    @Override
    public void run(String... args) throws Exception {
        scheduleManager.startTask(task, CRON);
    }
}
