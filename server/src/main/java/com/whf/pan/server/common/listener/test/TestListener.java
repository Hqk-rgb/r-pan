package com.whf.pan.server.common.listener.test;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import com.whf.pan.server.common.event.test.TestEvent;

/**
 * @author whf
 * @className TestListener
 * @description 测试事件处理器
 * @date 2023/12/22 18:08
 */
@Slf4j
@Component
public class TestListener {
    /**
     * 监听测试事件
     *
     * @param event
     * @throws InterruptedException
     */
//    @TransactionalEventListener()
    @EventListener(TestEvent.class)
    @Async(value = "eventListenerTaskExecutor")
    public void test(TestEvent event) throws InterruptedException {
        Thread.sleep(2000);
        log.info("TestEventListener start process, th thread name is {}", Thread.currentThread().getName());
    }
}
