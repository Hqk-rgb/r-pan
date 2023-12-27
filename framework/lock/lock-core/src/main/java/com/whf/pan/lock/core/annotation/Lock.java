package com.whf.pan.lock.core.annotation;

import com.whf.pan.lock.core.key.KeyGenerator;
import com.whf.pan.lock.core.key.StandardKeyGenerator;

import java.lang.annotation.*;

/**
 * @author whf
 * @className Lock
 * @description 自定义锁的注解
 * @date 2023/12/27 08:34
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Lock {

    /**
     * 锁的名称
     *
     * @return
     */
    String name() default "";

    /**
     * 锁的过期时长
     *
     * @return
     */
    long expireSecond() default 60L;

    /**
     * 自定义锁的key，支持el表达式
     *
     * @return
     */
    String[] keys() default {};

    /**
     * 指定锁key的生成器
     *
     * @return
     */
    Class<? extends KeyGenerator> keyGenerator() default StandardKeyGenerator.class;
}
