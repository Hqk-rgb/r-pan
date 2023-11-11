package com.whf.pan.server.modules.file.context;

import lombok.Data;

import java.io.Serializable;

/**
 * @author whf
 * @className QueryUploadedChunksContext
 * @description 查询用户已上传的分片列表的上下文信息实体
 * @date 2023/11/11 18:35
 */
@Data
public class QueryUploadedChunksContext implements Serializable {

    private static final long serialVersionUID = 524382458303059032L;
    /**
     * 文件的唯一标识
     */
    private String identifier;

    /**
     * 当前登录的用户ID
     */
    private Long userId;
}
