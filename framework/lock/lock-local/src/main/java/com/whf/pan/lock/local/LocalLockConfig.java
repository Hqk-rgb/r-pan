package com.whf.pan.lock.local;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.support.locks.DefaultLockRegistry;
import org.springframework.integration.support.locks.LockRegistry;

/**
 * @author whf
 * @className LocalLockConfig
 * @description 本地锁配置类
 * @date 2023/12/27 08:53
 */

@SpringBootConfiguration
@Slf4j
public class LocalLockConfig {
    /**
     * 配置本地锁注册器
     *
     * @return
     */
    @Bean
    public LockRegistry localLockRegistry() {
        LockRegistry lockRegistry = new DefaultLockRegistry();
        log.info("the local lock is loaded successfully!");
        return lockRegistry;
    }
}
