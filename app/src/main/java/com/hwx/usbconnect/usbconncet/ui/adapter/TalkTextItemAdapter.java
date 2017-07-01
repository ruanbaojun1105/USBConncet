package com.hwx.usbconnect.usbconncet.ui.adapter;


import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hwx.usbconnect.usbconncet.R;

import java.util.List;

/**
 *
 */
public class TalkTextItemAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public TalkTextItemAdapter(List<String> data) {
        super(R.layout.ble_item_text, data);
    }


    @Override
    protected void convert(final BaseViewHolder helper, final String item) {
        helper.setText(R.id.text,item.toString());
    }
}