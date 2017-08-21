package com.smpl.base.Exception;

/**
 * 系统运行异常处理类，用于处理系统运行时的异常  空指针 等 未知异常处理
 */
public class SystemException extends  RuntimeException {

    private  String msg;//异常信息

    public SystemException(Throwable cause, String msg) {
        super(cause);
        this.msg = msg;
    }

    public SystemException(String msg) {
        this.msg = msg;
    }

    public SystemException(String message, Throwable cause, String msg) {
        super(message, cause);
        this.msg = msg;
    }
}
