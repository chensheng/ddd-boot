package io.github.chensheng.dddboot.web.core;

public class BizException extends RuntimeException {
    private static final long serialVersionUID = 2297217127746164129L;

    private String code;

    public BizException() {
        this(ResponseType.BIZ_ERROR.getMsg());
    }

    public BizException(String msg) {
        this(ResponseType.BIZ_ERROR.getCode(), msg);
    }

    public BizException(String code, String msg) {
        super(msg);
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
