package com.whf.pan.server.modules.file.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author whf
 * @className FolderFlagEnum
 * @description 文件夹标识枚举类
 * @date 2023/10/30 16:22
 */
@AllArgsConstructor
@Getter
public enum FolderFlagEnum {
    /**
     * 非文件夹
     */
    NO(0),
    /**
     * 是文件夹
     */
    YES(1);

    private Integer code;
}
