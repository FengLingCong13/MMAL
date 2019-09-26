package com.xhhp.mmall.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;

/**
 * ServerResponse class
 *
 * @author Flc
 * @date 2019/9/25
 */
//保证序列化是，null的值不序列化
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class ServerResponse<T> implements Serializable {

    private int status;
    private String msg;
    private T data;

    private ServerResponse(int status) {
        this.status = status;
    }

    private ServerResponse(int status, T data) {
        this.status = status;
        this.data = data;
    }

    private ServerResponse(int status, String msg, T data) {
        this.data = data;
        this.msg = msg;
        this.status = status;
    }

    private ServerResponse(int status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    @JsonIgnore
    public boolean isSuccess() {
        return this.status == ResponseCode.SUCCESS.getCode();
    }

    public int getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }

    public T getData() {
        return data;
    }

    public static <T> ServerResponse<T> createBySuccess() {
        return new ServerResponse<>(ResponseCode.SUCCESS.getCode());
    }

    public static <T> ServerResponse<T> createBySuccessMessage(String msg) {
        return new ServerResponse<>(ResponseCode.SUCCESS.getCode(), msg);
    }

    public static <T> ServerResponse<T> createBySuccess(T data) {
        return new ServerResponse<>(ResponseCode.SUCCESS.getCode(), data);
    }

    public static <T> ServerResponse<T> createBySuccess(String msg, T data) {
        return new ServerResponse<>(ResponseCode.SUCCESS.getCode(), msg, data);
    }


    public static <T> ServerResponse<T> createByERROR() {
        return new ServerResponse<>(ResponseCode.ERROR.getCode());
    }

    public static <T> ServerResponse<T> createByERRORMessage(String errorMsg) {
        return new ServerResponse<>(ResponseCode.SUCCESS.getCode(), errorMsg);
    }

    public static <T> ServerResponse<T> createByErrorCodeMessage(int errorCode, String errorMessage) {
        return new ServerResponse<>(ResponseCode.SUCCESS.getCode(), errorMessage);
    }




}
