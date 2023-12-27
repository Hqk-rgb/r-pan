package com.whf.pan.lock.zookeeper;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.support.locks.LockRegistry;
import org.springframework.integration.zookeeper.config.CuratorFrameworkFactoryBean;
import org.springframework.integration.zookeeper.lock.ZookeeperLockRegistry;

import javax.annotation.Resource;

/**
 * @author whf
 * @className ZooKeeperLockConfig
 * @description 基于zk的分布式锁配置类
 * @date 2023/12/27 09:33
 */

@SpringBootConfiguration
@Slf4j
public class ZooKeeperLockConfig {

    @Resource
    private ZooKeeperLockProperties properties;

    /**
     * 配置zk的客户端
     *
     * @return
     */
    @Bean
    public CuratorFrameworkFactoryBean curatorFrameworkFactoryBean() {
        return new CuratorFrameworkFactoryBean(properties.getHost());
    }

    /**
     * 配置zk分布式锁的注册器
     *
     * @param curatorFramework
     * @return
     */
    @Bean
    public LockRegistry zookeeperLockRegistry(CuratorFramework curatorFramework) {
        ZookeeperLockRegistry lockRegistry = new ZookeeperLockRegistry(curatorFramework);
        log.info("the zookeeper lock is loaded successfully!");
        return lockRegistry;
    }
}
