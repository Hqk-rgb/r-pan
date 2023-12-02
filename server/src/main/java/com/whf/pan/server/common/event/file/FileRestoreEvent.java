package com.whf.pan.server.common.event.file;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

import java.util.List;

/**
 * @author whf
 * @className FileRestoreEvent
 * @description 文件还原事件实体
 * @date 2023/12/2 15:32
 */
@EqualsAndHashCode
@ToString
@Getter
@Setter
public class FileRestoreEvent extends ApplicationEvent {

    /**
      * 被成功还原的文件记录ID集合
      */
    private List<Long> fileIdList;

    public FileRestoreEvent(Object source, List<Long> fileIdList) {
        super(source);
        this.fileIdList = fileIdList;
    }
}
