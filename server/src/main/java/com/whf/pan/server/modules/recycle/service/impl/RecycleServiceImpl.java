package com.whf.pan.server.modules.recycle.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.whf.pan.core.constants.Constants;
import com.whf.pan.core.exception.BusinessException;
import com.whf.pan.server.common.event.file.FilePhysicalDeleteEvent;
import com.whf.pan.server.common.event.file.FileRestoreEvent;
import com.whf.pan.server.modules.file.context.QueryFileListContext;
import com.whf.pan.server.modules.file.entity.UserFile;
import com.whf.pan.server.modules.file.enums.DelFlagEnum;
import com.whf.pan.server.modules.file.service.IUserFileService;
import com.whf.pan.server.modules.file.vo.UserFileVO;
import com.whf.pan.server.modules.recycle.context.DeleteContext;
import com.whf.pan.server.modules.recycle.context.QueryRecycleFileListContext;
import com.whf.pan.server.modules.recycle.context.RestoreContext;
import com.whf.pan.server.modules.recycle.service.IRecycleService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author whf
 * @className RecycleServiceImpl
 * @description 回收站模块业务处理类
 * @date 2023/12/2 14:29
 */
@Service
public class RecycleServiceImpl implements IRecycleService, ApplicationContextAware {

    @Resource
    private IUserFileService userFileService;

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * 查询用户的回收站文件列表
     *
     * @param context
     * @return
     */
    @Override
    public List<UserFileVO> recycles(QueryRecycleFileListContext context) {
        QueryFileListContext queryFileListContext = new QueryFileListContext();
        queryFileListContext.setUserId(context.getUserId());
        queryFileListContext.setDelFlag(DelFlagEnum.YES.getCode());
        return userFileService.getFileList(queryFileListContext);
    }

    /**
     * 文件还原
     * <p>
     * 1、检查操作权限
     * 2、检查是不是可以还原
     * 3、执行文件还原的操作
     * 4、执行文件还原的后置操作
     *
     * @param context
     */
    @Override
    public void restore(RestoreContext context) {
        checkRestorePermission(context);
        checkRestoreFilename(context);
        doRestore(context);
        afterRestore(context);
    }


    /**
     * 检查文件还原的操作权限
     *
     * @param context
     */
    private void checkRestorePermission(RestoreContext context) {
        List<Long> fileIdList = context.getFileIdList();
        List<UserFile> records = userFileService.listByIds(fileIdList);
        if (CollectionUtils.isEmpty(records)) {
            throw new BusinessException("文件还原失败");
        }
        Set<Long> userIdSet = records.stream().map(UserFile::getUserId).collect(Collectors.toSet());
        if (userIdSet.size() > 1) {
            throw new BusinessException("您无权执行文件还原");
        }

        if (!userIdSet.contains(context.getUserId())) {
            throw new BusinessException("您无权执行文件还原");
        }
        context.setRecords(records);
    }

    /**
     * 检查要还原的文件名称是不是被占用
     * <p>
     * 1、要还原的文件列表中有同一个文件夹下面相同名称的文件 不允许还原
     * 2、要还原的文件当前的父文件夹下面存在同名文件，我们不允许还原
     *
     * @param context
     */
    private void checkRestoreFilename(RestoreContext context) {
        List<UserFile> records = context.getRecords();

        Set<String> filenameSet = records.stream().map(record -> record.getFilename() + Constants.COMMON_SEPARATOR + record.getParentId()).collect(Collectors.toSet());
        if (filenameSet.size() != records.size()) {
            throw new BusinessException("文件还原失败，该还原文件中存在同名文件，请逐个还原并重命名");
        }

        for (UserFile record : records) {
            LambdaQueryWrapper<UserFile> wrapper = new LambdaQueryWrapper();
            wrapper.eq(UserFile::getUserId, context.getUserId())
                    .eq(UserFile::getParentId,record.getParentId())
                    .eq(UserFile::getFilename, record.getFilename())
                    .eq(UserFile::getDelFlag, DelFlagEnum.NO.getCode());
//            QueryWrapper queryWrapper = Wrappers.query();
//            queryWrapper.eq("user_id", context.getUserId());
//            queryWrapper.eq("parent_id", record.getParentId());
//            queryWrapper.eq("filename", record.getFilename());
//            queryWrapper.eq("del_flag", DelFlagEnum.NO.getCode());
            if (userFileService.count(wrapper) > 0) {
                throw new BusinessException("文件: " + record.getFilename() + " 还原失败，该文件夹下面已经存在了相同名称的文件或者文件夹，请重命名之后再执行文件还原操作");
            }
        }
    }

