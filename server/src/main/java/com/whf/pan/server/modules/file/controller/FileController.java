package com.whf.pan.server.modules.file.controller;

import com.google.common.base.Splitter;
import com.whf.pan.core.constants.Constants;
import com.whf.pan.core.response.R;
import com.whf.pan.core.utils.IdUtil;
import com.whf.pan.server.common.utils.UserIdUtil;
import com.whf.pan.server.modules.file.constants.FileConstants;
import com.whf.pan.server.modules.file.context.CreateFolderContext;
import com.whf.pan.server.modules.file.context.QueryFileListContext;
import com.whf.pan.server.modules.file.converter.FileConverter;
import com.whf.pan.server.modules.file.enums.DelFlagEnum;
import com.whf.pan.server.modules.file.po.CreateFolderPO;
import com.whf.pan.server.modules.file.service.IUserFileService;
import com.whf.pan.server.modules.file.vo.UserFileVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author whf
 * @className FileController
 * @description 文件模块控制器
 * @date 2023/11/2 13:56
 */
@RestController
@Validated
public class FileController {

    @Resource
    private IUserFileService userFileService;

    @Resource
    private FileConverter fileConverter;

    @ApiOperation(
            value = "获取文件列表",
            notes = "该接口提供了用户查询某文件夹下某些文件类型的文件列表的功能",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    @GetMapping("files")
    public R<List<UserFileVO>> list(@NotBlank(message = "父文件夹ID不能为空") @RequestParam(value = "parentId",required = false) String parentId,
                                    @RequestParam(value = "fileTypes",required = false,defaultValue = FileConstants.ALL_FILE_TYPE) String fileTypes){

        // 解密 parentId
        Long realParentId = IdUtil.decrypt(parentId);
        List<Integer> fileTypeArray = null;

        if (!Objects.equals(FileConstants.ALL_FILE_TYPE,fileTypes)){
            fileTypeArray = Splitter.on(Constants.COMMON_SEPARATOR).splitToList(fileTypes).stream().map(Integer::valueOf).collect(Collectors.toList());
        }

        QueryFileListContext context = new QueryFileListContext();
        context.setParentId(realParentId);
        context.setFileTypeArray(fileTypeArray);
        context.setUserId(UserIdUtil.get());
        context.setDelFlag(DelFlagEnum.NO.getCode());

        List<UserFileVO> result = userFileService.getFileList(context);
        return R.data(result);

    }

    @ApiOperation(
            value = "创建文件夹",
            notes = "该接口提供了创建文件夹的功能",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    @PostMapping(value = "file/folder")
    public R<String> createFolder(@Validated @RequestBody CreateFolderPO createFolderPO){
        CreateFolderContext context = fileConverter.createFolderPOTOCreateFolderContext(createFolderPO);
        Long fileId = userFileService.createFolder(context);
        return R.data(IdUtil.encrypt(fileId));
    }

}
