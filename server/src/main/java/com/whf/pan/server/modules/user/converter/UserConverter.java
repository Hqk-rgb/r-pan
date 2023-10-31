package com.whf.pan.server.modules.user.converter;

import com.whf.pan.server.modules.user.context.*;
import com.whf.pan.server.modules.user.entity.User;
import com.whf.pan.server.modules.user.po.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * @author whf
 * @className UserConverter
 * @description TODO
 * @date 2023/10/30 13:38
 */
@Mapper(componentModel = "spring")
public interface UserConverter {

    /** 将 UserRegisterPO 转化成 UserRegisterContext
     * @param userRegisterPO
     * @return null
     */
    UserRegisterContext userRegisterPOToUserRegisterContext(UserRegisterPO userRegisterPO);

    /** 将 UserRegisterContext 转化成 User
     * @param userRegisterContext
     * @return null
     */
    @Mapping(target = "password", ignore = true)
    User userRegisterContextToUser(UserRegisterContext userRegisterContext);

    /** 将 UserLoginPO 转化成 UserLoginContext
     * @param userLoginPO
     * @return null
     */
    UserLoginContext userLoginPOToUserLoginContext(UserLoginPO userLoginPO);

    /** 将 CheckUsernamePO 转化成 CheckUsernameContext
     * @param checkUsernamePO
     * @return null
     */
    CheckUsernameContext checkUsernamePOToCheckUsernameContext(CheckUsernamePO checkUsernamePO);

    /** 将 CheckAnswerPO 转化成 CheckUsernameContext
     * @param checkAnswerPO
     * @return null
     */
    CheckAnswerContext checkAnswerPOToCheckAnswerContext(CheckAnswerPO checkAnswerPO);
    /**
     * ResetPasswordPO 转化成 ResetPasswordContext
     *
     * @param resetPasswordPO
     * @return
     */
    ResetPasswordContext resetPasswordPOTOResetPasswordContext(ResetPasswordPO resetPasswordPO);

    /**
     * ChangePasswordPO 转化成 ChangePasswordContext
     *
     * @param changePasswordPO
     * @return
     */
    ChangePasswordContext changePasswordPOTOChangePasswordContext(ChangePasswordPO changePasswordPO);

}
