package com.whf.pan.server.modules.share.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.hash.BloomFilter;
import com.whf.pan.core.constants.Constants;
import com.whf.pan.core.exception.BusinessException;
import com.whf.pan.core.utils.IdUtil;
import com.whf.pan.server.common.config.ServerConfig;
import com.whf.pan.server.modules.share.context.CreateShareUrlContext;
import com.whf.pan.server.modules.share.context.SaveShareFilesContext;
import com.whf.pan.server.modules.share.entity.Share;
import com.whf.pan.server.modules.share.enums.ShareDayTypeEnum;
import com.whf.pan.server.modules.share.enums.ShareStatusEnum;
import com.whf.pan.server.modules.share.service.IShareFileService;
import com.whf.pan.server.modules.share.service.IShareService;
import com.whf.pan.server.modules.share.mapper.ShareMapper;
import com.whf.pan.server.modules.share.vo.ShareUrlVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Objects;

/**
* @author 26570
* @description 针对表【r_pan_share(用户分享表)】的数据库操作Service实现
* @createDate 2023-10-28 15:48:20
*/
@Slf4j
@Service
public class ShareServiceImpl extends ServiceImpl<ShareMapper, Share>
    implements IShareService {

    @Resource
    private ServerConfig config;

    @Resource
    private IShareService shareService;

    @Resource
    private IShareFileService shareFileService;


    // @Resource
    //private BloomFilterManager manager;

    private static final String BLOOM_FILTER_NAME = "SHARE_SIMPLE_DETAIL";
    /**
     * 创建分享链接
     * <p>
     * 1、拼装分享实体，保存到数据库
     * 2、保存分享和对应文件的关联关系
     * 3、拼装返回实体并返回
     *
     * @param context
     * @return
     */
    @Transactional(rollbackFor = BusinessException.class)
    @Override
    public ShareUrlVO create(CreateShareUrlContext context) {
        saveShare(context);
        saveShareFiles(context);
        ShareUrlVO vo = assembleShareVO(context);
        // afterCreate(context, vo);
        return vo;
    }

    /**
     * 拼装分享的实体，并保存到数据库中
     *
     * @param context
     */
    private void saveShare(CreateShareUrlContext context) {
        Share record = new Share();

        record.setShareId(IdUtil.get());
        record.setShareName(context.getShareName());
        record.setShareType(context.getShareType());
        record.setShareDayType(context.getShareDayType());

        Integer shareDay = ShareDayTypeEnum.getShareDayByCode(context.getShareDayType());
        if (Objects.equals(Constants.MINUS_ONE_INT, shareDay)) {
            throw new BusinessException("分享天数非法");
        }

        record.setShareDay(shareDay);
        record.setShareEndTime(DateUtil.offsetDay(new Date(), shareDay));
        record.setShareUrl(createShareUrl(record.getShareId()));
        record.setShareCode(createShareCode());
        record.setShareStatus(ShareStatusEnum.NORMAL.getCode());
        record.setCreateUser(context.getUserId());
        record.setCreateTime(new Date());

        if (!save(record)) {
            throw new BusinessException("保存分享信息失败");
        }

        context.setRecord(record);
    }

    /**
     * 创建分享的URL
     *
     * @param shareId
     * @return
     */
    private String createShareUrl(Long shareId) {
        if (Objects.isNull(shareId)) {
            throw new BusinessException("分享的ID不能为空");
        }
        String sharePrefix = config.getSharePrefix();
        if (!sharePrefix.endsWith(Constants.SLASH_STR)) {
            sharePrefix += Constants.SLASH_STR;
        }
        return sharePrefix + URLEncoder.encode(IdUtil.encrypt(shareId));
    }

    /**
     * 创建分享的分享码
     *
     * @return
     */
    private String createShareCode() {
        return RandomStringUtils.randomAlphabetic(4).toLowerCase();
    }

    /**
     * 保存分享和分享文件的关联关系
     *
     * @param context
     */
    private void saveShareFiles(CreateShareUrlContext context) {
        SaveShareFilesContext saveShareFilesContext = new SaveShareFilesContext();
        saveShareFilesContext.setShareId(context.getRecord().getShareId());
        saveShareFilesContext.setShareFileIdList(context.getShareFileIdList());
        saveShareFilesContext.setUserId(context.getUserId());
        shareFileService.saveShareFiles(saveShareFilesContext);
    }

    /**
     * 拼装对应的返回VO
     *
     * @param context
     * @return
     */
    private ShareUrlVO assembleShareVO(CreateShareUrlContext context) {
        Share record = context.getRecord();
        ShareUrlVO vo = new ShareUrlVO();
        vo.setShareId(record.getShareId());
        vo.setShareName(record.getShareName());
        vo.setShareUrl(record.getShareUrl());
        vo.setShareCode(record.getShareCode());
        vo.setShareStatus(record.getShareStatus());
        return vo;
    }

    /**
     * 创建分享链接后置处理
     *
     * @param context
     * @param vo
     */
    private void afterCreate(CreateShareUrlContext context, ShareUrlVO vo) {
//        BloomFilter<Long> bloomFilter = manager.getFilter(BLOOM_FILTER_NAME);
//        if (Objects.nonNull(bloomFilter)) {
//            bloomFilter.put(context.getRecord().getShareId());
//            log.info("crate share, add share id to bloom filter, share id is {}", context.getRecord().getShareId());
//        }
    }


}




