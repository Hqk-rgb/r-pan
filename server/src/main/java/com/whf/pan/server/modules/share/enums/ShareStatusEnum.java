package com.whf.pan.server.modules.share.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author whf
 * @className ShareStatusEnum
 * @description 分享状态枚举类
 * @date 2023/12/4 14:15
 */
@AllArgsConstructor
@Getter
public enum ShareStatusEnum {
    NORMAL(0, "正常状态"),
    FILE_DELETED(1, "有文件被删除");

    private Integer code;

    private String desc;
}
