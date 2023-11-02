package com.whf.pan.server.modules.file;

import cn.hutool.core.lang.Assert;
import com.whf.pan.server.PanLauncher;
import com.whf.pan.server.modules.file.context.QueryFileListContext;
import com.whf.pan.server.modules.file.enums.DelFlagEnum;
import com.whf.pan.server.modules.file.service.IUserFileService;
import com.whf.pan.server.modules.file.vo.UserFileVO;
import com.whf.pan.server.modules.user.context.UserLoginContext;
import com.whf.pan.server.modules.user.context.UserRegisterContext;
import com.whf.pan.server.modules.user.service.IUserService;
import com.whf.pan.server.modules.user.vo.UserInfoVO;
import org.apache.commons.collections.CollectionUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author whf
 * @className FileTest
 * @description 文件模块单元测试类
 * @date 2023/11/2 16:29
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = PanLauncher.class)
@Transactional
public class FileTest {
    @Resource
    private IUserFileService userFileService;

    @Resource
    private IUserService userService;

    /**
     * 测试用户查询文件列表成功
     */
    @Test
    public void testQueryUserFileListSuccess(){
        Long userId = register();
        UserInfoVO userInfoVO = info(userId);

        QueryFileListContext context = new QueryFileListContext();
        context.setParentId(userInfoVO.getRootFileId());
        context.setUserId(userId);
        context.setFileTypeArray(null);
        context.setDelFlag(DelFlagEnum.NO.getCode());

        List<UserFileVO> result = userFileService.getFileList(context);
        Assert.isTrue(CollectionUtils.isEmpty(result));
    }

/**********************************************************************私有方法********************************************************************************/

    /**
     * 用户注册
     *
     * @return 新用户的ID
     */
    private Long register() {
        UserRegisterContext context = createUserRegisterContext();
        Long register = userService.register(context);
        Assert.isTrue(register.longValue() > 0L);
        return register;
    }

    /**
     * 查询登录用户的基本信息
     *
     * @param userId
     * @return
     */
    private UserInfoVO info(Long userId) {
        UserInfoVO userInfoVO = userService.info(userId);
        Assert.notNull(userInfoVO);
        return userInfoVO;
    }



    private final static String USERNAME = "Faker";
    private final static String PASSWORD = "123456789";
    private final static String QUESTION = "question";
    private final static String ANSWER = "answer";


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

    /**********************************************************************私有方法********************************************************************************/


}
