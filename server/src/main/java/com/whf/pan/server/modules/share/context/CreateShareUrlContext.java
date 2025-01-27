package com.whf.pan.server.modules.share.context;

import com.whf.pan.server.modules.share.entity.Share;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author whf
 * @className CreateShareUrlContext
 * @description 创建分享链接上下文实体对象
 * @date 2023/12/4 14:29
 */
@Data
public class CreateShareUrlContext implements Serializable {

    private static final long serialVersionUID = -695761280608304162L;
    /**
     * 分享的名称
     */
    private String shareName;

    /**
     * 分享的类型
     */
    private Integer shareType;

    /**
     * 分享的日期类型
     */
    private Integer shareDayType;

    /**
     * 该分项对应的文件ID集合
     */
    private List<Long> shareFileIdList;

    /**
     * 当前登录的用户ID
     */
    private Long userId;

    /**
     * 已经保存的分享实体信息
     */
    private Share record;
}
