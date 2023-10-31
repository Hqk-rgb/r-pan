package com.whf.pan.server.modules.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.whf.pan.server.modules.user.entity.UserSearchHistory;
import com.whf.pan.server.modules.user.service.IUserSearchHistoryService;
import com.whf.pan.server.modules.user.mapper.UserSearchHistoryMapper;
import org.springframework.stereotype.Service;

/**
* @author 26570
* @description 针对表【r_pan_user_search_history(用户搜索历史表)】的数据库操作Service实现
* @createDate 2023-10-28 15:41:24
*/
@Service(value =  "userSearchHistoryService")
public class UserSearchHistoryServiceImpl extends ServiceImpl<UserSearchHistoryMapper, UserSearchHistory>
    implements IUserSearchHistoryService {

}




