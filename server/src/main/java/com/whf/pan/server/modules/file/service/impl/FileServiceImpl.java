package com.whf.pan.server.modules.file.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.google.common.collect.Lists;
import com.whf.pan.core.exception.BusinessException;
import com.whf.pan.core.utils.FileUtil;
import com.whf.pan.core.utils.IdUtil;
import com.whf.pan.server.common.event.log.ErrorLogEvent;
import com.whf.pan.server.modules.file.context.FileSaveContext;
import com.whf.pan.server.modules.file.entity.File;
import com.whf.pan.server.modules.file.service.IFileService;
import com.whf.pan.server.modules.file.mapper.FileMapper;
import com.whf.pan.storage.engine.core.StorageEngine;
import com.whf.pan.storage.engine.core.context.DeleteFileContext;
import com.whf.pan.storage.engine.core.context.StoreFileContext;
import com.whf.pan.storage.engine.local.LocalStorageEngine;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;

/**
* @author 26570
* @description 针对表【r_pan_file(物理文件信息表)】的数据库操作Service实现
* @createDate 2023-10-28 15:45:30
*/
@Service(value = "fileService")
public class FileServiceImpl extends ServiceImpl<FileMapper, File> implements IFileService, ApplicationContextAware {

    @Autowired
    private StorageEngine storageEngine;

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }


    /**
     * 上传单文件并保存实体记录
     * <p>
     * 1、上传单文件
     * 2、保存实体记录
     *
     * @param context
     */
    @Override
    public void saveFile(FileSaveContext context) {
        storeMultipartFile(context);
        File record = doSaveFile(context.getFilename(),
                context.getRealPath(),
                context.getTotalSize(),
                context.getIdentifier(),
                context.getUserId());
        context.setRecord(record);
    }

    /**
     * 上传单文件
     * 该方法委托文件存储引擎实现
     *
     * @param context
     */
    private void storeMultipartFile(FileSaveContext context) {
        try {
            StoreFileContext storeFileContext = new StoreFileContext();
            storeFileContext.setInputStream(context.getFile().getInputStream());
            storeFileContext.setFilename(context.getFilename());
            storeFileContext.setTotalSize(context.getTotalSize());
            storageEngine.store(storeFileContext);
            context.setRealPath(storeFileContext.getRealPath());
        } catch (IOException e) {
            e.printStackTrace();
            throw new BusinessException("文件上传失败");
        }
    }

    /**
     * 保存实体文件记录
     *
     * @param filename
     * @param realPath
     * @param totalSize
     * @param identifier
     * @param userId
     * @return
     */
    private File doSaveFile(String filename, String realPath, Long totalSize, String identifier, Long userId) {
        File record = assembleRPanFile(filename, realPath, totalSize, identifier, userId);
        if (!save(record)) {

            try {
                DeleteFileContext deleteFileContext = new DeleteFileContext();
                deleteFileContext.setRealFilePathList(Lists.newArrayList(realPath));
                storageEngine.delete(deleteFileContext);
            } catch (IOException e) {
                e.printStackTrace();
                ErrorLogEvent errorLogEvent = new ErrorLogEvent(this,"文件删除失败，请执行手动删除！文件路径: "+realPath , userId);
                applicationContext.publishEvent(errorLogEvent);
                /*ErrorLogEvent errorLogEvent = new ErrorLogEvent("文件物理删除失败，请执行手动删除！文件路径: {}" + realPath, userId);
                producer.sendMessage(PanChannels.ERROR_LOG_OUTPUT, errorLogEvent);*/
            }
        }
        return record;
    }

    /**
     * 拼装文件实体对象
     *
     * @param filename
     * @param realPath
     * @param totalSize
     * @param identifier
     * @param userId
     * @return
     */
    private File assembleRPanFile(String filename, String realPath, Long totalSize, String identifier, Long userId) {
        File record = new File();

        record.setFileId(IdUtil.get());
        record.setFilename(filename);
        record.setRealPath(realPath);
        record.setFileSize(String.valueOf(totalSize));
        record.setFileSizeDesc(FileUtil.byteCountToDisplaySize(totalSize));
        record.setFileSuffix(FileUtil.getFileSuffix(filename));
        record.setFilePreviewContentType(FileUtil.getContentType(realPath));
        record.setIdentifier(identifier);
        record.setCreateUser(userId);
        record.setCreateTime(new Date());

        return record;
    }


}




