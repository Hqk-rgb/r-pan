package com.whf.pan.server.modules.file.service;

import com.whf.pan.server.modules.file.context.CreateFolderContext;
import com.whf.pan.server.modules.file.entity.UserFile;
import com.baomidou.mybatisplus.extension.service.IService;

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

}
