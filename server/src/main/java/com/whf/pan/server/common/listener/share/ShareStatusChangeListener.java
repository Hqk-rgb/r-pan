package com.whf.pan.server.common.listener.share;

import com.whf.pan.server.common.event.file.DeleteFileEvent;
import com.whf.pan.server.common.event.file.FileRestoreEvent;
import com.whf.pan.server.modules.file.entity.UserFile;
import com.whf.pan.server.modules.file.enums.DelFlagEnum;
import com.whf.pan.server.modules.file.service.IUserFileService;
import com.whf.pan.server.modules.share.service.IShareService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.context.event.EventListener;

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
public class ShareStatusChangeListener {

    @Resource
    private IUserFileService userFileService;

    @Resource
    private IShareService shareService;

    /**
     * 监听文件被删除之后，刷新所有受影响的分享的状态
     *
     * @param event
     */
    @Async(value = "eventListenerTaskExecutor")
    @EventListener(DeleteFileEvent.class)
    public void changeShare2FileDeleted(DeleteFileEvent event) {
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
     * @param event
     */
    @Async(value = "eventListenerTaskExecutor")
    @EventListener(FileRestoreEvent.class)
    public void changeShare2Normal(FileRestoreEvent event) {
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
