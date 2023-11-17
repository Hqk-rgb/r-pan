package com.whf.pan.server.modules.file.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.whf.pan.core.constants.Constants;
import com.whf.pan.core.exception.BusinessException;
import com.whf.pan.core.utils.FileUtil;
import com.whf.pan.core.utils.IdUtil;
import com.whf.pan.server.common.event.file.DeleteFileEvent;
import com.whf.pan.server.common.utils.HttpUtil;
import com.whf.pan.server.modules.file.constants.FileConstants;
import com.whf.pan.server.modules.file.context.*;
import com.whf.pan.server.modules.file.converter.FileConverter;
import com.whf.pan.server.modules.file.entity.File;
import com.whf.pan.server.modules.file.entity.UserFile;
import com.whf.pan.server.modules.file.enums.DelFlagEnum;
import com.whf.pan.server.modules.file.enums.FileTypeEnum;
import com.whf.pan.server.modules.file.enums.FolderFlagEnum;
import com.whf.pan.server.modules.file.service.IFileChunkService;
import com.whf.pan.server.modules.file.service.IFileService;
import com.whf.pan.server.modules.file.service.IUserFileService;
import com.whf.pan.server.modules.file.mapper.UserFileMapper;
import com.whf.pan.server.modules.file.vo.FileChunkUploadVO;
import com.whf.pan.server.modules.file.vo.UploadedChunksVO;
import com.whf.pan.server.modules.file.vo.UserFileVO;
import com.whf.pan.storage.engine.core.StorageEngine;
import com.whf.pan.storage.engine.core.context.ReadFileContext;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
* @author 26570
* @description 针对表【r_pan_user_file(用户文件信息表)】的数据库操作Service实现
* @createDate 2023-10-28 15:45:30
*/
@Service(value = "userFileService")
public class UserFileServiceImpl extends ServiceImpl<UserFileMapper, UserFile> implements IUserFileService, ApplicationContextAware {

    @Resource
    private IFileService fileService;

    @Resource
    private FileConverter fileConverter;

    private  ApplicationContext applicationContext;

    @Resource
    private IFileChunkService fileChunkService;

    @Resource
    private StorageEngine storageEngine;



    /** 创建文件夹信息
     * @param createFolderContext
     * @return
     */
    @Override
    public Long createFolder(CreateFolderContext createFolderContext) {
        return saveUserFile(createFolderContext.getParentId(),
                createFolderContext.getFolderName(),
                FolderFlagEnum.YES,
                null,
                null,
                createFolderContext.getUserId(),
                null);
    }

    /**
     * 保存用户文件的映射记录
     *
     * @param parentId
     * @param filename
     * @param folderFlagEnum
     * @param fileType       文件类型（1 普通文件 2 压缩文件 3 excel 4 word 5 pdf 6 txt 7 图片 8 音频 9 视频 10 ppt 11 源码文件 12 csv）
     * @param realFileId
     * @param userId
     * @param fileSizeDesc
     * @return
     */
    private Long saveUserFile(Long parentId,
                              String filename,
                              FolderFlagEnum folderFlagEnum,
                              Integer fileType,
                              Long realFileId,
                              Long userId,
                              String fileSizeDesc) {
        UserFile entity = assembleUserFile(parentId, userId, filename, folderFlagEnum, fileType, realFileId, fileSizeDesc);
        if (!save((entity))) {
            throw new BusinessException("保存文件信息失败");
        }
        return entity.getFileId();
    }

