package com.whf.pan.server.modules.file.context;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author whf
 * @className DeleteFileContext
 * @description 批量删除文件上下文实体
 * @date 2023/11/4 13:14
 */
@Data
public class DeleteFileContext implements Serializable {

    private static final long serialVersionUID = 2909244302290540449L;

    /**
     * 要删除的文件ID集合
     */
    private List<Long> fileIdList;

    /**
     * 当前的登录用户ID
     */
    private Long userId;
}
