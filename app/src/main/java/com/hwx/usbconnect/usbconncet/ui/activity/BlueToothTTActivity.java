package com.hwx.usbconnect.usbconncet.ui.activity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.hwx.usbconnect.usbconncet.R;
import com.hwx.usbconnect.usbconncet.ui.BluetoothService;
import com.hwx.usbconnect.usbconncet.ui.adapter.BleTextItemAdapter;
import com.hwx.usbconnect.usbconncet.ui.widget.StateButton;
import com.hwx.usbconnect.usbconncet.utils.IClickListener;
import com.hwx.usbconnect.usbconncet.utils.LogUtils;
import com.jaeger.library.StatusBarUtil;
import com.joanzapata.iconify.widget.IconTextView;
import com.yalantis.starwars.TilesFrameLayout;
import com.yalantis.starwars.interfaces.TilesFrameLayoutListener;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author bj
 * @date 17 06 24
 */
public class BlueToothTTActivity extends SimpleActivity {
    @BindView(R.id.icon_head)
    IconTextView iconHead;
    @BindView(R.id.btnSearch)
    StateButton btnSearch;
    @BindView(R.id.lvDevices)
    RecyclerView lvBTDevices;
    @BindView(R.id.tiles_frame_layout)
    TilesFrameLayout mTilesFrameLayout;
    //<a href="http://wiley.iteye.com/blog/1179417">http://wiley.iteye.com/blog/1179417</a>
    private BleTextItemAdapter itemAdapter;
    private List<String> lstDevices = new ArrayList<String>();
    private BluetoothAdapter btAdapt;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected int getLayout() {
        return R.layout.ble_main;
    }

    @Override
    protected void initEventAndData() {
        init();
    }

