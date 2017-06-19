package com.hwx.usbconnect.usbconncet.ui.adapter;


import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hwx.usbconnect.usbconncet.R;
import com.hwx.usbconnect.usbconncet.ui.fragment.OnLineImageFragment;

import java.util.List;

/**
 *
 */
public class ItemClickAdapter extends BaseQuickAdapter<OnLineImageFragment.ImageFontTag, BaseViewHolder> {

    public ItemClickAdapter(List<OnLineImageFragment.ImageFontTag> data) {
        super(R.layout.item_text, data);
    }


    @Override
    protected void convert(final BaseViewHolder helper, final OnLineImageFragment.ImageFontTag item) {
        helper.setText(R.id.text,item.getName());
    }
}