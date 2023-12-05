package com.whf.pan.server.modules.share.context;

import com.whf.pan.server.modules.share.entity.Share;
import lombok.Data;

import java.io.Serializable;

/**
 * @author whf
 * @className CheckShareCodeContext
 * @description 校验分享码上下文实体对象
 * @date 2023/12/5 14:50
 */
@Data
public class CheckShareCodeContext implements Serializable {

    private static final long serialVersionUID = 741760674705745086L;
    /**
     * 分享ID
     */
    private Long shareId;

    /**
     * 分享码
     */
    private String shareCode;

    /**
     * 对应的分享实体
     */
    private Share record;
}
