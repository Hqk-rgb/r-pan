package com.whf.pan.storage.engine.local.config;

import com.whf.pan.core.utils.FileUtil;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author whf
 * @className LocalStorageEngineConfig
 * @description TODO
 * @date 2023/11/5 16:38
 */
@Component
@ConfigurationProperties(prefix = "com.whf.pan.storage.engine.local")
@Data
public class LocalStorageEngineConfig {
    /**
     * 实际存放路径的前缀
     */
    private String rootFilePath = FileUtil.generateDefaultStoreFileRealPath();

    /**
     * 实际存放文件分片的路径的前缀
     */
    private String rootFileChunkPath = FileUtil.generateDefaultStoreFileChunkRealPath();
}
