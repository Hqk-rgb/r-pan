package com.whf.pan.server.modules.user.context;

import lombok.Data;

import java.io.Serializable;

/**
 * @author whf
 * @className CheckUsernameContext
 * @description TODO
 * @date 2023/10/31 13:48
 */
@Data
public class CheckUsernameContext implements Serializable {

    private static final long serialVersionUID = 5590704388206482304L;
    /**
     * 用户名称
     */
    private String username;
}
