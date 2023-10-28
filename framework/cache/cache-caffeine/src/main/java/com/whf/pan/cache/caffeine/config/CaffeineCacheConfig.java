package com.whf.pan.cache.caffeine.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.whf.pan.cache.core.constants.CacheConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;

import javax.annotation.Resource;


/**
 * @author whf
 * @className CaffeineCacheConfig
 * @description TODO
 * @date 2023/10/28 19:21
 */
@SpringBootConfiguration
@EnableCaching
@Slf4j
public class CaffeineCacheConfig {

    @Resource
    private CaffeineCacheProperties properties;

    @Bean
    public CacheManager caffeineCacheManager(){
        // 创建 CaffeineCacheManager 对象并传入一个缓存的名称 R_PAN_CACHE
        CaffeineCacheManager cacheManager = new CaffeineCacheManager(CacheConstants.R_PAN_CACHE_NAME);
        // 设置是否允许缓存中存储 null 值
        cacheManager.setAllowNullValues(properties.getAllowNullValue());
        // 创建了一个 Caffeine 缓存构建器对象，并设置了一些缓存的初始化参数，比如初始容量和最大容量
        Caffeine<Object,Object> caffeineBuilder = Caffeine.newBuilder()
                .initialCapacity(properties.getInitCacheCapacity())
                .maximumSize(properties.getMaxCacheCapacity());
        // 将上面创建的 Caffeine 缓存构建器设置到 CaffeineCacheManager 中，以便在创建缓存时使用。
        cacheManager.setCaffeine(caffeineBuilder);
        return cacheManager;
    }
}
