package com.whf.pan.server.modules.file.enums;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

/**
 * @author whf
 * @className DelFlagEnum
 * @description 文件删除标识枚举类
 * @date 2023/10/30 16:39
 */
@Getter
@AllArgsConstructor
public enum DelFlagEnum {
    /**
     * 未删除
     */
    NO(0),
    /**
     * 已删除
     */
    YES(1);

    private Integer code;
}
