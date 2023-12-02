package com.whf.pan.server.modules.recycle.context;

import lombok.Data;

import java.io.Serializable;

/**
 * @author whf
 * @className QueryRecycleFileListContext
 * @description 查询用户回收站文件列表上下文实体对象
 * @date 2023/12/2 14:31
 */
@Data
public class QueryRecycleFileListContext implements Serializable {

    private static final long serialVersionUID = -611005373176762641L;
    private Long userId;
}
