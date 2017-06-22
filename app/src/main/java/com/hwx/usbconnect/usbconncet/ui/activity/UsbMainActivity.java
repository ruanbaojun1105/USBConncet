package com.hwx.usbconnect.usbconncet.ui.activity;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Process;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.ViewPager;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.hwx.usbconnect.usbconncet.Constants;
import com.hwx.usbconnect.usbconncet.R;
import com.hwx.usbconnect.usbconncet.bluetooth.BluetoothService;
import com.hwx.usbconnect.usbconncet.bluetooth.CommandReceiver;
import com.hwx.usbconnect.usbconncet.ftp.UtilsFTP;
import com.hwx.usbconnect.usbconncet.ui.ScanHelper;
import com.hwx.usbconnect.usbconncet.ui.adapter.MyFragmentPagerAdapter;
import com.hwx.usbconnect.usbconncet.ui.fragment.InfoFragment;
import com.hwx.usbconnect.usbconncet.ui.fragment.MainFragment;
import com.hwx.usbconnect.usbconncet.ui.fragment.OnLineFragment;
import com.hwx.usbconnect.usbconncet.ui.fragment.UseFragment;
import com.hwx.usbconnect.usbconncet.utils.AnimTextView;
import com.hwx.usbconnect.usbconncet.utils.IClickListener;
import com.hwx.usbconnect.usbconncet.utils.LogUtils;
import com.hwx.usbconnect.usbconncet.utils.StateButton;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.joanzapata.iconify.widget.IconTextView;
import com.liulishuo.magicprogresswidget.MagicProgressCircle;
import com.umeng.analytics.MobclickAgent;

import org.apache.commons.net.ftp.FTPFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import me.weyye.hipermission.HiPermission;
import me.weyye.hipermission.PermissionCallback;
import me.weyye.hipermission.PermissionItem;

public class UsbMainActivity extends SimpleActivity {

    @BindView(R.id.tabs)
    TabLayout tabs;
    @BindView(R.id.icon_text)
    IconTextView icon_text;
    @BindView(R.id.ble_icon_text)
    IconTextView ble_icon_text;
    @BindView(R.id.icon_dowload)
    IconTextView iconDowload;
    @BindView(R.id.container)
    ViewPager mViewPager;
    @BindView(R.id.main_content)
    RelativeLayout mainContent;
//    @BindView(R.id.fab)
//    FloatingActionButton fab;
    private MyFragmentPagerAdapter mSectionsPagerAdapter;
    private List<Fragment> fragmentList = new ArrayList<>();
    public static ScanHelper mScanHelper;
    private InfoFragment infoFragment;
    private UtilsFTP ftp = null;
    private Dialog dialog = null;

    public static void changeAppLanguage(Context context) {
        if (!Constants.isOpenEN)
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
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        //mScanHelper.registerReceiver();
    }
    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
        //mScanHelper.unregisterReceiver();
    }
