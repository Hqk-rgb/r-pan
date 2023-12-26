package com.whf.pan.server.modules.user.context;

import lombok.Data;

import java.io.Serializable;

/**
 * @author whf
 * @className QueryUserSearchHistoryContext
 * @description 用户查询搜索历史记录上下文实体
 * @date 2023/12/26 20:04
 */
@Data
public class QueryUserSearchHistoryContext implements Serializable {

    /**
     * 当前登录用户的ID
     */
    private Long userId;
}