    /**
     * 执行文件还原的动作
     *
     * @param context
     */
    private void doRestore(RestoreContext context) {
        List<UserFile> records = context.getRecords();
        records.stream().forEach(record -> {
            record.setDelFlag(DelFlagEnum.NO.getCode());
            record.setUpdateUser(context.getUserId());
            record.setUpdateTime(new Date());
        });
        boolean updateFlag = userFileService.updateBatchById(records);
        if (!updateFlag) {
            throw new BusinessException("文件还原失败");
        }
    }



    /**
     * 文件还原的后置操作
     * <p>
     * 1、发布文件还原事件
     *
     * @param context
     */
    private void afterRestore(RestoreContext context) {
        FileRestoreEvent event = new FileRestoreEvent(this,context.getFileIdList());
        applicationContext.publishEvent(event);
        //producer.sendMessage(PanChannels.FILE_RESTORE_OUTPUT, event);
    }

    /**
     * 文件彻底删除
     * <p>
     * 1、校验操作权限
     * 2、递归查找所有子文件
     * 3、执行文件删除的动作
     * 4、删除后的后置动作
     *
     * @param context
     */
    @Override
    public void delete(DeleteContext context) {
        checkFileDeletePermission(context);
        findAllFileRecords(context);
        doDelete(context);
        afterDelete(context);
    }

    /**
     * 校验文件删除的操作权限
     *
     * @param context
     */
    private void checkFileDeletePermission(DeleteContext context) {
        LambdaQueryWrapper<UserFile> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserFile::getUserId, context.getUserId())
                .in(UserFile::getFileId, context.getFileIdList());
//        QueryWrapper queryWrapper = Wrappers.query();
//        queryWrapper.eq("user_id", context.getUserId());
//        queryWrapper.in("file_id", context.getFileIdList());
        List<UserFile> records = userFileService.list(wrapper);
        if (CollectionUtils.isEmpty(records) || records.size() != context.getFileIdList().size()) {
            throw new BusinessException("您无权删除该文件");
        }
        context.setRecords(records);
    }

    /**
     * 递归查询所有的子文件
     *
     * @param context
     */
    private void findAllFileRecords(DeleteContext context) {
        List<UserFile> records = context.getRecords();
        List<UserFile> allRecords = userFileService.findAllFileRecords(records);
        context.setAllRecords(allRecords);
    }

    /**
     * 执行文件删除的动作
     *
     * @param context
     */
    private void doDelete(DeleteContext context) {
        List<UserFile> allRecords = context.getAllRecords();
        List<Long> fileIdList = allRecords.stream().map(UserFile::getFileId).collect(Collectors.toList());
        if (!userFileService.removeByIds(fileIdList)) {
            throw new BusinessException("文件删除失败");
        }
    }

    /**
     * 文件彻底删除之后的后置函数
     * <p>
     * 1、发送一个文件彻底删除的事件
     *
     * @param context
     */
    private void afterDelete(DeleteContext context) {
        FilePhysicalDeleteEvent event = new FilePhysicalDeleteEvent(this,context.getAllRecords());
        applicationContext.publishEvent(event);
        // producer.sendMessage(PanChannels.PHYSICAL_DELETE_FILE_OUTPUT, event);
    }

}
