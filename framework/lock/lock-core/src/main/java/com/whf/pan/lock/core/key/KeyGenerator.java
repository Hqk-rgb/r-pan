package com.whf.pan.lock.core.key;

import com.whf.pan.lock.core.LockContext;

/**
 * @author whf
 * @className KeyGenerator
 * @description 锁的key的生成器顶级接口
 * @date 2023/12/27 08:36
 */
public interface KeyGenerator {
    /**
     * 生成锁的key
     *
     * @param lockContext
     * @return
     */
    String generateKey(LockContext lockContext);
}
