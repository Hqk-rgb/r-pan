package com.whf.pan.server.modules.user.context;

import com.whf.pan.server.modules.user.entity.User;
import lombok.Data;

import java.io.Serializable;

/**
 * @author whf
 * @className ChangePasswordContext
 * @description 用户在线修改密码上下文信息实体
 * @date 2023/10/31 19:09
 */
@Data
public class ChangePasswordContext implements Serializable {

    private static final long serialVersionUID = -1839667625247393740L;
    /**
     * 当前登录的用户ID
     */
    private Long userId;

    /**
     * 旧密码
     */
    private String oldPassword;

    /**
     * 新密码
     */
    private String newPassword;

    /**
     * 当前登录用户的实体信息
     */
    private User entity;
}
