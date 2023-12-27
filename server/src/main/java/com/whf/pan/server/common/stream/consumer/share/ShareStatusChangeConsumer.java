package com.whf.pan.server.common.stream.consumer.share;

import com.whf.pan.server.common.stream.event.file.DeleteFileEvent;
import com.whf.pan.server.common.stream.event.file.FileRestoreEvent;
import com.whf.pan.server.common.stream.channel.PanChannels;
import com.whf.pan.server.modules.file.entity.UserFile;
import com.whf.pan.server.modules.file.enums.DelFlagEnum;
import com.whf.pan.server.modules.file.service.IUserFileService;
import com.whf.pan.server.modules.share.service.IShareService;
import com.whf.pan.stream.core.AbstractConsumer;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author whf
 * @className ShareStatusChangeListener
 * @description 监听文件状态变更导致分享状态变更的处理器
 * @date 2023/12/22 16:17
 */
@Component
public class ShareStatusChangeConsumer extends AbstractConsumer {

    @Resource
    private IUserFileService userFileService;

    @Resource
    private IShareService shareService;

    /**
     * 监听文件被删除之后，刷新所有受影响的分享的状态
     *
     * @param message
     */
    @StreamListener(PanChannels.DELETE_FILE_INPUT)
    public void changeShare2FileDeleted(Message<DeleteFileEvent> message) {
        if (isEmptyMessage(message)) {
            return;
        }
        printLog(message);
        DeleteFileEvent event = message.getPayload();
        List<Long> fileIdList = event.getFileIdList();
        if (CollectionUtils.isEmpty(fileIdList)) {
            return;
        }
        List<UserFile> allRecords = userFileService.findAllFileRecordsByFileIdList(fileIdList);
        List<Long> allAvailableFileIdList = allRecords.stream()
                .filter(record -> Objects.equals(record.getDelFlag(), DelFlagEnum.NO.getCode()))
                .map(UserFile::getFileId)
                .collect(Collectors.toList());
        allAvailableFileIdList.addAll(fileIdList);
        shareService.refreshShareStatus(allAvailableFileIdList);
    }

    /**
     * 监听文件被还原后，刷新所有受影响的分享的状态
     *
     * @param message
     */
    @StreamListener(PanChannels.FILE_RESTORE_INPUT)
    public void changeShare2Normal(Message<FileRestoreEvent> message) {
        if (isEmptyMessage(message)) {
            return;
        }
        printLog(message);
        FileRestoreEvent event = message.getPayload();
        List<Long> fileIdList = event.getFileIdList();
        if (CollectionUtils.isEmpty(fileIdList)) {
            return;
        }
        List<UserFile> allRecords = userFileService.findAllFileRecordsByFileIdList(fileIdList);
        List<Long> allAvailableFileIdList = allRecords.stream()
                .filter(record -> Objects.equals(record.getDelFlag(), DelFlagEnum.NO.getCode()))
                .map(UserFile::getFileId)
                .collect(Collectors.toList());
        allAvailableFileIdList.addAll(fileIdList);
        shareService.refreshShareStatus(allAvailableFileIdList);
    }
}
