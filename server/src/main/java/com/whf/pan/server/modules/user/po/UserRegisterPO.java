package com.whf.pan.server.modules.user.po;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * @author whf
 * @className UserRegisterPO
 * @description 注册用户参数实体对象
 * @date 2023/10/30 12:25
 */
@Data
@ApiModel(value = "用户注册参数")
public class UserRegisterPO implements Serializable {
    private static final long serialVersionUID = -2291447462213627480L;

    @ApiModelProperty(value = "用户名", required = true)
    @NotBlank(message = "用户名不能为空")
    @Pattern(regexp = "^[0-9A-Za-z]{4,16}$", message = "请输入4-16位只包含数字和字母的用户名")
    private String username;

    @ApiModelProperty(value = "密码", required = true)
    @NotBlank(message = "密码不能为空")
    @Length(min = 8, max = 16, message = "请输入8-16位的密码")
    private String password;

    @ApiModelProperty(value = "密码问题", required = true)
    @NotBlank(message = "密保问题不能为空")
    @Length(max = 100, message = "密保问题不能超过100个字符")
    private String question;

    @ApiModelProperty(value = "密码答案", required = true)
    @NotBlank(message = "密保答案不能为空")
    @Length(max = 100, message = "密保答案不能超过100个字符")
    private String answer;
}
