package com.whf.pan.server.modules.test.controller;

import com.whf.pan.core.response.R;
import com.whf.pan.server.common.annotation.LoginIgnore;
import com.whf.pan.server.common.event.test.TestEvent;
import com.whf.pan.server.common.stream.channel.PanChannels;
import com.whf.pan.server.common.stream.event.TestEvents;
import com.whf.pan.stream.core.IStreamProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试处理器
 */
@RestController
public class TestController implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Autowired
    @Qualifier(value = "defaultStreamProducer")
    private IStreamProducer producer;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    /**
     * 测试事件发布
     *
     * @return
     */
    @GetMapping("test")
    @LoginIgnore
    public R test() {
        applicationContext.publishEvent(new TestEvent(this, "test"));
        return R.success("DONE");
    }

    /**
     * 测试流事件发布
     *
     * @return
     */
    @GetMapping("stream/test")
    @LoginIgnore
    public R streamTest(String name) {
        TestEvents testEvents = new TestEvents();
        testEvents.setName(name);
        producer.sendMessage(PanChannels.TEST_OUTPUT, testEvents);
        return R.success();
    }

}
