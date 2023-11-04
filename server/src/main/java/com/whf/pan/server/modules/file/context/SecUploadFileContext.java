package com.whf.pan.server.modules.file.context;

import lombok.Data;

import java.io.Serializable;

/**
 * @author whf
 * @className SecUploadFileContext
 * @description 秒传文件接口上下文对象实体
 * @date 2023/11/4 18:54
 */
@Data
public class SecUploadFileContext implements Serializable {

    private static final long serialVersionUID = 1483839235425985314L;
    /**
     * 文件的父ID
     */
    private Long parentId;

    /**
     * 文件名称
     */
    private String filename;

    /**
     * 文件的唯一标识
     */
    private String identifier;

    /**
     * 当前登录用的ID
     */
    private Long userId;
}
