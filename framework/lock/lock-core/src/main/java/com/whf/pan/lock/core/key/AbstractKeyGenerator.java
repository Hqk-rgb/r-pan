package com.whf.pan.lock.core.key;

import com.google.common.collect.Maps;
import com.whf.pan.core.utils.SpElUtil;
import com.whf.pan.lock.core.LockContext;
import com.whf.pan.lock.core.annotation.Lock;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Arrays;
import java.util.Map;

/**
 * @author whf
 * @className AbstractKeyGenerator
 * @description 锁的key生成器的公用父类
 * @date 2023/12/27 08:35
 */
public abstract class AbstractKeyGenerator implements KeyGenerator{

    /**
     * 生成锁的key
     *
     * @param lockContext
     * @return
     */
    @Override
    public String generateKey(LockContext lockContext) {
        Lock annotation = lockContext.getAnnotation();
        String[] keys = annotation.keys();
        Map<String, String> keyValueMap = Maps.newHashMap();
        if (ArrayUtils.isNotEmpty(keys)) {
            Arrays.stream(keys).forEach(key -> {
                keyValueMap.put(key, SpElUtil.getStringValue(key, lockContext.getClassName(), lockContext.getMethodName(), lockContext.getClassType(),
                        lockContext.getMethod(), lockContext.getArgs(), lockContext.getParameterTypes(), lockContext.getTarget()));
            });
        }
        return doGenerateKey(lockContext, keyValueMap);
    }

    /**
     * 具体逻辑下沉到子类去实现
     */
    protected abstract String doGenerateKey(LockContext lockContext, Map<String, String> keyValueMap);

}
