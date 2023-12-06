package com.whf.pan.server.modules.share.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.whf.pan.web.serializer.IdEncryptSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author whf
 * @className ShareSimpleDetailVO
 * @description 查询分享简单详情返回实体对象
 * @date 2023/12/6 08:10
 */
@ApiModel("查询分享简单详情返回实体对象")
@Data
public class ShareSimpleDetailVO implements Serializable {

    private static final long serialVersionUID = -4188889603320630991L;

    @ApiModelProperty("分享ID")
    @JsonSerialize(using = IdEncryptSerializer.class)
    private Long shareId;

    @ApiModelProperty("分享名称")
    private String shareName;

    @ApiModelProperty("分享人信息")
    private ShareUserInfoVO shareUserInfoVO;
}
