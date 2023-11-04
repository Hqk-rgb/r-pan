package com.whf.pan.server.modules.file.po;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
/**
 * @author whf
 * @className SecUploadFilePO
 * @description 秒传文件接口参数对象实体
 * @date 2023/11/4 18:50
 */

@Data
@ApiModel(value = "秒传文件接口参数对象实体")
public class SecUploadFilePO implements Serializable {

    private static final long serialVersionUID = -7048347974551629134L;

    @ApiModelProperty(value = "秒传的父文件夹ID", required = true)
    @NotBlank(message = "父文件夹ID不能为空")
    private String parentId;

    @ApiModelProperty(value = "文件名称", required = true)
    @NotBlank(message = "文件名称不能为空")
    private String filename;

    @ApiModelProperty(value = "文件的唯一标识", required = true)
    @NotBlank(message = "文件的唯一标识不能为空")
    private String identifier;
}
