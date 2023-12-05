package com.whf.pan.server.common.utils;

import com.whf.pan.core.constants.Constants;

import java.util.Objects;

/**
 * @author whf
 * @className ShareIdUtil
 * @description 分享ID存储工具类
 * @date 2023/12/5 15:37
 */
public class ShareIdUtil {
    private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    /**
     * 设置当前线程的分享ID
     *
     * @param shareId
     */
    public static void set(Long shareId) {
        threadLocal.set(shareId);
    }

    /**
     * 获取当前线程的分享ID
     *
     * @return
     */
    public static Long get() {
        Long shareId = threadLocal.get();
        if (Objects.isNull(shareId)) {
            return Constants.ZERO_LONG;
        }
        return shareId;
    }
}
