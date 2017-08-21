package com.smpl.base.Exception;

/**
 *   业务异常处理类   用于处理业务异常 一些校验 需终止操作 抛出异常
 */
public class BusinessException extends RuntimeException {

    private  String msg;//异常信息

    public BusinessException(String message) {
        super(message);
        this.msg = msg;
    }

    public BusinessException(String message, Throwable cause, String msg) {
        super(message, cause);
        this.msg = msg;
    }

    public BusinessException(Throwable cause, String msg) {
        super(cause);
        this.msg = msg;
    }
}
