package com.whf.pan.server.common.stream.event.search;

import lombok.*;
import org.springframework.context.ApplicationEvent;

import java.io.Serializable;


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
@NoArgsConstructor
public class UserSearchEvent implements Serializable {

    private static final long serialVersionUID = 711847068718304917L;

    private String keyword;

    private Long userId;

    public UserSearchEvent(String keyword, Long userId) {
        this.keyword = keyword;
        this.userId = userId;
    }

}
