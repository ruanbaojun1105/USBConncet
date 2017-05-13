package com.hwx.usbconnect.usbconncet.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.hwx.usbconnect.usbconncet.R;
import com.hwx.usbconnect.usbconncet.ui.ScanHelper;
import com.hwx.usbconnect.usbconncet.ui.adapter.MyFragmentPagerAdapter;
import com.hwx.usbconnect.usbconncet.ui.fragment.InfoFragment;
import com.hwx.usbconnect.usbconncet.ui.fragment.MainFragment;
import com.hwx.usbconnect.usbconncet.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class UsbMainActivity extends AppCompatActivity {

    private MyFragmentPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private List<Fragment> fragmentList=new ArrayList<>();
    public static ScanHelper mScanHelper;
    private InfoFragment infoFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usb_main);
        fragmentList.clear();
        fragmentList.add(MainFragment.newInstance());
        infoFragment=InfoFragment.newInstance();
        fragmentList.add(infoFragment);
        mSectionsPagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(),fragmentList);
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        init();
    }

    private void init() {
        mScanHelper = new ScanHelper(this);
        mScanHelper.setScanListener(new ScanHelper.ScanListener() {
            @Override
            public void scan(String data) {
                //接收数据
                if (infoFragment!=null)
                    infoFragment.setThis_info(data);
            }
        });
        mScanHelper.registerReceiver();
        mScanHelper.startScan(mScanHelper.checkScanDevice(Constants.DEVICE_VIDS,Constants.DEVICE_PIDS));
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_usb_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mScanHelper.unregisterReceiver();
        mScanHelper.stopScan();
    }
}
