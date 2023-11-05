package com.whf.pan.storage.engine.core;


import com.whf.pan.storage.engine.core.context.DeleteFileContext;
import com.whf.pan.storage.engine.core.context.StoreFileContext;

import java.io.IOException;

/**
 * @author whf
 * @className StorageEngine
 * @description 文件存储引擎的顶级接口
 * 该接口定义所有需要向外暴露给业务层面的相关文件操作的功能
 * 业务方只能调用该接口的方法，而不能直接使用具体的实现方案去做业务调用
 * @date 2023/11/5 11:50
 */
public interface StorageEngine {
    /**
     * 存储物理文件
     *
     * @param context
     * @throws IOException
     */
    void store(StoreFileContext context) throws IOException;

    /**
     * 删除物理文件
     *
     * @param context
     * @throws IOException
     */
    void delete(DeleteFileContext context) throws IOException;

}
