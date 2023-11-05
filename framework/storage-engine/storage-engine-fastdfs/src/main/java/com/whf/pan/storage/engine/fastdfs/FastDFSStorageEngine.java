package com.whf.pan.storage.engine.fastdfs;

import com.whf.pan.storage.engine.core.AbstractStorageEngine;
import com.whf.pan.storage.engine.core.context.DeleteFileContext;
import com.whf.pan.storage.engine.core.context.StoreFileContext;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author whf
 * @className FastDFSStorageEngine
 * @description 基于 FastDFS 实现的文件存储引擎
 * @date 2023/11/5 13:03
 */
@Component
public class FastDFSStorageEngine extends AbstractStorageEngine {
    @Override
    protected void doStore(StoreFileContext context) throws IOException {

    }

    @Override
    protected void doDelete(DeleteFileContext context) throws IOException {

    }
}
