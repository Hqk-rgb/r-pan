package com.whf.pan.server.modules.share.service;

import com.whf.pan.server.modules.file.vo.UserFileVO;
import com.whf.pan.server.modules.share.context.*;
import com.whf.pan.server.modules.share.entity.Share;
import com.baomidou.mybatisplus.extension.service.IService;
import com.whf.pan.server.modules.share.vo.ShareDetailVO;
import com.whf.pan.server.modules.share.vo.ShareSimpleDetailVO;
import com.whf.pan.server.modules.share.vo.ShareUrlListVO;
import com.whf.pan.server.modules.share.vo.ShareUrlVO;

import java.util.List;

/**
* @author 26570
* @description 针对表【r_pan_share(用户分享表)】的数据库操作Service
* @createDate 2023-10-28 15:48:20
*/
public interface IShareService extends IService<Share> {

    /**
     * 创建分享链接
     *
     * @param context
     * @return
     */
    ShareUrlVO create(CreateShareUrlContext context);


    /**
     * 查询用户的分享列表
     *
     * @param context
     * @return
     */
    List<ShareUrlListVO> getShares(QueryShareListContext context);

    /**
     * 取消分享链接
     *
     * @param context
     */
    void cancelShare(CancelShareContext context);

    /**
     * 校验分享码
     *
     * @param context
     * @return
     */
    String checkShareCode(CheckShareCodeContext context);

    /**
     * 查询分享的详情
     *
     * @param context
     * @return
     */
    ShareDetailVO detail(QueryShareDetailContext context);

    /**
     * 查询分享的简单详情
     *
     * @param context
     * @return
     */
    ShareSimpleDetailVO simpleDetail(QueryShareSimpleDetailContext context);

    /**
     * 获取下一级的文件列表
     *
     * @param context
     * @return
     */
    List<UserFileVO> fileList(QueryChildFileListContext context);

    /**
     * 转存至我的网盘
     *
     * @param context
     */
    void saveFiles(ShareSaveContext context);

    /**
     * 分享的文件下载
     *
     * @param context
     */
    void download(ShareFileDownloadContext context);

    /**
     * 刷新受影响的对应的分享的状态
     *
     * @param allAvailableFileIdList
     */
    void refreshShareStatus(List<Long> allAvailableFileIdList);
}
