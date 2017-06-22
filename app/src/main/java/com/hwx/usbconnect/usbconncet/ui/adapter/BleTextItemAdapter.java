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
public class BleTextItemAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public BleTextItemAdapter(List<String> data) {
        super(R.layout.ble_item_text, data);
    }


    @Override
    protected void convert(final BaseViewHolder helper, final String item) {
        helper.setText(R.id.text,"{fa-bluetooth @color/colormain5}  "+item.toString());
    }
}