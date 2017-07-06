package com.hwx.usbconnect.usbconncet.ui.adapter;


import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hwx.usbconnect.usbconncet.R;
import com.hwx.usbconnect.usbconncet.bean.MessageTalk;
import com.hwx.usbconnect.usbconncet.bean.MessageTalkMe;
import com.hwx.usbconnect.usbconncet.bean.MessageTalkOther;

import java.util.List;

/**
 *
 */
public class TalkMulItemAdapter extends BaseMultiItemQuickAdapter<MessageTalk, BaseViewHolder> {

    public TalkMulItemAdapter(List<MessageTalk> data) {
        super(data);
        addItemType(1, R.layout.talk_item_text_me);
        addItemType(2, R.layout.talk_item_text_other);
    }



    @Override
    protected void convert(BaseViewHolder helper, MessageTalk item) {
        helper.setText(R.id.text,item.getMessage());
        if (item instanceof MessageTalkMe){
        }
        if (item instanceof MessageTalkOther){
            MessageTalkOther itemother= (MessageTalkOther) item;
            helper.setText(R.id.icon,itemother.getIcon().replace("\t","").replace("\n",""));
            helper.setText(R.id.name,itemother.getName().replace("\t","").replace("\n",""));
        }
    }
}