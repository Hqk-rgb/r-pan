package com.whf.pan.server.modules.share.context;

import com.whf.pan.server.modules.share.entity.Share;
import com.whf.pan.server.modules.share.vo.ShareDetailVO;
import lombok.Data;

import java.io.Serializable;

/**
 * @author whf
 * @className QueryShareDetailContext
 * @description 查询分享详情的上下文实体对象
 * @date 2023/12/5 16:04
 */
@Data
public class QueryShareDetailContext implements Serializable {

    private static final long serialVersionUID = 5465351186516308202L;

    /**
     * 对应的分享ID
     */
    private Long shareId;

    /**
     * 分享实体
     */
    private Share record;

    /**
     * 分享详情的VO对象
     */
    private ShareDetailVO vo;
}
