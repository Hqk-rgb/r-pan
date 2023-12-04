package com.whf.pan.server.modules.share.converter;

import com.whf.pan.server.modules.share.context.CreateShareUrlContext;
import com.whf.pan.server.modules.share.po.CreateShareUrlPO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * @author whf
 * @className ShareConverter
 * @description 分享模块实体转化工具类
 * @date 2023/12/4 14:14
 */
@Mapper(componentModel = "spring")
public interface ShareConverter {

    @Mapping(target = "userId", expression = "java(com.whf.pan.server.common.utils.UserIdUtil.get())")
    CreateShareUrlContext createShareUrlPOTOCreateShareUrlContext(CreateShareUrlPO createShareUrlPO);

}
