package com.whf.pan.storage.engine.oss;

import com.whf.pan.storage.engine.core.AbstractStorageEngine;
import com.whf.pan.storage.engine.core.context.DeleteFileContext;
import com.whf.pan.storage.engine.core.context.StoreFileChunkContext;
import com.whf.pan.storage.engine.core.context.StoreFileContext;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author whf
 * @className OSSStorageEngine
 * @description 基于 OSS 实现的文件存储引擎
 * @date 2023/11/5 13:06
 */
@Component
public class OSSStorageEngine extends AbstractStorageEngine {
    @Override
    protected void doStore(StoreFileContext context) throws IOException {

    }

    @Override
    protected void doDelete(DeleteFileContext context) throws IOException {

    }

    @Override
    protected void doStoreChunk(StoreFileChunkContext context) throws IOException {

    }
}
