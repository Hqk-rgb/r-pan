package com.whf.pan.storage.engine.core.context;

import lombok.Data;

import java.io.OutputStream;
import java.io.Serializable;

/**
 * @author whf
 * @className ReadFileContext
 * @description 文件读取的上下文实体信息
 * @date 2023/11/17 15:47
 */
@Data
public class ReadFileContext implements Serializable {
    private static final long serialVersionUID = -635034250236909651L;
    /**
     * 文件的真实存储路径
     */
    private String realPath;

    /**
     * 文件的输出流
     */
    private OutputStream outputStream;
}
