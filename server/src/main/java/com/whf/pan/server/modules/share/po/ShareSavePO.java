package com.whf.pan.server.modules.share.po;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author whf
 * @className ShareSavePO
 * @description 保存至我的网盘参数实体对象
 * @date 2023/12/6 10:35
 */
@Data
@ApiModel("保存至我的网盘参数实体对象")
public class ShareSavePO implements Serializable {

    private static final long serialVersionUID = 2715945533428465442L;

    @ApiModelProperty(value = "要转存的文件ID集合，多个使用公用分隔符拼接", required = true)
    @NotBlank(message = "请选择要保存的文件")
    private String fileIds;

    @ApiModelProperty(value = "要转存到的文件夹ID", required = true)
    @NotBlank(message = "请选择要保存到的文件夹")
    private String targetParentId;
}
