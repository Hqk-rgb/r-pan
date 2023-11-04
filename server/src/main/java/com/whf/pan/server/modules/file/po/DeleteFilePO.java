package com.whf.pan.server.modules.file.po;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author whf
 * @className DeleteFilePO
 * @description 批量删除文件入参对象实体
 * @date 2023/11/4 13:12
 */
@Data
@ApiModel(value = "批量删除文件入参对象实体")
public class DeleteFilePO implements Serializable {

    private static final long serialVersionUID = 4174036527552390177L;

    @ApiModelProperty(value = "要删除的文件ID，多个使用公用的分隔符分割", required = true)
    @NotBlank(message = "请选择要删除的文件信息")
    private String fileIds;
}
