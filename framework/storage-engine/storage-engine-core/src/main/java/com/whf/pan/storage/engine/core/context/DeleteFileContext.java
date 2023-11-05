package com.whf.pan.storage.engine.core.context;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author whf
 * @className DeleteFileContext
 * @description 删除物理文件的上下文实体信息
 * @date 2023/11/5 16:24
 */
@Data
public class DeleteFileContext implements Serializable {

    private static final long serialVersionUID = 5778153799543886437L;

    /**
     * 要删除的物理文件路径的集合
     */
    private List<String> realFilePathList;
}
