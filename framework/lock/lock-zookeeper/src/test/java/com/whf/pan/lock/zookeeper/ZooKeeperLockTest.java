package com.whf.pan.lock.zookeeper;

import com.whf.pan.core.constants.Constants;
import com.whf.pan.lock.core.LockConstants;
import com.whf.pan.lock.zookeeper.test.instance.LockTester;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.integration.support.locks.LockRegistry;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

/**
 * @author whf
 * @className ZooKeeperLockConfigTest
 * @description TODO
 * @date 2023/12/27 09:48
 */
@SpringBootTest(classes = ZooKeeperLockTest.class)
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootApplication(scanBasePackages = Constants.BASE_COMPONENT_SCAN_PATH + ".lock")
public class ZooKeeperLockTest {
    @Autowired
    private LockRegistry lockRegistry;

    @Autowired
    private LockTester lockTester;

    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    /**
     * 测试手动获取锁
     *
     * @throws InterruptedException
     */
    @Test
    public void lockRegistryTest() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(10);
        for (int i = 0; i < 10; i++) {
            threadPoolTaskExecutor.execute(() -> {
                Lock lock = lockRegistry.obtain(LockConstants.R_PAN_LOCK);
                boolean lockResult = false;
                try {
                    lockResult = lock.tryLock(60L, TimeUnit.SECONDS);
                    if (lockResult) {
                        System.out.println(Thread.currentThread().getName() + " get the lock.");
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } finally {
                    if (lockResult) {
                        System.out.println(Thread.currentThread().getName() + " release the lock.");
                        lock.unlock();
                    }
                }
                countDownLatch.countDown();
            });
        }
        countDownLatch.await();
    }

    /**
     * 测试锁注解
     *
     * @throws InterruptedException
     */
    @Test
    public void lockTesterTest() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(10);
        for (int i = 0; i < 10; i++) {
            threadPoolTaskExecutor.execute(() -> {
                lockTester.testLock("whf");
                countDownLatch.countDown();
            });
        }
        countDownLatch.await();
    }

}