package com.whf.pan.core.response;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

import java.io.Serializable;

/**
 * @className R
 * @description 公用返回对象
 * @author whf
 * @date 2023/10/17 18:59
 */

/*
   @JsonInclude(JsonInclude.Include.NON_NULL)
   保证json序列化的时候，如果value为null的时候，key也会消失
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
public class R<T> implements Serializable {
    /**
     * 状态码
     */
    private final int code;
    private String message;
    private T data;

    private R(int code) {
        this.code = code;
    }

    private R(int code, String message) {
        this.code = code;
        this.message = message;
    }

    private R(int code, T data) {
        this.code = code;
        this.data = data;
    }

    private R(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    /**
     *  @JsonIgnore
     *  后台推数据到前台的时候，就会把 isSuccess 这个属性忽略掉
     *  @JSONField(serialize = false)
     *  不序列化 isSuccess 属性
     */

    @JsonIgnore
    @JSONField(serialize = false)
    public boolean isSuccess() {
        return code == ResponseCode.SUCCESS.getCode();
    }

    public static <T> R<T> success() {
        return new R<T>(ResponseCode.SUCCESS.getCode());
    }

    public static <T> R<T> success(String message) {
        return new R<T>(ResponseCode.SUCCESS.getCode(), message);
    }

    public static <T> R<T> data(T data) {
        return new R<T>(ResponseCode.SUCCESS.getCode(), data);
    }

    public static <T> R<T> success(String message, T data) {
        return new R<T>(ResponseCode.SUCCESS.getCode(), message, data);
    }

    public static <T> R<T> fail() {
        return new R<T>(ResponseCode.ERROR.getCode(), ResponseCode.ERROR.getDesc());
    }

    public static <T> R<T> fail(String error_message) {
        return new R<T>(ResponseCode.ERROR.getCode(), error_message);
    }

    public static <T> R<T> fail(int error_code, String error_message) {
        return new R<T>(error_code, error_message);
    }

    public static <T> R<T> fail(ResponseCode responseCode) {
        return new R<T>(responseCode.getCode(), responseCode.getDesc());
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }
}
