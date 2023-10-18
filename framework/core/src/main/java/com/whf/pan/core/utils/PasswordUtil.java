package com.whf.pan.core.utils;

/**
 * @author whf
 * @className 密码工具类
 * @description TODO
 * @date 2023/10/18 13:27
 */
public class PasswordUtil {

    /**
     * 随机生成盐值
     *
     * @return
     */
    public static String getSalt() {
        return MessageDigestUtil.md5(UUIDUtil.getUUID());
    }

    /**
     * 密码加密
     *
     * @param salt
     * @param inputPassword
     * @return
     */
    public static String encryptPassword(String salt, String inputPassword) {
        return MessageDigestUtil.sha256(MessageDigestUtil.sha1(inputPassword) + salt);
    }
}
