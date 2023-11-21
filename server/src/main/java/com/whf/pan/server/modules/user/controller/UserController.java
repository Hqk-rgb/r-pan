package com.whf.pan.server.modules.user.controller;

import com.whf.pan.core.response.R;
import com.whf.pan.core.utils.IdUtil;
import com.whf.pan.server.common.annotation.LoginIgnore;
import com.whf.pan.server.common.utils.UserIdUtil;
import com.whf.pan.server.modules.user.context.*;
import com.whf.pan.server.modules.user.converter.UserConverter;
import com.whf.pan.server.modules.user.po.*;
import com.whf.pan.server.modules.user.service.IUserService;
import com.whf.pan.server.modules.user.vo.UserInfoVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author whf
 * @className UserController
 * @description 用户模块的控制器实体
 * @date 2023/10/30 12:16
 */
@RestController
@RequestMapping("/user")
@Api(tags = "用户模块")
public class UserController {
    @Resource
    private IUserService userService;

    @Resource
    private UserConverter userConverter;

    @LoginIgnore
    @ApiOperation(
            value = "用户注册",
            notes = "该接口提供了用户注册的功能，实现幂等性注册的逻辑，放心多并发调用",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    @PostMapping("/register")
    public R register(@Validated @RequestBody UserRegisterPO userRegisterPO){
        UserRegisterContext userRegisterContext = userConverter.userRegisterPOToUserRegisterContext(userRegisterPO);
        Long userId = userService.register(userRegisterContext);
        return R.success("注册成功",IdUtil.encrypt(userId));
        // return R.data(IdUtil.encrypt(userId));
    }

    @LoginIgnore
    @ApiOperation(
            value = "用户登录",
            notes = "该接口提供了用户登录的功能，成功登录后，会返回有时效性的 accessToken 供后续服务使用",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    @PostMapping("/login")
    public R login(@Validated @RequestBody UserLoginPO userLoginPO){
        UserLoginContext userLoginContext = userConverter.userLoginPOToUserLoginContext(userLoginPO);
        String accessToken = userService.login(userLoginContext);
        return R.success("登录成功",accessToken);
    }

    @ApiOperation(
            value = "用户登出",
            notes = "该接口提供了用户登出的功能",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    @PostMapping("/exit")
    public R exit(){
        userService.exit(UserIdUtil.get());
        return R.success("成功退出");
    }

    @ApiOperation(
            value = "用户忘记密码-校验用户名",
            notes = "该接口提供了用户忘记密码-校验用户名的功能",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    @LoginIgnore
    @PostMapping("username/check")
    public R checkUsername(@Validated @RequestBody CheckUsernamePO checkUsernamePO){
        CheckUsernameContext context = userConverter.checkUsernamePOToCheckUsernameContext(checkUsernamePO);
        String question = userService.checkUsername(context);
        return R.success("校验用户名成功",question);
    }

    @ApiOperation(
            value = "用户忘记密码-校验密保答案",
            notes = "该接口提供了用户忘记密码-校验密保答案的功能",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    @LoginIgnore
    @PostMapping("answer/check")
    public R checkAnswer(@Validated @RequestBody CheckAnswerPO checkAnswerPO) {
        CheckAnswerContext checkAnswerContext = userConverter.checkAnswerPOToCheckAnswerContext(checkAnswerPO);
        String token = userService.checkAnswer(checkAnswerContext);
        return R.success("校验密保答案成功",token);
    }

    @ApiOperation(
            value = "用户忘记密码-重置新密码",
            notes = "该接口提供了用户忘记密码-重置新密码的功能",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    @PostMapping("password/reset")
    @LoginIgnore
    public R resetPassword(@Validated @RequestBody ResetPasswordPO resetPasswordPO) {
        ResetPasswordContext resetPasswordContext = userConverter.resetPasswordPOTOResetPasswordContext(resetPasswordPO);
        userService.resetPassword(resetPasswordContext);
        return R.success("重置新密码成功");
    }

    @ApiOperation(
            value = "用户在线修改密码",
            notes = "该接口提供了用户在线修改密码的功能",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    @PostMapping("password/change")
    public R changePassword(@Validated @RequestBody ChangePasswordPO changePasswordPO) {
        ChangePasswordContext changePasswordContext = userConverter.changePasswordPOTOChangePasswordContext(changePasswordPO);
        changePasswordContext.setUserId(UserIdUtil.get());
        userService.changePassword(changePasswordContext);
        return R.success();
    }

    @ApiOperation(
            value = "查询登录用户的基本信息",
            notes = "该接口提供了查询登录用户的基本信息的功能",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    @GetMapping("/")
    public R<UserInfoVO> info() {
        UserInfoVO userInfoVO = userService.info(UserIdUtil.get());
        return R.data(userInfoVO);
    }
}
