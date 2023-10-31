package com.whf.pan.server.modules.user.context;

import com.whf.pan.server.modules.user.entity.User;
import lombok.Data;

import java.io.Serializable;

/**
 * @author whf
 * @className UserRegisterContext
 * @description 用户注册业务的上下文实体对象
 * @date 2023/10/30 13:34
 */
@Data
public class UserRegisterContext implements Serializable {

    private static final long serialVersionUID = -1294950272894294114L;
    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 密保问题
     */
    private String question;

    /**
     * 密保答案
     */
    private String answer;

    /**
     * 用户实体对象
     */
    private User entity;
}