//    @Override
//    public boolean onKeyUp(int keyCode, KeyEvent event) {
//        // TODO Auto-generated method stub
//        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
//            // MobclickAgent.onKillProcess(Context context);//如果开发者调用
//            // Process.kill 或者 System.exit 之类的方法杀死进程，请务必在此之前调用此方法，用来保存统计数据。
//            android.os.Process.killProcess(android.os.Process.myPid());
//            System.exit(0);
//            return true;
//        }
//        return false;
//    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mScanHelper.unregisterReceiver();
        mScanHelper.stopScan();
        Process.killProcess(Process.myPid());
        System.exit(0);
    }
    @Override
    protected int getLayout() {
        return R.layout.activity_usb_main;
    }
    @Override
    protected void initEventAndData() {
        changeAppLanguage(this);
        int i = 3;
        LogUtils.e(i++ + "3");
        fragmentList.clear();
        fragmentList.add(OnLineFragment.newInstance());
        fragmentList.add(MainFragment.newInstance());
        if (!Constants.isOpenCutInfo) {
            infoFragment = InfoFragment.newInstance();
            fragmentList.add(infoFragment);
        }
        String[] aa = Constants.isOpenCutInfo ? new String[]{"实时模式", getString(R.string.vdadg), getString(R.string.vfahta)} : new String[]{"实时模式", getString(R.string.vdadg), getString(R.string.fgaata), getString(R.string.vfahta)};
        fragmentList.add(UseFragment.newInstance(getString(R.string.ddavv), getString(R.string.vavdgsdgsa)));
        mSectionsPagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), aa, fragmentList);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                ((OnLineFragment) fragmentList.get(0)).setCleanAnim(true);
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                ((OnLineFragment) fragmentList.get(0)).setCleanAnim(state != 0);
            }
        });
        tabs.setupWithViewPager(mViewPager);

        icon_text.setText("{fa-circle-o-notch spin}");
        iconDowload.setOnClickListener(new IClickListener() {
            @Override
            protected void onIClick(View v) {
                toStarDownImage();
            }
        });
        iconDowload.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Toast.makeText(UsbMainActivity.this, R.string.vdatttttt, Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        init();
        initBle();
        if (!getPackageManager().hasSystemFeature("android.hardware.usb.host")) {
            Toast.makeText(this, R.string.vadft, Toast.LENGTH_SHORT).show();
        }
    }
    private void init() {
        mScanHelper = new ScanHelper(this, icon_text);
//        mScanHelper.setScanListener(new ScanHelper.ScanListener() {
//            @Override
//            public void scan(String data) {
//                //接收数据
//                if (infoFragment != null)
//                    infoFragment.setThis_info(data);
//            }
//        });
        mScanHelper.registerReceiver();
        mScanHelper.startScan(mScanHelper.checkScanDevice(Constants.DEVICE_VIDS, Constants.DEVICE_PIDS));
        icon_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mScanHelper.startScan(mScanHelper.checkScanDevice(Constants.DEVICE_VIDS, Constants.DEVICE_PIDS));
            }
        });
        icon_text.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (icon_text != null && !mScanHelper.isScanConn())
                    icon_text.setText("{fa-usb @color/red}");//错误
            }
        }, 3500);
    }

    public boolean isOffLinePage() {
        return mViewPager.getCurrentItem()==1;
    }

    public void setIcon_text(String aa) {
        icon_text.setText(aa);
    }

    private void initBle() {
        //fab.setImageDrawable(new IconDrawable(mContext, FontAwesomeIcons.fa_bluetooth).colorRes(R.color.white));//.sizeDp(25););
        //fab.bringToFront();
        ble_icon_text.setOnClickListener(new IClickListener() {
            @Override
            protected void onIClick(View v) {
                if (!HiPermission.checkPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION)) {
                    List<PermissionItem> permissionItems = new ArrayList<PermissionItem>();
                    permissionItems.add(new PermissionItem(Manifest.permission.ACCESS_FINE_LOCATION, "位置权限", R.drawable.permission_ic_location));
                    permissionItems.add(new PermissionItem(Manifest.permission.ACCESS_COARSE_LOCATION, "位置权限", R.drawable.permission_ic_location));
                    HiPermission.create(mContext).title("").permissions(permissionItems)
                            .filterColor(ResourcesCompat.getColor(getResources(), R.color.colorPrimary, mContext.getTheme()))//permission icon color
                            .msg("请给权限")
                            .style(R.style.PermissionBlueStyle)
                            .checkMutiPermission(new PermissionCallback() {
                                @Override
                                public void onClose() {
                                    Toast.makeText(UsbMainActivity.this, "请授予权限", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onFinish() {
                                    startActivity(new Intent(mContext,BlueToothTTActivity.class));
                                }

                                @Override
                                public void onDeny(String permission, int position) {

                                }

                                @Override
                                public void onGuarantee(String permission, int position) {

                                }
                            });
                }else startActivity(new Intent(mContext,BlueToothTTActivity.class));

            }
        });
        ble_icon_text.bringToFront();
        ble_icon_text.setText("{fa-bluetooth @color/red}");
        ObjectAnimator rotation = ObjectAnimator.ofFloat(ble_icon_text, "rotation", 0f, 360f);
        ObjectAnimator fadeInOut = ObjectAnimator.ofFloat(ble_icon_text,  "alpha", 1f, 0f,1f);
        final AnimatorSet animSet = new AnimatorSet();
        animSet.play(rotation).with(fadeInOut);
        animSet.setDuration(3000);
        animSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
            }
            @Override
            public void onAnimationEnd(Animator animator) {
                //fab.setVisibility(View.GONE);
                ble_icon_text.setText("{fa-bluetooth @color/colormain5}");
            }

            @Override
            public void onAnimationCancel(Animator animator) {
                ble_icon_text.setText("{fa-bluetooth @color/colormain5}");
            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        final AnimatorSet animSet1 = new AnimatorSet();
        animSet1.play(rotation).with(fadeInOut);
        animSet1.setDuration(3000);
        animSet1.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
            }
            @Override
            public void onAnimationEnd(Animator animator) {
                //fab.setVisibility(View.VISIBLE);
                ble_icon_text.setText("{fa-bluetooth @color/red}");
            }

            @Override
            public void onAnimationCancel(Animator animator) {
                ble_icon_text.setText("{fa-bluetooth @color/red}");
            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        new CommandReceiver() {
            @Override
            public void onDataReceived(byte[] buffer, byte function, byte safeCod) {
            }
            @Override
            public void onFail() {
                Toast.makeText(UsbMainActivity.this, "BLE lost", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onLost() {
                Toast.makeText(UsbMainActivity.this, "BLE lost", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDeviceInfo(String name, String address) {
            }
            @Override
            public void onStadeTag(int stade) {
                LogUtils.e("stade---"+stade);
                switch (stade){
                    case 0:
                        animSet1.start();
                        //connect_state.setText("we're doing nothing");
                        break;
                    case 1:
                        animSet1.start();
                        //connect_state.setText("now listening for incoming connections");
                        break;
                    case 2:
                        ble_icon_text.setText("{fa-circle-o-notch spin}");
                       //connect_state.setText("now initiating an outgoing connection");
                        break;
                    case 3:
                        animSet.start();
                        //connect_state.setText("now connected to a remote "+CONNECT_NAME);
                        break;
                }
            }
        }.regiest();
    }

    public void toStarDownImage() {
        dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_edit_image);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Display d = ((Activity) mContext).getWindowManager().getDefaultDisplay(); // 为获取屏幕宽、高
        WindowManager.LayoutParams p = dialog.getWindow().getAttributes(); // 获取对话框当前的参数值
        p.width = (int) (d.getWidth() * 0.8);
        //p.height =(int)( d.getHeight()*0.6);
        dialog.getWindow().setAttributes(p);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        final MagicProgressCircle magicProgressCircle = (MagicProgressCircle) dialog.findViewById(R.id.demo_mpc);
        magicProgressCircle.setSmoothPercent(0);
        final AnimTextView animTextView = (AnimTextView) dialog.findViewById(R.id.demo_tv);
        animTextView.setSmoothPercent(0);
        final TextView info_text = (TextView) dialog.findViewById(R.id.info_text);
        StateButton cancal = (StateButton) dialog.findViewById(R.id.cancel);
        cancal.setVisibility(View.VISIBLE);
        cancal.setOnClickListener(new IClickListener() {
            @Override
            protected void onIClick(View v) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (ftp != null) {
                            try {
                                ftp.disconnect();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }).start();
            }
        });
        info_text.setText("开始连接");
        dialog.show();
        Thread mThread = new Thread() {
            public void run() {
                ftp = new UtilsFTP("39.108.147.206", 21, "anonymous", "anonymous", false);
                try {
                    ftp.connect();
//					连接成功后发心跳包
//							int count = 0;
//							while (ftp.getClient().sendNoOp()) {
//								Thread.sleep(3000);
////								toast("心跳" + String.valueOf(count));
//								count++;
//							}
//					ftp.disconnect();
//					toast("断开成功");
                    Thread.sleep(1000);
                    String itemPath = MainFragment.getInnerSDCardPath() + "/HWX-SPINNER/";
                    download(info_text, itemPath, dialog, magicProgressCircle, animTextView);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    info_text.post(new Runnable() {
                        @Override
                        public void run() {
                            info_text.setText("error");
                            dialog.dismiss();
                        }
                    });
                }
            }

        };
        mThread.start();
    }

    public void download(final TextView text, final String path, final Dialog dialog, final MagicProgressCircle magicProgressCircle, final AnimTextView animTextView) {
        new Thread() {
            public void run() {
                try {
                    UtilsFTP.IProgressListener listener = new UtilsFTP.IProgressListener() {
                        long BEG = 0;

                        @Override
                        public void onProgress(long len, long total) {
                            if (System.currentTimeMillis() - BEG > 200 || len == total) {
                                BEG = System.currentTimeMillis();
                                double a = 100 * (double) len / (double) total;
                                String result = String.format("%.2f", a);
                                LogUtils.e(result + "%");
                            }
                        }
                    };
                    FTPFile[] ftpFiles = ftp.getClient().listFiles("/picture");
                    int tag = 0;
                    if (ftpFiles != null) {
                        tag = ftpFiles.length;
                        for (int i = 0; i < tag; i++) {
                            final FTPFile file = ftpFiles[i];
                            if (file.getSize() != 240)
                                continue;
                            final int finalI = i;
                            final int finalTag1 = tag - 1;
                            text.post(new Runnable() {
                                @Override
                                public void run() {
                                    text.setText("Downloading at " + finalI + ":" + file.getName() + "  \t" + file.getSize() + " byte");
                                    animTextView.setProgress((int) ((float) finalI / (float) finalTag1 * 100f));
                                    magicProgressCircle.setSmoothPercent(animTextView.getPercent());
                                }
                            });
                            ftp.downloadWithProgress("/picture/" + file.getName(), new File(path + file.getName()), listener);
                        }
                    }
                    if (ftp.isConnected()) {
                        ftp.disconnect();
                        ftpFiles = null;
                    }
                    final int finalTag = tag;
                    text.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            dialog.dismiss();
                            Toast.makeText(mContext, finalTag > 0 ? "OK" : "No content", Toast.LENGTH_SHORT).show();
                            if (finalTag > 0)
                                ((MainFragment) mSectionsPagerAdapter.getItem(1)).updaData();
                        }
                    }, 500);
                } catch (Exception e) {
                    // TODO: handle exception
                    text.post(new Runnable() {
                        @Override
                        public void run() {
                            text.setText("error");
                            dialog.dismiss();
                        }
                    });
                }
            }

            ;

        }.start();
    }


    public static long exitTime;
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if ((System.currentTimeMillis() - exitTime) > 1200) {
                Toast.makeText(mContext, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                BluetoothService.getInstance().stop();
                mScanHelper.unregisterReceiver();
                mScanHelper.stopScan();
                MobclickAgent.onKillProcess(mContext);//如果开发者调用
                Process.killProcess(Process.myPid());
                System.exit(0);
            }
            LogUtils.e("~~~~~~~~~onback");
            return true;
        }
        return false;
    }
}
