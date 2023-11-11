package com.whf.pan.server.modules.file.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author whf
 * @className FileChunkUploadVO
 * @description 文件分片上传的响应实体
 * @date 2023/11/6 21:12
 */
@ApiModel("文件分片上传的响应实体")
@Data
public class FileChunkUploadVO implements Serializable {

    private static final long serialVersionUID = -7483750564911327886L;

    @ApiModelProperty("是否需要合并文件 0 不需要 1 需要")
    private Integer mergeFlag;
}
