package com.whf.pan.server.modules.share.service;

import com.whf.pan.server.modules.share.context.CancelShareContext;
import com.whf.pan.server.modules.share.context.CreateShareUrlContext;
import com.whf.pan.server.modules.share.context.QueryShareListContext;
import com.whf.pan.server.modules.share.entity.Share;
import com.baomidou.mybatisplus.extension.service.IService;
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

}
