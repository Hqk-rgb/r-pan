package com.whf.pan.server.modules.file.po;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author whf
 * @className CreateFolderPO
 * @description 创建文件夹参数实体
 * @date 2023/11/3 13:59
 */
@Data
@ApiModel(value = "创建文件夹参数实体", description = "创建文件夹参数")
public class CreateFolderPO implements Serializable {

    private static final long serialVersionUID = 100494904674778046L;

    @ApiModelProperty(value = "加密的父文件夹ID",required = true)
    @NotBlank(message = "父文件夹ID不能为空")
    private String parentId;

    @ApiModelProperty(value = "文件夹名称",required = true)
    @NotBlank(message = "文件夹名称不能为空")
    private String folderName;

}
