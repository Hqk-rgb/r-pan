package com.whf.pan.server.modules.share.context;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author whf
 * @className SaveShareFilesContext
 * @description 保存文件和分享的关联关系上下文实体对象
 * @date 2023/12/4 14:36
 */
@Data
public class SaveShareFilesContext implements Serializable {

    private static final long serialVersionUID = -3311366998883061581L;
    /**
     * 分享的ID
     */
    private Long shareId;

    /**
     * 分享对应的文件的ID集合
     */
    private List<Long> shareFileIdList;

    /**
     * 当前登录的用户ID
     */
    private Long userId;
}
