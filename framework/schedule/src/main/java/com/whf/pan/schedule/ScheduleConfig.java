package com.whf.pan.schedule;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

/**
 * @author whf
 * @className ScheduleConfig
 * @description 定时模块配置类
 * @date 2023/10/29 14:06
 */
@SpringBootConfiguration
public class ScheduleConfig {

    /**
     * 将下面的方法的返回值注册为一个 Spring Bean
     * 返回一个 ThreadPoolTaskScheduler 对象，用于执行定时任务
     * @return
     */
    @Bean
    public ThreadPoolTaskScheduler taskScheduler(){
        return new ThreadPoolTaskScheduler();
    }

}
