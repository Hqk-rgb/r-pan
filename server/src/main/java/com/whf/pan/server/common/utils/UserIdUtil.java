package com.whf.pan.server.common.utils;

import com.whf.pan.core.constants.Constants;

import java.util.Objects;

/**
 * @author whf
 * @className 用户ID存储工具类
 * @description TODO
 * @date 2023/10/31 08:22
 */
public class UserIdUtil {
    private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    /**
     * 设置当前线程的用户ID
     *
     * @param userId
     */
    public static void set(Long userId) {
        threadLocal.set(userId);
    }

    /**
     * 获取当前线程的用户ID
     *
     * @return
     */
    public static Long get() {
        Long userId = threadLocal.get();
        if (Objects.isNull(userId)) {
            return Constants.ZERO_LONG;
        }
        return userId;
    }
}
