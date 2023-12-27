package com.whf.pan.lock.zookeeper;

import com.whf.pan.lock.core.LockConstants;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author whf
 * @className ZooKeeperLockProperties
 * @description TODO
 * @date 2023/12/27 09:39
 */
@Data
@Component
@ConfigurationProperties(prefix = "com.whf.pan.lock.zookeeper")
public class ZooKeeperLockProperties {
    /**
     * zk链接地址，多个使用逗号隔开
     */
    private String host = "127.0.0.1:2181";

    /**
     * zk分布式锁的根路径
     */
    private String rootPath = LockConstants.R_PAN_LOCK_PATH;
}
