package com.whf.pan.server.modules.file.po;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author whf
 * @className QueryUploadedChunksPO
 * @description 已上传分片列表的参数实体
 * @date 2023/11/11 18:25
 */

@ApiModel("查询用户已上传分片列表的参数实体")
@Data
public class QueryUploadedChunksPO implements Serializable {

    private static final long serialVersionUID = -4904756995092527394L;
    @ApiModelProperty(value = "文件的唯一标识", required = true)
    @NotBlank(message = "文件唯一标识不能为空")
    private String identifier;
}
