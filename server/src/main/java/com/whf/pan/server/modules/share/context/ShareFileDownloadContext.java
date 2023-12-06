package com.whf.pan.server.modules.share.context;

import lombok.Data;

import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;

/**
 * @author whf
 * @className ShareFileDownloadContext
 * @description 分享文件下载上下文实体对象
 * @date 2023/12/6 10:54
 */
@Data
public class ShareFileDownloadContext implements Serializable {

    private static final long serialVersionUID = -6220895484916815825L;
    /**
     * 要下载的文件ID
     */
    private Long fileId;

    /**
     * 当前登录的用户ID
     */
    private Long userId;

    /**
     * 分享ID
     */
    private Long shareId;

    /**
     * 相应实体
     */
    private HttpServletResponse response;
}
