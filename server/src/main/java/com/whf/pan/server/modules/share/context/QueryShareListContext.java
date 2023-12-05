package com.whf.pan.server.modules.share.context;

import lombok.Data;

import java.io.Serializable;

/**
 * @author whf
 * @className QueryShareListContext
 * @description 查询用户已有的分享链接列表的上下文实体对象
 * @date 2023/12/5 13:52
 */
@Data
public class QueryShareListContext implements Serializable {

    private static final long serialVersionUID = 1712781177079446187L;
    /**
     * 当前登录的用户ID
     */
    private Long userId;
}
