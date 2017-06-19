package com.hwx.usbconnect.usbconncet.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

import com.hwx.usbconnect.usbconncet.R;
import com.hwx.usbconnect.usbconncet.bluetooth.BluetoothService;
import com.hwx.usbconnect.usbconncet.utils.AnimTextView;
import com.hwx.usbconnect.usbconncet.utils.AppConfig;
import com.hwx.usbconnect.usbconncet.utils.Constants;
import com.hwx.usbconnect.usbconncet.utils.DebugLog;
import com.hwx.usbconnect.usbconncet.utils.LogUtils;
import com.hwx.usbconnect.usbconncet.utils.ToastUtils;
import com.joanzapata.iconify.widget.IconTextView;
import com.liulishuo.magicprogresswidget.MagicProgressCircle;

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
    private IconTextView icon_text;
    private Context mContext;
    private boolean isScanConn;
    private UsbManager mManager;
    private UsbDeviceConnection connection;
    private ScanListener mScanListener;
    private UsbEndpoint outEndpoint;//发送数据
    private UsbEndpoint inEndpoint;//接收数据

    private Dialog dialog;
    private AnimTextView animTextView;
    private MagicProgressCircle magicProgressCircle;


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
                case 110:
                    inEndpoint=null;
                    outEndpoint=null;
                    isScanConn=false;
                    icon_text.setText("{fa-times-circle @color/red}");//错误
                    Toast.makeText(mContext, R.string.dsttaat,Toast.LENGTH_SHORT).show();
                    break;
                case 111:
                    tag=0;
                    Toast.makeText(mContext, R.string.dtadtat,Toast.LENGTH_SHORT).show();
                    icon_text.setText("{fa-check-circle-o @color/light_orange}");//ok
                    icon_text.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            icon_text.setText("{fa-check @color/colormain5}");//ok
                        }
                    },2000);
                    try {
                        dialog.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (com.hwx.usbconnect.usbconncet.Constants.isOpenLim) {
                        int a = AppConfig.getInstance().getInt("success", 1);
                        AppConfig.getInstance().putInt("success", a + 1);
                        if (a > 20) {
                            new AlertDialog.Builder(mContext).setMessage("当前设置成功次数超过20次,请授权")
                                    .setIcon(android.R.drawable.ic_dialog_info)
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                        }
                                    })
                                    .setNegativeButton("取消", null)
                                    .show();
                        }
                    }
                    break;
                case 112:
                    LogUtils.e("-----------------------112");
                    Toast.makeText(mContext, R.string.vfaddt,Toast.LENGTH_SHORT).show();
                    icon_text.setText("{fa-times-circle @color/red}");//错误
                    try {
                        dialog.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
//                    outEndpoint=null;
//                    isScanConn=false;
                    tag=0;
                    break;
                case 113:
                    icon_text.setText("{fa-certificate spin}");
                    //mProgressDialog = ProgressDialog.show(mContext, null, null);
                    dialog = new Dialog(mContext);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.dialog_edit_image);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.setCancelable(false);
                    dialog.setCanceledOnTouchOutside(false);
                    //Display d = ((Activity)mContext).getWindowManager().getDefaultDisplay(); // 为获取屏幕宽、高
                    //WindowManager.LayoutParams p = dialog.getWindow().getAttributes(); // 获取对话框当前的参数值
                    //p.width =(int)( d.getWidth()*0.3);
                    //p.height =(int)( d.getHeight()*0.6);
                    //dialog.getWindow().setAttributes(p);
                    magicProgressCircle= (MagicProgressCircle) dialog.findViewById(R.id.demo_mpc);
                    magicProgressCircle.setSmoothPercent(0);
                    animTextView= (AnimTextView) dialog.findViewById(R.id.demo_tv);
                    animTextView.setSmoothPercent(0);
                    dialog.show();
                    Toast.makeText(mContext, R.string.tatttewa,Toast.LENGTH_SHORT).show();
                    break;
                case 114:
                    try {
                        starPross();
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                    break;
                case 115:
                    icon_text.setText("{fa-times-circle @color/red}");//错误
                    inEndpoint=null;
                    outEndpoint=null;
                    isScanConn=false;
                    if (connection!=null)
                        connection.close();
                    connection=null;
                    dialog=null;
                    animTextView=null;
                    magicProgressCircle=null;
                    Toast.makeText(mContext, R.string.datsdat,Toast.LENGTH_SHORT).show();
                    break;
                case 116:
                    icon_text.setText("{fa-check @color/colormain5}");//ok
                    break;
                case 117:
                    icon_text.setText("{fa-times-circle @color/red}");//错误
                    break;
            }
        }
    };

    public void starPross() {
        if (animTextView!=null)
            animTextView.setProgress((int) (((((float)tag)+1)/((float)amit))*100));
        LogUtils.e(animTextView.getPercent()+"progress");
        if (magicProgressCircle!=null)
            magicProgressCircle.setSmoothPercent(animTextView.getPercent());
    }

    public void star() {
        mHandler.sendEmptyMessage(113);
    }
    public void close() {
        mHandler.sendEmptyMessage(110);
    }

    public ScanHelper(Context context,IconTextView icon_text){
        mContext = context;
        this.icon_text=icon_text;
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

    /**
     * 拆分byte数组
     * @param bytes
     * @param copies
     * @return
     */
    static byte[][] split_bytes(byte[] bytes, int copies) {

        double split_length = Double.parseDouble(copies + "");

        int array_length = (int) Math.ceil(bytes.length / split_length);
        byte[][] result = new byte[array_length][];

        int from, to;

        for (int i = 0; i < array_length; i++) {

            from = (int) (i * split_length);
            to = (int) (from + split_length);

            //if (to > bytes.length)
            //to = bytes.length;

            result[i] = Arrays.copyOfRange(bytes, from, to);
        }

        return result;
    }

//    public void sendData(final byte[] data) {
//        if (outEndpoint!=null&&connection!=null){
//                   byte aaa[]=new byte[240];
//                    for (int i = 0; i < 240; i++) {
//                        aaa[i]=(byte)i;
//                    }
//                    aaa[238]=(byte)0x0c;
//                    aaa[239]=(byte)0x0d;
//            //int out = connection.bulkTransfer(outEndpoint,(byte[]) aaa, 80, 0);
//            int out = connection.bulkTransfer(outEndpoint, data, data.length, 0);//0秒超时说明可一直等待
//            LogUtils.e("--------"+out);
//            getData();
//        }
//    }
    int tag=0;
    int amit=0;
    public void sendData(final byte[] data) {
        if (outEndpoint!=null&&connection!=null) {
            /*byte aaa[] = new byte[996];
            for (int i = 0; i < 996; i++) {
                aaa[i] = (byte)0x88;
            }*/
            LogUtils.e("总共内容长度"+data.length);
            final byte[][] bytes = split_bytes(data,64);
            amit=bytes.length;
//            for (int i = 0; i < bytes.length; i++) {
//                for (int j = 0; j < bytes[i].length; j++) {
//                    System.out.print(bytes[i][j] + " ");
//                }
//                click.click(bytes[i]);
//            }
            Onclick click=new Onclick() {
                @Override
                public void click(final byte aaa[]) {
                    LogUtils.e("当前"+tag+"---总共"+amit);
                    if (tag>=amit) {
                        tag=0;
                        return;
                    }
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            int out = connection.bulkTransfer(outEndpoint, aaa, aaa.length, 0);
                            LogUtils.e(tag+"--------"+out);
                            if (out>0){
                                mHandler.sendEmptyMessage(114);
                                tag++;
                                if (tag>=amit) {
                                    mHandler.sendEmptyMessage(111);//传输结束
                                    return;
                                }
                                click(bytes[tag]);
                            }else {
                                LogUtils.e("--------中途失败补一1");
                                int out1 = connection.bulkTransfer(outEndpoint, aaa, aaa.length, 0);
                                if (out1>0){
                                    LogUtils.e("--------中途失败补一OK1");
                                    tag++;
                                    if (tag>=amit) {
                                        mHandler.sendEmptyMessage(111);//传输结束
                                        return;
                                    }
                                    click(bytes[tag]);
                                }else {
                                    LogUtils.e("--------中途失败补一2");
                                    int out2 = connection.bulkTransfer(outEndpoint, aaa, aaa.length, 0);
                                    if (out2>0){
                                        LogUtils.e("--------中途失败补一OK2");
                                        tag++;
                                        if (tag>=amit) {
                                            mHandler.sendEmptyMessage(111);//传输结束
                                            return;
                                        }
                                        click(bytes[tag]);
                                    }else {
                                        LogUtils.e("--------中途失败补第三次，不补了");
                                        mHandler.sendEmptyMessage(112);//失败本次传输
                                    }
                                }
                            }
                        }
                    }).start();
                }
            };
            click.click(bytes[0]);
            getData();
        }
    }

    interface  Onclick{
        void click(byte aaa[]);
    }

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

    public static byte[] sendDataSSS(byte function,byte[] content,boolean isAutoSafeCode) {
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
        return a;
    }

    private static final String ACTION_USB_PERMISSION="com.android.example.USB_PERMISSION";//设备权限的广播
    private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            DebugLog.d(action);
            if (ACTION_USB_PERMISSION.equals(action)) {
                LogUtils.e("ACTION_USB_PERMISSION:");
                synchronized (this) {
                    UsbDevice device = (UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        if (device != null) {
                            startScan(device);
                        }
                    }
                }
            }else if(UsbManager.ACTION_USB_DEVICE_ATTACHED.equals(action)){//拔出后再次插入
                LogUtils.e("ACTION_USB_DEVICE_ATTACHED:");
                UsbDevice device = checkScanDevice(Constants.DEVICE_VIDS,Constants.DEVICE_PIDS);
                if(device != null){
                    startScan(device);
                }
            }else if(UsbManager.ACTION_USB_DEVICE_DETACHED.equals(action)){//设备拔出
                LogUtils.e("ACTION_USB_DEVICE_DETACHED:");
                mHandler.sendEmptyMessage(115);
            }else if("android.hardware.usb.action.USB_STATE".equals(action)){
                boolean connected = intent.getExtras().getBoolean("connected");
                LogUtils.e("USB_STATE:"+connected);
            }else if("android.hardware.usb.action.USB_DISCONNECTED".equals(action)){
                LogUtils.e("USB_DISCONNECTED:");
            }else if("android.hardware.usb.action.USB_CONNECTED".equals(action)){
                LogUtils.e("USB_CONNECTED:");
            }
        }
    };

    public void registerReceiver() {
        IntentFilter filter= new IntentFilter();
        //filter.addAction(ACTION_USB_ATTACHED);
        filter.addAction(ACTION_USB_PERMISSION);
        filter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
        filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);

        filter.addAction("android.hardware.usb.action.USB_STATE");
        filter.addAction("android.hardware.action.USB_DISCONNECTED");
        filter.addAction("android.hardware.action.USB_CONNECTED");

