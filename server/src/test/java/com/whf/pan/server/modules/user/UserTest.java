package com.whf.pan.server.modules.user;

import cn.hutool.core.lang.Assert;
import com.whf.pan.core.exception.BusinessException;
import com.whf.pan.core.utils.JwtUtil;
import com.whf.pan.server.PanLauncher;
import com.whf.pan.server.modules.user.constants.UserConstants;
import com.whf.pan.server.modules.user.context.*;
import com.whf.pan.server.modules.user.service.IUserService;
import com.whf.pan.server.modules.user.vo.UserInfoVO;
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

    private final static String USERNAME = "Faker";
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
    /**************************************************用户忘记密码-校验用户名*****************************************************/

    /**
     * 校验用户名称通过
     */
    @Test
    public void checkUsernameSuccess() {
        UserRegisterContext context = createUserRegisterContext();
        Long register = userService.register(context);
        Assert.isTrue(register.longValue() > 0L);

        CheckUsernameContext checkUsernameContext = new CheckUsernameContext();
        checkUsernameContext.setUsername(USERNAME);
        String question = userService.checkUsername(checkUsernameContext);
        Assert.isTrue(StringUtils.isNotBlank(question));;
    }

    /**
     * 校验用户名称不存在
     */
    @Test(expected = BusinessException.class)
    public void checkUsernameNotExist(){
        UserRegisterContext context = createUserRegisterContext();
        Long register = userService.register(context);
        Assert.isTrue(register.longValue() > 0L);

        CheckUsernameContext checkUsernameContext = new CheckUsernameContext();
        checkUsernameContext.setUsername(USERNAME+"_change");
        userService.checkUsername(checkUsernameContext);
    }

    /**************************************************用户忘记密码-校验用户名*****************************************************/

    /**
     * 校验密保答案通过
     */
    @Test
    public void checkAnswerSuccess() {
        UserRegisterContext context = createUserRegisterContext();
        Long register = userService.register(context);
        Assert.isTrue(register.longValue() > 0L);

        CheckAnswerContext checkAnswerContext = new CheckAnswerContext();
        checkAnswerContext.setUsername(USERNAME);
        checkAnswerContext.setQuestion(QUESTION);
        checkAnswerContext.setAnswer(ANSWER);

        String token = userService.checkAnswer(checkAnswerContext);
        Assert.isTrue(StringUtils.isNotBlank(token));
    }

    /**
     * 校验密保答案失败
     */
    @Test(expected = BusinessException.class)
    public void checkAnswerFail(){
        UserRegisterContext context = createUserRegisterContext();
        Long register = userService.register(context);
        Assert.isTrue(register.longValue() > 0L);

        CheckAnswerContext checkAnswerContext = new CheckAnswerContext();
        checkAnswerContext.setUsername(USERNAME);
        checkAnswerContext.setQuestion(QUESTION);
        checkAnswerContext.setAnswer(ANSWER + "_change");

        userService.checkAnswer(checkAnswerContext);

    }

    /**************************************************用户忘记密码-重置密码*****************************************************/


    /**
     * 正常重置用户密码
     */
    @Test
    public void resetPasswordSuccess() {
        UserRegisterContext context = createUserRegisterContext();
        Long register = userService.register(context);
        Assert.isTrue(register.longValue() > 0L);

        CheckAnswerContext checkAnswerContext = new CheckAnswerContext();
        checkAnswerContext.setUsername(USERNAME);
        checkAnswerContext.setQuestion(QUESTION);
        checkAnswerContext.setAnswer(ANSWER);

        String token = userService.checkAnswer(checkAnswerContext);

        Assert.isTrue(StringUtils.isNotBlank(token));

        ResetPasswordContext resetPasswordContext = new ResetPasswordContext();
        resetPasswordContext.setUsername(USERNAME);
        resetPasswordContext.setPassword(PASSWORD + "_change");
        resetPasswordContext.setToken(token);

        userService.resetPassword(resetPasswordContext);
    }

    /**
     * 用户重置密码失败-token异常
     */
    @Test(expected = BusinessException.class)
    public void resetPasswordTokenError() {
        UserRegisterContext context = createUserRegisterContext();
        Long register = userService.register(context);
        Assert.isTrue(register.longValue() > 0L);

        CheckAnswerContext checkAnswerContext = new CheckAnswerContext();
        checkAnswerContext.setUsername(USERNAME);
        checkAnswerContext.setQuestion(QUESTION);
        checkAnswerContext.setAnswer(ANSWER);

        String token = userService.checkAnswer(checkAnswerContext);

        Assert.isTrue(StringUtils.isNotBlank(token));

        ResetPasswordContext resetPasswordContext = new ResetPasswordContext();
        resetPasswordContext.setUsername(USERNAME);
        resetPasswordContext.setPassword(PASSWORD + "_change");
        resetPasswordContext.setToken(token + "_change");

        userService.resetPassword(resetPasswordContext);
    }

/**************************************************用户在线修改密码*****************************************************/

/**
     * 正常在线修改密码
     */
    @Test
    public void changePasswordSuccess() {
        UserRegisterContext context = createUserRegisterContext();
        Long register = userService.register(context);
        Assert.isTrue(register.longValue() > 0L);

        ChangePasswordContext changePasswordContext = new ChangePasswordContext();

        changePasswordContext.setUserId(register);
        changePasswordContext.setOldPassword(PASSWORD);
        changePasswordContext.setNewPassword(PASSWORD + "_change");

        userService.changePassword(changePasswordContext);
    }

    /**
     * 修改密码失败-旧密码错误
     */
    @Test(expected = BusinessException.class)
    public void changePasswordFailByWrongOldPassword() {
        UserRegisterContext context = createUserRegisterContext();
        Long register = userService.register(context);
        Assert.isTrue(register.longValue() > 0L);

        ChangePasswordContext changePasswordContext = new ChangePasswordContext();

        changePasswordContext.setUserId(register);
        changePasswordContext.setOldPassword(PASSWORD + "_change");
        changePasswordContext.setNewPassword(PASSWORD + "_change");

        userService.changePassword(changePasswordContext);
    }
    /*****************************************查询在线用户基本信息*******************************************************************/
    @Test
    public void testQueryUserInfo() {

        UserRegisterContext context = createUserRegisterContext();
        Long register = userService.register(context);
        Assert.isTrue(register.longValue() > 0L);

        UserInfoVO userInfoVO = userService.info(register);
        Assert.notNull(userInfoVO);
    }

}