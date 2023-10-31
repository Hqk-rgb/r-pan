package com.whf.pan.server.modules.user.context;

import lombok.Data;

import java.io.Serializable;

/**
 * @author whf
 * @className CheckAnswerContext
 * @description TODO
 * @date 2023/10/31 15:38
 */
@Data
public class CheckAnswerContext implements Serializable {

    private static final long serialVersionUID = 2202836372447857324L;
    /**
     * 用户名称
     */
    private String username;

    /**
     * 密保问题
     */
    private String question;

    /**
     * 密保答案
     */
    private String answer;
}
