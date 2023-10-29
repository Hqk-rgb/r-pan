package com.whf.pan.cache.redis.test;

import cn.hutool.core.lang.Assert;
import com.whf.pan.cache.core.constants.CacheConstants;
import com.whf.pan.cache.redis.test.instance.CacheAnnotationTester;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * @author whf
 * @className RedisCacheTest
 * @description TODO
 * @date 2023/10/29 13:05
 */
@SpringBootTest(classes = RedisCacheTest.class)
@SpringBootApplication
@RunWith(SpringJUnit4ClassRunner.class)
public class RedisCacheTest {
    @Resource
    private CacheManager cacheManager;

    @Resource
    private CacheAnnotationTester cacheAnnotationTester;

    @Test
    public void redisCacheManagerTest() {
        Cache cache = cacheManager.getCache(CacheConstants.R_PAN_CACHE_NAME);
        Assert.notNull(cache);
        cache.put("name", "value");
        String value = cache.get("name", String.class);
        Assert.isTrue("value".equals(value));
    }

    @Test
    public void redisCacheAnnotationTest() {
        for (int i = 0; i < 2; i++) {
            cacheAnnotationTester.testCacheable("whf");
        }
    }
}
