package com.junyouava.sdk;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * API 响应结果
 *
 * @param <T> 响应数据类型
 */
public class Result<T> {
    /**
     * HTTP 状态码或业务状态码
     */
    @JsonProperty("code")
    private int code;

    /**
     * 业务错误代码（字符串）
     */
    @JsonProperty("err_code")
    private String errCode;

    /**
     * 是否成功
     */
    @JsonProperty("success")
    private boolean success;

    /**
     * 错误或成功消息
     */
    @JsonProperty("message")
    private String message;

    /**
     * 响应数据
     */
    @JsonProperty("data")
    private T data;

    public Result() {
    }

    public Result(boolean success, int code, String message, T data) {
        this.success = success;
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}

