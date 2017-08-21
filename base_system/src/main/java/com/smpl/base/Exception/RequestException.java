package com.smpl.base.Exception;

/**
 * 请求异常处理类，用于请求时 所产产生 异常的处理  比如 404  500 等
 */
public class RequestException extends RuntimeException {


    private  String msg;//异常信息


    public RequestException(String msg) {
        this.msg = msg;
    }

    public RequestException(String message, Throwable cause, String msg) {
        super(message, cause);
        this.msg = msg;
    }

    public RequestException(Throwable cause, String msg) {
        super(cause);
        this.msg = msg;
    }
}
