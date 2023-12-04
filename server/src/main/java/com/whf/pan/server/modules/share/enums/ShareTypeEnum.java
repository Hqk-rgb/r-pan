package com.whf.pan.server.modules.share.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author whf
 * @className ShareTypeEnum
 * @description 分享类型枚举类
 * @date 2023/12/4 14:15
 */
@AllArgsConstructor
@Getter
public enum ShareTypeEnum {

    NEED_SHARE_CODE(0, "有提取码");

    private Integer code;

    private String desc;
}
