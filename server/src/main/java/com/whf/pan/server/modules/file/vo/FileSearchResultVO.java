package com.whf.pan.server.modules.file.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.whf.pan.web.serializer.Date2StringSerializer;
import com.whf.pan.web.serializer.IdEncryptSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author whf
 * @className FileSearchResultVO
 * @description 用户搜索文件列表相应实体
 * @date 2023/11/19 13:26
 */
@Data
@ApiModel(value = "用户搜索文件列表相应实体")
public class FileSearchResultVO implements Serializable {

    private static final long serialVersionUID = -2314434960486636645L;

    @JsonSerialize(using = IdEncryptSerializer.class)
    @ApiModelProperty(value = "文件ID")
    private Long fileId;

    @JsonSerialize(using = IdEncryptSerializer.class)
    @ApiModelProperty(value = "父文件夹ID")
    private Long parentId;

    @ApiModelProperty(value = "父文件夹名称")
    private String parentFilename;

    @ApiModelProperty(value = "文件名称")
    private String filename;

    @ApiModelProperty(value = "文件大小描述")
    private String fileSizeDesc;

    @ApiModelProperty(value = "文件夹标识 0 否 1 是")
    private Integer folderFlag;

    @ApiModelProperty(value = "文件类型 1 普通文件 2 压缩文件 3 excel 4 word 5 pdf 6 txt 7 图片 8 音频 9 视频 10 ppt 11 源码文件 12 csv")
    private Integer fileType;

    @ApiModelProperty(value = "文件更新时间")
    @JsonSerialize(using = Date2StringSerializer.class)
    private Date updateTime;
}
