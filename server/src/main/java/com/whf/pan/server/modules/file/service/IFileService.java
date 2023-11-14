package com.whf.pan.server.modules.file.service;

import com.whf.pan.server.modules.file.context.FileChunkMergeAndSaveContext;
import com.whf.pan.server.modules.file.context.FileSaveContext;
import com.whf.pan.server.modules.file.entity.File;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author 26570
* @description 针对表【r_pan_file(物理文件信息表)】的数据库操作Service
* @createDate 2023-10-28 15:45:30
*/
public interface IFileService extends IService<File> {

    /**
     * 根据条件查询用户的实际文件列表
     *
     * @param context
     * @return
     */
//    List<File> getFileList(QueryRealFileListContext context);

    /**
     * 上传单文件并保存实体记录
     *
     * @param context
     */
    void saveFile(FileSaveContext context);

    /**
     * 合并物理文件并保存物理文件记录
     *
     * @param context
     */
    void mergeFileChunkAndSaveFile(FileChunkMergeAndSaveContext context);

}
