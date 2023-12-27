//package com.whf.pan.server.common.listener.log;
//
//import javax.annotation.Resource;
//
//import com.whf.pan.core.utils.IdUtil;
//import com.whf.pan.server.common.event.log.ErrorLogEvent;
//import com.whf.pan.server.modules.log.entity.ErrorLog;
//import com.whf.pan.server.modules.log.service.IErrorLogService;
//import org.springframework.context.event.EventListener;
//import org.springframework.scheduling.annotation.Async;
//import org.springframework.stereotype.Component;
//
//import java.util.Date;
//
///**
// * @author whf
// * @className ErrorLogEventListener
// * @description 系统错误日志监听器
// * @date 2023/11/5 17:11
// */
//@Component
//public class ErrorLogEventListener {
//
//    @Resource
//    private IErrorLogService iErrorLogService;
//
//    /**
//     * 监听系统错误日志事件，并保存到数据库中
//     *
//     * @param event
//     */
//    @EventListener(ErrorLogEvent.class)
//    @Async(value = "eventListenerTaskExecutor")
//    public void saveErrorLog(ErrorLogEvent event) {
//        ErrorLog record = new ErrorLog();
//        record.setId(IdUtil.get());
//        record.setLogContent(event.getErrorMsg());
//        record.setLogStatus(0);
//        record.setCreateUser(event.getUserId());
//        record.setCreateTime(new Date());
//        record.setUpdateUser(event.getUserId());
//        record.setUpdateTime(new Date());
//        iErrorLogService.save(record);
//    }
//}
