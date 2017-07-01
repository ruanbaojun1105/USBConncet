package com.hwx.usbconnect.usbconncet.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/6/30.
 */

public class MessageTalkOther extends MessageTalk {
    boolean isOther;
    String name;
    String icon;
    public MessageTalkOther() {
    }

    public MessageTalkOther(String name, String icon, String message) {
        this.name = name;
        this.icon = icon;
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public String getIcon() {
        return icon;
    }

    public boolean isOther() {
        return isOther;
    }

    public void setIsOther(boolean me) {
        isOther = me;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public int getItemType() {
        return 2;
    }
}
