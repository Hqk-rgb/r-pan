package com.whf.pan.server.modules.file.service;

import com.whf.pan.server.modules.file.context.*;
import com.whf.pan.server.modules.file.entity.UserFile;
import com.baomidou.mybatisplus.extension.service.IService;
import com.whf.pan.server.modules.file.vo.FileChunkUploadVO;
import com.whf.pan.server.modules.file.vo.UploadedChunksVO;
import com.whf.pan.server.modules.file.vo.UserFileVO;

import java.util.List;

/**
* @author 26570
* @description 针对表【r_pan_user_file(用户文件信息表)】的数据库操作Service
* @createDate 2023-10-28 15:45:30
*/
public interface IUserFileService extends IService<UserFile> {

    /** 创建文件夹信息
     * @param createFolderContext
     * @return
     */
    Long createFolder(CreateFolderContext createFolderContext);
    /**
     * 查询用户的根文件夹信息
     *
     * @param userId
     * @return
     */
    UserFile getUserRootFile(Long userId);

    /**
     * 查询用户的文件列表
     * @param context
     * @return
     */
    List<UserFileVO> getFileList(QueryFileListContext context);

    /**
     * 更新文件名称
     *
     * @param context
     */
    void updateFilename(UpdateFilenameContext context);

    /**
     * 批量删除用户文件
     *
     * @param context
     */
    void deleteFile(DeleteFileContext context);

    /**
     * 文件秒传功能
     *
     * @param context
     * @return
     */
    boolean secUpload(SecUploadFileContext context);

    /**
     * 单文件上传
     *
     * @param context
     */
    void upload(FileUploadContext context);

    /**
     * 文件分片上传
     *
     * @param context
     * @return
     */
    FileChunkUploadVO chunkUpload(FileChunkUploadContext context);

    /**
     * 查询用户已上传的分片列表
     *
     * @param context
     * @return
     */
    UploadedChunksVO getUploadedChunks(QueryUploadedChunksContext context);
}
