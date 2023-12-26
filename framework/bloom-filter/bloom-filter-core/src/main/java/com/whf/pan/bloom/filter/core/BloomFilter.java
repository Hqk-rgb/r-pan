package com.whf.pan.bloom.filter.core;

/**
 * @author whf
 * @className BloomFilter
 * @description 布隆过滤器的顶级接口
 * @date 2023/12/26 21:05
 */
public interface BloomFilter<T> {

    /**
     * 放入元素
     *
     * @param object
     * @return
     */
    boolean put(T object);

    /**
     * 判断元素是不是可能存在
     *
     * @param object
     * @return
     */
    boolean mightContain(T object);

    /**
     * 清空过滤器
     */
    void clear();
}
