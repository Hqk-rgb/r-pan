package com.whf.pan.server.modules.file.context;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author whf
 * @className FileSearchContext
 * @description 搜索文件上下文实体信息
 * @date 2023/11/19 13:28
 */
@Data
public class FileSearchContext implements Serializable {


    private static final long serialVersionUID = 6381053761686790280L;
    /**
     * 搜索的关键字
     */
    private String keyword;

    /**
     * 搜索的文件类型集合
     */
    private List<Integer> fileTypeArray;

    /**
     * 当前登录的用户ID
     */
    private Long userId;
}
