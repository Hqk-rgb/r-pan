package com.whf.pan.server.common.stream.consumer.log;

import com.whf.pan.core.utils.IdUtil;
import com.whf.pan.server.common.stream.event.log.ErrorLogEvent;
import com.whf.pan.server.common.stream.channel.PanChannels;
import com.whf.pan.server.modules.log.entity.ErrorLog;
import com.whf.pan.server.modules.log.service.IErrorLogService;
import com.whf.pan.stream.core.AbstractConsumer;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.Message;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author whf
 * @className ErrorLogEventListener
 * @description 系统错误日志监听器
 * @date 2023/11/5 17:11
 */
@Component
public class ErrorLogEventConsumer extends AbstractConsumer {

    @Resource
    private IErrorLogService iErrorLogService;

    /**
     * 监听系统错误日志事件，并保存到数据库中
     *
     * @param message
     */
    @StreamListener(PanChannels.ERROR_LOG_INPUT)
    public void saveErrorLog(Message<ErrorLogEvent> message) {
        if (isEmptyMessage(message)) {
            return;
        }
        printLog(message);
        ErrorLogEvent event = message.getPayload();
        ErrorLog record = new ErrorLog();
        record.setId(IdUtil.get());
        record.setLogContent(event.getErrorMsg());
        record.setLogStatus(0);
        record.setCreateUser(event.getUserId());
        record.setCreateTime(new Date());
        record.setUpdateUser(event.getUserId());
        record.setUpdateTime(new Date());
        iErrorLogService.save(record);
    }
}
