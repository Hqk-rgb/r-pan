package com.whf.pan.server.modules.recycle.context;

import com.whf.pan.server.modules.file.entity.UserFile;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author whf
 * @className DeleteContext
 * @description 删除文件的上下文实体对象
 * @date 2023/12/2 15:10
 */
@Data
public class DeleteContext implements Serializable {

    private static final long serialVersionUID = -8660409440477700811L;

    /**
     * 要操作的文件ID的集合
     */
    private List<Long> fileIdList;

    /**
     * 当前登录的用户ID
     */
    private Long userId;

    /**
     * 要被删除的文件记录列表
     */
    private List<UserFile> records;

    /**
     * 所有要被删除的文件记录列表
     */
    private List<UserFile> allRecords;
}
