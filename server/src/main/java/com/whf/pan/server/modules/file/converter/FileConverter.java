package com.whf.pan.server.modules.file.converter;

import com.whf.pan.server.modules.file.context.CreateFolderContext;
import com.whf.pan.server.modules.file.context.UpdateFilenameContext;
import com.whf.pan.server.modules.file.po.CreateFolderPO;
import com.whf.pan.server.modules.file.po.UpdateFilenamePO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * @author whf
 * @className FileConverter
 * @description 文件模块实体转化工具类
 * @date 2023/11/3 14:06
 */
@Mapper(componentModel = "spring")
public interface FileConverter {

    @Mapping(target = "parentId", expression = "java(com.whf.pan.core.utils.IdUtil.decrypt(createFolderPO.getParentId()))")
    @Mapping(target = "userId", expression = "java(com.whf.pan.server.common.utils.UserIdUtil.get())")
    CreateFolderContext createFolderPOTOCreateFolderContext(CreateFolderPO createFolderPO);

    @Mapping(target = "fileId", expression = "java(com.whf.pan.core.utils.IdUtil.decrypt(updateFilenamePO.getFileId()))")
    @Mapping(target = "userId", expression = "java(com.whf.pan.server.common.utils.UserIdUtil.get())")
    UpdateFilenameContext updateFilenamePOTOUpdateFilenameContext(UpdateFilenamePO updateFilenamePO);

}
