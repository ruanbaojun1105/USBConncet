package com.hwx.usbconnect.usbconncet.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/6/30.
 */

public abstract class MessageTalk implements Serializable, MultiItemEntity {
    String message;

    public MessageTalk() {
    }

    public MessageTalk(String message) {
        this.message = message;
    }
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
