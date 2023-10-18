package com.whf.pan.core.utils;

import java.util.UUID;

/**
 * @author whf
 * @className UUID 工具类
 * @description TODO
 * @date 2023/10/18 13:27
 */
public class UUIDUtil {
    public static final String EMPTY_STR = "";
    public static final String HYPHEN_STR = "-";

    /**
     * 获取UUID字符串
     *
     * @return
     */
    public static String getUUID() {
        return UUID.randomUUID().toString().replace(HYPHEN_STR, EMPTY_STR).toUpperCase();
    }
}
