package com.whf.pan.server.modules.file.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.whf.pan.core.exception.BusinessException;
import com.whf.pan.core.utils.IdUtil;
import com.whf.pan.server.modules.file.context.FileChunkSaveContext;
import com.whf.pan.server.modules.file.converter.FileConverter;
import com.whf.pan.server.modules.file.entity.FileChunk;
import com.whf.pan.server.modules.file.enums.MergeFlagEnum;
import com.whf.pan.server.modules.file.service.IFileChunkService;
import com.whf.pan.server.modules.file.mapper.FileChunkMapper;
import com.whf.pan.storage.engine.core.StorageEngine;
import com.whf.pan.storage.engine.core.context.StoreFileChunkContext;
import org.springframework.stereotype.Service;
//import com.whf.pan.lock.core.annotation.Lock;
import com.whf.pan.server.common.config.ServerConfig;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Date;

/**
* @author 26570
* @description 针对表【r_pan_file_chunk(文件分片信息表)】的数据库操作Service实现
* @createDate 2023-10-28 15:45:30
*/
@Service
public class FileChunkServiceImpl extends ServiceImpl<FileChunkMapper, FileChunk> implements IFileChunkService {

    @Resource
    private ServerConfig config;

    @Resource
    private FileConverter fileConverter;

    @Resource
    private StorageEngine storageEngine;

    /**
     * 文件分片保存
     * <p>
     * 1、保存文件分片和记录
     * 2、判断文件分片是否全部上传完成
     *
     * @param context
     */
    //@Lock(name = "saveChunkFileLock", keys = {"#context.userId", "#context.identifier"}, expireSecond = 10L)
    @Override
    public synchronized void saveChunkFile(FileChunkSaveContext context) {
        doSaveChunkFile(context);
        doJudgeMergeFile(context);
    }

    /**
     * 判断是否所有的分片均没上传完成
     *
     * @param context
     */
    private void doJudgeMergeFile(FileChunkSaveContext context) {
        QueryWrapper queryWrapper = Wrappers.query();
        queryWrapper.eq("identifier", context.getIdentifier());
        queryWrapper.eq("create_user", context.getUserId());
        int count = count(queryWrapper);
        if (count == context.getTotalChunks().intValue()) {
            context.setMergeFlagEnum(MergeFlagEnum.READY);
        }
    }

    /**
     * 执行文件分片上传保存的操作
     * <p>
     * 1、委托文件存储引擎存储文件分片
     * 2、保存文件分片记录
     *
     * @param context
     */
    private void doSaveChunkFile(FileChunkSaveContext context) {
        doStoreFileChunk(context);
        doSaveRecord(context);
    }

    /**
     * 委托文件存储引擎保存文件分片
     *
     * @param context
     */
    private void doStoreFileChunk(FileChunkSaveContext context) {
        try {
            StoreFileChunkContext storeFileChunkContext = fileConverter.fileChunkSaveContextTOStoreFileChunkContext(context);
            storeFileChunkContext.setInputStream(context.getFile().getInputStream());
            storageEngine.storeChunk(storeFileChunkContext);
            context.setRealPath(storeFileChunkContext.getRealPath());
        } catch (IOException e) {
            e.printStackTrace();
            throw new BusinessException("文件分片上传失败");
        }
    }
    /**
     * 保存文件分片记录
     *
     * @param context
     */
    private void doSaveRecord(FileChunkSaveContext context) {
        FileChunk record = FileChunk.builder()
                .id(IdUtil.get())
                .identifier(context.getIdentifier())
                .realPath(context.getRealPath())
                .chunkNumber(context.getChunkNumber())
                .expirationTime(DateUtil.offsetDay(new Date(),config.getChunkFileExpirationDays()))
                .createUser(context.getUserId())
                .createTime(new Date())
                .build();
//        FileChunk record = new FileChunk();
//        record.setId(IdUtil.get());
//        record.setIdentifier(context.getIdentifier());
//        record.setRealPath(context.getRealPath());
//        record.setChunkNumber(context.getChunkNumber());
//        record.setExpirationTime(DateUtil.offsetDay(new Date(), config.getChunkFileExpirationDays()));
//        record.setCreateUser(context.getUserId());
//        record.setCreateTime(new Date());
        if (!save(record)) {
            throw new BusinessException("文件分片上传失败");
        }
    }
}




