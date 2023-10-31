package com.whf.pan.server.modules.user;

import cn.hutool.core.lang.Assert;
import com.whf.pan.core.exception.BusinessException;
import com.whf.pan.core.utils.JwtUtil;
import com.whf.pan.server.PanLauncher;
import com.whf.pan.server.modules.user.constants.UserConstants;
import com.whf.pan.server.modules.user.context.UserLoginContext;
import com.whf.pan.server.modules.user.context.UserRegisterContext;
import com.whf.pan.server.modules.user.service.IUserService;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author whf
 * @className UserTest
 * @description TODO
 * @date 2023/10/30 17:32
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = PanLauncher.class)
@Transactional
public class UserTest {

    private final static String USERNAME = "Joker";
    private final static String PASSWORD = "123456789";
    private final static String QUESTION = "question";
    private final static String ANSWER = "answer";

    @Resource
    private IUserService userService;

    /**
     * 测试成功注册用户信息
     */
    @Test
    public void testRegisterUser(){
        UserRegisterContext context = createUserRegisterContext();
        Long register = userService.register(context);
        Assert.isTrue(register.longValue() > 0L);
    }

    /**
     * 测试重复用户名称注册
     */
    @Test(expected = BusinessException.class)
    public void testRegisterDuplicateUsername(){
        UserRegisterContext context = createUserRegisterContext();
        Long register = userService.register(context);
        Assert.isTrue(register.longValue() > 0L);
        userService.register(context);
    }

    /**
     * 构建注册用户上下文信息
     *
     * @return context
     */
    private UserRegisterContext createUserRegisterContext() {
        UserRegisterContext context = new UserRegisterContext();
        context.setUsername(USERNAME);
        context.setPassword(PASSWORD);
        context.setQuestion(QUESTION);
        context.setAnswer(ANSWER);
        return context;
    }
    /********************************************用户登录********************************************************/

    /**
     * 测试登录成功
     */
    @Test
    public void loginSuccess(){
        UserRegisterContext context = createUserRegisterContext();
        Long register = userService.register(context);
        Assert.isTrue(register.longValue() > 0L);

        UserLoginContext userLoginContext = createUserLoginContext();
        String accessToken = userService.login(userLoginContext);

        Assert.isTrue(StringUtils.isNotBlank(accessToken));

    }

    /**
     * 测试登录失败：用户名不正确
     */
    @Test(expected = BusinessException.class)
    public void wrongUsername(){
        //先注册
        UserRegisterContext registerContext = createUserRegisterContext();
        Long register = userService.register(registerContext);
        Assert.isTrue(register.longValue() > 0L);
        //后登录
        UserLoginContext context = createUserLoginContext();
        context.setUsername(context.getUsername()+"_change");
        userService.login(context);

    }
    /**
     * 测试登录成功：密码错误
     */
    @Test(expected = BusinessException.class)
    public void wrongPassword(){
        //先注册
        UserRegisterContext registerContext = createUserRegisterContext();
        Long register = userService.register(registerContext);
        Assert.isTrue(register.longValue() > 0L);
        //后登录
        UserLoginContext context = createUserLoginContext();
        context.setPassword(context.getPassword()+"_change");
        userService.login(context);

    }
    /**
     * 构建登录用户上下文信息
     *
     * @return context
     */
    private UserLoginContext createUserLoginContext() {
        UserLoginContext context = new UserLoginContext();
        context.setUsername(USERNAME);
        context.setPassword(PASSWORD);
        return context;
    }

    /***********************************************用户成功退出登录*********************************************************/

    /**
     * 用户成功登出
     */
    @Test
    public void exitSuccess() {
        UserRegisterContext context = createUserRegisterContext();
        Long register = userService.register(context);
        Assert.isTrue(register.longValue() > 0L);

        UserLoginContext userLoginContext = createUserLoginContext();
        String accessToken = userService.login(userLoginContext);

        Assert.isTrue(StringUtils.isNotBlank(accessToken));

        Long userId = (Long) JwtUtil.analyzeToken(accessToken, UserConstants.LOGIN_USER_ID);

        userService.exit(userId);
    }

}