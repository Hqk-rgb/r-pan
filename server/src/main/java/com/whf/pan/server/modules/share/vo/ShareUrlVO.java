package com.whf.pan.server.modules.share.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.whf.pan.web.serializer.IdEncryptSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author whf
 * @className ShareUrlVO
 * @description 创建分享链接的返回实体对象
 * @date 2023/12/4 14:26
 */
@ApiModel(value = "创建分享链接的返回实体对象")
@Data
public class ShareUrlVO implements Serializable {

    private static final long serialVersionUID = -7342621017586498374L;

    @JsonSerialize(using = IdEncryptSerializer.class)
    @ApiModelProperty("分享链接的ID")
    private Long shareId;

    @ApiModelProperty("分享链接的名称")
    private String shareName;

    @ApiModelProperty("分享链接的URL")
    private String shareUrl;

    @ApiModelProperty("分享链接的分享码")
    private String shareCode;

    @ApiModelProperty("分享链接的状态")
    private Integer shareStatus;
}
