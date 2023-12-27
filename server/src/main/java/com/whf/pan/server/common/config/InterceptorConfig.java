package com.whf.pan.server.common.config;

import com.whf.pan.server.common.interceptor.BloomFilterInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author whf
 * @className InterceptorConfig
 * @description 项目拦截器配置类
 * @date 2023/12/27 08:05
 */
@SpringBootConfiguration
@Slf4j
public class InterceptorConfig implements WebMvcConfigurer {

    @Resource
    private List<BloomFilterInterceptor> interceptorList;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        if (CollectionUtils.isNotEmpty(interceptorList)) {
            interceptorList.stream().forEach(bloomFilterInterceptor -> {
                registry.addInterceptor(bloomFilterInterceptor)
                        .addPathPatterns(bloomFilterInterceptor.getPathPatterns())
                        .excludePathPatterns(bloomFilterInterceptor.getExcludePatterns());
                log.info("add bloomFilterInterceptor {} finish.", bloomFilterInterceptor.getName());
            });
        }
    }
}
