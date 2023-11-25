package com.whf.pan.storage.engine.local.initializer;

import com.whf.pan.storage.engine.local.config.LocalStorageEngineConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;

/**
 * @author whf
 * @className UploadFolderAndChunksFolderInitializer
 * @description 初始化上传文件根目录和文件分片存储根目录的初始化器
 * @date 2023/11/25 16:51
 */
@Component
@Slf4j
public class UploadFolderAndChunksFolderInitializer implements CommandLineRunner {

    @Resource
    private LocalStorageEngineConfig config;

    @Override
    public void run(String... args) throws Exception {
        FileUtils.forceMkdir(new File(config.getRootFilePath()));
        log.info("文件存储根目录已被创建！\nthe root file path has been created!");
        FileUtils.forceMkdir(new File(config.getRootFileChunkPath()));
        log.info("文件分片存储根目录已被创建！\nthe root file chunk path has been created!");
    }
}
