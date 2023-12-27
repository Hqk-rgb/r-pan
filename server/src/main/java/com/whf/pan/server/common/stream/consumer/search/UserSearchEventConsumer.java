package com.whf.pan.server.common.stream.consumer.search;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.whf.pan.core.utils.IdUtil;
import com.whf.pan.server.common.stream.channel.PanChannels;
import com.whf.pan.server.common.stream.event.search.UserSearchEvent;
import com.whf.pan.server.modules.user.entity.UserSearchHistory;
import com.whf.pan.server.modules.user.service.IUserSearchHistoryService;
import com.whf.pan.stream.core.AbstractConsumer;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.context.event.EventListener;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.messaging.Message;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author whf
 * @className UserSearchEventListener
 * @description 用户搜索事件监听器
 * @date 2023/11/19 15:00
 */
@Component
public class UserSearchEventConsumer extends AbstractConsumer {

    @Resource
    private IUserSearchHistoryService userSearchHistoryService;

    /**
     * 监听用户搜索事件，将其保存到用户的搜索历史记录当中
     *
     * @param message
     */
    @StreamListener(PanChannels.USER_SEARCH_INPUT)
    public void saveSearchHistory(Message<UserSearchEvent> message) {
        if (isEmptyMessage(message)) {
            return;
        }
        printLog(message);
        UserSearchEvent event = message.getPayload();
        UserSearchHistory record = new UserSearchHistory();

        record.setId(IdUtil.get());
        record.setUserId(event.getUserId());
        record.setSearchContent(event.getKeyword());
        record.setCreateTime(new Date());
        record.setUpdateTime(new Date());

        try {
            userSearchHistoryService.save(record);
        } catch (DuplicateKeyException e) {
            UpdateWrapper updateWrapper = Wrappers.update();
            updateWrapper.eq("user_id", event.getUserId());
            updateWrapper.eq("search_content", event.getKeyword());
            updateWrapper.set("update_time", new Date());
            userSearchHistoryService.update(updateWrapper);
        }

    }
}
