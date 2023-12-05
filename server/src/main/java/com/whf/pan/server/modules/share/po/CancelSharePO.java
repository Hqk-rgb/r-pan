package com.whf.pan.server.modules.share.po;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author whf
 * @className CancelSharePO
 * @description 取消分享参数实体对象
 * @date 2023/12/5 14:23
 */
@ApiModel("取消分享参数实体对象")
@Data
public class CancelSharePO implements Serializable {

    private static final long serialVersionUID = 6689661521203082401L;

    @ApiModelProperty(value = "要取消的分享ID的集合，多个使用公用的分割符拼接", required = true)
    @NotBlank(message = "请选择要取消的分享")
    private String shareIds;
}
