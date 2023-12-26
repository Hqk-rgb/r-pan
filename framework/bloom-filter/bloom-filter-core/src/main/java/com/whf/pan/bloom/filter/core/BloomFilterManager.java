package com.whf.pan.bloom.filter.core;

import java.util.Collection;

/**
 * @author whf
 * @className BloomFilterManager
 * @description 布隆过滤器管理器的顶级接口
 * @date 2023/12/26 21:06
 */
public interface BloomFilterManager {

    /**
     * 根据名称获取对应的布隆过滤器
     *
     * @param name
     * @return
     */
    BloomFilter getFilter(String name);

    /**
     * 获取目前管理中存在的布隆过滤器名称列表
     *
     * @return
     */
    Collection<String> getFilterNames();
}
