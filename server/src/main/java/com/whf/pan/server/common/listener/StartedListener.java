//package com.whf.pan.server.common.listener;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.boot.ansi.AnsiColor;
//import org.springframework.boot.ansi.AnsiOutput;
//import org.springframework.boot.context.event.ApplicationReadyEvent;
//import org.springframework.context.ApplicationListener;
//import org.springframework.context.ConfigurableApplicationContext;
//import org.springframework.stereotype.Component;
//
///**
// * @author whf
// * @className StartedListener
// * @description 监听容器启动事件,启动之后打印启动信息
// * @date 2023/10/18 19:58
// */
//@Component
//@Slf4j
//public class StartedListener implements ApplicationListener<ApplicationReadyEvent> {
//    @Override
//    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
//        String serverPort = applicationReadyEvent.getApplicationContext().getEnvironment().getProperty("server.port", "8080");
//        String serverUrl = String.format("http://%s:%s", "127.0.0.1", serverPort);
//        log.info(AnsiOutput.toString(AnsiColor.BRIGHT_BLUE, "r pan server started at : ", serverUrl));
//        if (checkShowServerDoc(applicationReadyEvent.getApplicationContext())) {
//            log.info(AnsiOutput.toString(AnsiColor.BRIGHT_BLUE, "r pan server's doc started at : ", serverUrl + "/doc.html"));
//        }
//        log.info(AnsiOutput.toString(AnsiColor.BRIGHT_YELLOW, "r pan server has started successfully!"));
//    }
//
//    /**
//     * 校验是否显示接口文档
//     *
//     * @param applicationContext
//     * @return
//     */
//    private boolean checkShowServerDoc(ConfigurableApplicationContext applicationContext) {
//        return applicationContext.getEnvironment().getProperty("swagger2.show", Boolean.class, true) && applicationContext.containsBean("swagger2Config");
//    }
//}
