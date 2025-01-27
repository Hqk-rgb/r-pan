package com.whf.pan.server.modules.user.service;

import com.whf.pan.server.modules.user.context.*;
import com.whf.pan.server.modules.user.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.whf.pan.server.modules.user.vo.UserInfoVO;

/**
* @author 26570
* @description 针对表【r_pan_user(用户信息表)】的数据库操作Service
* @createDate 2023-10-28 15:41:24
*/
public interface IUserService extends IService<User> {
    /**
     * 用户注册业务
     *
     * @param userRegisterContext
     * @return
     */
    Long register(UserRegisterContext userRegisterContext);

    /**
     * 用户登录业务
     *
     * @param userLoginContext
     * @return
     */
    String login (UserLoginContext userLoginContext);

    /**
     * 用户退出登录
     *
     * @param userId
     * @return
     */
    void exit(Long userId);

    /**
     * 校验用户名
     *
     * @param context
     * @return
     */
    String checkUsername(CheckUsernameContext context);

    /**
     * 用户忘记密码-校验密保答案
     *
     * @param checkAnswerContext
     * @return
     */
    String checkAnswer(CheckAnswerContext checkAnswerContext);

    /**
     * 重置用户密码
     *
     * @param resetPasswordContext
     * @return
     */
    void resetPassword(ResetPasswordContext resetPasswordContext);

    /**
     * 在线修改密码
     *
     * @param changePasswordContext
     */
    void changePassword(ChangePasswordContext changePasswordContext);

    /**
     * 查询在线用户的基本信息
     *
     * @param userId
     * @return
     */
    UserInfoVO info(Long userId);
}
