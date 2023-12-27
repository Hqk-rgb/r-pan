package com.whf.pan.server.common.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;

/**
 * @author whf
 * @className BloomFilterInterceptor
 * @description 布隆过滤器拦截器顶级接口
 * @date 2023/12/27 08:01
 */
public interface BloomFilterInterceptor extends HandlerInterceptor {
    /**
     * 拦截器的名称
     *
     * @return
     */
    String getName();

    /**
     * 要拦截的URI的集合
     *
     * @return
     */
    String[] getPathPatterns();

    /**
     * 要排除拦截的URI的集合
     *
     * @return
     */
    String[] getExcludePatterns();
}
