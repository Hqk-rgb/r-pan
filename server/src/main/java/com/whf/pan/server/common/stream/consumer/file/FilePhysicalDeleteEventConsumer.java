package com.whf.pan.server.common.stream.consumer.file;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.whf.pan.core.constants.Constants;
import com.whf.pan.server.common.stream.channel.PanChannels;
import com.whf.pan.server.common.stream.event.file.FilePhysicalDeleteEvent;
import com.whf.pan.server.common.stream.event.log.ErrorLogEvent;
import com.whf.pan.stream.core.AbstractConsumer;
import com.whf.pan.stream.core.IStreamProducer;
import com.whf.pan.server.modules.file.entity.File;
import com.whf.pan.server.modules.file.entity.UserFile;
import com.whf.pan.server.modules.file.enums.FolderFlagEnum;
import com.whf.pan.server.modules.file.service.IFileService;
import com.whf.pan.server.modules.file.service.IUserFileService;
import com.whf.pan.storage.engine.core.StorageEngine;
import com.whf.pan.storage.engine.core.context.DeleteFileContext;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.stream.annotation.StreamListener;

import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author whf
 * @className FilePhysicalDeleteEventListener
 * @description 文件物理删除监听器
 * @date 2023/12/3 12:08
 */
@Component
public class FilePhysicalDeleteEventConsumer extends AbstractConsumer {


    @Resource
    private IFileService fileService;

    @Resource
    private IUserFileService userFileService;

    @Resource
    private StorageEngine storageEngine;

    @Autowired
    @Qualifier(value = "defaultStreamProducer")
    private IStreamProducer producer;

    /**
     * 监听文件物理删除事件执行器
     * <p>
     * 该执行器是一个资源释放器，释放被物理删除的文件列表中关联的实体文件记录
     * <p>
     * 1、查询所有无引用的实体文件记录
     * 2、删除记录
     * 3、物理清理文件（委托文件存储引擎）
     *
     * @param message
     */
    @StreamListener(PanChannels.PHYSICAL_DELETE_FILE_INPUT)
    public void physicalDeleteFile(Message<FilePhysicalDeleteEvent> message) {
        if (isEmptyMessage(message)) {
            return;
        }
        printLog(message);
        FilePhysicalDeleteEvent event = message.getPayload();
        List<UserFile> allRecords = event.getAllRecords();
        if (CollectionUtils.isEmpty(allRecords)) {
            return;
        }
        List<Long> realFileIdList = findAllUnusedRealFileIdList(allRecords);
        if (CollectionUtils.isEmpty(realFileIdList)) {
            return;
        }
        List<File> realFileRecords = fileService.listByIds(realFileIdList);
        if (CollectionUtils.isEmpty(realFileRecords)) {
            return;
        }
        if (!fileService.removeByIds(realFileIdList)) {
            producer.sendMessage(PanChannels.ERROR_LOG_OUTPUT, new ErrorLogEvent("实体文件记录：" + JSON.toJSONString(realFileIdList) + "， 物理删除失败，请执行手动删除", Constants.ZERO_LONG));
            return;
        }
        physicalDeleteFileByStorageEngine(realFileRecords);
    }

    /*******************************************private*******************************************/

    /**
     * 委托文件存储引擎执行物理文件的删除
     *
     * @param realFileRecords
     */
    private void physicalDeleteFileByStorageEngine(List<File> realFileRecords) {
        List<String> realPathList = realFileRecords.stream().map(File::getRealPath).collect(Collectors.toList());
        DeleteFileContext deleteFileContext = new DeleteFileContext();
        deleteFileContext.setRealFilePathList(realPathList);
        try {
            storageEngine.delete(deleteFileContext);
        } catch (IOException e) {
            producer.sendMessage(PanChannels.ERROR_LOG_OUTPUT, new ErrorLogEvent("实体文件：" + JSON.toJSONString(realPathList) + "， 物理删除失败，请执行手动删除", Constants.ZERO_LONG));
        }
    }

    /**
     * 查找所有没有被引用的真实文件记录ID集合
     *
     * @param allRecords
     * @return
     */
    private List<Long> findAllUnusedRealFileIdList(List<UserFile> allRecords) {
        List<Long> realFileIdList = allRecords.stream()
                .filter(record -> Objects.equals(record.getFolderFlag(), FolderFlagEnum.NO.getCode()))
                .filter(this::isUnused)
                .map(UserFile::getRealFileId)
                .collect(Collectors.toList());
        return realFileIdList;
    }

    /**
     * 校验文件的真实文件ID是不是没有被引用了
     *
     * @param record
     * @return
     */
    private boolean isUnused(UserFile record) {
        QueryWrapper queryWrapper = Wrappers.query();
        queryWrapper.eq("real_file_id", record.getRealFileId());
        return userFileService.count(queryWrapper) == Constants.ZERO_INT.intValue();
    }

}
