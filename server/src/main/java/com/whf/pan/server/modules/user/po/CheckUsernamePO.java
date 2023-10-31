package com.whf.pan.server.modules.user.po;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * @author whf
 * @className CheckUsernamePO
 * @description 校验用户名称PO对象
 * @date 2023/10/31 13:40
 */
@ApiModel(value = "用户忘记密码-校验用户名参数")
@Data
public class CheckUsernamePO implements Serializable {

    private static final long serialVersionUID = 8326609876320359322L;

    @ApiModelProperty(value = "用户名", required = true)
    @NotBlank(message = "用户名称不能为空")
    @Pattern(regexp = "^[0-9A-Za-z]{4,16}$", message = "请输入4-16位只包含数字和字母的用户名")
    private String username;
}
