package com.whf.pan.server.modules.file.vo;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.whf.pan.web.serializer.IdEncryptSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author whf
 * @className FolderTreeNodeVO
 * @description TODO
 * @date 2023/11/17 18:49
 */
@ApiModel("文件夹树节点实体")
@Data
public class FolderTreeNodeVO implements Serializable {

    private static final long serialVersionUID = -6759646211799896329L;

    @ApiModelProperty("文件夹名称")
    private String label;

    @ApiModelProperty("文件ID")
    @JsonSerialize(using = IdEncryptSerializer.class)
    private Long id;

    @ApiModelProperty("父文件ID")
    @JsonSerialize(using = IdEncryptSerializer.class)
    private Long parentId;

    @ApiModelProperty("子节点集合")
    private List<FolderTreeNodeVO> children;

    public void print() {
        String jsonString = JSON.toJSONString(this);
        System.out.println(jsonString);
    }
}
