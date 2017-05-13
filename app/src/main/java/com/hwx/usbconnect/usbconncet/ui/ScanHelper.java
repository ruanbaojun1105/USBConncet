package com.hwx.usbconnect.usbconncet.ui;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.hwx.usbconnect.usbconncet.bluetooth.BluetoothService;
import com.hwx.usbconnect.usbconncet.utils.Constants;
import com.hwx.usbconnect.usbconncet.utils.DebugLog;
import com.hwx.usbconnect.usbconncet.utils.LogUtils;
import com.hwx.usbconnect.usbconncet.utils.ToastUtils;

import java.io.IOException;
import java.lang.annotation.ElementType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * @Description:
 *      帮助类,使用时需要把广播绑定在activity的生命周期中,用于监听插入USB设备和请求权限的广播,提供了两个方法
 *      registerReceiver()和unregisterReceiver();
 *      如果要想对监听扫码的结果进行监听需要为其设置监听器
 *      setScanListener(ScanListener scanListener)
 *      请在启动扫码方法startScan之前进行设置,否则可能会丢失掉信息
 *
 *      请注意在manifest文件的manifest节点下设置权限:
 *
        <uses-permission android:name="android.permission.SYSTEM_ALERT_WINDOW"/>
        <uses-feature android:name="android.hardware.usb.host"/>
 */

public class ScanHelper {
    private Context mContext;
    private boolean isScanConn;
    private UsbManager mManager;
    private UsbDeviceConnection connection;
    private ScanListener mScanListener;
    private UsbEndpoint outEndpoint;


