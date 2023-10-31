package com.whf.pan.server.modules.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.whf.pan.cache.core.constants.CacheConstants;
import com.whf.pan.core.exception.BusinessException;
import com.whf.pan.core.response.ResponseCode;
import com.whf.pan.core.utils.IdUtil;
import com.whf.pan.core.utils.JwtUtil;
import com.whf.pan.core.utils.PasswordUtil;
import com.whf.pan.server.modules.file.constants.FileConstants;
import com.whf.pan.server.modules.file.context.CreateFolderContext;
import com.whf.pan.server.modules.file.service.IUserFileService;
import com.whf.pan.server.modules.user.constants.UserConstants;
import com.whf.pan.server.modules.user.context.UserLoginContext;
import com.whf.pan.server.modules.user.context.UserRegisterContext;
import com.whf.pan.server.modules.user.converter.UserConverter;
import com.whf.pan.server.modules.user.entity.User;
import com.whf.pan.server.modules.user.service.IUserService;
import com.whf.pan.server.modules.user.mapper.UserMapper;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Objects;

/**
* @author 26570
* @description 针对表【r_pan_user(用户信息表)】的数据库操作Service实现
* @createDate 2023-10-28 15:41:24
*/
@Service(value = "userService")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Resource
    private UserConverter userConverter;
    @Resource
    private IUserFileService userFileService;
    @Resource
    private CacheManager cacheManager;


    /**
     * 用户注册的业务实现
     * 需要实现的功能点：
     * 1、注册用户信息
     * 2、创建新用户的根本目录信息
     * <p>
     * 需要实现的技术难点：
     * 1、该业务是幂等的
     * 2、要保证用户名全局唯一
     * <p>
     * 实现技术难点的处理方案：
     * 1、幂等性通过数据库表对于用户名字段添加唯一索引，我们上有业务捕获对应的冲突异常，转化返回
     *
     * @param userRegisterContext
     * @return
     */
    @Override
    public Long register(UserRegisterContext userRegisterContext) {
        assembleUserEntity(userRegisterContext);
        doRegister(userRegisterContext);
        createUserRootFolder(userRegisterContext);
        return userRegisterContext.getEntity().getUserId();
    }

    /**
     * 实体转化
     * 由上下文信息转化成用户实体，封装进上下文
     *
     * @param userRegisterContext
     */
    private void assembleUserEntity(UserRegisterContext userRegisterContext) {
        User entity = userConverter.userRegisterContextToUser(userRegisterContext);
        String salt = PasswordUtil.getSalt(),
                dbPassword = PasswordUtil.encryptPassword(salt, userRegisterContext.getPassword());
        entity.setUserId(IdUtil.get());
        entity.setSalt(salt);
        entity.setPassword(dbPassword);
        entity.setCreateTime(new Date());
        entity.setUpdateTime(new Date());
        userRegisterContext.setEntity(entity);
    }

    /**
     * 实现注册用户的业务
     * 需要捕获数据库的唯一索引冲突异常，来实现全局用户名称唯一
     *
     * @param userRegisterContext
     */
    private void doRegister(UserRegisterContext userRegisterContext) {
        User entity = userRegisterContext.getEntity();
        if (Objects.nonNull(entity)) {
            try {
                if (!save(entity)) {
                    throw new BusinessException("用户注册失败");
                }
            } catch (DuplicateKeyException duplicateKeyException) {
                throw new BusinessException("用户名已存在");
            }
            return;
        }
        throw new BusinessException(ResponseCode.ERROR);
    }

    /**
     * 创建用户的根目录信息
     *
     * @param userRegisterContext
     */
    private void createUserRootFolder(UserRegisterContext userRegisterContext) {
        CreateFolderContext createFolderContext = new CreateFolderContext();
        createFolderContext.setParentId(FileConstants.TOP_PARENT_ID);
        createFolderContext.setUserId(userRegisterContext.getEntity().getUserId());
        createFolderContext.setFolderName(FileConstants.ALL_FILE_CN_STR);
        userFileService.createFolder(createFolderContext);
    }
/********************************************** 用户登录 **********************************************************/
    /**
     * 用户登录业务实现
     * <p>
     * 需要实现的功能：
     * 1、用户的登录信息校验
     * 2、生成一个具有时效性的accessToken
     * 3、将accessToken缓存起来，去实现单机登录
     *
     * @param userLoginContext
     * @return
     */
    @Override
    public String login(UserLoginContext userLoginContext) {
        checkLoginInfo(userLoginContext);
        generateAndSaveAccessToken(userLoginContext);
        return userLoginContext.getAccessToken();
    }


    /**
     * 校验用户名密码
     *
     * @param userLoginContext
     */
    private void checkLoginInfo(UserLoginContext userLoginContext) {
        String username = userLoginContext.getUsername();
        String password = userLoginContext.getPassword();
        User entity = getUserByUsername(username);
        if (Objects.isNull(entity)){
            throw new BusinessException("用户名称不存在");
        }

        String salt = entity.getSalt();
        String encryptPassword = PasswordUtil.encryptPassword(salt,password);
        String dbPassword = entity.getPassword();
        if (!Objects.equals(encryptPassword,dbPassword)){
            throw new BusinessException("用户密码错误");
        }
        userLoginContext.setEntity(entity);
    }

    /**
     * 生成并保存登录之后的凭证
     *
     * @param userLoginContext
     */
    private void generateAndSaveAccessToken(UserLoginContext userLoginContext) {
        User entity = userLoginContext.getEntity();

        String accessToken = JwtUtil.generateToken(entity.getUsername(), UserConstants.LOGIN_USER_ID, entity.getUserId(), UserConstants.ONE_DAY_LONG);

        Cache cache = cacheManager.getCache(CacheConstants.R_PAN_CACHE_NAME);
        cache.put(UserConstants.USER_LOGIN_PREFIX + entity.getUserId(), accessToken);

        userLoginContext.setAccessToken(accessToken);
    }

    /**
     * 通过用户名称获取用户实体信息
     *
     * @param username
     * @return
     */
    private User getUserByUsername(String username) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("username", username);
        return getOne(wrapper);
    }

/*****************************************************用户登出******************************************************/

    /**
     * 用户退出登录
     * 手段：清除用户的登录缓存
     * @param userId
     * @return
     */
    @Override
    public void exit(Long userId) {
        try {
            Cache cache = cacheManager.getCache(CacheConstants.R_PAN_CACHE_NAME);
            cache.evict(UserConstants.USER_LOGIN_PREFIX + userId);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException("用户退出登录失败");
        }
    }

}




