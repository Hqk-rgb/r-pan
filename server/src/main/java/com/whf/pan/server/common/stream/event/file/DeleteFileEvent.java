package com.whf.pan.server.common.stream.event.file;

import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * @author whf
 * @className DeleteFileEvent
 * @description 文件删除事件
 * @date 2023/11/4 13:25
 */
@Getter
@Setter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
public class DeleteFileEvent  implements Serializable {

    private static final long serialVersionUID = 3838342730503794584L;

    private List<Long> fileIdList;

    public DeleteFileEvent(List<Long> fileIdList) {
        this.fileIdList = fileIdList;
    }
}