    private static final int HANDLER_SCAN_INPUT = 1;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case HANDLER_SCAN_INPUT:
                    if(mScanListener != null){
                        mScanListener.scan(msg.obj.toString());
                    }
                    break;
            }
        }
    };
    /**
     * 设备权限的广播
     */
    private static final String ACTION_USB_PERMISSION="com.android.example.USB_PERMISSION";
    /**
     * 插入USB的广播
     */
    private static final String ACTION_USB_ATTACHED="android.hardware.usb.action.USB_DEVICE_ATTACHED";

    private static final String ACTION_USB_UNPIN="android.hardware.usb.action.USB_STATE";
    private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            DebugLog.d(action);
            if (ACTION_USB_PERMISSION.equals(action)) {
                synchronized (this) {
                    UsbDevice device = (UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        if (device != null) {
                            //在这里增加通信的代码
                            startScan(device);
                        }
                    }
                }
            }else if(UsbManager.ACTION_USB_DEVICE_ATTACHED.equals(action)){
                UsbDevice device = checkScanDevice(Constants.DEVICE_VIDS,Constants.DEVICE_PIDS);
                if(device != null){
                    startScan(device);
                }else {

                }
            }else if(ACTION_USB_UNPIN.equals(action)){

            }

        }
    };

    public ScanHelper(Context context){
        mContext = context;
        isScanConn = true;

//        init();
    }


    public ScanListener getScanListener() {
        return mScanListener;
    }

    public void setScanListener(ScanListener scanListener) {
        mScanListener = scanListener;
    }

    public boolean isScanConn() {
            return outEndpoint!=null&&connection!=null;
    }

    public void sendData(final byte[] data) {
        if (outEndpoint!=null&&connection!=null){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    int out = connection.bulkTransfer(outEndpoint, data, data.length, 3000);
                }
            }).start();
        }
    }

    /**
     *发送数据
     */
    public void sendData(byte function,byte[] content,boolean isAutoSafeCode) {
        isAutoSafeCode=true;//所有都自动算出来
        byte[] head=new byte[]{BluetoothService.starCode,BluetoothService.addrCode,function,(byte) (content.length/256),(byte) (content.length%256)};
        byte safe=BluetoothService.safeCode;
        if (isAutoSafeCode)
            safe=BluetoothService.checkSafeCod(content);
        byte[] end=new byte[]{safe,BluetoothService.endCode[0],BluetoothService.endCode[1]};
        List<byte[]> list=new ArrayList<byte[]>();
        list.add(head);
        list.add(content);
        list.add(end);

        byte[] a= BluetoothService.sysCopy(list);
        sendData(a);
    }

    public void registerReceiver() {
        IntentFilter filter= new IntentFilter();
        //filter.addAction(ACTION_USB_ATTACHED);
        filter.addAction(ACTION_USB_PERMISSION);
        filter.addAction(ACTION_USB_UNPIN);
        filter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
        filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
        mContext.registerReceiver(mUsbReceiver, filter);
    }

    public void unregisterReceiver(){
        mContext.unregisterReceiver(mUsbReceiver);
    }

    /**
     * 检测是否有我们所需要的设备
     * @return
     */
    public UsbDevice checkScanDevice(int[] vids, int[] pids) {
        if(vids.length != pids.length){
            ToastUtils.showToast(mContext,"vids与pids的数量不匹配!");
            return null;
        }
        if(vids == null || pids == null){
            ToastUtils.showToast(mContext,"vids或pids不能为空");
            return null;
        }
        mManager = (UsbManager)mContext.getSystemService(Context.USB_SERVICE);
        HashMap<String, UsbDevice> devices = mManager.getDeviceList();
        Iterator<UsbDevice> deviceIterator = devices.values().iterator();
        int count = devices.size();
        if (count > 0) {
            while (deviceIterator.hasNext()) {
                UsbDevice dev = deviceIterator.next();
                int pid = dev.getProductId();
                int vid = dev.getVendorId();
                if (hasDevice(vids,pids,vid,pid)) {
                    if(!mManager.hasPermission(dev)){
                        PendingIntent mPermissionIntent = PendingIntent.getBroadcast(mContext, 0, new Intent(ACTION_USB_PERMISSION), 0);
                        mManager.requestPermission(dev,mPermissionIntent);
                        return null;
                    }
                    return dev;
                }
            }

        }
        return null;
    }

    private boolean hasDevice(int[] vids,int[] pids,int vid,int pid){
        for (int i = 0; i < vids.length; i++) {
            if(vids[i] == vid && pids[i] == pid){
                return true;
            }
        }

        return false;
    }

    /**
     * 开启USB接收和发送
     * @param device
     */
    public void startScan(final UsbDevice device){
        if(device == null)
            return;
        isScanConn = true;
        new Thread(){
            @Override
            public void run() {
                while (isScanConn){
                    UsbInterface usbInterface= device.getInterface(0);
                    //UsbEndpoint endpoint= usbInterface.getEndpoint(0);//0是输入1是输出
                    UsbEndpoint inEndpoint = usbInterface.getEndpoint(0);  //读数据节点
                    outEndpoint = usbInterface.getEndpoint(1); //写数据节点
                    connection= mManager.openDevice(device);
                    if(connection == null){
                        //Toast.makeText(mContext,"不能打开连接!", Toast.LENGTH_SHORT).show();
                        stopScan();
                        LogUtils.e("不能打开连接!");
                        return;
                    }
                    connection.claimInterface(usbInterface, true);
                    if(device != null){
                        //收数据
                        byte[] bytes = new byte[512];
                        connection.bulkTransfer(inEndpoint, bytes, bytes.length, 2000);//do in another thread
                        int len = 2;
                        for (;len < bytes.length ; len++){
                            if(bytes[len] == '\r'){
                                break;
                            }
                        }
                        if(len >= bytes.length)return;
                        String str =  new String(bytes,2,len-2);
                        Message message = Message.obtain();
                        message.what = HANDLER_SCAN_INPUT;
                        message.obj = str;
                        mHandler.sendMessage(message);
                        if(connection != null)
                            connection.close();
                    }
                }
            }
        }.start();
    }

    public void stopScan(){
        if(connection != null){
            connection.close();
        }
        isScanConn = false;
    }

    public interface ScanListener{
        void scan(String data);
    }

}
