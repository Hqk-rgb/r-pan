package com.whf.pan.storage.engine.local;

import com.whf.pan.core.utils.FileUtil;
import com.whf.pan.storage.engine.core.AbstractStorageEngine;
import com.whf.pan.storage.engine.core.context.*;
import com.whf.pan.storage.engine.local.config.LocalStorageEngineConfig;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

/**
 * @author whf
 * @className LocalStorageEngine
 * @description 本地文件存储引擎实现类
 * @date 2023/11/5 12:46
 */
@Component
public class LocalStorageEngine extends AbstractStorageEngine {

    @Resource
    private LocalStorageEngineConfig config;

    /**
     * 执行保存物理文件的动作
     * 下沉到具体的子类去实现
     *
     * @param context
     */
    @Override
    protected void doStore(StoreFileContext context) throws IOException {
        String basePath = config.getRootFilePath();
        String realFilePath = FileUtil.generateStoreFileRealPath(basePath, context.getFilename());
        FileUtil.writeStream2File(context.getInputStream(), new File(realFilePath), context.getTotalSize());
        context.setRealPath(realFilePath);
    }

    /**
     * 执行删除物理文件的动作
     * 下沉到子类去实现
     *
     * @param context
     * @throws IOException
     */
    @Override
    protected void doDelete(DeleteFileContext context) throws IOException {
        FileUtil.deleteFiles(context.getRealFilePathList());
    }

    /**
     * 执行保存文件分片
     * 下沉到底层去实现
     *
     * @param context
     * @throws IOException
     */
    @Override
    protected void doStoreChunk(StoreFileChunkContext context) throws IOException {
        String basePath = config.getRootFileChunkPath();
        String realFilePath = FileUtil.generateStoreFileChunkRealPath(basePath, context.getIdentifier(),context.getChunkNumber());
        FileUtil.writeStream2File(context.getInputStream(), new File(realFilePath), context.getTotalSize());
        context.setRealPath(realFilePath);
    }

    /**
     * 执行文件分片合并的动作
     * 下沉到子类实现
     *
     * @param context
     */
    @Override
    protected void doMergeFile(MergeFileContext context) throws IOException {
        String basePath = config.getRootFilePath();
        String realFilePath = FileUtil.generateStoreFileRealPath(basePath, context.getFilename());
        FileUtil.createFile(new File(realFilePath));
        List<String> chunkPaths = context.getRealPathList();
        for (String chunkPath : chunkPaths) {
            FileUtil.appendWrite(Paths.get(realFilePath), new File(chunkPath).toPath());
        }
        FileUtil.deleteFiles(chunkPaths);
        context.setRealPath(realFilePath);
    }

    /**
     * 读取文件内容并写入到输出流中
     * 下沉到子类去实现
     *
     * @param context
     */
    @Override
    protected void doReadFile(ReadFileContext context) throws IOException {
        File file = new File(context.getRealPath());
        FileUtil.writeFile2OutputStream(new FileInputStream(file), context.getOutputStream(), file.length());
    }


}
