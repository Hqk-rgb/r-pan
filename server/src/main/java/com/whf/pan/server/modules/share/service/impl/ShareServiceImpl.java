package com.whf.pan.server.modules.share.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.Lists;
import com.whf.pan.server.modules.file.entity.UserFile;
import com.whf.pan.server.modules.share.vo.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.hash.BloomFilter;
import com.whf.pan.core.constants.Constants;
import com.whf.pan.core.exception.BusinessException;
import com.whf.pan.core.response.ResponseCode;
import com.whf.pan.core.utils.IdUtil;
import com.whf.pan.core.utils.JwtUtil;
import com.whf.pan.core.utils.UUIDUtil;
import com.whf.pan.server.common.config.ServerConfig;
import com.whf.pan.server.modules.file.context.QueryFileListContext;
import com.whf.pan.server.modules.file.enums.DelFlagEnum;
import com.whf.pan.server.modules.file.service.IUserFileService;
import com.whf.pan.server.modules.file.vo.UserFileVO;
import com.whf.pan.server.modules.share.constants.ShareConstants;
import com.whf.pan.server.modules.share.context.*;
import com.whf.pan.server.modules.share.entity.Share;
import com.whf.pan.server.modules.share.entity.ShareFile;
import com.whf.pan.server.modules.share.enums.ShareDayTypeEnum;
import com.whf.pan.server.modules.share.enums.ShareStatusEnum;
import com.whf.pan.server.modules.share.service.IShareFileService;
import com.whf.pan.server.modules.share.service.IShareService;
import com.whf.pan.server.modules.share.mapper.ShareMapper;
import com.whf.pan.server.modules.share.vo.ShareDetailVO;
import com.whf.pan.server.modules.share.vo.ShareUrlListVO;
import com.whf.pan.server.modules.share.vo.ShareUrlVO;
import com.whf.pan.server.modules.user.entity.User;
import com.whf.pan.server.modules.user.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

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

    @Resource
    private IUserService userService;

    @Resource
    private IUserFileService userFileService;


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


    /******************************************************************查询用户的分享列表************************************************************/

    /**
     * 查询用户的分享列表
     *
     * @param context
     * @return
     */
    @Override
    public List<ShareUrlListVO> getShares(QueryShareListContext context) {
        return baseMapper.selectShareVOListByUserId(context.getUserId());
    }

    /******************************************************************取消分享链接************************************************************/


    /**
     * 取消分享链接
     * <p>
     * 1、校验用户操作权限
     * 2、删除对应的分享记录
     * 3、删除对应的分享文件关联关系记录
     *
     * @param context
     */
    @Transactional(rollbackFor = BusinessException.class)
    @Override
    public void cancelShare(CancelShareContext context) {
        checkUserCancelSharePermission(context);
        doCancelShare(context);
        doCancelShareFiles(context);
    }

    /**
     * 检查用户是否拥有取消对应分享链接的权限
     *
     * @param context
     */
    private void checkUserCancelSharePermission(CancelShareContext context) {
        List<Long> shareIdList = context.getShareIdList();
        Long userId = context.getUserId();
        List<Share> records = listByIds(shareIdList);
        if (CollectionUtils.isEmpty(records)) {
            throw new BusinessException("您无权限操作取消分享的动作");
        }
        for (Share record : records) {
            if (!Objects.equals(userId, record.getCreateUser())) {
                throw new BusinessException("您无权限操作取消分享的动作");
            }
        }
    }

    /**
     * 执行取消文件分享的动作
     *
     * @param context
     */
    private void doCancelShare(CancelShareContext context) {
        List<Long> shareIdList = context.getShareIdList();
        if (!removeByIds(shareIdList)) {
            throw new BusinessException("取消分享失败");
        }
    }

    /**
     * 取消文件和分享的关联关系数据
     *
     * @param context
     */
    private void doCancelShareFiles(CancelShareContext context) {
        LambdaQueryWrapper<ShareFile> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(ShareFile::getShareId, context.getShareIdList())
                .eq(ShareFile::getCreateUser, context.getUserId());
//        QueryWrapper queryWrapper = Wrappers.query();
//        queryWrapper.in("share_id", context.getShareIdList());
//        queryWrapper.eq("create_user", context.getUserId());
        if (!shareFileService.remove(wrapper)) {
            throw new BusinessException("取消分享失败");
        }
    }

    /******************************************************************校验分享码************************************************************/


    /**
     * 校验分享码
     * <p>
     * 1、检查分享的状态是不是正常
     * 2、校验分享的分享码是不是正确
     * 3、生成一个短时间的分享token 返回给上游
     *
     * @param context
     * @return
     */
    @Override
    public String checkShareCode(CheckShareCodeContext context) {
        Share record = checkShareStatus(context.getShareId());
        context.setRecord(record);
        doCheckShareCode(context);
        return generateShareToken(context);
    }

    /**
     * 检查分享的状态是不是正常
     *
     * @param shareId
     * @return
     */
    private Share checkShareStatus(Long shareId) {
        Share record = getById(shareId);

        if (Objects.isNull(record)) {
            throw new BusinessException(ResponseCode.SHARE_CANCELLED);
        }

        if (Objects.equals(ShareStatusEnum.FILE_DELETED.getCode(), record.getShareStatus())) {
            throw new BusinessException(ResponseCode.SHARE_FILE_MISS);
        }

        if (Objects.equals(ShareDayTypeEnum.PERMANENT_VALIDITY.getCode(), record.getShareDayType())) {
            return record;
        }

        if (record.getShareEndTime().before(new Date())) {
            throw new BusinessException(ResponseCode.SHARE_EXPIRE);
        }

        return record;
    }

    /**
     * 校验分享码是不是正确
     *
     * @param context
     */
    private void doCheckShareCode(CheckShareCodeContext context) {
        Share record = context.getRecord();
        if (!Objects.equals(context.getShareCode(), record.getShareCode())) {
            throw new BusinessException("分享码错误");
        }
    }

    /**
     * 生成一个短期的分享token
     *
     * @param context
     * @return
     */
    private String generateShareToken(CheckShareCodeContext context) {
        Share record = context.getRecord();
        String token = JwtUtil.generateToken(UUIDUtil.getUUID(), ShareConstants.SHARE_ID, record.getShareId(), ShareConstants.ONE_HOUR_LONG);
        return token;
    }

    /******************************************************************查看分享详情************************************************************/


    /**
     * 查询分享的详情
     * <p>
     * 1、校验分享的状态
     * 2、初始化分享实体
     * 3、查询分享的主体信息
     * 4、查询分享的文件列表
     * 5、查询分享者的信息
     *
     * @param context
     * @return
     */
    @Override
    public ShareDetailVO detail(QueryShareDetailContext context) {
        Share record = checkShareStatus(context.getShareId());
        context.setRecord(record);
        initShareVO(context);
        assembleMainShareInfo(context);
        assembleShareFilesInfo(context);
        assembleShareUserInfo(context);
        return context.getVo();
    }

    /**
     * 初始化文件详情的VO实体
     *
     * @param context
     */
    private void initShareVO(QueryShareDetailContext context) {
        ShareDetailVO vo = new ShareDetailVO();
        context.setVo(vo);
    }

    /**
     * 查询分享的主体信息
     *
     * @param context
     */
    private void assembleMainShareInfo(QueryShareDetailContext context) {
        Share record = context.getRecord();
        ShareDetailVO vo = context.getVo();
        vo.setShareId(record.getShareId());
        vo.setShareName(record.getShareName());
        vo.setCreateTime(record.getCreateTime());
        vo.setShareDay(record.getShareDay());
        vo.setShareEndTime(record.getShareEndTime());
    }

    /**
     * 查询分享对应的文件列表
     * <p>
     * 1、查询分享对应的文件ID集合
     * 2、根据文件ID来查询文件列表信息
     *
     * @param context
     */
    private void assembleShareFilesInfo(QueryShareDetailContext context) {
        List<Long> fileIdList = getShareFileIdList(context.getShareId());

        QueryFileListContext queryFileListContext = new QueryFileListContext();
        queryFileListContext.setUserId(context.getRecord().getCreateUser());
        queryFileListContext.setDelFlag(DelFlagEnum.NO.getCode());
        queryFileListContext.setFileIdList(fileIdList);

        List<UserFileVO> rPanUserFileVOList = userFileService.getFileList(queryFileListContext);
        context.getVo().setRPanUserFileVOList(rPanUserFileVOList);
    }

    /**
     * 查询分享对应的文件ID集合
     *
     * @param shareId
     * @return
     */
    private List<Long> getShareFileIdList(Long shareId) {
        if (Objects.isNull(shareId)) {
            return Lists.newArrayList();
        }
        LambdaQueryWrapper<ShareFile> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(ShareFile::getFileId)
                .eq(ShareFile::getShareId, shareId);
//        QueryWrapper queryWrapper = Wrappers.query();
//        queryWrapper.select("file_id");
//        queryWrapper.eq("share_id", shareId);
        List<Long> fileIdList = shareFileService.listObjs(wrapper, value -> (Long) value);
        return fileIdList;
    }

    /**
     * 查询分享者的信息
     *
     * @param context
     */
    private void assembleShareUserInfo(QueryShareDetailContext context) {
        User record = userService.getById(context.getRecord().getCreateUser());
        if (Objects.isNull(record)) {
            throw new BusinessException("用户信息查询失败");
        }
        ShareUserInfoVO shareUserInfoVO = new ShareUserInfoVO();

        shareUserInfoVO.setUserId(record.getUserId());
        shareUserInfoVO.setUsername(encryptUsername(record.getUsername()));

        context.getVo().setShareUserInfoVO(shareUserInfoVO);
    }

    /**
     * 加密用户名称
     *
     * @param username
     * @return
     */
    private String encryptUsername(String username) {
        StringBuffer stringBuffer = new StringBuffer(username);
        stringBuffer.replace(Constants.TWO_INT, username.length() - Constants.TWO_INT, Constants.COMMON_ENCRYPT_STR);
        return stringBuffer.toString();
    }

    /******************************************************************查看分享简单详情************************************************************/

    /**
     * 查询分享的简单详情
     * <p>
     * 1、校验分享的状态
     * 2、初始化分享实体
     * 3、查询分享的主体信息
     * 4、查询分享者的信息
     *
     * @param context
     * @return
     */
    @Override
    public ShareSimpleDetailVO simpleDetail(QueryShareSimpleDetailContext context) {
        Share record = checkShareStatus(context.getShareId());
        context.setRecord(record);
        initShareSimpleVO(context);
        assembleMainShareSimpleInfo(context);
        assembleShareSimpleUserInfo(context);
        return context.getVo();
    }

    /**
     * 初始化简单分享详情的VO对象
     *
     * @param context
     */
    private void initShareSimpleVO(QueryShareSimpleDetailContext context) {
        ShareSimpleDetailVO vo = new ShareSimpleDetailVO();
        context.setVo(vo);
    }

    /**
     * 填充简单分享详情实体信息
     *
     * @param context
     */
    private void assembleMainShareSimpleInfo(QueryShareSimpleDetailContext context) {
        Share record = context.getRecord();
        ShareSimpleDetailVO vo = context.getVo();
        vo.setShareId(record.getShareId());
        vo.setShareName(record.getShareName());
    }

    /**
     * 拼装简单文件分享详情的用户信息
     *
     * @param context
     */
    private void assembleShareSimpleUserInfo(QueryShareSimpleDetailContext context) {
        User record = userService.getById(context.getRecord().getCreateUser());
        if (Objects.isNull(record)) {
            throw new BusinessException("用户信息查询失败");
        }
        ShareUserInfoVO shareUserInfoVO = new ShareUserInfoVO();

        shareUserInfoVO.setUserId(record.getUserId());
        shareUserInfoVO.setUsername(encryptUsername(record.getUsername()));

        context.getVo().setShareUserInfoVO(shareUserInfoVO);
    }

    /******************************************************************获取下一级文件列表************************************************************/

    /**
     * 获取下一级的文件列表
     * <p>
     * 1、校验分享的状态
     * 2、校验文件的ID实在分享的文件列表中
     * 3、查询对应文件的子文件列表，返回
     *
     * @param context
     * @return
     */
    @Override
    public List<UserFileVO> fileList(QueryChildFileListContext context) {
        Share record = checkShareStatus(context.getShareId());
        context.setRecord(record);
        List<UserFileVO> allUserFileRecords = checkFileIdIsOnShareStatusAndGetAllShareUserFiles(context.getShareId(), Lists.newArrayList(context.getParentId()));
        Map<Long, List<UserFileVO>> parentIdFileListMap = allUserFileRecords.stream().collect(Collectors.groupingBy(UserFileVO::getParentId));
        List<UserFileVO> rPanUserFileVOS = parentIdFileListMap.get(context.getParentId());
        if (CollectionUtils.isEmpty(rPanUserFileVOS)) {
            return Lists.newArrayList();
        }
        return rPanUserFileVOS;
    }

    /**
     * 校验文件是否处于分享状态，返回该分享的所有文件列表
     *
     * @param shareId
     * @param fileIdList
     * @return
     */
    private List<UserFileVO> checkFileIdIsOnShareStatusAndGetAllShareUserFiles(Long shareId, List<Long> fileIdList) {
        List<Long> shareFileIdList = getShareFileIdList(shareId);
        if (CollectionUtils.isEmpty(shareFileIdList)) {
            return Lists.newArrayList();
        }
        List<UserFile> allFileRecords = userFileService.findAllFileRecordsByFileIdList(shareFileIdList);
        if (CollectionUtils.isEmpty(allFileRecords)) {
            return Lists.newArrayList();
        }
        allFileRecords = allFileRecords.stream()
                .filter(Objects::nonNull)
                .filter(record -> Objects.equals(record.getDelFlag(), DelFlagEnum.NO.getCode()))
                .collect(Collectors.toList());

        List<Long> allFileIdList = allFileRecords.stream().map(UserFile::getFileId).collect(Collectors.toList());

        if (allFileIdList.containsAll(fileIdList)) {
            return userFileService.transferVOList(allFileRecords);
        }

        throw new BusinessException(ResponseCode.SHARE_FILE_MISS);
    }
}




