package com.whf.pan.core.exception;

/**
 * @author whf
 * @className FrameworkException
 * @description 技术组件层面的异常对象
 * @date 2023/10/29 14:43
 */
public class FrameworkException extends RuntimeException{
    public FrameworkException(String message) {
        super(message);
    }
}
