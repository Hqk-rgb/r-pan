package com.whf.pan.server.common.annotation;

import java.lang.annotation.*;
/**
 * @author whf
 * @className NeedShareCode
 * @description 该注解主要影响需要分享码校验的接口
 * @date 2023/12/5 15:38
 */

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface NeedShareCode {
}
