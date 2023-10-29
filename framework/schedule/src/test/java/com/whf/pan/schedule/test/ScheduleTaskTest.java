package com.whf.pan.schedule.test;

import com.whf.pan.schedule.ScheduleManager;
import com.whf.pan.schedule.test.config.ScheduleTestConfig;
import com.whf.pan.schedule.test.task.SimpleScheduleTask;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * @author whf
 * @className ScheduleTaskTest
 * @description TODO
 * @date 2023/10/29 14:51
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ScheduleTestConfig.class)
public class ScheduleTaskTest {
    @Resource
    private ScheduleManager manager;

    @Resource
    private SimpleScheduleTask scheduleTask;

    @Test
    public void testRunScheduleTask() throws Exception {

        String cron = "0/5 * * * * ? ";

        String key = manager.startTask(scheduleTask, cron);

        Thread.sleep(10000);

        cron = "0/1 * * * * ? ";

        key = manager.updateTask(key, cron);

        Thread.sleep(10000);

        manager.stopTask(key);

    }
}
