package com.whf.pan.server.modules.file.context;

import com.whf.pan.server.modules.file.entity.File;
import lombok.Data;

import java.io.Serializable;

/**
 * @author whf
 * @className FileChunkMergeAndSaveContext
 * @description 文件分片合并的上下文实体对象
 * @date 2023/11/14 17:48
 */
@Data
public class FileChunkMergeAndSaveContext implements Serializable {

    private static final long serialVersionUID = -829394807954164916L;
    /**
     * 文件名称
     */
    private String filename;

    /**
     * 文件唯一标识
     */
    private String identifier;

    /**
     * 文件总大小
     */
    private Long totalSize;

    /**
     * 文件的父文件夹ID
     */
    private Long parentId;

    /**
     * 当前登录的用户ID
     */
    private Long userId;

    /**
     * 物理文件记录
     */
    private File record;

    /**
     * 文件合并之后存储的真实的物理路径
     */
    private String realPath;
}
