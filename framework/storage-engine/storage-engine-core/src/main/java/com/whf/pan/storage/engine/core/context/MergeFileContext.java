package com.whf.pan.storage.engine.core.context;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author whf
 * @className MergeFileContext
 * @description 合并文件上下文对象
 * @date 2023/11/14 17:58
 */
@Data
public class MergeFileContext implements Serializable {

    private static final long serialVersionUID = -5794128998214618645L;
    /**
     * 文件名称
     */
    private String filename;

    /**
     * 文件唯一标识
     */
    private String identifier;

    /**
     * 当前登录的用户ID
     */
    private Long userId;

    /**
     * 文件分片的真实存储路径集合
     */
    private List<String> realPathList;

    /**
     * 文件合并后的真实物理存储路径
     */
    private String realPath;
}