    public void init() {
        StatusBarUtil.setTranslucentForImageView(this, 0, iconHead);
        mTilesFrameLayout.setOnAnimationFinishedListener(new TilesFrameLayoutListener() {
            @Override
            public void onAnimationFinished() {
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

        btAdapt = BluetoothAdapter.getDefaultAdapter();// 初始化本机蓝牙功能
        btnSearch.setOnClickListener(new IClickListener() {
            @Override
            protected void onIClick(View v) {
                searchBle();
            }
        });

        // ListView及其数据源 适配器  
        itemAdapter = new BleTextItemAdapter(lstDevices);
        final LinearLayoutManager manager = new LinearLayoutManager(this);
        itemAdapter.openLoadAnimation();
        lvBTDevices.setLayoutManager(manager);
        lvBTDevices.setAdapter(itemAdapter);
        lvBTDevices.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (btAdapt.isDiscovering()) btAdapt.cancelDiscovery();
                LogUtils.e("ble  :  " + lstDevices.get(position));
                String str = lstDevices.get(position);
                String[] values = str.split("\\|");
                String address = values[2];
                Log.e("address", values[2]);
                BluetoothDevice btDev = btAdapt.getRemoteDevice(address);
                try {
                    if (btDev.getBondState() == BluetoothDevice.BOND_NONE) {
                        //利用反射方法调用BluetoothDevice.createBond(BluetoothDevice remoteDevice);
                        Method createBondMethod = BluetoothDevice.class
                                .getMethod("createBond");
                        Log.d("BlueToothTTActivity", "开始配对");
                        Boolean returnValue = (Boolean) createBondMethod.invoke(btDev);
                    } else if (btDev.getBondState() == BluetoothDevice.BOND_BONDED) {
                        connect(btDev);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        searchBle();
        // ========================================================  
        //if (btAdapt.getState() == BluetoothAdapter.STATE_OFF)// 读取蓝牙状态并显示
        // ============================================================
        // 注册Receiver来获取蓝牙设备相关的结果  
        IntentFilter intent = new IntentFilter();
        intent.addAction(BluetoothDevice.ACTION_FOUND);// 用BroadcastReceiver来取得搜索结果
        intent.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        intent.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
        intent.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(searchDevices, intent);
//        RefushReceiver receiver=new RefushReceiver();
//        receiver.registRefushReceiver();
    }


    private BroadcastReceiver searchDevices = new BroadcastReceiver() {

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Bundle b = intent.getExtras();
            Object[] lstName = b.keySet().toArray();

            // 显示所有收到的消息及其细节  
            for (int i = 0; i < lstName.length; i++) {
                String keyName = lstName[i].toString();
                Log.e(keyName, String.valueOf(b.get(keyName)));
            }
            BluetoothDevice device = null;
            // 搜索设备时，取得设备的MAC地址  
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                device = intent
                        .getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (device.getBondState() == BluetoothDevice.BOND_NONE) {
                    String str = "{fa-bluetooth @color/red 19sp}\t" + getString(R.string.seer) + "|" + device.getName() + "|"
                            + device.getAddress();
                    if (lstDevices.indexOf(str) == -1)// 防止重复添加  
                        itemAdapter.addData(str); // 获取设备名称和mac地址
                }
            } else if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {
                device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                switch (device.getBondState()) {
                    case BluetoothDevice.BOND_BONDING:
                        Log.d("BlueToothTTActivity", "正在配对......");
                        break;
                    case BluetoothDevice.BOND_BONDED:
                        Log.d("BlueToothTTActivity", "完成配对");
                        connect(device);//连接设备
                        break;
                    case BluetoothDevice.BOND_NONE:
                        Log.d("BlueToothTTActivity", "取消配对");
                    default:
                        break;
                }
            }

        }
    };

    @Override
    protected void onDestroy() {
        this.unregisterReceiver(searchDevices);
        super.onDestroy();
    }


    private void connect(BluetoothDevice btDev) {

        BluetoothDevice device = btAdapt
                .getRemoteDevice(btDev.getAddress());
        Log.d("BlueToothTTActivity", "开始连接...");
        BluetoothService.getInstance().connect(device);
        mTilesFrameLayout.startAnimation();
        //onBackPressed();
        /*UUID uuid = UUID.fromString(SPP_UUID);
        try {
            btSocket = btDev.createRfcommSocketToServiceRecord(uuid);
            Log.d("BlueToothTTActivity", "开始连接...");
            btSocket.connect();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }  */
    }

    void searchBle() {
        if (btAdapt.getState() == BluetoothAdapter.STATE_OFF) {// 如果蓝牙还没开启
            Toast.makeText(BlueToothTTActivity.this, R.string.vrrete, Toast.LENGTH_SHORT)
                    .show();
            btAdapt.enable();
            btnSearch.setVisibility(View.VISIBLE);
            return;
        }
        if (btAdapt.isDiscovering())
            btAdapt.cancelDiscovery();
        lstDevices.clear();
        Object[] lstDevice = btAdapt.getBondedDevices().toArray();
        for (int i = 0; i < lstDevice.length; i++) {
            BluetoothDevice device = (BluetoothDevice) lstDevice[i];
            String str = "{fa-bluetooth @color/colormain5 22sp}\t" + getString(R.string.tgfh) + "|" + device.getName() + "|"
                    + device.getAddress();
            itemAdapter.addData(str); // 获取设备名称和mac地址
        }
        setTitle(getString(R.string.vbrt) + btAdapt.getAddress());
        btAdapt.startDiscovery();
    }

    @Deprecated
    private class RefushReceiver extends BroadcastReceiver {
        public void registRefushReceiver() {
            IntentFilter intent = new IntentFilter();
            intent.addAction(BluetoothDevice.ACTION_FOUND);// 用BroadcastReceiver来取得搜索结果
            intent.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
            intent.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
            intent.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
            //registerReceiver(searchDevices, intent);
            LocalBroadcastManager.getInstance(BlueToothTTActivity.this.getApplicationContext())
                    .registerReceiver(this, intent);
        }

        @Override
        public void onReceive(Context arg0, Intent intent) {
            String action = intent.getAction();
            Bundle b = intent.getExtras();
            Object[] lstName = b.keySet().toArray();

            // 显示所有收到的消息及其细节
            for (int i = 0; i < lstName.length; i++) {
                String keyName = lstName[i].toString();
                Log.e(keyName, String.valueOf(b.get(keyName)));
            }
            BluetoothDevice device = null;
            // 搜索设备时，取得设备的MAC地址
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                device = intent
                        .getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (device.getBondState() == BluetoothDevice.BOND_NONE) {
                    String str = "未配对|" + device.getName() + "|"
                            + device.getAddress();
                    if (lstDevices.indexOf(str) == -1)// 防止重复添加
                        itemAdapter.addData(str); // 获取设备名称和mac地址
                }
            } else if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {
                device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                switch (device.getBondState()) {
                    case BluetoothDevice.BOND_BONDING:
                        Log.d("BlueToothTTActivity", "正在配对......");
                        break;
                    case BluetoothDevice.BOND_BONDED:
                        Log.d("BlueToothTTActivity", "完成配对");
                        connect(device);//连接设备
                        break;
                    case BluetoothDevice.BOND_NONE:
                        Log.d("BlueToothTTActivity", "取消配对");
                    default:
                        break;
                }
            }

        }
    }
}  