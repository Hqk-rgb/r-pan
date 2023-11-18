package com.whf.pan.server.modules.file.context;

import com.whf.pan.server.modules.file.entity.UserFile;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author whf
 * @className CopyFileContext
 * @description 文件复制操作上下文实体对象
 * @date 2023/11/18 16:01
 */
@Data
public class CopyFileContext implements Serializable {

    private static final long serialVersionUID = 6095904724462196461L;

    /**
     * 要复制的文件ID集合
     */
    private List<Long> fileIdList;

    /**
     * 目标文件夹ID
     */
    private Long targetParentId;

    /**
     * 当前登录的用户ID
     */
    private Long userId;

    /**
     * 要复制的文件列表
     */
    private List<UserFile> prepareRecords;
}
