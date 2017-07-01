package com.hwx.usbconnect.usbconncet.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/6/30.
 */

public class MessageTalkMe extends MessageTalk  {
    boolean isMe;
    String name;
    public MessageTalkMe() {
    }

    public MessageTalkMe(String name,String message) {
        this.name = message;
        this.message = message;
    }

    @Override
    public int getItemType() {
        return 1;
    }
}
