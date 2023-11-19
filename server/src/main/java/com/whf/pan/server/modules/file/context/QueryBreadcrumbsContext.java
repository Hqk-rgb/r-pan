package com.whf.pan.server.modules.file.context;

import lombok.Data;

import java.io.Serializable;

/**
 * @author whf
 * @className QueryBreadcrumbsContext
 * @description 搜索文件面包屑列表的上下文信息实体
 * @date 2023/11/19 15:54
 */
@Data
public class QueryBreadcrumbsContext implements Serializable {

    private static final long serialVersionUID = -3039709231740738311L;
    /**
     * 文件ID
     */
    private Long fileId;

    /**
     * 当前登录的用户ID
     */
    private Long userId;
}
