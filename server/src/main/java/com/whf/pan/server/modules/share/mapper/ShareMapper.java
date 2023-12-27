package com.whf.pan.server.modules.share.mapper;

import com.whf.pan.server.modules.share.entity.Share;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.whf.pan.server.modules.share.vo.ShareUrlListVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author 26570
* @description 针对表【r_pan_share(用户分享表)】的数据库操作Mapper
* @createDate 2023-10-28 15:48:20
* @Entity com.whf.pan.server.modules.share.entity.Share
*/
public interface ShareMapper extends BaseMapper<Share> {

    /**
     * 查询用户的分享列表
     *
     * @param userId
     * @return
     */
    List<ShareUrlListVO> selectShareVOListByUserId(@Param("userId") Long userId);

    /**
     * 滚动查询已存在的分享ID集合
     *
     * @param startId
     * @param limit
     * @return
     */
    List<Long> rollingQueryShareId(@Param("startId") long startId, @Param("limit") long limit);

}




