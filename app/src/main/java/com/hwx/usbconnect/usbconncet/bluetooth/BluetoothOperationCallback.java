package com.hwx.usbconnect.usbconncet.bluetooth;

/**
 * Created by Aamir on 2017/3/8.
 */

public class BluetoothOperationCallback {
    public void onConnect(int err, String desc) {}
    public void onDataRecv(int err, String desc, byte[] data, int len) {}
}
