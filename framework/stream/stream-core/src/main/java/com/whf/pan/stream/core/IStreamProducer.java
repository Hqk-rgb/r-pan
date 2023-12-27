package com.whf.pan.stream.core;

import java.util.Map;

/**
 * @author whf
 * @className IStreamProducer
 * @description 消息发送者顶级接口
 * @date 2023/12/27 13:22
 */
public interface IStreamProducer {
    /**
     * 发送消息
     *
     * @param channelName
     * @param deploy
     * @return
     */
    boolean sendMessage(String channelName, Object deploy);

    /**
     * 发送消息
     *
     * @param channelName
     * @param deploy
     * @param headers
     * @return
     */
    boolean sendMessage(String channelName, Object deploy, Map<String, Object> headers);
}
