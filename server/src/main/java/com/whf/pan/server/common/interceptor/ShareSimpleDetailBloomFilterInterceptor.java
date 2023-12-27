package com.whf.pan.server.common.interceptor;

import com.whf.pan.bloom.filter.core.BloomFilter;
import com.whf.pan.bloom.filter.core.BloomFilterManager;
import com.whf.pan.core.exception.BusinessException;
import com.whf.pan.core.response.ResponseCode;
import com.whf.pan.core.utils.IdUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

/**
 * @author whf
 * @className ShareSimpleDetailBloomFilterInterceptor
 * @description 查询简单分享详情布隆过滤器拦截器
 * @date 2023/12/27 08:02
 */

@Slf4j
@Component
public class ShareSimpleDetailBloomFilterInterceptor implements BloomFilterInterceptor{

    @Autowired
    private BloomFilterManager manager;

    private static final String BLOOM_FILTER_NAME = "SHARE_SIMPLE_DETAIL";

    /**
     * 拦截器的名称
     *
     * @return
     */
    @Override
    public String getName() {
        return "ShareSimpleDetailBloomFilterInterceptor";
    }

    /**
     * 要拦截的URI的集合
     *
     * @return
     */
    @Override
    public String[] getPathPatterns() {
        return ArrayUtils.toArray("/share/simple");
    }

    /**
     * 要排除拦截的URI的集合
     *
     * @return
     */
    @Override
    public String[] getExcludePatterns() {
        return new String[0];
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String encShareId = request.getParameter("shareId");
        if (StringUtils.isBlank(encShareId)) {
            throw new BusinessException("分享ID不能为空");
        }
        BloomFilter<Long> bloomFilter = manager.getFilter(BLOOM_FILTER_NAME);
        if (Objects.isNull(bloomFilter)) {
            log.info("the bloomFilter named {} is null, give up existence judgment...", BLOOM_FILTER_NAME);
            return true;
        }
        Long shareId = IdUtil.decrypt(encShareId);
        boolean mightContain = bloomFilter.mightContain(shareId);
        if (mightContain) {
            log.info("the bloomFilter named {} judge shareId {} mightContain pass...", BLOOM_FILTER_NAME, shareId);
            return true;
        }
        log.info("the bloomFilter named {} judge shareId {} mightContain fail...", BLOOM_FILTER_NAME, shareId);
        throw new BusinessException(ResponseCode.SHARE_CANCELLED);
    }
}
