package com.whf.pan.server.modules.file.po;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author whf
 * @className TransferFilePO
 * @description 文件转移参数实体对象
 * @date 2023/11/18 12:15
 */
@ApiModel("文件转移参数实体对象")
@Data
public class TransferFilePO implements Serializable {

    private static final long serialVersionUID = 6045081008724041586L;

    @ApiModelProperty("要转移的文件ID集合，多个使用公用分隔符隔开")
    @NotBlank(message = "请选择要转移的文件")
    private String fileIds;

    @ApiModelProperty("要转移到的目标文件夹的ID")
    @NotBlank(message = "请选择要转移到哪个文件夹下面")
    private String targetParentId;
}
