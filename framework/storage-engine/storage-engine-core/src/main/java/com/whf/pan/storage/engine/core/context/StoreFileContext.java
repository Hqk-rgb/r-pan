package com.whf.pan.storage.engine.core.context;

import lombok.Data;

import java.io.InputStream;
import java.io.Serializable;

/**
 * @author whf
 * @className StoreFileContext
 * @description 文件存储引擎存储物理文件的上下文实体
 * @date 2023/11/5 16:23
 */
@Data
public class StoreFileContext implements Serializable {

    private static final long serialVersionUID = 83191736202061396L;

    /**
     * 上传的文件名称
     */
    private String filename;

    /**
     * 文件的总大小
     */
    private Long totalSize;

    /**
     * 文件的输入流信息
     */
    private InputStream inputStream;

    /**
     * 文件上传后的物理路径
     */
    private String realPath;
}
