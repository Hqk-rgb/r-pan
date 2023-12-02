package com.whf.pan.server.modules.recycle.po;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author whf
 * @className DeletePO
 * @description 文件删除参数实体
 * @date 2023/12/2 17:51
 */
@ApiModel("文件删除参数实体")
@Data
public class DeletePO implements Serializable {

    private static final long serialVersionUID = -2396491297086389058L;
    @ApiModelProperty(value = "要删除的文件ID集合，多个使用公用分割符分隔", required = true)
    @NotBlank(message = "请选择要删除的文件")
    private String fileIds;

}
