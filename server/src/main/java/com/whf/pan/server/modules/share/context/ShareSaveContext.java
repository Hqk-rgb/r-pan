package com.whf.pan.server.modules.share.context;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author whf
 * @className ShareSaveContext
 * @description 保存到我的网盘上下文实体对象
 * @date 2023/12/6 10:36
 */
@Data
public class ShareSaveContext implements Serializable {

    private static final long serialVersionUID = 284511656782906915L;

    /**
     * 要保存的文件ID列表
     */
    private List<Long> fileIdList;

    /**
     * 目标文件夹ID
     */
    private Long targetParentId;

    /**
     * 当前登录的用户ID
     */
    private Long userId;

    /**
     * 分享的ID
     */
    private Long shareId;
}
