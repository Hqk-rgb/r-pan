package com.whf.pan.server.common.schedule.task;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.whf.pan.core.constants.Constants;
import com.whf.pan.schedule.ScheduleTask;
import com.whf.pan.server.common.event.log.ErrorLogEvent;
import com.whf.pan.server.modules.file.entity.FileChunk;
import com.whf.pan.server.modules.file.service.IFileChunkService;
import com.whf.pan.storage.engine.core.StorageEngine;
import com.whf.pan.storage.engine.core.context.DeleteFileContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author whf
 * @className CleanExpireChunkFileTask
 * @description 过期分片清理任务
 * @date 2023/11/26 10:27
 */
@Component
@Slf4j
public class CleanExpireChunkFileTask implements ScheduleTask, ApplicationContextAware {

    private static final Long BATCH_SIZE = 500L;

    @Resource
    private IFileChunkService iFileChunkService;

    @Resource
    private StorageEngine storageEngine;

    private ApplicationContext applicationContext;


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

    }

    /**
     * 获取定时任务的名称
     *
     * @return
     */
    @Override
    public String getName() {
        return "CleanExpireChunkFileTask";
    }

    /**
     * 执行清理任务
     * <p>
     * 1、滚动查询过期的文件分片
     * 2、删除物理文件（委托文件存储引擎去实现）
     * 3、删除过期文件分片的记录信息
     * 4、重置上次查询的最大文件分片记录ID，继续滚动查询
     */
    @Override
    public void run() {
        log.info("{} 开始清理过期分片文件 \n start clean expire chunk file...", getName());

        List<FileChunk> expireFileChunkRecords;
        Long scrollPointer = 1L;

        do {
            expireFileChunkRecords = scrollQueryExpireFileChunkRecords(scrollPointer);
            if (CollectionUtils.isNotEmpty(expireFileChunkRecords)) {
                deleteRealChunkFiles(expireFileChunkRecords);
                List<Long> idList = deleteChunkFileRecords(expireFileChunkRecords);
                scrollPointer = Collections.max(idList);
            }
        } while (CollectionUtils.isNotEmpty(expireFileChunkRecords));

        log.info("{} 结束清理过期分片文件 \n finish clean expire chunk file...", getName());
    }

    /*********************************************private*********************************************/

    /**
     * 滚动查询过期的文件分片记录
     *
     * @param scrollPointer
     * @return
     */
    private List<FileChunk> scrollQueryExpireFileChunkRecords(Long scrollPointer) {
        LambdaQueryWrapper<FileChunk> wrapper = new LambdaQueryWrapper<>();
        wrapper.le(FileChunk::getExpirationTime, new Date())
                .ge(FileChunk::getId, scrollPointer)
                .last(" limit " + BATCH_SIZE);
        return iFileChunkService.list(wrapper);
//        QueryWrapper queryWrapper = Wrappers.query();
//        queryWrapper.le("expiration_time", new Date());
//        queryWrapper.ge("id", scrollPointer);
//        queryWrapper.last(" limit " + BATCH_SIZE);
//        return iFileChunkService.list(queryWrapper);
    }

    /**
     * 物理删除过期的文件分片文件实体
     *
     * @param expireFileChunkRecords
     */
    private void deleteRealChunkFiles(List<FileChunk> expireFileChunkRecords) {
        DeleteFileContext deleteFileContext = new DeleteFileContext();
        List<String> realPaths = expireFileChunkRecords.stream().map(FileChunk::getRealPath).collect(Collectors.toList());
        deleteFileContext.setRealFilePathList(realPaths);
        try {
            storageEngine.delete(deleteFileContext);
        } catch (IOException e) {
            saveErrorLog(realPaths);
        }
    }

    /**
     * @param realPaths
     */
    private void saveErrorLog(List<String> realPaths) {
        ErrorLogEvent event = new ErrorLogEvent(this,"文件物理删除失败，请手动执行文件删除！文件路径为：" + JSON.toJSONString(realPaths), Constants.ZERO_LONG);
        applicationContext.publishEvent(event);
        //producer.sendMessage(PanChannels.ERROR_LOG_OUTPUT, event);
    }

    /**
     * 删除过期文件分片记录
     *
     * @param expireFileChunkRecords
     * @return
     */
    private List<Long> deleteChunkFileRecords(List<FileChunk> expireFileChunkRecords) {
        List<Long> idList = expireFileChunkRecords.stream().map(FileChunk::getId).collect(Collectors.toList());
        iFileChunkService.removeByIds(idList);
        return idList;
    }


}
