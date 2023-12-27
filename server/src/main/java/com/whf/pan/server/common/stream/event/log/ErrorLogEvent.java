package com.whf.pan.server.common.stream.event.log;

import lombok.*;
import org.springframework.context.ApplicationEvent;

import java.io.Serializable;

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
@NoArgsConstructor
public class ErrorLogEvent implements Serializable {

    private static final long serialVersionUID = -8641513371928404723L;
    /**
     * 错误日志的内容
     */
    private String errorMsg;

    /**
     * 当前登录的用户ID
     */
    private Long userId;

    public ErrorLogEvent(String errorMsg, Long userId) {
        this.errorMsg = errorMsg;
        this.userId = userId;
    }
}
