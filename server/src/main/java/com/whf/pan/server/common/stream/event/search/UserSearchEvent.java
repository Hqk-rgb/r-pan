package com.whf.pan.server.common.stream.event.search;

import lombok.*;
import org.springframework.context.ApplicationEvent;


/**
 * @author whf
 * @className UserSearchEvent
 * @description 用户搜索事件
 * @date 2023/11/19 13:41
 */
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class UserSearchEvent extends ApplicationEvent {

    private static final long serialVersionUID = -1001802356886547037L;

    private String keyword;

    private Long userId;

    public UserSearchEvent(Object source,String keyword, Long userId) {
        super(source);
        this.keyword = keyword;
        this.userId = userId;
    }

}
