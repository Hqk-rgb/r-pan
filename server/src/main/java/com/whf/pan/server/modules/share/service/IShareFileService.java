package com.whf.pan.server.modules.share.service;

import com.whf.pan.server.modules.share.context.SaveShareFilesContext;
import com.whf.pan.server.modules.share.entity.ShareFile;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 26570
* @description 针对表【r_pan_share_file(用户分享文件表)】的数据库操作Service
* @createDate 2023-10-28 15:48:20
*/
public interface IShareFileService extends IService<ShareFile> {

    /**
     * 保存分享的文件的对应关系
     *
     * @param context
     */
    void saveShareFiles(SaveShareFilesContext context);

}
