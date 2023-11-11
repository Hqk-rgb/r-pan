package com.whf.pan.server.modules.file.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author whf
 * @className UploadedChunksVO
 * @description 文件分片列表返回实体
 * @date 2023/11/11 18:21
 */
@ApiModel("查询用户已上传的文件分片列表返回实体")
@Data
public class UploadedChunksVO implements Serializable {

    private static final long serialVersionUID = 980260509635410980L;

    @ApiModelProperty("已上传的分片编号列表")
    private List<Integer> uploadedChunks;
}
