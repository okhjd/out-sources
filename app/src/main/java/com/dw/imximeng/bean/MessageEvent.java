package com.dw.imximeng.bean;

/**
 * @author hjd
 * @Created_Time 2018\6\24 0024
 */

public class MessageEvent {
    private MessageType msgCode;
    private boolean message;

    public MessageType getMsgCode() {
        return msgCode;
    }

    public void setMsgCode(MessageType msgCode) {
        this.msgCode = msgCode;
    }

    public boolean isMessage() {
        return message;
    }

    public void setMessage(boolean message) {
        this.message = message;
    }

    public enum MessageType{
        LANGUAGE,
        REFRESH_MAIN,
        SIGN_IN
    }
}
