package com.hwx.usbconnect.usbconncet.ui.fragment;


import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hwx.usbconnect.usbconncet.R;
import com.hwx.usbconnect.usbconncet.bluetooth.BluetoothService;
import com.hwx.usbconnect.usbconncet.bluetooth.CommandReceiver;
import com.hwx.usbconnect.usbconncet.font.Font16;
import com.hwx.usbconnect.usbconncet.ui.ScanHelper;
import com.hwx.usbconnect.usbconncet.ui.activity.SimpleFragment;
import com.hwx.usbconnect.usbconncet.ui.activity.UsbMainActivity;
import com.hwx.usbconnect.usbconncet.ui.adapter.VPagerAdapter;
import com.hwx.usbconnect.usbconncet.utils.IClickListener;
import com.hwx.usbconnect.usbconncet.utils.JColorBar;
import com.hwx.usbconnect.usbconncet.utils.LogUtils;
import com.hwx.usbconnect.usbconncet.utils.SpinnerTopView;
import com.hwx.usbconnect.usbconncet.utils.StateButton;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import zhou.colorpalette.ColorSelectDialog;


public class OnLineFragment extends SimpleFragment {

    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.text_btn)
    Button text_btn;
    @BindView(R.id.image_btn)
    Button imageBtn;
    @BindView(R.id.fragment_mode)
    View fragment_mode;
    @BindView(R.id.fragment)
    FrameLayout fragment;
    @BindView(R.id.color_btn)
    Button colorBtn;
    @BindView(R.id.color_bar)
    JColorBar color_bar;
    @BindView(R.id.this_tag_number)
    TextView thisTagNumber;
    @BindView(R.id.all_tag_number)
    TextView allTagNumber;
    @BindView(R.id.mode_btn)
    StateButton modeBtn;
    @BindView(R.id.other_btn)
    StateButton otherBtn;
    @BindView(R.id.update_lin)
    LinearLayout updateLin;
    @BindView(R.id.shangxia_btn)
    StateButton shangxiaBtn;
    @BindView(R.id.hunse_btn)
    StateButton hunseBtn;
    @BindView(R.id.zuoyou_btn)
    StateButton zuoyouBtn;
    @BindView(R.id.gunding_btn)
    StateButton gundingBtn;
    @BindView(R.id.mode_lin)
    LinearLayout modeLin;
    @BindView(R.id.version_btn)
    StateButton versionBtn;
    @BindView(R.id.auto_btn)
    StateButton autoBtn;
    @BindView(R.id.other_lin)
    LinearLayout otherLin;
    private List<Fragment> fragments = new ArrayList<>();
    //private String[] titles = new String[]{"编辑", "文本预览", "图片预览"};
    private List<View> views;
    private byte[] Picture1_ByteT;
    private String text;
    private boolean isVisiImage = false;
    private boolean isVisiText = false;

    private ColorSelectDialog colorSelectDialog = null;
    private int lastColor;

    public OnLineFragment() {
        // Required empty public constructor
    }

    public static OnLineFragment newInstance() {
        OnLineFragment fragment = new OnLineFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_online;
    }

    @Override
    protected void initEventAndData() {
        prepareFragment();
        thisTagNumber.setText(String.valueOf(100));
        allTagNumber.setText(String.valueOf(50));


        color_bar.setColorsUdateListener(new JColorBar.ColorsUdateListener() {
            @Override
            public void update(int color) {
                setColor(color);
            }
        });
        text_btn.setOnClickListener(new IClickListener() {
            @Override
            protected void onIClick(View v) {
                //setTextT("平庸却说平凡可贵");
                fragment_mode.setVisibility(View.GONE);
                fragment.setVisibility(View.VISIBLE);
                getChildFragmentManager().beginTransaction().replace(R.id.fragment, fragments.get(0)).commit();
            }
        });
        imageBtn.setOnClickListener(new IClickListener() {
            @Override
            protected void onIClick(View v) {
                fragment_mode.setVisibility(View.GONE);
                fragment.setVisibility(View.VISIBLE);
                getChildFragmentManager().beginTransaction().replace(R.id.fragment, fragments.get(1)).commit();
            }
        });
        modeBtn.setOnClickListener(new IClickListener() {
            @Override
            protected void onIClick(View v) {
                modeLin.setVisibility(View.VISIBLE);
                otherLin.setVisibility(View.GONE);
                updateLin.setVisibility(View.GONE);
            }
        });
        otherBtn.setOnClickListener(new IClickListener() {
            @Override
            protected void onIClick(View v) {
                modeLin.setVisibility(View.GONE);
                otherLin.setVisibility(View.VISIBLE);
                updateLin.setVisibility(View.GONE);
            }
        });
        colorBtn.setOnClickListener(new IClickListener() {
            @Override
            protected void onIClick(View v) {
                if (colorSelectDialog == null) {
                    colorSelectDialog = new ColorSelectDialog(mContext);
                    colorSelectDialog.setOnColorSelectListener(new ColorSelectDialog.OnColorSelectListener() {
                        @Override
                        public void onSelectFinish(int color) {
                            setColor(color);
                            otherLin.setVisibility(View.GONE);
                            updateLin.setVisibility(View.VISIBLE);
                        }
                    });
                }
                colorSelectDialog.setLastColor(lastColor);
                colorSelectDialog.show();
            }
        });

        views = new ArrayList<>();
        SpinnerTopView topView=new SpinnerTopView(mContext);
        SpinnerTopView topView1=new SpinnerTopView(mContext);
        View.OnClickListener onClickListener=new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (modeLin.getVisibility()==View.VISIBLE)
                    modeLin.setVisibility(View.GONE);
                if (otherLin.getVisibility()==View.VISIBLE)
                    otherLin.setVisibility(View.GONE);
                if (updateLin.getVisibility()==View.GONE)
                    updateLin.setVisibility(View.VISIBLE);
                if (fragment_mode.getVisibility()==View.GONE)
                    fragment_mode.setVisibility(View.VISIBLE);
                if (fragment.getVisibility()==View.VISIBLE)
                    fragment.setVisibility(View.GONE);
//                if (fragment_mode.getVisibility()==View.GONE) {
//                    fragment_mode.setVisibility(View.VISIBLE);
//                    ValueAnimator fadeInIn = ValueAnimator.ofFloat(0f, 1f);
//                    fadeInIn.setDuration(500);
//                    fadeInIn.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//                        @Override
//                        public void onAnimationUpdate(ValueAnimator valueAnimator) {
//                            float a=(float) valueAnimator.getAnimatedValue();
//                            LogUtils.d("--"+a);
//                            fragment_mode.setAlpha(a);
//                        }
//                    });
//                    fadeInIn.start();
//                }
//                if (fragment.getVisibility()==View.VISIBLE) {
//                    ObjectAnimator fadeInOut = ObjectAnimator.ofFloat(fragment,  "alpha", 1f,0f);
//                    fadeInOut.setDuration(500);
//                    fadeInOut.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//                        @Override
//                        public void onAnimationUpdate(ValueAnimator valueAnimator) {
//                            float a=(float) valueAnimator.getAnimatedValue();
//                            LogUtils.d("--"+a);
//                            if (a==0.0){
//                                fragment.setVisibility(View.GONE);
//                            }
//                        }
//                    });
//                    fadeInOut.start();
//                }
            }
        };
        topView.setOnClickListener(onClickListener);
        topView1.setOnClickListener(onClickListener);
        views.add(topView.setAnim3());
        //views.add(new SpinnerTopView(mContext).setAnim1());
        views.add(topView1.setAnim2());
        VPagerAdapter vPagerAdapter = new VPagerAdapter(views);
        viewpager.setAdapter(vPagerAdapter);
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                setCleanAnim(true);
            }

            @Override
            public void onPageSelected(int position) {
                //setThisCleanAnim(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                //setCleanAnim(state!=0);
                setThisCleanAnim(state != 0);
            }
        });

        new CommandReceiver() {
            @Override
            public void onDataReceived(byte[] buffer, byte function, byte safeCod) {
                if (function==0x01){//电量
                    if (thisTagNumber!=null)
                        thisTagNumber.setText(String.valueOf(buffer[0]));
                }
                if (function==0x02){//速度
                    if (allTagNumber!=null)
                        allTagNumber.setText(String.valueOf(buffer[0]));
                }
            }
            @Override
            public void onFail() {
            }
            @Override
            public void onLost() {
            }

            @Override
            public void onDeviceInfo(String name, String address) {
            }
            @Override
            public void onStadeTag(int stade) {
                LogUtils.e("stade---"+stade);
                switch (stade){
                    case 0:
                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                }
            }
        }.regiest();

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putByteArray("data", Picture1_ByteT);
        outState.putString("text", text);
        outState.putBoolean("isVisiImage", isVisiImage);
        outState.putBoolean("isVisiText", isVisiText);
        outState.putInt("lastColor", lastColor);
        outState.putFloat("x", color_bar.getX());
    }

    @Override
    public void onViewStateRestored(@Nullable final Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState == null)
            return;
        Picture1_ByteT = savedInstanceState.getByteArray("data");
        text = savedInstanceState.getString("text");
        isVisiImage = savedInstanceState.getBoolean("isVisiImage");
        isVisiText = savedInstanceState.getBoolean("isVisiText");
        lastColor = savedInstanceState.getInt("lastColor");
        color_bar.post(new Runnable() {
            @Override
            public void run() {
                color_bar.setX(savedInstanceState.getFloat("x"));
            }
        });

        setColor(lastColor, true);
        if (isVisiText)
            setTextT(text);
        else if (isVisiImage)
            setPicture1_ByteT(Picture1_ByteT);//防止
    }

    public void setPicture1_ByteT(byte[] picture1_ByteT) {
        setPicture1_ByteT(picture1_ByteT, 2);
    }

    public void setPicture1_ByteT(byte[] picture1_ByteT, int baifenbi) {
        if (baifenbi!=1) {//图片
            byte[] data = Font16.byteMerger(new byte[]{(byte) 0x01, (byte) 0x00, (byte) 0x00}, picture1_ByteT);
            BluetoothService.getInstance().sendData((byte) 0x03, data, false);
        }
        Picture1_ByteT = picture1_ByteT;
        isVisiImage = true;
        isVisiText = false;
        for (View v : views) {
            SpinnerTopView spinnerTopView = (SpinnerTopView) v;
            spinnerTopView.setPrerent(baifenbi);
            spinnerTopView.setPicture1_ByteT(Picture1_ByteT);
        }
    }


    private void setColor(int color) {
        setColor(color, true);
    }

    private void setColor(int color, boolean relayout) {
        BluetoothService.getInstance().sendData((byte) 0x10, new byte[]{(byte) Color.red(color),(byte) Color.green(color),(byte) Color.blue(color)}, false);
        lastColor = color;
        for (View v : views) {
            SpinnerTopView spinnerTopView = (SpinnerTopView) v;
            spinnerTopView.setLastColor(lastColor, relayout);
        }
    }

    public void setTextT(String text) {
        setTextT(text, 0);
    }

    public void setTextT(String text, int fontStyle) {
        if (TextUtils.isEmpty(text))
            return;
        this.text = text;
        isVisiText = true;
        isVisiImage = false;
        isStopAuto =true;
        try {
            byte[] data1 = Font16.byteMerger(new byte[]{(byte) 0x01, (byte) 0x00, (byte) 0x00}, text.getBytes("GB2312"));
            byte[] data12 = new Font16(mContext).getStringFontByte(text, fontStyle);
            BluetoothService.getInstance().sendData((byte) 0x03, data1, false);
            BluetoothService.getInstance().sendData((byte) 0x09, data12, false);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        byte[] fff = new Font16(mContext).getStringAndFont(text, fontStyle);
        if (fff.length == 0)
            return;
        Picture1_ByteT = new byte[240];
        for (int i = 0; i < fff.length; i++) {
            if (i == 240) {
                break;
            }
            Picture1_ByteT[i] = fff[i];
        }
        setPicture1_ByteT(Picture1_ByteT, 1);
        /*for (View v : views) {
            SpinnerTopView spinnerTopView = (SpinnerTopView) v;
            spinnerTopView.setText(text);
        }*/
    }

    public void changeToOne(String text, int fontStyle) {
        fragment.setVisibility(View.GONE);
        fragment_mode.setVisibility(View.VISIBLE);
        if (!TextUtils.isEmpty(text))
            setTextT(text, fontStyle);
    }

    public void changeToOne(byte[] Picture1_ByteT) {
        fragment.setVisibility(View.GONE);
        fragment_mode.setVisibility(View.VISIBLE);
        if (Picture1_ByteT != null)
            setPicture1_ByteT(Picture1_ByteT);
    }
    public void setThisCleanAnim(boolean isCleanAnim) {
        setThisCleanAnim(isCleanAnim ? -1 : viewpager.getCurrentItem());
    }

    public void setThisCleanAnim(int position) {
        for (int i = 0; i < views.size(); i++) {
            SpinnerTopView spinnerTopView = (SpinnerTopView) views.get(i);
            if (i != position) {
                spinnerTopView.cleanAnim();
            } else {
                spinnerTopView.starZeroAnim();
            }
        }
    }


    public void setCleanAnim(boolean cleanAnim) {
        for (View v : views) {
            SpinnerTopView spinnerTopView = (SpinnerTopView) v;
            if (cleanAnim) {
                spinnerTopView.cleanAnim();
            } else {
                spinnerTopView.starZeroAnim();
            }
        }
    }

    private void prepareFragment() {
        //fragments.add(new OnLineModeFragment());
        fragments.add(new OnLineTextFragment());
        fragments.add(new OnLineImageFragment());
//        for (Fragment fragment : fragments) {
//            getChildFragmentManager().beginTransaction().add(R.id.fragment, fragment).hide(fragment).commit();
//        }
    }

    private void updateFragment(int position) {
        if (position > fragments.size() - 1) {
            return;
        }
        for (int i = 0; i < fragments.size(); i++) {
            FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
            Fragment fragment = fragments.get(i);
            if (i == position) {
                transaction.show(fragment);
            } else {
                transaction.hide(fragment);
            }
            transaction.commit();
        }
    }


    boolean isStopAuto=true;//是否停止自动
    /**
     * 自动轮询
     */
    private void autoPicture(final String[] fileArr, final int curNumber) {
        if (isStopAuto)
            return;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (isStopAuto)
                    return;
                LogUtils.e("auto--pic" + curNumber);
                final byte[] aa = MainFragment.readFile(mContext, fileArr[curNumber]);
                autoBtn.post(new Runnable() {
                    @Override
                    public void run() {
                        setPicture1_ByteT(aa);
                        if (curNumber+1==fileArr.length) {
                            isStopAuto=!isStopAuto;
                            autoBtn.setNormalBackgroundColor(isStopAuto?getResources().getColor(R.color.colormain6):Color.RED);
                        }
                    }
                });
                if (curNumber+1==fileArr.length) {
                    return;
                }
                autoPicture(fileArr,curNumber+1);
            }
        }).start();
    }

    @OnClick({R.id.shangxia_btn, R.id.zuoyou_btn, R.id.gunding_btn, R.id.version_btn, R.id.auto_btn,R.id.hunse_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.zuoyou_btn:
                BluetoothService.getInstance().sendData((byte) 0x11, new byte[]{(byte)0x01}, false);
                modeLin.setVisibility(View.GONE);
                updateLin.setVisibility(View.VISIBLE);
                break;
            case R.id.shangxia_btn:
                BluetoothService.getInstance().sendData((byte) 0x11, new byte[]{(byte)0x02}, false);
                modeLin.setVisibility(View.GONE);
                updateLin.setVisibility(View.VISIBLE);
                //otherLin.setVisibility(View.GONE);
                break;
            case R.id.gunding_btn:
                BluetoothService.getInstance().sendData((byte) 0x11, new byte[]{(byte)0x03}, false);
                modeLin.setVisibility(View.GONE);
                updateLin.setVisibility(View.VISIBLE);
                break;
            case R.id.hunse_btn:
                BluetoothService.getInstance().sendData((byte) 0x11, new byte[]{(byte)0x04}, false);
                modeLin.setVisibility(View.GONE);
                updateLin.setVisibility(View.VISIBLE);
                break;
            case R.id.version_btn:
                byte[] ta=ScanHelper.sendDataSSS((byte)0x06, new byte[]{(byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x03}, false);
                BluetoothService.getInstance().sendData(ta);
                otherLin.setVisibility(View.GONE);
                updateLin.setVisibility(View.VISIBLE);
                break;
            case R.id.auto_btn:
                isStopAuto=!isStopAuto;
                autoBtn.setNormalBackgroundColor(isStopAuto?getResources().getColor(R.color.colormain6):Color.RED);
                String itemPath = MainFragment.getInnerSDCardPath() + "/HWX-SPINNER/";
                String[] fileArr = MainFragment.getFileAll(mContext, new File(itemPath), false, false);
                if (fileArr==null)
                    isStopAuto=!isStopAuto;
                autoPicture(fileArr,0);
                break;
        }
    }
}
