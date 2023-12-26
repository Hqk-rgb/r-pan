package com.whf.pan.server.modules.user.service;

import com.whf.pan.server.modules.user.context.QueryUserSearchHistoryContext;
import com.whf.pan.server.modules.user.entity.UserSearchHistory;
import com.baomidou.mybatisplus.extension.service.IService;
import com.whf.pan.server.modules.user.vo.UserSearchHistoryVO;

import java.util.List;

/**
* @author 26570
* @description 针对表【r_pan_user_search_history(用户搜索历史表)】的数据库操作Service
* @createDate 2023-10-28 15:41:24
*/
public interface IUserSearchHistoryService extends IService<UserSearchHistory> {

    /**
     * 查询用户的搜索历史记录，默认十条
     *
     * @param context
     * @return
     */
    List<UserSearchHistoryVO> getUserSearchHistories(QueryUserSearchHistoryContext context);

}
