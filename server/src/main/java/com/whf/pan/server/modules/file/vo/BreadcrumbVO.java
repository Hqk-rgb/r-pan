package com.whf.pan.server.modules.file.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.whf.pan.server.modules.file.entity.UserFile;
import com.whf.pan.web.serializer.IdEncryptSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author whf
 * @className BreadcrumbVO
 * @description 面包屑列表展示实体
 * @date 2023/11/19 15:52
 */
@ApiModel("面包屑列表展示实体")
@Data
public class BreadcrumbVO implements Serializable {

    private static final long serialVersionUID = 3588754004465789085L;

    @ApiModelProperty("文件ID")
    @JsonSerialize(using = IdEncryptSerializer.class)
    private Long id;

    @ApiModelProperty("父文件夹ID")
    @JsonSerialize(using = IdEncryptSerializer.class)
    private Long parentId;

    @ApiModelProperty("文件夹名称")
    private String name;

    /**
     * 实体转换
     *
     * @param record
     * @return
     */
    public static BreadcrumbVO transfer(UserFile record) {
        BreadcrumbVO vo = new BreadcrumbVO();

        if (Objects.nonNull(record)) {
            vo.setId(record.getFileId());
            vo.setParentId(record.getParentId());
            vo.setName(record.getFilename());
        }

        return vo;
    }
}
