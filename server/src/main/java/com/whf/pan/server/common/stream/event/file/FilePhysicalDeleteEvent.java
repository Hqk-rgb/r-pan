package com.whf.pan.server.common.stream.event.file;

import com.whf.pan.server.modules.file.entity.UserFile;
import lombok.*;
import org.springframework.context.ApplicationEvent;

import java.io.Serializable;
import java.util.List;

/**
 * @author whf
 * @className FilePhysicalDeleteEvent
 * @description 文件被物理删除的事件实体
 * @date 2023/12/2 18:12
 */
@Getter
@Setter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
public class FilePhysicalDeleteEvent implements Serializable {

    private static final long serialVersionUID = 984874786997980633L;
    /**
      * 所有被物理删除的文件实体集合
      */
    private List<UserFile> allRecords;

    public FilePhysicalDeleteEvent(List<UserFile> allRecords) {
        this.allRecords = allRecords;
    }
}
