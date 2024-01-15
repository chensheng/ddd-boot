package io.github.chensheng.dddboot.web.core;

import java.io.Serializable;

public class Response<T> implements Serializable {
    private static final long serialVersionUID = -4175260707179037233L;

    protected String code;

    protected String msg;

    protected T data;

    public Response() {
    }

    public Response(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Response(String code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return ResponseType.BIZ_SUCCESS.getCode().equals(code);
    }
}
