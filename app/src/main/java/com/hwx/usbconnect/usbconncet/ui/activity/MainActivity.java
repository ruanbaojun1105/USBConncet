package com.hwx.usbconnect.usbconncet.ui.activity;

import android.hardware.usb.UsbDevice;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.hwx.usbconnect.usbconncet.R;
import com.hwx.usbconnect.usbconncet.ui.ScanHelper;
import com.hwx.usbconnect.usbconncet.utils.Constants;

public class MainActivity extends AppCompatActivity {
    private ScanHelper mScanHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_usb);
        init();
        final EditText editText= (EditText) findViewById(R.id.editText);
        Button button= (Button) findViewById(R.id.set_btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(editText.getText()))
                    mScanHelper.sendData(editText.getText().toString().trim().getBytes());
            }
        });
    }
    private void init() {
        mScanHelper = new ScanHelper(this);
        mScanHelper.setScanListener(new ScanHelper.ScanListener() {
            @Override
            public void scan(String data) {
                //接收数据
            }
        });
        mScanHelper.registerReceiver();
        mScanHelper.startScan(mScanHelper.checkScanDevice(Constants.DEVICE_VIDS,Constants.DEVICE_PIDS));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mScanHelper.unregisterReceiver();
        mScanHelper.stopScan();
    }


}
