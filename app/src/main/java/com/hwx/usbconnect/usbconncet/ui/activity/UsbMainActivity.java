package com.hwx.usbconnect.usbconncet.ui.activity;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.hwx.usbconnect.usbconncet.R;
import com.hwx.usbconnect.usbconncet.ui.ScanHelper;
import com.hwx.usbconnect.usbconncet.ui.adapter.MyFragmentPagerAdapter;
import com.hwx.usbconnect.usbconncet.ui.fragment.InfoFragment;
import com.hwx.usbconnect.usbconncet.ui.fragment.MainFragment;
import com.hwx.usbconnect.usbconncet.ui.fragment.UseFragment;
import com.hwx.usbconnect.usbconncet.utils.Constants;
import com.hwx.usbconnect.usbconncet.utils.LogUtils;
import com.joanzapata.iconify.widget.IconTextView;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class UsbMainActivity extends AppCompatActivity {

    private MyFragmentPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private IconTextView icon_text;
    private List<Fragment> fragmentList=new ArrayList<>();
    public static ScanHelper mScanHelper;
    private InfoFragment infoFragment;

    public static void changeAppLanguage(Context context) {
        if (!com.hwx.usbconnect.usbconncet.Constants.isOpenEN)
            return;
        // 本地语言设置"zh" : "en"
        Resources resources = context.getResources();
        Configuration config = resources.getConfiguration();
        Locale locale = new Locale("en");
        Locale.setDefault(locale);
        config.locale = locale; //简体中文
        resources.updateConfiguration(config, null);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        changeAppLanguage(this);
        int i=3;
        LogUtils.e(i+++"3");
        setContentView(R.layout.activity_usb_main);
        fragmentList.clear();
        fragmentList.add(MainFragment.newInstance());
        if (!com.hwx.usbconnect.usbconncet.Constants.isOpenCutInfo) {
            infoFragment = InfoFragment.newInstance();
            fragmentList.add(infoFragment);
        }
        fragmentList.add(UseFragment.newInstance(getString(R.string.ddavv),getString(R.string.vavdgsdgsa)));
        mSectionsPagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), com.hwx.usbconnect.usbconncet.Constants.isOpenCutInfo?new String[]{getString(R.string.vdadg),getString(R.string.vfahta)}:new String[]{getString(R.string.vdadg),getString(R.string.fgaata),getString(R.string.vfahta)},fragmentList);
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        icon_text= (IconTextView) findViewById(R.id.icon_text);
        icon_text.setText("{fa-circle-o-notch spin}");
        init();
        if (!getPackageManager().hasSystemFeature("android.hardware.usb.host")){
            Toast.makeText(this, R.string.vadft,Toast.LENGTH_SHORT).show();
        }
    }

    private void init() {
        mScanHelper = new ScanHelper(this,icon_text);
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
        icon_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mScanHelper.startScan(mScanHelper.checkScanDevice(Constants.DEVICE_VIDS,Constants.DEVICE_PIDS));
            }
        });
        icon_text.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (icon_text!=null&&!mScanHelper.isScanConn())
                    icon_text.setText("{fa-times-circle @color/red}");//错误
            }
        },3500);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
    /*@Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            // MobclickAgent.onKillProcess(Context context);//如果开发者调用
            // Process.kill 或者 System.exit 之类的方法杀死进程，请务必在此之前调用此方法，用来保存统计数据。
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);
            return true;
        }
        return false;
    }*/

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mScanHelper.unregisterReceiver();
        mScanHelper.stopScan();
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
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

}
