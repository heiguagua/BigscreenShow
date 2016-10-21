package com.chinawiserv.deepone.manager.model.common;

import java.io.Serializable;

/**
 * Created by zengpzh on 2016/6/22.
 */
public class ResponseHead implements Serializable{

    private static final long serialVersionUID = 6713609713683555027L;

    public ResponseHead(){this.status = Status.SUCCESS.getCode();}

    public ResponseHead(int status){
        this(status, null);
    }

    public ResponseHead(int status, String token) {
        this(status, token, null);
    }

    public ResponseHead(int status, String token, String message) {
        this(status, token, message, 0);
    }

    public ResponseHead(int status, String token, String message, int total) {
        this.status = status;
        this.token = token;
        this.message = message;
        this.total = total;
    }

    private int status;

    private String token;

    private String message;

    private int total;

    public void setStatus(int status) {
        this.status = status;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getStatus() {
        return status;
    }

    public String getToken() {
        return token;
    }

    public String getMessage() {
        return message;
    }

    public int getTotal() {
        return total;
    }

    public enum Status {

        SUCCESS(200, "Http response success."),
        WARNING_INFO(201, "Warning information."),
        NEED_LOGIN(202, "Need login."),
        SERVER_EXCEPTION(500, "Server-side exceptions.");

        private int code;

        private String desc;

        Status(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public static Status getStatusByCode(int code) {
            switch (code) {
                case 200:
                    return SUCCESS;
                case 201:
                    return WARNING_INFO;
                case 202:
                    return NEED_LOGIN;
                case 500:
                    return SERVER_EXCEPTION;
                default:
                    return null;
            }
        }

        public int getCode() {
            return code;
        }

        public String getDesc() {
            return desc;
        }
    }

}
