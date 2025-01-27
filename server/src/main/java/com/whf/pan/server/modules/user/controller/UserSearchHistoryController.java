package com.whf.pan.server.modules.user.controller;

import com.whf.pan.core.response.R;
import com.whf.pan.server.common.utils.UserIdUtil;
import com.whf.pan.server.modules.user.context.QueryUserSearchHistoryContext;
import com.whf.pan.server.modules.user.service.IUserSearchHistoryService;
import com.whf.pan.server.modules.user.vo.UserSearchHistoryVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author whf
 * @className UserSearchHistoryController
 * @description 用户搜索历史模块
 * @date 2023/12/26 20:02
 */
@RestController
@Api(tags = "用户搜索历史模块")
public class UserSearchHistoryController {

    @Resource
    private IUserSearchHistoryService iUserSearchHistoryService;

    @ApiOperation(
            value = "获取用户最新的搜索历史记录，默认十条",
            notes = "该接口提供了获取用户最新的搜索历史记录的功能",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    @GetMapping("user/search/histories")
    public R<List<UserSearchHistoryVO>> getUserSearchHistories() {
        QueryUserSearchHistoryContext context = new QueryUserSearchHistoryContext();
        context.setUserId(UserIdUtil.get());
        List<UserSearchHistoryVO> result = iUserSearchHistoryService.getUserSearchHistories(context);
        return R.data(result);
    }
}
