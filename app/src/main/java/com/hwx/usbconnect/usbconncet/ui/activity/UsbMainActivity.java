package com.hwx.usbconnect.usbconncet.ui.activity;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Process;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hwx.usbconnect.usbconncet.AppConfig;
import com.hwx.usbconnect.usbconncet.Constants;
import com.hwx.usbconnect.usbconncet.IBackService;
import com.hwx.usbconnect.usbconncet.MsgBackInterface;
import com.hwx.usbconnect.usbconncet.MsgInterface;
import com.hwx.usbconnect.usbconncet.R;
import com.hwx.usbconnect.usbconncet.bean.MessageTalk;
import com.hwx.usbconnect.usbconncet.bean.MessageTalkMe;
import com.hwx.usbconnect.usbconncet.bean.MessageTalkOther;
import com.hwx.usbconnect.usbconncet.ftp.UtilsFTP;
import com.hwx.usbconnect.usbconncet.ui.BluetoothService;
import com.hwx.usbconnect.usbconncet.ui.UsbScanHelper;
import com.hwx.usbconnect.usbconncet.ui.adapter.MyFragmentPagerAdapter;
import com.hwx.usbconnect.usbconncet.ui.brocast.CommandReceiver;
import com.hwx.usbconnect.usbconncet.ui.brocast.TcpAIDLService;
import com.hwx.usbconnect.usbconncet.ui.fragment.MainFragment;
import com.hwx.usbconnect.usbconncet.ui.fragment.OnLineFragment;
import com.hwx.usbconnect.usbconncet.ui.fragment.TalkFragment;
import com.hwx.usbconnect.usbconncet.ui.widget.AnimTextView;
import com.hwx.usbconnect.usbconncet.ui.widget.StateButton;
import com.hwx.usbconnect.usbconncet.utils.ACache;
import com.hwx.usbconnect.usbconncet.utils.IClickListener;
import com.hwx.usbconnect.usbconncet.utils.LogUtils;
import com.jaeger.library.StatusBarUtil;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.joanzapata.iconify.widget.IconTextView;
import com.liulishuo.magicprogresswidget.MagicProgressCircle;
import com.umeng.analytics.MobclickAgent;
import com.xw.repo.XEditText;

import org.apache.commons.net.ftp.FTPFile;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
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
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.navigation)
    NavigationView navigation;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    //    @BindView(R.id.fab)