    /**
     * 用户文件映射关系实体转化
     * 1、构建并填充实体
     * 2、处理文件命名一致的问题
     *
     * @param parentId
     * @param userId
     * @param filename
     * @param folderFlagEnum
     * @param fileType
     * @param realFileId
     * @param fileSizeDesc
     * @return
     */
    private UserFile assembleUserFile(Long parentId, Long userId, String filename, FolderFlagEnum folderFlagEnum, Integer fileType, Long realFileId, String fileSizeDesc) {

        UserFile entity = UserFile.builder()
                .fileId(IdUtil.get())
                .userId(userId)
                .parentId(parentId)
                .realFileId(realFileId)
                .filename(filename)
                .folderFlag(folderFlagEnum.getCode())
                .fileSizeDesc(fileSizeDesc)
                .fileType(fileType)
                .delFlag(DelFlagEnum.NO.getCode())
                .createUser(userId)
                .createTime(new Date())
                .updateTime(new Date())
                .updateUser(userId).build();
//        UserFile entity = new UserFile();
//        entity.setFileId(IdUtil.get());
//        entity.setUserId(userId);
//        entity.setParentId(parentId);
//        entity.setRealFileId(realFileId);
//        entity.setFilename(filename);
//        entity.setFolderFlag(folderFlagEnum.getCode());
//        entity.setFileSizeDesc(fileSizeDesc);
//        entity.setFileType(fileType);
//        entity.setDelFlag(DelFlagEnum.NO.getCode());
//        entity.setCreateUser(userId);
//        entity.setCreateTime(new Date());
//        entity.setUpdateUser(userId);
//        entity.setUpdateTime(new Date());

        handleDuplicateFilename(entity);

        return entity;
    }
    /**
     * 处理用户重复名称
     * 如果同一文件夹下面有文件名称重复
     * 按照系统级规则重命名文件
     *
     * @param entity
     */
    private void handleDuplicateFilename(UserFile entity) {
        String filename = entity.getFilename(),
                newFilenameWithoutSuffix,
                newFilenameSuffix;
        int newFilenamePointPosition = filename.lastIndexOf(Constants.POINT_STR);
        if (newFilenamePointPosition == Constants.MINUS_ONE_INT) {
            newFilenameWithoutSuffix = filename;
            newFilenameSuffix = StringUtils.EMPTY;
        } else {
            newFilenameWithoutSuffix = filename.substring(Constants.ZERO_INT, newFilenamePointPosition);
            newFilenameSuffix = filename.replace(newFilenameWithoutSuffix, StringUtils.EMPTY);
        }

        List<UserFile> existRecords = getDuplicateFilename(entity, newFilenameWithoutSuffix);

        if (CollectionUtils.isEmpty(existRecords)) {
            return;
        }

        List<String> existFilenames = existRecords.stream().map(UserFile::getFilename).collect(Collectors.toList());

        int count = 1;
        String newFilename;

        do {
            newFilename = assembleNewFilename(newFilenameWithoutSuffix, count, newFilenameSuffix);
            count++;
        } while (existFilenames.contains(newFilename));

        entity.setFilename(newFilename);
    }

