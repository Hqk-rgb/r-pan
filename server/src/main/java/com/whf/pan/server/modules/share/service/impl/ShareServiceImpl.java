package com.whf.pan.server.modules.share.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.whf.pan.server.modules.share.entity.Share;
import com.whf.pan.server.modules.share.service.ShareService;
import com.whf.pan.server.modules.share.mapper.ShareMapper;
import org.springframework.stereotype.Service;

/**
* @author 26570
* @description 针对表【r_pan_share(用户分享表)】的数据库操作Service实现
* @createDate 2023-10-28 15:48:20
*/
@Service
public class ShareServiceImpl extends ServiceImpl<ShareMapper, Share>
    implements ShareService{

}




