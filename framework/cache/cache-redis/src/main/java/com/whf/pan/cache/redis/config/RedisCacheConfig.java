package com.whf.pan.cache.redis.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @author whf
 * @className RedisCacheConfig
 * @description RedisCache 的配置类
 *  该缓存方案不支持事务，它直接集成 spring-boot-starter-data-redis,所以舍弃自定义配置，直接默认使用 spring 配置
 * @date 2023/10/29 12:06
 */
@SpringBootConfiguration
@EnableCaching
@Slf4j
public class RedisCacheConfig {

    /**
     * 定制链接和操作 Redis 的客户端工具
     * @return
     */
    @Bean
    public RedisTemplate<String,Object> redisTemplate(RedisConnectionFactory redisConnectionFactory){
        // 创建了一个Jackson2JsonRedisSerializer的实例。
        // 这是一个用于序列化和反序列化Java对象为JSON格式的工具。在这里，它被配置为将任意类型的Java对象转换为JSON格式。
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
        // 这里创建了一个StringRedisSerializer的实例，用于将字符串进行序列化和反序列化
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        // 创建了一个RedisTemplate的实例，用于与Redis服务器进行通信。
        // 泛型参数指定了键（Key）的类型为String，值（Value）的类型为Object。
        RedisTemplate<String,Object> redisTemplate = new RedisTemplate<>();

        // 将传入的连接工厂 redisConnectionFactory 与 redisTemplate 关联
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        // 将键的序列化器设置为之前创建的StringRedisSerializer
        redisTemplate.setKeySerializer(stringRedisSerializer);
        // 意味着RedisTemplate将能够将Java对象序列化为JSON格式并存储在Redis中
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        // 设置哈希表的键的序列化器
        redisTemplate.setHashKeySerializer(stringRedisSerializer);
        // 设置哈希表的值的序列化器
        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);

        return redisTemplate;
    }

    @Bean
    public CacheManager redisCacheManager(RedisConnectionFactory redisConnectionFactory){
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration
                .defaultCacheConfig()
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new Jackson2JsonRedisSerializer(Object.class)));

        RedisCacheManager cacheManager = RedisCacheManager
                .builder(RedisCacheWriter.lockingRedisCacheWriter(redisConnectionFactory))
                .cacheDefaults(redisCacheConfiguration)
                .transactionAware()
                .build();

        log.info("the redis cache manager is loaded successfully!");
        return cacheManager;
    }

}
