package com.whf.pan.server.modules.file.context;

import lombok.Data;

import java.io.Serializable;

/**
 * @author whf
 * @className QueryFolderTreeContext
 * @description 查询文件夹树的上文实体信息
 * @date 2023/11/17 18:48
 */
@Data
public class QueryFolderTreeContext implements Serializable {


    private static final long serialVersionUID = 5736418769863878117L;
    /**
     * 当前登录的用户ID
     */
    private Long userId;
}
