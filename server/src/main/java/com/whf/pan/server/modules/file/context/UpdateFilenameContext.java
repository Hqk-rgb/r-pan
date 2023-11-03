package com.whf.pan.server.modules.file.context;

import com.whf.pan.server.modules.file.entity.UserFile;
import lombok.Data;

import java.io.Serializable;

/**
 * @author whf
 * @className UpdateFilenameContext
 * @description 文件重命名参数上下文对象
 * @date 2023/11/3 18:00
 */
@Data
public class UpdateFilenameContext implements Serializable {

    private static final long serialVersionUID = -7632614109763492007L;

    /**
     * 要更新的文件ID
     */
    private Long fileId;

    /**
     * 新的文件名称
     */
    private String newFilename;

    /**
     * 当前的登录用户ID
     */
    private Long userId;

    /**
     * 要更新的文件记录实体
     */
    private UserFile entity;
}
