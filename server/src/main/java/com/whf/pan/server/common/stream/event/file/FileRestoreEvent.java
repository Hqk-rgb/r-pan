package com.whf.pan.server.common.stream.event.file;

import lombok.*;
import org.springframework.context.ApplicationEvent;

import java.io.Serializable;
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
@NoArgsConstructor
public class FileRestoreEvent implements Serializable {

    private static final long serialVersionUID = 3950778244739809898L;
    /**
      * 被成功还原的文件记录ID集合
      */
    private List<Long> fileIdList;

    public FileRestoreEvent(List<Long> fileIdList) {
        this.fileIdList = fileIdList;
    }
}
