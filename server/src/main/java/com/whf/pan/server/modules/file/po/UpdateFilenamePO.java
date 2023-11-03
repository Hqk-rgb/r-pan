package com.whf.pan.server.modules.file.po;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author whf
 * @className UpdateFilenamePO
 * @description 文件重命名参数对象
 * @date 2023/11/3 17:56
 */
@Data
@ApiModel(value = "文件重命名参数对象")
public class UpdateFilenamePO implements Serializable {

    private static final long serialVersionUID = -9026185282688615181L;

    @ApiModelProperty(value = "更新的文件ID",required = true)
    @NotBlank(message = "更新的文件ID不能为空")
    private String fileId;

    @ApiModelProperty(value = "新的文件名称",required = true)
    @NotBlank(message = "更新的文件名称不能为空")
    private String newFilename;


}
