package com.whf.pan.server.common.event.file;

import com.whf.pan.server.modules.file.entity.UserFile;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.context.ApplicationEvent;

import java.util.List;

/**
 * @author whf
 * @className FilePhysicalDeleteEvent
 * @description TODO
 * @date 2023/12/2 18:12
 */
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class FilePhysicalDeleteEvent extends ApplicationEvent {

    /**
      * 所有被物理删除的文件实体集合
      */
    private List<UserFile> allRecords;

    public FilePhysicalDeleteEvent(Object source, List<UserFile> allRecords) {
        super(source);
        this.allRecords = allRecords;
    }
}
