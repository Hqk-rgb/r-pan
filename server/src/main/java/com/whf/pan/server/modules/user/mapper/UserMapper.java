package com.whf.pan.server.modules.user.mapper;

import com.whf.pan.server.modules.user.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
* @author 26570
* @description 针对表【r_pan_user(用户信息表)】的数据库操作Mapper
* @createDate 2023-10-28 15:41:24
* @Entity com.whf.pan.server.modules.user.entity.User
*/
public interface UserMapper extends BaseMapper<User> {
    /**
     * 通过用户名称查询用户设置的密保问题
     * @param username
     * @return
     */

    String selectQuestionByUsername(@Param("username") String username);
}




