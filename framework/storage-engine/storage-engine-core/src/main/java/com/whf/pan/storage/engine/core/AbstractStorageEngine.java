package com.whf.pan.storage.engine.core;


import com.whf.pan.cache.core.constants.CacheConstants;
import com.whf.pan.core.exception.FrameworkException;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * @author whf
 * @className AbstractStorageEngine
 * @description 顶级文件存储引擎的公用父类
 * 具体的文件存储实现方案的公用逻辑需要抽离到该类中
 * @date 2023/11/5 11:55
 */
public abstract class AbstractStorageEngine implements StorageEngine{
    @Resource
    private CacheManager cacheManager;

    protected Cache getCache() {
        if (Objects.isNull(cacheManager)) {
            throw new FrameworkException("具体的缓存实现需要引用到项目中");
        }
        return cacheManager.getCache(CacheConstants.R_PAN_CACHE_NAME);
    }


}
