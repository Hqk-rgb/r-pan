package com.whf.pan.server.modules.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.whf.pan.server.modules.user.entity.User;
import com.whf.pan.server.modules.user.service.UserService;
import com.whf.pan.server.modules.user.mapper.UserMapper;
import org.springframework.stereotype.Service;

/**
* @author 26570
* @description 针对表【r_pan_user(用户信息表)】的数据库操作Service实现
* @createDate 2023-10-28 15:41:24
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

}




