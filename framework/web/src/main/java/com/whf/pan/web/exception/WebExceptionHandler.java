package com.whf.pan.web.exception;

import com.whf.pan.core.exception.BusinessException;
import org.springframework.validation.BindException;
import com.whf.pan.core.response.R;
import com.whf.pan.core.response.ResponseCode;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

/**
 * @author whf
 * @className WebExceptionHandler
 * @description 全局异常处理器
 * @date 2023/10/24 20:26
 */
@RestControllerAdvice
public class WebExceptionHandler {
    @ExceptionHandler(value = BusinessException.class)
    public R businessExceptionHandler(BusinessException e) {
        return R.fail(e.getCode(), e.getMsg());
    }

    /**
     * 方法参数不匹配异常
     * @param e
     * @return
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public R methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        ObjectError objectError = e.getBindingResult().getAllErrors().stream().findFirst().get();
        return R.fail(ResponseCode.ERROR_PARAM.getCode(), objectError.getDefaultMessage());
    }

    /**
     * 违反约束异常
     * @param e
     * @return
     */
    @ExceptionHandler(value = ConstraintViolationException.class)
    public R constraintViolationExceptionHandler(ConstraintViolationException e) {
        ConstraintViolation constraintViolation = e.getConstraintViolations().stream().findFirst().get();
        return R.fail(ResponseCode.ERROR_PARAM.getCode(), constraintViolation.getMessage());
    }

    /**
     * 缺少servlet请求参数异常
     * @param e
     * @return
     */
    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    public R missingServletRequestParameterExceptionHandler(MissingServletRequestParameterException e) {
        return R.fail(ResponseCode.ERROR_PARAM.getCode(), ResponseCode.ERROR_PARAM.getDesc());
    }

    /**
     * 非法状态异常
     * @param e
     * @return
     */
    @ExceptionHandler(value = IllegalStateException.class)
    public R illegalStateExceptionHandler(IllegalStateException e) {
        return R.fail(ResponseCode.ERROR_PARAM.getCode(), ResponseCode.ERROR_PARAM.getDesc());
    }

    /**
     * 绑定异常
     * @param e
     * @return
     */
    @ExceptionHandler(value = BindException.class)
    public R bindExceptionHandler(BindException e) {
        FieldError fieldError = e.getBindingResult().getFieldErrors().stream().findFirst().get();
        return R.fail(ResponseCode.ERROR_PARAM.getCode(), fieldError.getDefaultMessage());
    }

    /**
     * 运行时异常
     * @param e
     * @return
     */
    @ExceptionHandler(value = RuntimeException.class)
    public R runtimeExceptionHandler(RuntimeException e) {
        e.printStackTrace();
        return R.fail(ResponseCode.ERROR.getCode(), e.getMessage());
    }

}
