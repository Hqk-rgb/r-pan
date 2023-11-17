package com.whf.pan.server.modules.file.context;

import lombok.Data;

import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;

/**
 * @author whf
 * @className FilePreviewContext
 * @description 文件预览的上下文实体对象
 * @date 2023/11/17 18:31
 */
@Data
public class FilePreviewContext implements Serializable {

    private static final long serialVersionUID = -6075391187375975083L;

    /**
     * 文件ID
     */
    private Long fileId;

    /**
     * 请求响应对象
     */
    private HttpServletResponse response;

    /**
     * 当前登录的用户ID
     */
    private Long userId;
}
