package com.whf.pan.server.modules.share.context;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author whf
 * @className CancelShareContext
 * @description 取消分享的上下文实体对象
 * @date 2023/12/5 14:24
 */
@Data
public class CancelShareContext implements Serializable {

    private static final long serialVersionUID = 587341478784885508L;

    /**
     * 当前登录的用户ID
     */
    private Long userId;

    /**
     * 要取消的分享ID集合
     */
    private List<Long> shareIdList;
}
