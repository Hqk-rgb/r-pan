package com.whf.pan.server.modules.file.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.whf.pan.server.modules.file.entity.UserFile;
import com.whf.pan.server.modules.file.service.UserFileService;
import com.whf.pan.server.modules.file.mapper.UserFileMapper;
import org.springframework.stereotype.Service;

/**
* @author 26570
* @description 针对表【r_pan_user_file(用户文件信息表)】的数据库操作Service实现
* @createDate 2023-10-28 15:45:30
*/
@Service
public class UserFileServiceImpl extends ServiceImpl<UserFileMapper, UserFile>
    implements UserFileService{

}




