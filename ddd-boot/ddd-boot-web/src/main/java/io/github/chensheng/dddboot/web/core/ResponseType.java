package io.github.chensheng.dddboot.web.core;

public enum ResponseType {
    BIZ_SUCCESS("BIZ_SUCCESS", "BIZ_SUCCESS"),
    BIZ_ERROR("BIZ_ERROR", "BIZ_ERROR"),
    SYS_ERROR("SYS_ERROR", "SYS_ERROR");

    private String code;

    private String msg;

    ResponseType(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

}
