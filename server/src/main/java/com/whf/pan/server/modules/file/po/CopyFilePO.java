package com.whf.pan.server.modules.file.po;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author whf
 * @className CopyFilePO
 * @description 文件复制参数实体对象
 * @date 2023/11/18 16:00
 */
@ApiModel("文件复制参数实体对象")
@Data
public class CopyFilePO implements Serializable {

    private static final long serialVersionUID = -7869404438248050395L;

    @ApiModelProperty("要复制的文件ID集合，多个使用公用分隔符隔开")
    @NotBlank(message = "请选择要复制的文件")
    private String fileIds;

    @ApiModelProperty("要转移到的目标文件夹的ID")
    @NotBlank(message = "请选择要转移到哪个文件夹下面")
    private String targetParentId;
}
