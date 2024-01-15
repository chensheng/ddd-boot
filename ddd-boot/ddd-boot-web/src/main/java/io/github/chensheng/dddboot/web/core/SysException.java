package io.github.chensheng.dddboot.web.core;

public class SysException extends RuntimeException {
    private static final long serialVersionUID = -736090133183009242L;

    private String code;

    public SysException(Throwable e) {
        super(e);
        this.code = ResponseType.SYS_ERROR.getCode();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