    /**
     * 查找同一父文件夹下面的同名文件数量
     *
     * @param entity
     * @param newFilenameWithoutSuffix
     * @return
     */
    private List<UserFile> getDuplicateFilename(UserFile entity, String newFilenameWithoutSuffix) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("parent_id", entity.getParentId());
        wrapper.eq("folder_flag", entity.getFolderFlag());
        wrapper.eq("user_id", entity.getUserId());
        wrapper.eq("del_flag", DelFlagEnum.NO.getCode());
        wrapper.likeRight("filename", newFilenameWithoutSuffix);
        return list(wrapper);
    }

    /**
     * 拼装新文件名称
     * 拼装规则参考操作系统重复文件名称的重命名规范
     *
     * @param newFilenameWithoutSuffix
     * @param count
     * @param newFilenameSuffix
     * @return
     */
    private String assembleNewFilename(String newFilenameWithoutSuffix, int count, String newFilenameSuffix) {
        String newFilename = new StringBuilder(newFilenameWithoutSuffix)
                .append(FileConstants.CN_LEFT_PARENTHESES_STR)
                .append(count)
                .append(FileConstants.CN_RIGHT_PARENTHESES_STR)
                .append(newFilenameSuffix)
                .toString();
        return newFilename;
    }

    /**
     * 查询用户的根文件夹信息
     *
     * @param userId
     * @return
     */
    @Override
    public UserFile getUserRootFile(Long userId) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("user_id", userId);
        wrapper.eq("parent_id", FileConstants.TOP_PARENT_ID);
        wrapper.eq("del_flag", DelFlagEnum.NO.getCode());
        wrapper.eq("folder_flag", FolderFlagEnum.YES.getCode());
        return getOne(wrapper);
    }

    /**
     * 查询用户的文件列表
     * @param context
     * @return
     */
    @Override
    public List<UserFileVO> getFileList(QueryFileListContext context) {
        return baseMapper.selectFileList(context);
    }

    /***********************************************************************文件重命名*************************************************************************************************/

    /**
     * 更新文件名称
     * 1、校验更新文件名称的条件
     * 2、执行更新文件名称的操作
     *
     * @param context
     */
    @Override
    public void updateFilename(UpdateFilenameContext context) {
        checkUpdateFilenameCondition(context);
        doUpdateFilename(context);
    }
    /**
     * 更新文件名称的条件校验
     * <p>
     * 1、文件ID是有效的
     * 2、用户有权限更新该文件的文件名称
     * 3、新旧文件名称不能一样
     * 4、不能使用当前文件夹下面的子文件的名称
     *
     * @param context
     */
    private void checkUpdateFilenameCondition(UpdateFilenameContext context) {

        Long fileId = context.getFileId();
        UserFile entity = getById(fileId);

        if (Objects.isNull(entity)) {
            throw new BusinessException("该文件ID无效");
        }

        if (!Objects.equals(entity.getUserId(), context.getUserId())) {
            throw new BusinessException("当前登录用户没有修改该文件名称的权限");
        }

        if (Objects.equals(entity.getFilename(), context.getNewFilename())) {
            throw new BusinessException("请换一个新的文件名称来修改");
        }


        QueryWrapper wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id", entity.getParentId());
        wrapper.eq("filename", context.getNewFilename());
        int count = count(wrapper);

        if (count > 0) {
            throw new BusinessException("该文件名称已被占用");
        }

        context.setEntity(entity);
    }
    /**
     * 执行文件重命名的操作
     *
     * @param context
     */
    private void doUpdateFilename(UpdateFilenameContext context) {
        UserFile entity = context.getEntity();
        entity.setFilename(context.getNewFilename());
        entity.setUpdateUser(context.getUserId());
        entity.setUpdateTime(new Date());

        if (!updateById(entity)) {
            throw new BusinessException("文件重命名失败");
        }
    }

    /*************************************************************************批量删除文件***********************************************************************************************/


    /**
     * 批量删除用户文件
     * <p>
     * 1、校验删除的条件
     * 2、执行批量删除的动作
     * 3、发布批量删除文件的事件，给其他模块订阅使用
     *
     * @param context
     */
    @Override
    public void deleteFile(DeleteFileContext context) {
        checkFileDeleteCondition(context);
        doDeleteFile(context);
        afterFileDelete(context);
    }

    /**
     * 删除文件之前的前置校验
     * <p>
     * 1、文件ID合法校验
     * 2、用户拥有删除该文件的权限
     *
     * @param context
     */
    private void checkFileDeleteCondition(DeleteFileContext context) {
        List<Long> fileIdList = context.getFileIdList();

        List<UserFile> rPanUserFiles = listByIds(fileIdList);
        if (rPanUserFiles.size() != fileIdList.size()) {
            throw new BusinessException("存在不合法的文件记录");
        }

        Set<Long> fileIdSet = rPanUserFiles.stream().map(UserFile::getFileId).collect(Collectors.toSet());
        int oldSize = fileIdSet.size();
        fileIdSet.addAll(fileIdList);
        int newSize = fileIdSet.size();

        if (oldSize != newSize) {
            throw new BusinessException("存在不合法的文件记录");
        }

        Set<Long> userIdSet = rPanUserFiles.stream().map(UserFile::getUserId).collect(Collectors.toSet());
        if (userIdSet.size() != 1) {
            throw new BusinessException("存在不合法的文件记录");
        }

        Long dbUserId = userIdSet.stream().findFirst().get();
        if (!Objects.equals(dbUserId, context.getUserId())) {
            throw new BusinessException("当前登录用户没有删除该文件的权限");
        }
    }

    /**
     * 执行文件删除的操作
     *
     * @param context
     */
    private void doDeleteFile(DeleteFileContext context) {
        List<Long> fileIdList = context.getFileIdList();

        UpdateWrapper updateWrapper = new UpdateWrapper();
        updateWrapper.in("file_id", fileIdList);
        updateWrapper.set("del_flag", DelFlagEnum.YES.getCode());
        updateWrapper.set("update_time", new Date());

        if (!update(updateWrapper)) {
            throw new BusinessException("文件删除失败");
        }
    }

    /**
     * 文件删除的后置操作
     * <p>
     * 1、对外发布文件删除的事件
     *
     * @param context
     */
    private void afterFileDelete(DeleteFileContext context) {
        DeleteFileEvent deleteFileEvent = new DeleteFileEvent(this,context.getFileIdList());
        applicationContext.publishEvent(deleteFileEvent);
    }




    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /*************************************************************************文件秒传*****************************************************************************************/



    /**
     * 文件秒传功能
     * <p>
     * 1、判断用户之前是否上传过该文件
     * 2、如果上传过该文件，只需要生成一个该文件和当前用户在指定文件夹下面的关联关系即可
     *
     * @param context
     * @return true 代表用户之前上传过相同文件并成功挂在了关联关系 false 用户没有上传过该文件，请手动执行上传逻辑
     */
    @Override
    public boolean secUpload(SecUploadFileContext context) {
    //        List<File> fileList = getFileListByUserIdAndIdentifier(context.getUserId(), context.getIdentifier());
    //        if (CollectionUtils.isNotEmpty(fileList)) {
    //            File record = fileList.get(Constants.ZERO_INT);
    //            saveUserFile(context.getParentId(),
    //                    context.getFilename(),
    //                    FolderFlagEnum.NO,
    //                    FileTypeEnum.getFileTypeCode(FileUtils.getFileSuffix(context.getFilename())),
    //                    record.getFileId(),
    //                    context.getUserId(),
    //                    record.getFileSizeDesc());
    //            return true;
    //        }
    //        return false;
        File record= getFileListByUserIdAndIdentifier(context.getUserId(), context.getIdentifier());
        if (Objects.isNull(record)){
            return false;
        }
        saveUserFile(context.getParentId(),
                context.getFilename(),
                FolderFlagEnum.NO,
                FileTypeEnum.getFileTypeCode(FileUtil.getFileSuffix(context.getFilename())),
                record.getFileId(),
                context.getUserId(),
                record.getFileSizeDesc());
        return true;
    }
    /**
     * 查询用户文件列表根据文件的唯一标识
     */

    private File getFileListByUserIdAndIdentifier(Long userId, String identifier) {
        QueryWrapper wrapper = Wrappers.query();
        wrapper.eq("create_user",userId);
        wrapper.eq("identifier",identifier);
        // 我们不能保证在高并发的情况下文件唯一标识全局唯一，所以有可能会查出多条数据
        List<File> records = fileService.list(wrapper);
        if (CollectionUtils.isEmpty(records)){
            return null;
        }
        //返回第一条记录
        return records.get(Constants.ZERO_INT);
    }



    /*************************************************************************单文件上传*****************************************************************************************/


    /**
     * 单文件上传
     * <p>
     * 1、上传文件并保存实体文件的记录
     * 2、保存用户文件的关系记录
     *
     * @param context
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void upload(FileUploadContext context) {
        saveFile(context);
        saveUserFile(context.getParentId(),
                context.getFilename(),
                FolderFlagEnum.NO,
                FileTypeEnum.getFileTypeCode(FileUtil.getFileSuffix(context.getFilename())),
                context.getRecord().getFileId(),
                context.getUserId(),
                context.getRecord().getFileSizeDesc());
    }

    /**
     * 上传文件并保存实体文件记录
     * 委托给实体文件的Service去完成该操作
     *
     * @param context
     */
    private void saveFile(FileUploadContext context) {
        FileSaveContext fileSaveContext = fileConverter.fileUploadContextTOFileSaveContext(context);
        fileService.saveFile(fileSaveContext);
        context.setRecord(fileSaveContext.getRecord());
    }

    /*************************************************************************文件分片上传*****************************************************************************************/
    /**
     * 文件分片上传
     * <p>
     * 1、上传实体文件
     * 2、保存分片文件记录
     * 3、校验是否全部分片上传完成
     *
     * @param context
     * @return
     */
    @Override
    public FileChunkUploadVO chunkUpload(FileChunkUploadContext context) {
        FileChunkSaveContext fileChunkSaveContext = fileConverter.fileChunkUploadContextTOFileChunkSaveContext(context);
        fileChunkService.saveChunkFile(fileChunkSaveContext);
        FileChunkUploadVO vo = new FileChunkUploadVO();
        vo.setMergeFlag(fileChunkSaveContext.getMergeFlagEnum().getCode());
        return vo;
    }

    /*************************************************************************查询用户已上传的分片列表**********************************************/

    /**
     * 查询用户已上传的分片列表
     * <p>
     * 1、查询已上传的分片列表
     * 2、封装返回实体
     *
     * @param context
     * @return
     */
    @Override
    public UploadedChunksVO getUploadedChunks(QueryUploadedChunksContext context) {
        QueryWrapper queryWrapper = Wrappers.query();
        queryWrapper.select("chunk_number");
        queryWrapper.eq("identifier", context.getIdentifier());
        queryWrapper.eq("create_user", context.getUserId());
        // 过期时间必须晚于（大于）当前时间
        queryWrapper.gt("expiration_time", new Date());

        List<Integer> uploadedChunks = fileChunkService.listObjs(queryWrapper, value -> (Integer) value);

        UploadedChunksVO vo = new UploadedChunksVO();
        vo.setUploadedChunks(uploadedChunks);
        return vo;
    }

    /************************************************************************文件分片合并**********************************************/


    /**
     * 文件分片合并
     * <p>
     * 1、文件分片物理合并
     * 2、保存文件实体记录
     * 3、保存文件用户关系映射
     *
     * @param context
     */
    @Override
    public void mergeFile(FileChunkMergeContext context) {
        mergeFileChunkAndSaveFile(context);
        saveUserFile(context.getParentId(),
                context.getFilename(),
                FolderFlagEnum.NO,
                FileTypeEnum.getFileTypeCode(FileUtil.getFileSuffix(context.getFilename())),
                context.getRecord().getFileId(),
                context.getUserId(),
                context.getRecord().getFileSizeDesc());
    }


    /**
     * 合并文件分片并保存物理文件记录
     *
     * @param context
     */
    private void mergeFileChunkAndSaveFile(FileChunkMergeContext context) {
        FileChunkMergeAndSaveContext fileChunkMergeAndSaveContext = fileConverter.fileChunkMergeContextTOFileChunkMergeAndSaveContext(context);
        fileService.mergeFileChunkAndSaveFile(fileChunkMergeAndSaveContext);
        context.setRecord(fileChunkMergeAndSaveContext.getRecord());
    }

    /***********************************************************************文件下载**********************************************/


    /**
     * 文件下载
     * <p>
     * 1、参数校验：校验文件是否存在，文件是否属于该用户
     * 2、校验该文件是不是一个文件夹
     * 3、执行下载的动作
     *
     * @param context
     */
    @Override
    public void download(FileDownloadContext context) {
        UserFile record = getById(context.getFileId());
        checkOperatePermission(record, context.getUserId());
        if (checkIsFolder(record)) {
            throw new BusinessException("文件夹暂不支持下载");
        }
        doDownload(record, context.getResponse());
    }

    /**
     * 校验用户的操作权限
     * <p>
     * 1、文件记录必须存在
     * 2、文件记录的创建者必须是该登录用户
     *
     * @param record
     * @param userId
     */
    private void checkOperatePermission(UserFile record, Long userId) {
        if (Objects.isNull(record)) {
            throw new BusinessException("当前文件记录不存在");
        }
        if (!record.getUserId().equals(userId)) {
            throw new BusinessException("您没有该文件的操作权限");
        }
    }

    /**
     * 检查当前文件记录是不是一个文件夹
     *
     * @param record
     * @return
     */
    private boolean checkIsFolder(UserFile record) {
        if (Objects.isNull(record)) {
            throw new BusinessException("当前文件记录不存在");
        }
        return FolderFlagEnum.YES.getCode().equals(record.getFolderFlag());
    }

    /**
     * 执行文件下载的动作
     * <p>
     * 1、查询文件的真实存储路径
     * 2、添加跨域的公共响应头
     * 3、拼装下载文件的名称、长度等等响应信息
     * 4、委托文件存储引擎去读取文件内容到响应的输出流中
     *
     * @param record
     * @param response
     */
    private void doDownload(UserFile record, HttpServletResponse response) {
        File realFileRecord = fileService.getById(record.getRealFileId());
        if (Objects.isNull(realFileRecord)) {
            throw new BusinessException("当前的文件记录不存在");
        }
        addCommonResponseHeader(response, MediaType.APPLICATION_OCTET_STREAM_VALUE);
        addDownloadAttribute(response, record, realFileRecord);
        realFile2OutputStream(realFileRecord.getRealPath(), response);
    }
    /**
     * 添加公共的文件读取响应头
     *
     * @param response
     * @param contentTypeValue
     */
    private void addCommonResponseHeader(HttpServletResponse response, String contentTypeValue) {
        response.reset();
        HttpUtil.addCorsResponseHeaders(response);
        response.addHeader(FileConstants.CONTENT_TYPE_STR, contentTypeValue);
        response.setContentType(contentTypeValue);
    }

    /**
     * 添加文件下载的属性信息
     *
     * @param response
     * @param record
     * @param realFileRecord
     */
    private void addDownloadAttribute(HttpServletResponse response, UserFile record, File realFileRecord) {
        try {
            response.addHeader(FileConstants.CONTENT_DISPOSITION_STR,
                    FileConstants.CONTENT_DISPOSITION_VALUE_PREFIX_STR + new String(record.getFilename().getBytes(FileConstants.GB2312_STR), FileConstants.IOS_8859_1_STR));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new BusinessException("文件下载失败");
        }
        response.setContentLengthLong(Long.valueOf(realFileRecord.getFileSize()));
    }

    /**
     * 委托文件存储引擎去读取文件内容并写入到输出流中
     *
     * @param realPath
     * @param response
     */
    private void realFile2OutputStream(String realPath, HttpServletResponse response) {
        try {
            ReadFileContext context = new ReadFileContext();
            context.setRealPath(realPath);
            context.setOutputStream(response.getOutputStream());
            storageEngine.realFile(context);
        } catch (IOException e) {
            e.printStackTrace();
            throw new BusinessException("文件下载失败");
        }
    }
}




