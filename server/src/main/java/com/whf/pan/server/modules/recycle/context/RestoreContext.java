package com.whf.pan.server.modules.recycle.context;

import com.whf.pan.server.modules.file.entity.UserFile;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author whf
 * @className RestoreContext
 * @description 还原文件的上下文实体对象
 * @date 2023/12/2 15:05
 */
@Data
public class RestoreContext implements Serializable {

    private static final long serialVersionUID = -2896118992872469713L;
    /**
     * 要操作的文件ID的集合
     */
    private List<Long> fileIdList;

    /**
     * 当前登录的用户ID
     */
    private Long userId;

    /**
     * 要被还原的文件记录列表
     */
    private List<UserFile> records;
}
