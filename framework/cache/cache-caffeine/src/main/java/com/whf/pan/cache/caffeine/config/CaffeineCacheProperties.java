package com.whf.pan.cache.caffeine.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author whf
 * @className CaffeineCacheProperties
 * @description Caffeine Cache 自定义配置属性类
 * @date 2023/10/28 19:14
 */
@Data
@Component
@ConfigurationProperties(prefix = "com.whf.pan.cache.caffeine")
public class CaffeineCacheProperties {

    /**
     * 缓存初始容量
     * com.whf.pan.cache.caffeine.init-cache-capacity
     */
    private Integer initCacheCapacity = 256;

    /**
     * 缓存最大容量，超过之后会按照 recently or very often (最近最少)策略进行缓存剔除
     * com.whf.pan.cache.caffeine.max-cache-capacity
     */
    private Long maxCacheCapacity = 10000L;

    /**
     * 是否允许空值 null 作为缓存的 value
     * com.whf.pan.cache.caffeine.allow-null-values
     */
    private Boolean allowNullValue = Boolean.TRUE;

}
