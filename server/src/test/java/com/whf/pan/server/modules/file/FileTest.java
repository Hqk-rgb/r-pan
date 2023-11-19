package com.whf.pan.server.modules.file;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Assert;
import com.google.common.collect.Lists;
import com.whf.pan.core.exception.BusinessException;
import com.whf.pan.core.utils.IdUtil;
import com.whf.pan.server.PanLauncher;
import com.whf.pan.server.modules.file.context.*;
import com.whf.pan.server.modules.file.entity.File;
import com.whf.pan.server.modules.file.entity.FileChunk;
import com.whf.pan.server.modules.file.enums.DelFlagEnum;
import com.whf.pan.server.modules.file.enums.MergeFlagEnum;
import com.whf.pan.server.modules.file.service.IFileChunkService;
import com.whf.pan.server.modules.file.service.IFileService;
import com.whf.pan.server.modules.file.service.IUserFileService;
import com.whf.pan.server.modules.file.vo.*;
import com.whf.pan.server.modules.user.context.UserLoginContext;
import com.whf.pan.server.modules.user.context.UserRegisterContext;
import com.whf.pan.server.modules.user.service.IUserService;
import com.whf.pan.server.modules.user.vo.UserInfoVO;
import com.whf.pan.storage.engine.core.StorageEngine;
import lombok.AllArgsConstructor;
import org.apache.commons.collections.CollectionUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;

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
    private IFileService fileService;

    @Resource
    private IUserService userService;

    @Resource
    private IFileChunkService fileChunkService;


    /***************************************************************测试用户查询文件列表成功***********************************************************************/

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
    /*******************************************************************创建新文件夹功能实现***************************************************************************/

    /**
     * 测试创建文件夹成功
     */
    @Test
    public void testCreateFolderSuccess() {

        Long userId = register();
        UserInfoVO userInfoVO = info(userId);

        CreateFolderContext context = new CreateFolderContext();
        context.setParentId(userInfoVO.getRootFileId());
        context.setUserId(userId);
        context.setFolderName("folder-name");

        Long fileId = userFileService.createFolder(context);
        Assert.notNull(fileId);
    }
    /************************************************************************文件重命名*********************************************************************************************/


    /**
     * 测试文件重命名失败-文件ID无效
     */
    @Test(expected = BusinessException.class)
    public void testUpdateFilenameFailByWrongFileId() {
        Long userId = register();
        UserInfoVO userInfoVO = info(userId);

        CreateFolderContext context = new CreateFolderContext();
        context.setParentId(userInfoVO.getRootFileId());
        context.setUserId(userId);
        context.setFolderName("folder-name");

        Long fileId = userFileService.createFolder(context);
        Assert.notNull(fileId);

        UpdateFilenameContext updateFilenameContext = new UpdateFilenameContext();
        updateFilenameContext.setFileId(fileId + 1);
        updateFilenameContext.setUserId(userId);
        updateFilenameContext.setNewFilename("folder-name-new");

        userFileService.updateFilename(updateFilenameContext);
    }

    /**
     * 测试当前用户ID无效
     */
    @Test(expected = BusinessException.class)
    public void testUpdateFilenameFailByWrongUserId() {
        Long userId = register();
        UserInfoVO userInfoVO = info(userId);

        CreateFolderContext context = new CreateFolderContext();
        context.setParentId(userInfoVO.getRootFileId());
        context.setUserId(userId);
        context.setFolderName("folder-name");

        Long fileId = userFileService.createFolder(context);
        Assert.notNull(fileId);

        UpdateFilenameContext updateFilenameContext = new UpdateFilenameContext();
        updateFilenameContext.setFileId(fileId);
        updateFilenameContext.setUserId(userId + 1);
        updateFilenameContext.setNewFilename("folder-name-new");

        userFileService.updateFilename(updateFilenameContext);
    }

    /**
     * 测试文件名称重复
     */
    @Test(expected = BusinessException.class)
    public void testUpdateFilenameFailByWrongFilename() {
        Long userId = register();
        UserInfoVO userInfoVO = info(userId);

        CreateFolderContext context = new CreateFolderContext();
        context.setParentId(userInfoVO.getRootFileId());
        context.setUserId(userId);
        context.setFolderName("folder-name");

        Long fileId = userFileService.createFolder(context);
        Assert.notNull(fileId);

        UpdateFilenameContext updateFilenameContext = new UpdateFilenameContext();
        updateFilenameContext.setFileId(fileId);
        updateFilenameContext.setUserId(userId);
        updateFilenameContext.setNewFilename("folder-name");

        userFileService.updateFilename(updateFilenameContext);
    }

    /**
     * 校验文件名称已被占用
     */
    @Test(expected = BusinessException.class)
    public void testUpdateFilenameFailByFilenameUnAvailable() {
        Long userId = register();
        UserInfoVO userInfoVO = info(userId);

        CreateFolderContext context = new CreateFolderContext();
        context.setParentId(userInfoVO.getRootFileId());
        context.setUserId(userId);
        context.setFolderName("folder-name-1");

        Long fileId = userFileService.createFolder(context);
        Assert.notNull(fileId);

        context.setParentId(userInfoVO.getRootFileId());
        context.setUserId(userId);
        context.setFolderName("folder-name-2");

        fileId = userFileService.createFolder(context);
        Assert.notNull(fileId);

        UpdateFilenameContext updateFilenameContext = new UpdateFilenameContext();
        updateFilenameContext.setFileId(fileId);
        updateFilenameContext.setUserId(userId);
        updateFilenameContext.setNewFilename("folder-name-1");

        userFileService.updateFilename(updateFilenameContext);
    }

    /**
     * 测试更新文件名称成功
     */
    @Test
    public void testUpdateFilenameSuccess() {
        Long userId = register();
        UserInfoVO userInfoVO = info(userId);

        CreateFolderContext context = new CreateFolderContext();
        context.setParentId(userInfoVO.getRootFileId());
        context.setUserId(userId);
        context.setFolderName("folder-name-old");

        Long fileId = userFileService.createFolder(context);
        Assert.notNull(fileId);

        UpdateFilenameContext updateFilenameContext = new UpdateFilenameContext();
        updateFilenameContext.setFileId(fileId);
        updateFilenameContext.setUserId(userId);
        updateFilenameContext.setNewFilename("folder-name-new");

        userFileService.updateFilename(updateFilenameContext);
    }

    /************************************************************************文件批量删除并发布事件*********************************************************************************************/

    /**
     * 校验文件删除失败-非法的文件ID
     */
    @Test(expected = BusinessException.class)
    public void testDeleteFileFailByWrongFileId() {
        Long userId = register();
        UserInfoVO userInfoVO = info(userId);

        CreateFolderContext context = new CreateFolderContext();
        context.setParentId(userInfoVO.getRootFileId());
        context.setUserId(userId);
        context.setFolderName("folder-name-old");

        Long fileId = userFileService.createFolder(context);
        Assert.notNull(fileId);

        DeleteFileContext deleteFileContext = new DeleteFileContext();
        List<Long> fileIdList = Lists.newArrayList();
        fileIdList.add(fileId + 1);
        deleteFileContext.setFileIdList(fileIdList);
        deleteFileContext.setUserId(userId);

        userFileService.deleteFile(deleteFileContext);
    }

    /**
     * 校验文件删除失败-非法的用户ID
     */
    @Test(expected =  BusinessException.class)
    public void testDeleteFileFailByWrongUserId() {
        Long userId = register();
        UserInfoVO userInfoVO = info(userId);

        CreateFolderContext context = new CreateFolderContext();
        context.setParentId(userInfoVO.getRootFileId());
        context.setUserId(userId);
        context.setFolderName("folder-name-old");

        Long fileId = userFileService.createFolder(context);
        Assert.notNull(fileId);

        DeleteFileContext deleteFileContext = new DeleteFileContext();
        List<Long> fileIdList = Lists.newArrayList();
        fileIdList.add(fileId);
        deleteFileContext.setFileIdList(fileIdList);
        deleteFileContext.setUserId(userId + 1);

        userFileService.deleteFile(deleteFileContext);
    }

    /**
     * 校验用户删除文件成功
     */
    @Test
    public void testDeleteFileSuccess() {
        Long userId = register();
        UserInfoVO userInfoVO = info(userId);

        CreateFolderContext context = new CreateFolderContext();
        context.setParentId(userInfoVO.getRootFileId());
        context.setUserId(userId);
        context.setFolderName("folder-name-old");

        Long fileId = userFileService.createFolder(context);
        Assert.notNull(fileId);

        DeleteFileContext deleteFileContext = new DeleteFileContext();
        List<Long> fileIdList = Lists.newArrayList();
        fileIdList.add(fileId);
        deleteFileContext.setFileIdList(fileIdList);
        deleteFileContext.setUserId(userId);

        userFileService.deleteFile(deleteFileContext);
    }

    /**********************************************************************秒传文件********************************************************************************/


    /**
     * 校验秒传文件成功
     */
    @Test
    public void testSecUploadSuccess() {
        Long userId = register();
        UserInfoVO userInfoVO = info(userId);

        String identifier = "123456789";

        File record = new File();
        record.setFileId(IdUtil.get());
        record.setFilename("filename");
        record.setRealPath("realpath");
        record.setFileSize("fileSize");
        record.setFileSizeDesc("fileSizeDesc");
        record.setFilePreviewContentType("");
        record.setIdentifier(identifier);
        record.setCreateUser(userId);
        record.setCreateTime(new Date());
        fileService.save(record);

        SecUploadFileContext context = new SecUploadFileContext();
        context.setIdentifier(identifier);
        context.setFilename("filename");
        context.setParentId(userInfoVO.getRootFileId());
        context.setUserId(userId);

        boolean result = userFileService.secUpload(context);
        Assert.isTrue(result);
    }

    /**
     * 校验秒传文件失败
     */
    @Test
    public void testSecUploadFail() {
        Long userId = register();
        UserInfoVO userInfoVO = info(userId);

        String identifier = "123456789";

        File record = new File();
        record.setFileId(IdUtil.get());
        record.setFilename("filename");
        record.setRealPath("realpath");
        record.setFileSize("fileSize");
        record.setFileSizeDesc("fileSizeDesc");
        record.setFilePreviewContentType("");
        record.setIdentifier(identifier);
        record.setCreateUser(userId);
        record.setCreateTime(new Date());
        fileService.save(record);

        SecUploadFileContext context = new SecUploadFileContext();
        context.setIdentifier(identifier + "_update");
        context.setFilename("filename");
        context.setParentId(userInfoVO.getRootFileId());
        context.setUserId(userId);

        boolean result = userFileService.secUpload(context);
        Assert.isFalse(result);
    }

    /**********************************************************************单文件上传********************************************************************************/

    /**
     * 测试单文件上传成功
     */
    @Test
    public void testUploadSuccess() {
        Long userId = register();
        UserInfoVO userInfoVO = info(userId);

        FileUploadContext context = new FileUploadContext();
        MultipartFile file = genarateMultipartFile();
        context.setFile(file);
        context.setParentId(userInfoVO.getRootFileId());
        context.setUserId(userId);
        context.setIdentifier("12345678");
        context.setTotalSize(file.getSize());
        context.setFilename(file.getOriginalFilename());
        userFileService.upload(context);

        QueryFileListContext queryFileListContext = new QueryFileListContext();
        queryFileListContext.setDelFlag(DelFlagEnum.NO.getCode());
        queryFileListContext.setUserId(userId);
        queryFileListContext.setParentId(userInfoVO.getRootFileId());
        System.out.println(queryFileListContext);
        List<UserFileVO> fileList = userFileService.getFileList(queryFileListContext);
        System.out.println(fileList);
        Assert.notEmpty(fileList);
        Assert.isTrue(fileList.size() == 1);
    }

    /**
     * 生成模拟的网络文件实体
     *
     * @return
     */
    private static MultipartFile genarateMultipartFile() {
        MultipartFile file = null;
        try {
            StringBuffer stringBuffer = new StringBuffer();
            for (int i = 0; i < 1024 * 1024; i++) {
                stringBuffer.append("a");
            }
            file = new MockMultipartFile("file", "test.txt", "multipart/form-data","test upload context".getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }


    /**********************************************************************单文件上传**************************************************************************/

    /**
     * 测试查询用户已上传的文件分片信息列表成功
     */
    @Test
    public void testQueryUploadedChunksSuccess() {
        Long userId = register();

        String identifier = "123456789";

        FileChunk record = new FileChunk();
        record.setId(IdUtil.get());
        record.setIdentifier(identifier);
        record.setRealPath("realPath");
        record.setChunkNumber(1);
        record.setExpirationTime(DateUtil.offsetDay(new Date(), 1));
        record.setCreateUser(userId);
        record.setCreateTime(new Date());
        boolean save = fileChunkService.save(record);
        Assert.isTrue(save);

        QueryUploadedChunksContext context = new QueryUploadedChunksContext();
        context.setIdentifier(identifier);
        context.setUserId(userId);

        UploadedChunksVO vo = userFileService.getUploadedChunks(context);
        Assert.notNull(vo);
        Assert.notEmpty(vo.getUploadedChunks());
    }

    /**********************************************************************文件分片合并**************************************************************************/

    /**
     * 测试文件分片上传成功
     */
    @Test
    public void uploadWithChunkTest() throws InterruptedException {
        Long userId = register();
        UserInfoVO userInfoVO = info(userId);

        CountDownLatch countDownLatch = new CountDownLatch(10);
        for (int i = 0; i < 10; i++) {
            new ChunkUploader(countDownLatch, i + 1, 10, userFileService, userId, userInfoVO.getRootFileId()).start();
        }
        countDownLatch.await();
    }


    /**********************************************************************测试文件夹树查询**************************************************************************/


    /**
     * 测试文件夹树查询
     */
    @Test
    public void getFolderTreeNodeVOListTest() {

        Long userId = register();
        UserInfoVO userInfoVO = info(userId);

        CreateFolderContext context = new CreateFolderContext();
        context.setParentId(userInfoVO.getRootFileId());
        context.setUserId(userId);
        context.setFolderName("folder-name-1");

        Long fileId = userFileService.createFolder(context);
        Assert.notNull(fileId);

        context.setFolderName("folder-name-2");

        fileId = userFileService.createFolder(context);
        Assert.notNull(fileId);

        context.setFolderName("folder-name-2-1");
        context.setParentId(fileId);

        userFileService.createFolder(context);
        Assert.notNull(fileId);

        QueryFolderTreeContext queryFolderTreeContext = new QueryFolderTreeContext();
        queryFolderTreeContext.setUserId(userId);
        List<FolderTreeNodeVO> folderTree = userFileService.getFolderTree(queryFolderTreeContext);

        Assert.isTrue(folderTree.size() == 1);
        folderTree.stream().forEach(FolderTreeNodeVO::print);
    }


    /**********************************************************************测试批量文件转移**************************************************************************/


    /**
     * 测试文件转移成功
     */
    @Test
    public void testTransferFileSuccess() {
        Long userId = register();
        UserInfoVO userInfoVO = info(userId);

        CreateFolderContext context = new CreateFolderContext();
        context.setParentId(userInfoVO.getRootFileId());
        context.setUserId(userId);
        context.setFolderName("folder-name-1");

        Long folder1 = userFileService.createFolder(context);
        Assert.notNull(folder1);

        context.setFolderName("folder-name-2");
        Long folder2 = userFileService.createFolder(context);
        Assert.notNull(folder2);

        TransferFileContext transferFileContext = new TransferFileContext();
        transferFileContext.setTargetParentId(folder1);
        transferFileContext.setFileIdList(Lists.newArrayList(folder2));
        transferFileContext.setUserId(userId);
        userFileService.transfer(transferFileContext);

        QueryFileListContext queryFileListContext = new QueryFileListContext();
        queryFileListContext.setParentId(userInfoVO.getRootFileId());
        queryFileListContext.setUserId(userId);
        queryFileListContext.setDelFlag(DelFlagEnum.NO.getCode());
        List<UserFileVO> records = userFileService.getFileList(queryFileListContext);
        Assert.notEmpty(records);
    }

    /**
     * 测试文件转移失败，目标文件夹是要转移的文件列表中的文件夹或者是其子文件夹
     */
    @Test(expected = BusinessException.class)
    public void testTransferFileFail() {
        Long userId = register();
        UserInfoVO userInfoVO = info(userId);

        CreateFolderContext context = new CreateFolderContext();
        context.setParentId(userInfoVO.getRootFileId());
        context.setUserId(userId);
        context.setFolderName("folder-name-1");

        Long folder1 = userFileService.createFolder(context);
        Assert.notNull(folder1);

        context.setParentId(folder1);
        context.setFolderName("folder-name-2");
        Long folder2 = userFileService.createFolder(context);
        Assert.notNull(folder2);

        TransferFileContext transferFileContext = new TransferFileContext();
        transferFileContext.setTargetParentId(folder2);
        transferFileContext.setFileIdList(Lists.newArrayList(folder1));
        transferFileContext.setUserId(userId);
        userFileService.transfer(transferFileContext);
    }

    /**********************************************************************文件复制********************************************************************************/

    /**
     * 测试文件复制成功
     */
    @Test
    public void testCopyFileSuccess() {
        Long userId = register();
        UserInfoVO userInfoVO = info(userId);

        CreateFolderContext context = new CreateFolderContext();
        context.setParentId(userInfoVO.getRootFileId());
        context.setUserId(userId);
        context.setFolderName("folder-name-1");

        Long folder1 = userFileService.createFolder(context);
        Assert.notNull(folder1);

        context.setFolderName("folder-name-2");
        Long folder2 = userFileService.createFolder(context);
        Assert.notNull(folder2);

        CopyFileContext copyFileContext = new CopyFileContext();
        copyFileContext.setTargetParentId(folder1);
        copyFileContext.setFileIdList(Lists.newArrayList(folder2));
        copyFileContext.setUserId(userId);
        userFileService.copy(copyFileContext);

        QueryFileListContext queryFileListContext = new QueryFileListContext();
        queryFileListContext.setParentId(folder1);
        queryFileListContext.setUserId(userId);
        queryFileListContext.setDelFlag(DelFlagEnum.NO.getCode());
        List<UserFileVO> records = userFileService.getFileList(queryFileListContext);
        Assert.notEmpty(records);
    }

    /**
     * 测试文件复制失败，目标文件夹是要转移的文件列表中的文件夹或者是其子文件夹
     */
    @Test(expected = BusinessException.class)
    public void testCopyFileFail() {
        Long userId = register();
        UserInfoVO userInfoVO = info(userId);

        CreateFolderContext context = new CreateFolderContext();
        context.setParentId(userInfoVO.getRootFileId());
        context.setUserId(userId);
        context.setFolderName("folder-name-1");

        Long folder1 = userFileService.createFolder(context);
        Assert.notNull(folder1);

        context.setParentId(folder1);
        context.setFolderName("folder-name-2");
        Long folder2 = userFileService.createFolder(context);
        Assert.notNull(folder2);

        CopyFileContext copyFileContext = new CopyFileContext();
        copyFileContext.setTargetParentId(folder2);
        copyFileContext.setFileIdList(Lists.newArrayList(folder1));
        copyFileContext.setUserId(userId);
        userFileService.copy(copyFileContext);
    }

    /**********************************************************************文件搜索********************************************************************************/

    /**
     * 测试文件搜索成功
     */
    @Test
    public void testSearchSuccess() {
        Long userId = register();
        UserInfoVO userInfoVO = info(userId);

        CreateFolderContext context = new CreateFolderContext();
        context.setParentId(userInfoVO.getRootFileId());
        context.setUserId(userId);
        context.setFolderName("folder-name-1");

        Long folder1 = userFileService.createFolder(context);
        Assert.notNull(folder1);

        FileSearchContext fileSearchContext = new FileSearchContext();
        fileSearchContext.setUserId(userId);
        fileSearchContext.setKeyword("folder-name");
        List<FileSearchResultVO> result = userFileService.search(fileSearchContext);
        Assert.notEmpty(result);

        fileSearchContext.setKeyword("name-1");
        result = userFileService.search(fileSearchContext);
        Assert.isTrue(CollectionUtils.isEmpty(result));
    }


    /**********************************************************************查询面包屑导航列表********************************************************************************/


    /**
     * 测试查询文件面包屑导航列表成功
     */
    @Test
    public void testGetBreadcrumbsSuccess() {
        Long userId = register();
        UserInfoVO userInfoVO = info(userId);

        CreateFolderContext context = new CreateFolderContext();
        context.setParentId(userInfoVO.getRootFileId());
        context.setUserId(userId);
        context.setFolderName("folder-name-1");

        Long folder1 = userFileService.createFolder(context);
        Assert.notNull(folder1);

        QueryBreadcrumbsContext queryBreadcrumbsContext = new QueryBreadcrumbsContext();
        queryBreadcrumbsContext.setFileId(folder1);
        queryBreadcrumbsContext.setUserId(userId);

        List<BreadcrumbVO> result = userFileService.getBreadcrumbs(queryBreadcrumbsContext);
        Assert.notEmpty(result);
        Assert.isTrue(result.size() == 2);
    }




    /**********************************************************************私有方法********************************************************************************/

    /**
     * 文件分片上传器
     */
    @AllArgsConstructor
    private static class ChunkUploader extends Thread {

        private CountDownLatch countDownLatch;

        private Integer chunk;

        private Integer chunks;

        private IUserFileService iUserFileService;

        private Long userId;

        private Long parentId;

        /**
         * 1、上传文件分片
         * 2、根据上传的结果来调用文件分片合并
         */
        @Override
        public void run() {
            super.run();
            MultipartFile file = genarateMultipartFile();
            Long totalSize = file.getSize() * chunks;
            String filename = "test.txt";
            String identifier = "123456789";

            FileChunkUploadContext fileChunkUploadContext = new FileChunkUploadContext();
            fileChunkUploadContext.setFilename(filename);
            fileChunkUploadContext.setIdentifier(identifier);
            fileChunkUploadContext.setTotalChunks(chunks);
            fileChunkUploadContext.setChunkNumber(chunk);
            fileChunkUploadContext.setCurrentChunkSize(file.getSize());
            fileChunkUploadContext.setTotalSize(totalSize);
            fileChunkUploadContext.setFile(file);
            fileChunkUploadContext.setUserId(userId);

            FileChunkUploadVO fileChunkUploadVO = iUserFileService.chunkUpload(fileChunkUploadContext);

            if (fileChunkUploadVO.getMergeFlag().equals(MergeFlagEnum.READY.getCode())) {
                System.out.println("分片 " + chunk + " 检测到可以合并分片");

                FileChunkMergeContext fileChunkMergeContext = new FileChunkMergeContext();
                fileChunkMergeContext.setFilename(filename);
                fileChunkMergeContext.setIdentifier(identifier);
                fileChunkMergeContext.setTotalSize(totalSize);
                fileChunkMergeContext.setParentId(parentId);
                fileChunkMergeContext.setUserId(userId);

                iUserFileService.mergeFile(fileChunkMergeContext);
                countDownLatch.countDown();
            } else {
                countDownLatch.countDown();
            }

        }

    }

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
