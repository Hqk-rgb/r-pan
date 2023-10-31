package com.whf.pan.server.modules.user.controller;

import com.whf.pan.core.response.R;
import com.whf.pan.core.utils.IdUtil;
import com.whf.pan.server.common.annotation.LoginIgnore;
import com.whf.pan.server.common.utils.UserIdUtil;
import com.whf.pan.server.modules.user.context.UserLoginContext;
import com.whf.pan.server.modules.user.context.UserRegisterContext;
import com.whf.pan.server.modules.user.converter.UserConverter;
import com.whf.pan.server.modules.user.po.UserLoginPO;
import com.whf.pan.server.modules.user.po.UserRegisterPO;
import com.whf.pan.server.modules.user.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
