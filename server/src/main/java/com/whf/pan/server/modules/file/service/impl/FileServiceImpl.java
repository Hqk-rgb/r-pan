package com.whf.pan.server.modules.file.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.whf.pan.server.modules.file.entity.File;
import com.whf.pan.server.modules.file.service.IFileService;
import com.whf.pan.server.modules.file.mapper.FileMapper;
import org.springframework.stereotype.Service;

/**
* @author 26570
* @description 针对表【r_pan_file(物理文件信息表)】的数据库操作Service实现
* @createDate 2023-10-28 15:45:30
*/
@Service
public class FileServiceImpl extends ServiceImpl<FileMapper, File>
    implements IFileService {

}




