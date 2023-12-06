package com.whf.pan.server.modules.share.context;

import com.whf.pan.server.modules.share.entity.Share;
import com.whf.pan.server.modules.share.vo.ShareSimpleDetailVO;
import lombok.Data;

import java.io.Serializable;

/**
 * @author whf
 * @className QueryShareSimpleDetailContext
 * @description 查询分享简单详情上下文实体信息
 * @date 2023/12/6 08:11
 */
@Data
public class QueryShareSimpleDetailContext implements Serializable {

    private static final long serialVersionUID = -5966270244397058469L;
    /**
     * 分享的ID
     */
    private Long shareId;

    /**
     * 分享对应的实体信息
     */
    private Share record;

    /**
     * 简单分享详情的VO对象
     */
    private ShareSimpleDetailVO vo;
}
