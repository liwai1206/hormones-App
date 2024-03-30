package com.wdk.sports.domain;

import java.io.Serializable;
import java.util.List;

public class ResultVO<T> implements Serializable {

    private String code;
    private T data;
    private String msg ;

    public ResultVO(String code, T data, String msg) {
        this.code = code;
        this.data = data;
        this.msg = msg;
    }

    public ResultVO() {

    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "ResultVO{" +
                "code='" + code + '\'' +
                ", data=" + data +
                ", msg='" + msg + '\'' +
                '}';
    }
}
