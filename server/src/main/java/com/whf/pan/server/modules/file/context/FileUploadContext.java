package com.whf.pan.server.modules.file.context;

import com.whf.pan.server.modules.file.entity.File;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

/**
 * @author whf
 * @className FileUploadContext
 * @description 单文件上传的上下文实体
 * @date 2023/11/5 15:45
 */
@Data
public class FileUploadContext implements Serializable {


    private static final long serialVersionUID = -3692875147990492260L;

    /**
     * 文件名称
     */
    private String filename;

    /**
     * 文件唯一标识
     */
    private String identifier;

    /**
     * 文件大小
     */
    private Long totalSize;

    /**
     * 文件的父文件夹ID
     */
    private Long parentId;

    /**
     * 要上传的文件实体
     */
    private MultipartFile file;

    /**
     * 当前登录的用户ID
     */
    private Long userId;

    /**
     * 实体文件记录
     */
    private File record;
}
