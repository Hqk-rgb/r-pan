package com.whf.pan.server.modules.file.service;

import com.whf.pan.server.modules.file.entity.FileChunk;
import com.baomidou.mybatisplus.extension.service.IService;
import com.whf.pan.server.modules.file.context.*;

/**
* @author 26570
* @description 针对表【r_pan_file_chunk(文件分片信息表)】的数据库操作Service
* @createDate 2023-10-28 15:45:30
*/
public interface IFileChunkService extends IService<FileChunk> {


    /**
     * 文件分片保存
     *
     * @param context
     */
    void saveChunkFile(FileChunkSaveContext context);

}
