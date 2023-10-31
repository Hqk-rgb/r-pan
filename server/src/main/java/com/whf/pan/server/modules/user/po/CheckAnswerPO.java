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
 * @className CheckAnswerPO
 * @description 校验密保答案PO对象
 * @date 2023/10/31 15:33
 */
@ApiModel(value = "用户忘记密码-校验密保答案参数")
@Data
public class CheckAnswerPO implements Serializable {

    private static final long serialVersionUID = 8317285340929154760L;

    @ApiModelProperty(value = "用户名", required = true)
    @NotBlank(message = "用户名称不能为空")
    @Pattern(regexp = "^[0-9A-Za-z]{4,16}$", message = "请输入4-16位只包含数字和字母的用户名")
    private String username;

    @ApiModelProperty(value = "密码问题", required = true)
    @NotBlank(message = "密保问题不能为空")
    @Length(max = 100, message = "密保问题不能超过100个字符")
    private String question;

    @ApiModelProperty(value = "密码答案", required = true)
    @NotBlank(message = "密保答案不能为空")
    @Length(max = 100, message = "密保答案不能超过100个字符")
    private String answer;
}
