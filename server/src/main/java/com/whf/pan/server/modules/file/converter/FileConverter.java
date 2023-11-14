package com.whf.pan.server.modules.file.converter;

import com.whf.pan.server.modules.file.context.*;
import com.whf.pan.server.modules.file.po.*;
import com.whf.pan.storage.engine.core.context.StoreFileChunkContext;
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

    @Mapping(target = "userId", expression = "java(com.whf.pan.server.common.utils.UserIdUtil.get())")
    DeleteFileContext deleteFilePOTODeleteFileContext(DeleteFilePO deleteFilePO);

    @Mapping(target = "parentId", expression = "java(com.whf.pan.core.utils.IdUtil.decrypt(secUploadFilePO.getParentId()))")
    @Mapping(target = "userId", expression = "java(com.whf.pan.server.common.utils.UserIdUtil.get())")
    SecUploadFileContext secUploadFilePOTOSecUploadFileContext(SecUploadFilePO secUploadFilePO);

    @Mapping(target = "parentId", expression = "java(com.whf.pan.core.utils.IdUtil.decrypt(fileUploadPO.getParentId()))")
    @Mapping(target = "userId", expression = "java(com.whf.pan.server.common.utils.UserIdUtil.get())")
    FileUploadContext fileUploadPOTOFileUploadContext(FileUploadPO fileUploadPO);

    @Mapping(target = "record", ignore = true)
    FileSaveContext fileUploadContextTOFileSaveContext(FileUploadContext context);

    @Mapping(target = "userId", expression = "java(com.whf.pan.server.common.utils.UserIdUtil.get())")
    FileChunkUploadContext fileChunkUploadPOTOFileChunkUploadContext(FileChunkUploadPO fileChunkUploadPO);

    FileChunkSaveContext fileChunkUploadContextTOFileChunkSaveContext(FileChunkUploadContext context);

    @Mapping(target = "realPath",ignore = true)
    StoreFileChunkContext fileChunkSaveContextTOStoreFileChunkContext(FileChunkSaveContext context);

    @Mapping(target = "userId", expression = "java(com.whf.pan.server.common.utils.UserIdUtil.get())")
    QueryUploadedChunksContext queryUploadedChunksPOTOQueryUploadedChunksContext(QueryUploadedChunksPO queryUploadedChunksPO);

    @Mapping(target = "userId", expression = "java(com.whf.pan.server.common.utils.UserIdUtil.get())")
    @Mapping(target = "parentId", expression = "java(com.whf.pan.core.utils.IdUtil.decrypt(fileChunkMergePO.getParentId()))")
    FileChunkMergeContext fileChunkMergePOTOFileChunkMergeContext(FileChunkMergePO fileChunkMergePO);

    FileChunkMergeAndSaveContext fileChunkMergeContextTOFileChunkMergeAndSaveContext(FileChunkMergeContext context);
}
