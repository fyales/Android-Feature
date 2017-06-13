package com.fyales.android.demo.rx.event;

/**
 * @author fyales
 * @since 2017-06-12
 */

public class SimpleEvent {

    public static final String SUCCESS = "success";
    public static final String FAILURE = "failure";

    private String msg;
    private String msgCode;

    public SimpleEvent(String msgCode,String msg) {
        this.msgCode = msgCode;
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public String getMsgCode() {
        return msgCode;
    }

}
