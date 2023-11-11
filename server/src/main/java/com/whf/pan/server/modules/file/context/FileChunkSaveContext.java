package com.whf.pan.server.modules.file.context;

import com.whf.pan.server.modules.file.enums.MergeFlagEnum;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

/**
 * @author whf
 * @className FileChunkSaveContext
 * @description 文件分片保存的上下文实体信息
 * @date 2023/11/6 21:27
 */
@Data
public class FileChunkSaveContext implements Serializable {

    private static final long serialVersionUID = -4926384530451928907L;

    /**
     * 文件名称
     */
    private String filename;

    /**
     * 文件唯一标识
     */
    private String identifier;

    /**
     * 总体的分片数
     */
    private Integer totalChunks;

    /**
     * 当前分片下标
     * 从1开始
     */
    private Integer chunkNumber;

    /**
     * 当前分片的大小
     */
    private Long currentChunkSize;

    /**
     * 文件的总大小
     */
    private Long totalSize;

    /**
     * 文件实体
     */
    private MultipartFile file;

    /**
     * 当前登录的用户ID
     */
    private Long userId;

    /**
     * 文件合并标识
     */
    private MergeFlagEnum mergeFlagEnum = MergeFlagEnum.NOT_READY;

    /**
     * 文件分片的真实存储路径
     */
    private String realPath;
}
