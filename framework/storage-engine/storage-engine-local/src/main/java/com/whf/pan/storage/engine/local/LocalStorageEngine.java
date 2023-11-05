package com.whf.pan.storage.engine.local;

import com.whf.pan.core.utils.FileUtil;
import com.whf.pan.storage.engine.core.AbstractStorageEngine;
import com.whf.pan.storage.engine.core.context.DeleteFileContext;
import com.whf.pan.storage.engine.core.context.StoreFileContext;
import com.whf.pan.storage.engine.local.config.LocalStorageEngineConfig;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;

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
}
