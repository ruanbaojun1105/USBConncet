package com.hwx.usbconnect.usbconncet.ui.adapter;

import android.bluetooth.BluetoothDevice;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hwx.usbconnect.usbconncet.R;

import java.util.List;

/**
 *
 */
public class ItemClickAdapter extends BaseQuickAdapter<BluetoothDevice, BaseViewHolder> {

    public ItemClickAdapter(List<BluetoothDevice> data) {
        super(R.layout.bluetooth_item, data);
    }


    @Override
    protected void convert(final BaseViewHolder helper, final BluetoothDevice item) {
        helper.setText(R.id.bluetooth_info,item.getName()+"\t"+item.getAddress());
    }
}