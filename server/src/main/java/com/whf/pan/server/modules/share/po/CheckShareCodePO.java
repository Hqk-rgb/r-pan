package com.whf.pan.server.modules.share.po;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author whf
 * @className CheckShareCodePO
 * @description 校验分享码参数实体对象
 * @date 2023/12/5 14:49
 */

@ApiModel("校验分享码参数实体对象")
@Data
public class CheckShareCodePO implements Serializable {

    private static final long serialVersionUID = 3769858353990947716L;

    @ApiModelProperty(value = "分享的ID", required = true)
    @NotBlank(message = "分享ID不能为空")
    private String shareId;

    @ApiModelProperty(value = "分享的分享码", required = true)
    @NotBlank(message = "分享的分享码不能为空")
    private String shareCode;
}
