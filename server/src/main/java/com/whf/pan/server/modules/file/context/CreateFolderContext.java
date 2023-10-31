package com.whf.pan.server.modules.file.context;

import lombok.Data;

import java.io.Serializable;

/**
 * @author whf
 * @className CreateFolderContext
 * @description 创建文件夹上下文实体
 * @date 2023/10/30 14:04
 */
@Data
public class CreateFolderContext implements Serializable {

    private static final long serialVersionUID = -219149623283982621L;
    /**
     * 父文件夹ID
     */
    private Long parentId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 文件夹名称
     */
    private String folderName;
}
