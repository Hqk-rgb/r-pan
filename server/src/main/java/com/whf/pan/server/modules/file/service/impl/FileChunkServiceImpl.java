package com.whf.pan.server.modules.file.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.whf.pan.server.modules.file.entity.FileChunk;
import com.whf.pan.server.modules.file.service.FileChunkService;
import com.whf.pan.server.modules.file.mapper.FileChunkMapper;
import org.springframework.stereotype.Service;

/**
* @author 26570
* @description 针对表【r_pan_file_chunk(文件分片信息表)】的数据库操作Service实现
* @createDate 2023-10-28 15:45:30
*/
@Service
public class FileChunkServiceImpl extends ServiceImpl<FileChunkMapper, FileChunk>
    implements FileChunkService{

}




