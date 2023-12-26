package com.whf.pan.server.modules.user.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author whf
 * @className UserSearchHistoryVO
 * @description TODO
 * @date 2023/12/26 20:03
 */
@ApiModel("用户搜索历史返回实体")
@Data
public class UserSearchHistoryVO implements Serializable {

    private static final long serialVersionUID = 7821800705999214189L;

    @ApiModelProperty("搜索文案")
    private String value;
}
