package com.whf.pan.server.modules.share.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.whf.pan.core.exception.BusinessException;
import com.whf.pan.core.utils.IdUtil;
import com.whf.pan.server.modules.share.context.SaveShareFilesContext;
import com.whf.pan.server.modules.share.entity.ShareFile;
import com.whf.pan.server.modules.share.service.IShareFileService;
import com.whf.pan.server.modules.share.mapper.ShareFileMapper;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
* @author 26570
* @description 针对表【r_pan_share_file(用户分享文件表)】的数据库操作Service实现
* @createDate 2023-10-28 15:48:20
*/
@Service
public class ShareFileServiceImpl extends ServiceImpl<ShareFileMapper, ShareFile>
    implements IShareFileService {

    /**
     * 保存分享的文件的对应关系
     *
     * @param context
     */
    @Override
    public void saveShareFiles(SaveShareFilesContext context) {
        Long shareId = context.getShareId();
        List<Long> shareFileIdList = context.getShareFileIdList();
        Long userId = context.getUserId();

        List<ShareFile> records = Lists.newArrayList();

        for (Long shareFileId : shareFileIdList) {
            ShareFile record = new ShareFile();
            record.setId(IdUtil.get());
            record.setShareId(shareId);
            record.setFileId(shareFileId);
            record.setCreateUser(userId);
            record.setCreateTime(new Date());
            records.add(record);
        }

        if (!saveBatch(records)) {
            throw new BusinessException("保存文件分享关联关系失败");
        }
    }

}




