package com.whf.pan.stream.core;


import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
/**
 * @author whf
 * @className DefaultStreamProducer
 * @description 默认的消息发送实体
 * @date 2023/12/27 13:23
 */
@Component(value = "defaultStreamProducer")
public class DefaultStreamProducer extends AbstractStreamProducer{

    /**
     * 发送消息的后置钩子函数
     *
     * @param message
     * @param result
     */
    @Override
    protected void afterSend(Message message, boolean result) {

    }

    /**
     * 发送消息的前置钩子函数
     *
     * @param message
     */
    @Override
    protected void preSend(Message message) {

    }
}