//    FloatingActionButton fab;
    private String name;

    private MyFragmentPagerAdapter mSectionsPagerAdapter;
    private List<Fragment> fragmentList = new ArrayList<>();
    public static UsbScanHelper mUsbScanHelper;
    private UtilsFTP ftp = null;
    private Dialog dialog = null;

    private IBackService iBackService;
    private Intent mServiceIntent;
    private List<MessageTalk> msgList = new ArrayList<>();
    private ServiceConnection conn = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
            iBackService = null;

        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            iBackService = IBackService.Stub.asInterface(service);
        }
    };

    public boolean isTcpConnected() {
        if (iBackService == null)
            return false;
        try {
            return iBackService.isConnect();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return false;
    }

    //    MsgInterface msgCallc;
    public void setBackMsgCall(final MsgInterface msgCall) {
//        msgCallc=msgCall;
        if (iBackService == null)
            return;
        try {//将聊天消息包裹一层再下发 用作数据恢复
            iBackService.getMessage(new MsgBackInterface() {
                @Override
                public void getBackMsg(String name, String msg) throws RemoteException {
                    if (msg.startsWith("^")) {
                        msg = msg.replace("^", "");
                        msgList.add(new MessageTalkMe(name, msg));
                    } else {
                        String[] arr = msg.split(":");
                        if (arr == null)
                            msgList.add(new MessageTalkOther(name, "", msg));
                        else if (arr.length < 2)
                            msgList.add(new MessageTalkOther(name, "", msg));
                        else {
                            StringBuilder bu = new StringBuilder("");
                            for (int i = 1; i < arr.length; i++) {
                                bu.append(arr[i]);
                            }
                            msgList.add(new MessageTalkOther(name, arr[0].replace("\t", "").replace("\n", ""), bu.toString()));
                        }
                    }
                    if (msgCall != null)
                        msgCall.getBackMsg(name, msg);
                }

                @Override
                public IBinder asBinder() {
                    return null;
                }
            });
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(final String text) {
//        msgList.add(new MessageTalkMe("测试",text));
//        if (msgCallc!=null)
//            msgCallc.getBackMsg("","");
        if (iBackService == null)
            return;
        if (TextUtils.isEmpty(text))
            return;
        try {
            if (!iBackService.isConnect()) {
                Snackbar.make(mViewPager, "There is a glitch in the server!", Snackbar.LENGTH_SHORT).show();
                //Toast.makeText(this, "There is a glitch in the server!", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    iBackService.sendMessage(text);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("list", (ArrayList<MessageTalk>) msgList);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        name=AppConfig.getInstance().getString("talk_name","");
        if (savedInstanceState == null)
            return;
        msgList = (ArrayList<MessageTalk>) savedInstanceState.getSerializable("list");
    }

    public List<MessageTalk> getMsgList() {
        return msgList;
    }

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
    protected void setStatusBar() {
//        StatusBarUtil.setColorForDrawerLayout(this,drawerLayout,mContext.getResources().getColor(R.color.colorPrimary), 15);
//        StatusBarUtil.setColor(UsbMainActivity.this,mContext.getResources().getColor(R.color.colorPrimary), 0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        //mUsbScanHelper.registerReceiver();
        LogUtils.e("onResume");
        bindService(mServiceIntent, conn, BIND_AUTO_CREATE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
        //mUsbScanHelper.unregisterReceiver();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Process.killProcess(Process.myPid());
        System.exit(0);
    }

    @Override
    protected void onStart() {
        super.onStart();
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
        setSupportActionBar(toolbar);
        //StatusBarUtil.setColorForDrawerLayout(this,drawerLayout,mContext.getResources().getColor(R.color.colorPrimary), 0);
        //StatusBarUtil.setTranslucent(this,0);
        setStatusBar();
        mServiceIntent = new Intent(this, TcpAIDLService.class);
//        StatusBarUtil.setTranslucentForImageView(this, 15, toolbar);
//        StatusBarUtil.setColorForDrawerLayout(this, drawerLayout, mContext.getResources().getColor(R.color.colorPrimary), 15);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.okfkkat,
//                R.string.clean);
//        drawerLayout.setDrawerListener(toggle);
//        toggle.syncState();
        initPage();
        initUSB();
        initBle();
        initData();
        if (!getPackageManager().hasSystemFeature("android.hardware.usb.host")) {
            Toast.makeText(this, R.string.vadft, Toast.LENGTH_SHORT).show();
        }
    }

    private void initPage(){
        fragmentList.clear();
        fragmentList.add(OnLineFragment.newInstance());
        fragmentList.add(MainFragment.newInstance());
        fragmentList.add(TalkFragment.newInstance());
        //fragmentList.add(UseFragment.newInstance(getString(R.string.ddavv), getString(R.string.vavdgsdgsa)));
        String[] aa = new String[]{getString(R.string.tew), getString(R.string.vdadg), getString(R.string.vedde), getString(R.string.vfahta)};
        mSectionsPagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), aa, fragmentList);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //LogUtils.e("positionOffset"+positionOffset+"positionOffsetPixels"+positionOffsetPixels);
                ((OnLineFragment) fragmentList.get(0)).setCleanAnim(true);
                InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm.isActive())
                    imm.hideSoftInputFromWindow(mViewPager.getWindowToken(), 0);
            }

            @Override
            public void onPageSelected(int position) {
                //if (!isLinePage())
//                StatusBarUtil.setColor(UsbMainActivity.this, mContext.getResources().getColor(R.color.colorPrimary), 15);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                ((OnLineFragment) fragmentList.get(0)).setCleanAnim(state != 0);
            }
        });
        tabs.setupWithViewPager(mViewPager);
    }

    public void initData(){
        if (TextUtils.isEmpty(AppConfig.getInstance().getString("talk_name",""))){
            String imei = null;
            try {
                imei = ((TelephonyManager) mContext.getSystemService(TELEPHONY_SERVICE)).getDeviceId();
            } catch (Exception e) {
                e.printStackTrace();
                imei="";
            }
            AppConfig.getInstance().putString("talk_name",imei.substring((imei.length()>6?imei.length()-6:0)));
        }
        name=AppConfig.getInstance().getString("talk_name","");
        TextView text_name= (TextView) navigation.getHeaderView(0).findViewById(R.id.text_name);
        text_name.setText(name);

        ACache aCache = ACache.get(mContext);
        Object obj = aCache.getAsObject("msg_list_t");
        if (obj != null) {
            msgList.clear();
            msgList = (List<MessageTalk>) obj;
        }
        navigation.getMenu().findItem(R.id.nav_info).setIcon(
                new IconDrawable(this, FontAwesomeIcons.fa_info_circle)
                        .colorRes(R.color.colormain6)
                        .actionBarSize());
        navigation.getMenu().findItem(R.id.nav_about).setIcon(
                new IconDrawable(this, FontAwesomeIcons.fa_circle_thin)
                        .colorRes(R.color.light_orange)
                        .actionBarSize());
        navigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_info:
                        startActivity(new Intent(mContext,UseInfoActivity.class));
                        break;
                    case R.id.nav_about:
                        new AlertDialog.Builder(mContext).setTitle("关于我们")
                                .setMessage("勇往直前，一马当先。")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                })
                                .setNegativeButton("取消", null)
                                .show();
                        break;
                }
                drawerLayout.closeDrawers();
                return false;
            }
        });
        View.OnClickListener clickListener=new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
                drawerLayout.closeDrawers();
            }
        };
        navigation.getHeaderView(0).setOnClickListener(clickListener);
        navigation.getHeaderView(0).findViewById(R.id.icon_head).setOnClickListener(clickListener);
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
    }

    private void initUSB() {
        mUsbScanHelper = new UsbScanHelper(this, icon_text);
//        mUsbScanHelper.setScanListener(new UsbScanHelper.ScanListener() {
//            @Override
//            public void scan(String data) {
//                //接收数据
//                if (infoFragment != null)
//                    infoFragment.setThis_info(data);
//            }
//        });
        mUsbScanHelper.registerReceiver();
        mUsbScanHelper.startScan(mUsbScanHelper.checkScanDevice(Constants.DEVICE_VIDS, Constants.DEVICE_PIDS));
        icon_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mUsbScanHelper.startScan(mUsbScanHelper.checkScanDevice(Constants.DEVICE_VIDS, Constants.DEVICE_PIDS));
            }
        });
        icon_text.setText("{fa-circle-o-notch spin}");
        icon_text.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (icon_text != null && !mUsbScanHelper.isScanConn())
                    icon_text.setText("{fa-usb @color/red}");//错误
            }
        }, 3500);
    }

    public boolean isOffLinePage() {
        return mViewPager.getCurrentItem() == 1;
    }

    public boolean isLinePage() {
        return mViewPager.getCurrentItem() == 0;
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
                    permissionItems.add(new PermissionItem(Manifest.permission.ACCESS_FINE_LOCATION, getString(R.string.vdtetet), R.drawable.permission_ic_location));
                    permissionItems.add(new PermissionItem(Manifest.permission.ACCESS_COARSE_LOCATION, getString(R.string.vdtetet), R.drawable.permission_ic_location));
                    HiPermission.create(mContext).title("BLE request").permissions(permissionItems)
                            .filterColor(ResourcesCompat.getColor(mContext.getResources(), R.color.colorPrimary, mContext.getTheme()))//permission icon color
                            .msg(getString(R.string.tseesta))
                            .style(R.style.PermissionBlueStyle)
                            .checkMutiPermission(new PermissionCallback() {
                                @Override
                                public void onClose() {
                                    Toast.makeText(UsbMainActivity.this, getString(R.string.tseesta), Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onFinish() {
                                    startActivity(new Intent(mContext, BlueToothTTActivity.class));
                                }

                                @Override
                                public void onDeny(String permission, int position) {

                                }

                                @Override
                                public void onGuarantee(String permission, int position) {

                                }
                            });
                } else startActivity(new Intent(mContext, BlueToothTTActivity.class));

            }
        });
        ble_icon_text.bringToFront();
        ble_icon_text.setText("{fa-bluetooth @color/red}");
        ObjectAnimator rotation = ObjectAnimator.ofFloat(ble_icon_text, "rotation", 0f, 360f);
        ObjectAnimator fadeInOut = ObjectAnimator.ofFloat(ble_icon_text, "alpha", 1f, 0f, 0.8f);
        final AnimatorSet animSet = new AnimatorSet();
        animSet.play(rotation).with(fadeInOut);
        animSet.setDuration(3000);
        animSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
            }

            @Override
            public void onAnimationEnd(Animator animator) {
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
                Toast.makeText(UsbMainActivity.this, R.string.jkasjkkt, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLost() {
                Toast.makeText(UsbMainActivity.this, R.string.retjkjt, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDeviceInfo(String name, String address) {
            }

            @Override
            public void onStadeTag(int stade) {
                LogUtils.e("stade---" + stade);
                switch (stade) {
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

    public String getName() {
        return name;
    }

    public void showDialog() {
        final XEditText et=new XEditText(mContext);
        et.setDisableEmoji(true);
        et.setMaxLines(1);
        et.setFilters(new InputFilter[]{new InputFilter.LengthFilter(8)});
        et.setHint(AppConfig.getInstance().getString("talk_name",""));
        new AlertDialog.Builder(mContext)
                .setIcon(new IconDrawable(mContext, FontAwesomeIcons.fa_edit))
                .setTitle(R.string.tetea)
                .setView(et)
                .setPositiveButton(R.string.dtaddssd, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(et.getWindowToken(), 0);
                        final String input = et.getText().toString();
                        if (TextUtils.isEmpty(input))
                            return;
                        name=input;
                        AppConfig.getInstance().putString("talk_name",input);
                        TextView text_name= (TextView) navigation.getHeaderView(0).findViewById(R.id.text_name);
                        text_name.setText(name);
                    }
                })
                .setNegativeButton(R.string.dadttsadts, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(et.getWindowToken(), 0);
                    }
                })
                .show();
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
        info_text.setText(R.string.viasiotei);
        dialog.show();
        Thread mThread = new Thread() {
            public void run() {
                ftp = new UtilsFTP("39.108.147.206", 21, "anonymous", "anonymous", false);
                try {
                    ftp.connect();
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
                            Snackbar.make(info_text,"Time out",Snackbar.LENGTH_SHORT).show();
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
            if (drawerLayout.isDrawerOpen(Gravity.LEFT)){
                drawerLayout.closeDrawers();
            }else {
                if ((System.currentTimeMillis() - exitTime) > 1200) {
                    Toast.makeText(mContext, R.string.vkohaskjltk, Toast.LENGTH_SHORT).show();
                    exitTime = System.currentTimeMillis();
                } else {
                    ACache aCache = ACache.get(mContext);
                    //List<MessageTalk> list=msgList.subList(msgList.size()>20?(msgList.size()-20):0,msgList.size());
                    aCache.put("msg_list_t", (Serializable) msgList, 2 * ACache.TIME_DAY);//2天
                    unbindService(conn);
                    BluetoothService.getInstance().stop();
                    mUsbScanHelper.unregisterReceiver();
                    mUsbScanHelper.stopScan();
                    MobclickAgent.onKillProcess(mContext);//如果开发者调用
                    Process.killProcess(Process.myPid());
                    System.exit(0);
                }
            }
            LogUtils.e("~~~~~~~~~onback");
            return true;
        }
        return false;
    }
}
