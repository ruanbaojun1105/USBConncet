package com.hwx.usbconnect.usbconncet.ui.adapter;


import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hwx.usbconnect.usbconncet.R;
import com.hwx.usbconnect.usbconncet.ui.fragment.MainFragment;
import com.hwx.usbconnect.usbconncet.ui.fragment.OnLineImageFragment;
import com.hwx.usbconnect.usbconncet.ui.widget.SpinnerTopView;

import java.util.List;

/**
 *
 */
public class SpinnerItemAdapter extends BaseQuickAdapter<OnLineImageFragment.ImageFontTag, BaseViewHolder> {

    private int color;
    public SpinnerItemAdapter(List<OnLineImageFragment.ImageFontTag> data,int color) {
        super(R.layout.spinlist_item_text, data);
        this.color=color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final OnLineImageFragment.ImageFontTag item) {
        helper.setText(R.id.text,item.getName().replace(".bin",""));
        final SpinnerTopView topView=helper.getView(R.id.topView);
        new Thread(new Runnable() {
            @Override
            public void run() {
                topView.setVisiBg(false);
                final byte[] aa=MainFragment.readFile(mContext, item.getPath());
                topView.post(new Runnable() {
                    @Override
                    public void run() {
                        //topView.setLastColor(color);
                        topView.setPrerent(4);
                        topView.setPicture1_ByteT(aa,false);
                    }
                });

            }
        }).start();

    }
}