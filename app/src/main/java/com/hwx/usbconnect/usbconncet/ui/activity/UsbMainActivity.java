package com.hwx.usbconnect.usbconncet.ui.activity;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.hwx.usbconnect.usbconncet.R;
import com.hwx.usbconnect.usbconncet.ui.ScanHelper;
import com.hwx.usbconnect.usbconncet.ui.adapter.MyFragmentPagerAdapter;
import com.hwx.usbconnect.usbconncet.ui.fragment.InfoFragment;
import com.hwx.usbconnect.usbconncet.ui.fragment.MainFragment;
import com.hwx.usbconnect.usbconncet.ui.fragment.UseFragment;
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
        fragmentList.add(UseFragment.newInstance(getString(R.string.ddavv),getString(R.string.vavdgsdgsa)));
        mSectionsPagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(),new String[]{getString(R.string.vdadg),getString(R.string.fgaata),getString(R.string.vfahta)},fragmentList);
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);


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
            mViewPager.setCurrentItem(2,false);
            return true;
        }
        if (id == R.id.action_exit) {
            System.exit(0);
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
