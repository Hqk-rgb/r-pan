package com.whf.pan.schedule.test.config;

import com.whf.pan.core.constants.Constants;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author whf
 * @className ScheduleConfigTest
 * @description 单元测试配置类
 * @date 2023/10/29 14:47
 */
@SpringBootConfiguration
@ComponentScan(Constants.BASE_COMPONENT_SCAN_PATH+".schedule")
public class ScheduleTestConfig {

}