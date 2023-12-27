package com.whf.pan.lock.local.test.instance;

import org.springframework.stereotype.Component;
import com.whf.pan.lock.core.annotation.Lock;

/**
 * @author whf
 * @className LockTester
 * @description lock实测实例
 * @date 2023/12/27 08:57
 */

@Component
public class LockTester {
    @Lock(name = "test", keys = "#name", expireSecond = 10L)
    public String testLock(String name) {
        System.out.println(Thread.currentThread().getName() + " get the lock.");
        String result = "hello " + name;
        System.out.println(Thread.currentThread().getName() + " release the lock.");
        return result;
    }
}
