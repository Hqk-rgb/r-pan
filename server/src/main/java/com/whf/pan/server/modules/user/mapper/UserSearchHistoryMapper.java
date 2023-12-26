package com.whf.pan.server.modules.user.mapper;

import com.whf.pan.server.modules.user.context.QueryUserSearchHistoryContext;
import com.whf.pan.server.modules.user.entity.UserSearchHistory;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.whf.pan.server.modules.user.vo.UserSearchHistoryVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author 26570
* @description 针对表【r_pan_user_search_history(用户搜索历史表)】的数据库操作Mapper
* @createDate 2023-10-28 15:41:24
* @Entity com.whf.pan.server.modules.user.entity.UserSearchHistory
*/
public interface UserSearchHistoryMapper extends BaseMapper<UserSearchHistory> {

    /**
     * 查询用户的最近十条搜索历史记录
     *
     * @param context
     * @return
     */
    List<UserSearchHistoryVO> selectUserSearchHistories(@Param("param") QueryUserSearchHistoryContext context);

}




