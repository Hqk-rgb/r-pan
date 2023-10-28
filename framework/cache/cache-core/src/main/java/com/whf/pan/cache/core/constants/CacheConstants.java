package com.whf.pan.cache.core.constants;

/**
 * @author whf
 * @className CacheConstants
 * @description 公用的缓存常量类
 * @date 2023/10/28 18:54
 */
public interface CacheConstants {
    /**
     * 公用的缓存name
     * 由于该缓存框架大部分复用Spring的Cache模块，所以使用统一的缓存名称
     */
    String R_PAN_CACHE_NAME = "R_PAN_CACHE";
}
