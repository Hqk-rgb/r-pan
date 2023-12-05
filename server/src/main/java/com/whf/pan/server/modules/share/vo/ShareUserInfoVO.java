package com.whf.pan.server.modules.share.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.whf.pan.web.serializer.IdEncryptSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author whf
 * @className ShareUserInfoVO
 * @description 分享者信息返回实体对象
 * @date 2023/12/5 16:06
 */
@Data
@ApiModel("分享者信息返回实体对象")
public class ShareUserInfoVO implements Serializable {

    private static final long serialVersionUID = 5982143344103694405L;

    @ApiModelProperty("分享者的ID")
    @JsonSerialize(using = IdEncryptSerializer.class)
    private Long userId;

    @ApiModelProperty("分享者的名称")
    private String username;
}
