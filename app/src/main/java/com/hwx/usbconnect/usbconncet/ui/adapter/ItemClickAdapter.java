package com.hwx.usbconnect.usbconncet.ui.adapter;


import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hwx.usbconnect.usbconncet.R;
import com.hwx.usbconnect.usbconncet.ui.fragment.MainFragment;
import com.hwx.usbconnect.usbconncet.ui.fragment.OnLineImageFragment;
import com.hwx.usbconnect.usbconncet.utils.SpinnerTopView;

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
        final SpinnerTopView topView=helper.getView(R.id.topView);
        new Thread(new Runnable() {
            @Override
            public void run() {
                topView.setVisiBg(false);
                final byte[] aa=MainFragment.readFile(mContext, item.getPath());
                topView.post(new Runnable() {
                    @Override
                    public void run() {
                        topView.setPicture1_ByteT(aa,false);
                    }
                });

            }
        }).start();

    }
}