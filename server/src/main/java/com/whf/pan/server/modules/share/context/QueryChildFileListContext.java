package com.whf.pan.server.modules.share.context;

import com.whf.pan.server.modules.share.entity.Share;
import lombok.Data;

import java.io.Serializable;

/**
 * @author whf
 * @className QueryChildFileListContext
 * @description 查询下一级文件列表的上下文实体信息
 * @date 2023/12/6 08:37
 */

@Data
public class QueryChildFileListContext implements Serializable {

    private static final long serialVersionUID = 4786301545477745887L;

    /**
     * 分享的ID
     */
    private Long shareId;

    /**
     * 父文件夹的ID
     */
    private Long parentId;

    /**
     * 分享对应的实体信息
     */
    private Share record;
}
