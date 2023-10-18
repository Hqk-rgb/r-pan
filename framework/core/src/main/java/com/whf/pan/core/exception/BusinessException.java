package com.whf.pan.core.exception;

import com.whf.pan.core.response.ResponseCode;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author whf
 * @className 自定义全局业务异常
 * @description TODO
 * @date 2023/10/18 12:53
 */

/*
   @EqualsAndHashCode(callSuper = true)
   根据子类自身的字段值和从父类继承的字段值来生成hashcode，当两个子类对象比较时，只有子类对象的本身的字段值和继承父类的字段值都相同，equals方法的返回值是true。
 */

@EqualsAndHashCode(callSuper = true)
@Data
public class BusinessException extends RuntimeException{
    /**
     * 错误码
     */
    private Integer code;

    /**
     * 错误信息
     */
    private String msg;

    public BusinessException(ResponseCode responseCode) {
        this.code = responseCode.getCode();
        this.msg = responseCode.getDesc();
    }

    public BusinessException(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public BusinessException(String msg) {
        this.code = ResponseCode.ERROR.getCode();
        this.msg = msg;
    }

    public BusinessException() {
        this.code = ResponseCode.ERROR.getCode();
        this.msg = ResponseCode.ERROR.getDesc();
    }
}
