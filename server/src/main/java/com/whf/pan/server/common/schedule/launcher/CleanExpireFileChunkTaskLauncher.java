package com.whf.pan.server.common.schedule.launcher;

import com.whf.pan.schedule.ScheduleManager;
import com.whf.pan.server.common.schedule.task.CleanExpireChunkFileTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * @author whf
 * @className CleanExpireFileChunkTaskLauncher
 * @description 定时清理过期的文件分片任务触发器
 * @date 2023/11/26 10:49
 */
@Slf4j
@Component
public class CleanExpireFileChunkTaskLauncher implements CommandLineRunner {

    //每天零点第一秒触发
//    private final static String CRON = "1 0 0 * * ? ";
    //每5秒触发
    private final static String CRON = "0/5 * * * * ? ";

    @Autowired
    private CleanExpireChunkFileTask task;

    @Autowired
    private ScheduleManager scheduleManager;

    @Override
    public void run(String... args) throws Exception {
        scheduleManager.startTask(task, CRON);
    }
}
