package com.smpl.base.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/9/4 0004.
 */
public class Result<E>  implements Serializable {


    private static final long serialVersionUID = 37468166158631349L;

    public static final Integer ERROR = -1;
    public static final Integer FAILED = 0;
    public static final Integer SUCCEED = 1;

    private StringBuilder message = new StringBuilder();
    private Integer code = SUCCEED;
    private E data;
    private Exception exception;


    public static Result error() {

        Result result = new Result();
        result.setCode(ERROR);
        return result;
    }

    public static Result error(Exception e) {
        Result result = new Result();
        result.setCode(ERROR);
        result.setMessage(e.getMessage());
        result.setException(e);
        return result;
    }

    public static Result error(String message, Exception e) {
        Result result = new Result();
        result.setCode(ERROR);
        result.setMessage(message);
        result.setException(e);
        return result;
    }

    public static Result error(String message) {
        Result result = new Result();
        result.setCode(ERROR);
        result.setMessage(message);
        return result;
    }

    public static Result falied(String message) {
        Result result = new Result();
        result.setCode(FAILED);
        result.setMessage(message);
        return result;
    }

    public static Result succeed() {
        Result result = new Result();
        result.setCode(SUCCEED);
        return result;
    }

    public static Result succeed(String message) {
        Result result = new Result();
        result.setCode(SUCCEED);
        result.setMessage(message);
        return result;
    }

    public static <T> Result<T> succeed(T data) {
        return new Result<T>(data);
    }

    public Result() {
    }

    public Result(String message) {
        setMessage(message);
    }

    public Result(E data) {
        setData(data);
    }

    public Result(Integer code, E data) {
        setCode(code);
        setData(data);
    }

    public Result(String message, E data) {
        setMessage(message);
        setData(data);
    }

    public Result(Integer code, String message, E data) {
        setCode(code);
        setMessage(message);
        setData(data);
    }

    public Boolean isError() {
        return this.code == ERROR;
    }

    public Boolean isFailed() {
        return this.code == FAILED;
    }

    public Boolean isSucceed() {
        return this.code == SUCCEED;
    }

    @Override
    public String toString() {
        return String.format("Code: %d; Message: %s", code, message.toString());
    }

    public Result(Integer code) {
        this();
        this.code = code;
    }

    public Result(Integer code, String message) {
        this(code);
        setMessage(message);
    }

    public void setError(String message, Object... args) {
        this.code = 0;
        setMessage(message, args);
    }

    public void setSuccess(String message, Object... args) {
        this.code = 1;
        setMessage(message, args);
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message.toString();
    }

    public void setMessage(String message, Object... args) {
        this.message.setLength(0);
        this.message.append(String.format(message, args));
    }

    public void addMessage(String message, Object... args) {
        if (args.length > 0) {
            this.message.append(String.format(message, args));
        } else {
            this.message.append(message);
        }
    }

    public E getData() {
        return data;
    }

    public void setData(E data) {
        this.data = data;
    }

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }
}
