package com.whf.pan.server.modules.share.controller;

import com.google.common.base.Splitter;
import com.whf.pan.core.constants.Constants;
import com.whf.pan.core.response.R;
import com.whf.pan.core.utils.IdUtil;
import com.whf.pan.server.modules.share.context.CreateShareUrlContext;
import com.whf.pan.server.modules.share.converter.ShareConverter;
import com.whf.pan.server.modules.share.po.CreateShareUrlPO;
import com.whf.pan.server.modules.share.service.IShareService;
import com.whf.pan.server.modules.share.vo.ShareUrlVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author whf
 * @className ShareController
 * @description 分享模块
 * @date 2023/12/4 14:16
 */
@Api(tags = "分享模块")
@RestController
@Validated
public class ShareController {

    @Resource
    private IShareService shareService;

    @Resource
    private ShareConverter shareConverter;

    @ApiOperation(
            value = "创建分享链接",
            notes = "该接口提供了创建分享链接的功能",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    @PostMapping("share")
    public R<ShareUrlVO> create(@Validated @RequestBody CreateShareUrlPO createShareUrlPO) {
        CreateShareUrlContext context = shareConverter.createShareUrlPOTOCreateShareUrlContext(createShareUrlPO);

        String shareFileIds = createShareUrlPO.getShareFileIds();
        List<Long> shareFileIdList = Splitter.on(Constants.COMMON_SEPARATOR).splitToList(shareFileIds).stream().map(IdUtil::decrypt).collect(Collectors.toList());

        context.setShareFileIdList(shareFileIdList);

        ShareUrlVO vo = shareService.create(context);
        return R.data(vo);
    }


}
