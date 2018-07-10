package com.dw.imximeng.bean;

/**
 * @author hjd
 * @Created_Time 2018\6\26 0026
 */
public class Result<T> {

    /**
     * status : 0
     * message : 手机号格式错误
     * data : []
     */

    private int status;
    private String message;
    private T data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
