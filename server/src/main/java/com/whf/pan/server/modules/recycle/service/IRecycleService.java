package com.whf.pan.server.modules.recycle.service;

import com.whf.pan.server.modules.file.vo.UserFileVO;
import com.whf.pan.server.modules.recycle.context.DeleteContext;
import com.whf.pan.server.modules.recycle.context.QueryRecycleFileListContext;
import com.whf.pan.server.modules.recycle.context.RestoreContext;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author whf
 * @className IRecycleService
 * @description 回收站模块业务处理接口
 * @date 2023/12/2 14:28
 */

public interface IRecycleService {

    /**
     * 查询用户的回收站文件列表
     *
     * @param context
     * @return
     */
    List<UserFileVO> recycles(QueryRecycleFileListContext context);

    /**
     * 文件还原
     *
     * @param context
     */
    void restore(RestoreContext context);

    /**
     * 文件彻底删除
     *
     * @param context
     */
    void delete(DeleteContext context);
}
