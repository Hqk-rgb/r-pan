package com.whf.pan.server.modules.share.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 用户分享文件表
 * @TableName r_pan_share_file
 */
@TableName(value ="r_pan_share_file")
@Data
public class ShareFile implements Serializable {


    @TableField(exist = false)
    private static final long serialVersionUID = 4014529298254337127L;
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 分享id
     */
    @TableField(value = "share_id")
    private Long shareId;

    /**
     * 文件记录ID
     */
    @TableField(value = "file_id")
    private Long fileId;

    /**
     * 分享创建人
     */
    @TableField(value = "create_user")
    private Long createUser;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;


}