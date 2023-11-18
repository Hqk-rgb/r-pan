package com.whf.pan.server.modules.file.context;

import com.whf.pan.server.modules.file.entity.UserFile;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author whf
 * @className TransferFileContext
 * @description 文件转移操作上下文实体对象
 * @date 2023/11/18 12:16
 */
@Data
public class TransferFileContext implements Serializable {

    private static final long serialVersionUID = -2405180016564414800L;

    /**
     * 要转移的文件ID集合
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
     * 要转移的文件列表
     */
    private List<UserFile> prepareRecords;
}
