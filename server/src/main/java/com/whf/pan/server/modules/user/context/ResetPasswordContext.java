package com.whf.pan.server.modules.user.context;

import lombok.Data;

import java.io.Serializable;

/**
 * @author whf
 * @className ResetPasswordContext
 * @description 重置用户密码上下文信息实体
 * @date 2023/10/31 17:24
 */
@Data
public class ResetPasswordContext implements Serializable {

    private static final long serialVersionUID = -5354782001147046730L;
    /**
     * 用户名称
     */
    private String username;

    /**
     * 用户新密码
     */
    private String password;

    /**
     * 重置密码的token信息
     */
    private String token;
}