//        filter.addAction("android.intent.action.UMS_CONNECTED");
//        filter.addAction("android.intent.action.UMS_DISCONNECTED");
//        filter.addAction(UsbManager.ACTION_USB_ACCESSORY_ATTACHED);
//        filter.addAction(UsbManager.ACTION_USB_ACCESSORY_DETACHED);
        mContext.registerReceiver(mUsbReceiver, filter);
        //LocalBroadcastManager.getInstance(App.getContext()).registerReceiver(mUsbReceiver, filter);
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

    void getData(){
        //读取数据1   两种方法读取数据
        byte[] byte2 = new byte[64];
        int ret = connection.bulkTransfer(inEndpoint, byte2, byte2.length, 3000);
        Log.e("ret", "ret:"+ret);
        for(Byte byte1 : byte2){
            System.err.println(byte1);
        }
        LogUtils.e("",""+new String(byte2));
    }

    /**
     * 开启USB接收和发送
     * @param device
     */
    public void startScan(final UsbDevice device){
        if(device == null)
            return;
        /*if (outEndpoint!=null)
            return;*/
        isScanConn = true;
        new Thread(){
            @Override
            public void run() {
                while (isScanConn){
                    UsbInterface usbInterface= device.getInterface(0);
                    //UsbEndpoint endpoint= usbInterface.getEndpoint(0);//0是输入1是输出
                    //UsbEndpoint inEndpoint = null;  //读数据节点
                    try {
                        inEndpoint = usbInterface.getEndpoint(0);
                        outEndpoint = usbInterface.getEndpoint(1); //写数据节点
                        connection= mManager.openDevice(device);
                    } catch (Exception e) {
                        e.printStackTrace();
                        mHandler.sendEmptyMessage(110);
                        return;
                    }
                    if(connection == null){
                        stopScan();
                        LogUtils.e("不能打开连接!");
                        mHandler.sendEmptyMessage(110);
                        return;
                    }
                    boolean isconn=connection.claimInterface(usbInterface, true);
                    LogUtils.e(isconn?"已打开连接!":"打开连接失败");
                    mHandler.sendEmptyMessage(isconn?116:117);
                    if(device != null){
                        //收数据
                        byte[] bytes = new byte[512];
                        int at=connection.bulkTransfer(inEndpoint, bytes, bytes.length, 0);//do in another thread
                        if (at>0)
                            LogUtils.e("get-----",""+at);
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
                        //LogUtils.e("get-----",str);
                        /*if(connection != null)
                            connection.close();*/
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
