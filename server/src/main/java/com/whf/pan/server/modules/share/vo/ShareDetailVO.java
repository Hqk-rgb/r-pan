package com.whf.pan.server.modules.share.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.whf.pan.server.modules.file.vo.UserFileVO;
import com.whf.pan.web.serializer.Date2StringSerializer;
import com.whf.pan.web.serializer.IdEncryptSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author whf
 * @className ShareDetailVO
 * @description TODO
 * @date 2023/12/5 16:03
 */
@ApiModel("分享详情的返回实体对象")
@Data
public class ShareDetailVO implements Serializable {

    private static final long serialVersionUID = -5614881582917275468L;

    @JsonSerialize(using = IdEncryptSerializer.class)
    @ApiModelProperty("分享的ID")
    private Long shareId;

    @ApiModelProperty("分享的名称")
    private String shareName;

    @ApiModelProperty("分享的创建时间")
    @JsonSerialize(using = Date2StringSerializer.class)
    private Date createTime;

    @ApiModelProperty("分享的过期类型")
    private Integer shareDay;

    @ApiModelProperty("分享的截止时间")
    @JsonSerialize(using = Date2StringSerializer.class)
    private Date shareEndTime;

    @ApiModelProperty("分享的文件列表")
    private List<UserFileVO> rPanUserFileVOList;

    @ApiModelProperty("分享者的信息")
    private ShareUserInfoVO shareUserInfoVO;
}
