package com.whf.pan.server.common.event.log;

import lombok.*;
import org.springframework.context.ApplicationEvent;

/**
 * @author whf
 * @className EventLogEvent
 * @description 错误日志事件
 * @date 2023/11/5 17:05
 */
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class ErrorLogEvent extends ApplicationEvent {
    /**
          * 错误日志的内容
          */
    private String errorMsg;

    /**
     * 当前登录的用户ID
     */
    private Long userId;

    public ErrorLogEvent(Object source, String errorMsg, Long userId) {
        super(source);
        this.errorMsg = errorMsg;
        this.userId = userId;
    }
}
