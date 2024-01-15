package io.github.chensheng.dddboot.web.core;

public class CommonResponse extends Response<Object> {
    private static final long serialVersionUID = 3550369337633557979L;

    public CommonResponse() {
    }

    public CommonResponse(String code, String msg) {
        super(code, msg);
    }

    public CommonResponse(String code, String msg, Object data) {
        super(code, msg, data);
    }

    public static CommonResponse bizSuccess(Object data) {
        return new CommonResponse(ResponseType.BIZ_SUCCESS.getCode(), ResponseType.BIZ_SUCCESS.getMsg(), data);
    }

    public static CommonResponse bizError(String msg) {
        return new CommonResponse(ResponseType.BIZ_ERROR.getCode(), msg);
    }

    public static CommonResponse sysError(String msg) {
        return new CommonResponse(ResponseType.SYS_ERROR.getCode(), msg);
    }
}
