package com.whf.pan.server.modules.file.controller;

import com.google.common.base.Splitter;
import com.whf.pan.core.constants.Constants;
import com.whf.pan.core.response.R;
import com.whf.pan.core.utils.IdUtil;
import com.whf.pan.server.common.utils.UserIdUtil;
import com.whf.pan.server.modules.file.constants.FileConstants;
import com.whf.pan.server.modules.file.context.*;
import com.whf.pan.server.modules.file.converter.FileConverter;
import com.whf.pan.server.modules.file.enums.DelFlagEnum;
import com.whf.pan.server.modules.file.po.*;
import com.whf.pan.server.modules.file.service.IUserFileService;
import com.whf.pan.server.modules.file.vo.FileChunkUploadVO;
import com.whf.pan.server.modules.file.vo.UploadedChunksVO;
import com.whf.pan.server.modules.file.vo.UserFileVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
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
    @ApiOperation(
            value = "文件重命名",
            notes = "该接口提供了文件重命名的功能",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    @PutMapping("file")
    public R updateFilename(@Validated @RequestBody UpdateFilenamePO updateFilenamePO) {
        UpdateFilenameContext context = fileConverter.updateFilenamePOTOUpdateFilenameContext(updateFilenamePO);
        userFileService.updateFilename(context);
        return R.success();
    }

    @ApiOperation(
            value = "批量删除文件",
            notes = "该接口提供了批量删除文件的功能",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    @DeleteMapping("file")
    public R deleteFilename(@Validated @RequestBody DeleteFilePO deleteFilePO) {
        DeleteFileContext context = fileConverter.deleteFilePOTODeleteFileContext(deleteFilePO);

        String fileIds = deleteFilePO.getFileIds();
        // 将一个包含了文件ID的字符串解析成一个 List<Long>类型的 fileIdList
        List<Long> fileIdList = Splitter.on(Constants.COMMON_SEPARATOR).splitToList(fileIds).stream().map(IdUtil::decrypt).collect(Collectors.toList());

        context.setFileIdList(fileIdList);
        userFileService.deleteFile(context);
        return R.success();
    }

    @ApiOperation(
            value = "文件秒传",
            notes = "该接口提供了文件秒传的功能",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    @PostMapping("file/sec-upload")
    public R secUpload(@Validated @RequestBody SecUploadFilePO secUploadFilePO) {

        SecUploadFileContext context = fileConverter.secUploadFilePOTOSecUploadFileContext(secUploadFilePO);

        boolean success = userFileService.secUpload(context);
        if (success) {
            return R.success();
        }
        return R.fail("文件唯一标识不存在，请手动执行文件上传的操作！");
    }

    @ApiOperation(
            value = "单文件上传",
            notes = "该接口提供了单文件上传的功能",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    @PostMapping("file/upload")
    public R upload(@Validated FileUploadPO fileUploadPO) {
        FileUploadContext context = fileConverter.fileUploadPOTOFileUploadContext(fileUploadPO);
        userFileService.upload(context);
        return R.success();
    }

    @ApiOperation(
            value = "文件分片上传",
            notes = "该接口提供了文件分片上传的功能",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    @PostMapping("file/chunk-upload")
    public R<FileChunkUploadVO> chunkUpload(@Validated FileChunkUploadPO fileChunkUploadPO) {
        FileChunkUploadContext context = fileConverter.fileChunkUploadPOTOFileChunkUploadContext(fileChunkUploadPO);
        FileChunkUploadVO vo = userFileService.chunkUpload(context);
        return R.data(vo);
    }

    @ApiOperation(
            value = "文件分片检查",
            notes = "该接口提供了查询已上传文件分片列表的功能",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    @GetMapping("file/chunk-upload")
    public R<UploadedChunksVO> getUploadedChunks(@Validated QueryUploadedChunksPO queryUploadedChunksPO) {
        QueryUploadedChunksContext context = fileConverter.queryUploadedChunksPOTOQueryUploadedChunksContext(queryUploadedChunksPO);
        UploadedChunksVO vo = userFileService.getUploadedChunks(context);
        return R.data(vo);
    }
    @ApiOperation(
            value = "文件分片合并",
            notes = "该接口提供了文件分片合并的功能",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    @GetMapping("file/merge")
    public R mergeFile(@Validated @RequestBody FileChunkMergePO fileChunkMergePO) {
        FileChunkMergeContext context = fileConverter.fileChunkMergePOTOFileChunkMergeContext(fileChunkMergePO);
        userFileService.mergeFile(context);
        return R.success();
    }

    @ApiOperation(
            value = "文件下载",
            notes = "该接口提供了文件下载的功能",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_OCTET_STREAM_VALUE
    )
    @GetMapping("file/download")
    public void download(@NotBlank(message = "文件ID不能为空") @RequestParam(value = "fileId", required = false) String fileId,
                         HttpServletResponse response) {
        FileDownloadContext context = new FileDownloadContext();
        context.setFileId(IdUtil.decrypt(fileId));
        context.setResponse(response);
        context.setUserId(UserIdUtil.get());
        userFileService.download(context);
    }

    @ApiOperation(
            value = "文件预览",
            notes = "该接口提供了文件预览的功能",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_OCTET_STREAM_VALUE
    )
    @GetMapping("file/preview")
    public void preview(@NotBlank(message = "文件ID不能为空") @RequestParam(value = "fileId", required = false) String fileId,
                        HttpServletResponse response) {
        FilePreviewContext context = new FilePreviewContext();
        context.setFileId(IdUtil.decrypt(fileId));
        context.setResponse(response);
        context.setUserId(UserIdUtil.get());
        userFileService.preview(context);
    }

}
