package com.whf.pan.core.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @className ResponseCode
 * @description 公用返回码
 * @author whf
 * @date 2023/10/17 18:59
 */
@AllArgsConstructor
@Getter
public enum ResponseCode {
    /**
     * 成功
     */
    SUCCESS(0, "SUCCESS"),
    /**
     * 错误
     */
    ERROR(1, "ERROR"),
    /**
     * token过期
     */
    TOKEN_EXPIRE(2, "TOKEN_EXPIRE"),
    /**
     * 参数错误
     */
    ERROR_PARAM(3, "ERROR_PARAM"),
    /**
     * 无权限访问
     */
    ACCESS_DENIED(4, "ACCESS_DENIED"),
    /**
     * 分享的文件丢失
     */
    SHARE_FILE_MISS(5, "分享的文件丢失"),
    /**
     * 分享已经被取消
     */
    SHARE_CANCELLED(6, "分享已经被取消"),
    /**
     * 分享已过期
     */
    SHARE_EXPIRE(7, "分享已过期"),
    /**
     * 需要登录
     */
    NEED_LOGIN(10, "NEED_LOGIN");

    /**
     * 状态码
     */
    private final Integer code;

    /**
     * 描述信息
     */
    private final String desc;
}
