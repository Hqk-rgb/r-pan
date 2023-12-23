package com.whf.pan.server.modules.share.service.cache;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.whf.pan.server.common.cache.AbstractManualCacheService;
import com.whf.pan.server.modules.share.entity.Share;
import com.whf.pan.server.modules.share.mapper.ShareMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author whf
 * @className ShareCacheService
 * @description 手动缓存实现分享业务的查询等操作
 * @date 2023/12/23 19:42
 */
@Component(value = "shareManualCacheService")
public class ShareCacheService extends AbstractManualCacheService<Share> {

    @Autowired
    private ShareMapper mapper;

    @Override
    protected BaseMapper<Share> getBaseMapper() {
        return mapper;
    }

    /**
     * 获取缓存key的模版信息
     *
     * @return
     */
    @Override
    public String getKeyFormat() {
        return "SHARE:ID:%s";
    }
}
