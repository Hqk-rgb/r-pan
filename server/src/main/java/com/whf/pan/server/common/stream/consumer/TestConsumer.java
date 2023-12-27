package com.whf.pan.server.common.stream.consumer;

import com.whf.pan.server.common.event.test.TestEvent;
import com.whf.pan.server.common.stream.channel.PanChannels;
import com.whf.pan.stream.core.AbstractConsumer;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

/**
 * @author whf
 * @className TestConsumer
 * @description 测试消息消费者
 * @date 2023/12/27 14:38
 */
@Component
public class TestConsumer extends AbstractConsumer {
    /**
     * 消费测试消息
     *
     * @param message
     */
    @  StreamListener(PanChannels.TEST_INPUT)
    public void consumeTestMessage(Message<TestEvent> message) {
        printLog(message);
    }
}
