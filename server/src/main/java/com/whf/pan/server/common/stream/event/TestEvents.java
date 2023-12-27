package com.whf.pan.server.common.stream.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author whf
 * @className TestEvent
 * @description 测试事件实体
 * @date 2023/12/27 14:49
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TestEvents implements Serializable {


    private static final long serialVersionUID = 370458322332370916L;
    /**
     * 消息属性-名称
     */
    private String name;
}
