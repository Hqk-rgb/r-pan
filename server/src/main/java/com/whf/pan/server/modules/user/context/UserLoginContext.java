package com.whf.pan.server.modules.user.context;

import com.whf.pan.server.modules.user.entity.User;
import lombok.Data;

import java.io.Serializable;

/**
 * @author whf
 * @className UserLoginContext
 * @description 用户登录业务的上下文实体对象
 * @date 2023/10/30 20:23
 */
@Data
public class UserLoginContext implements Serializable {

    private static final long serialVersionUID = 68323137816930511L;
    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 用户实体对象
     */
    private User entity;

    /**
     * 登录成功之后的凭证信息
     */
    private String accessToken;
    
}
