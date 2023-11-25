package com.whf.pan.storage.engine.oss.entity;

import lombok.*;

import java.io.Serializable;

/**
 * @author whf
 * @className ChunkUploadEntity
 * @description 文件分片上传树池化之后的全局信息载体
 * @date 2023/11/25 14:13
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChunkUploadEntity implements Serializable {

    private static final long serialVersionUID = -650380471420697548L;
    /**
     * 分片上传全局唯一的uploadId
     */
    public String uploadId;

    /**
     * 文件分片上传的实体名称
     */
    public String objectKey;
}
