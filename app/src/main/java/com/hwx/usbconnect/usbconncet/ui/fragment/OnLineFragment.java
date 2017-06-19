package com.hwx.usbconnect.usbconncet.ui.fragment;


import android.os.Bundle;
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

import com.hwx.usbconnect.usbconncet.R;
import com.hwx.usbconnect.usbconncet.ui.activity.SimpleFragment;
import com.hwx.usbconnect.usbconncet.ui.adapter.VPagerAdapter;
import com.hwx.usbconnect.usbconncet.utils.IClickListener;
import com.hwx.usbconnect.usbconncet.utils.SpinnerTopView;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import zhou.colorpalette.ColorSelectDialog;


public class OnLineFragment extends SimpleFragment {

    //    @BindView(R.id.vertical_pager)
//    VerticalPager verticalPager;
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
    @BindView(R.id.shangxia_btn)
    Button shangxiaBtn;
    @BindView(R.id.zuoyou_btn)
    Button zuoyouBtn;
    @BindView(R.id.guding_btn)
    Button gudingBtn;
    @BindView(R.id.color_btn)
    Button colorBtn;
    //    @BindView(R.id.topPanel1)
//    SpinnerTopView topPanel1;
//    @BindView(R.id.topPanel2)
//    SpinnerTopView topPanel2;
//    @BindView(R.id.topPanel3)
//    SpinnerTopView topPanel3;
    private byte[] fileData = new byte[240];
    private List<Fragment> fragments = new ArrayList<>();
    private int currentPage = 0;
    private String[] titles = new String[]{"编辑", "文本预览", "图片预览"};
    private List<View> views;
    private byte[] Picture1_ByteT;
    private String text;
    private boolean isVisiImage = false;
    private boolean isVisiText = false;

    private ColorSelectDialog colorSelectDialog;
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
        text_btn.setOnClickListener(new IClickListener() {
            @Override
            protected void onIClick(View v) {
                setTextT("平庸却说平凡可贵");
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

        colorBtn.setOnClickListener(new IClickListener() {
            @Override
            protected void onIClick(View v) {
                if (colorSelectDialog == null) {
                    colorSelectDialog = new ColorSelectDialog(mContext);
                    colorSelectDialog.setOnColorSelectListener(new ColorSelectDialog.OnColorSelectListener() {
                        @Override
                        public void onSelectFinish(int color) {
                            setColor(color);
                        }
                    });
                }
                colorSelectDialog.setLastColor(lastColor);
                colorSelectDialog.show();
            }
        });

        try {
            InputStream in = getResources().getAssets().open("five.bin");
            in.read(fileData, 0, 240);
            in.close();
        } catch (Exception ex) {
        }
//        topPanel1.setPicture1_ByteT(data);
//        topPanel2.setPicture1_ByteT(data);
//        topPanel3.setPicture1_ByteT(data);
        views = new ArrayList<>();
        views.add(new SpinnerTopView(mContext));
        views.add(new SpinnerTopView(mContext));
        views.add(new SpinnerTopView(mContext));
        VPagerAdapter vPagerAdapter = new VPagerAdapter(views);
        viewpager.setAdapter(vPagerAdapter);
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                setThisCleanAnim(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putByteArray("data", Picture1_ByteT);
        outState.putString("text", text);
        outState.putBoolean("isVisiImage", isVisiImage);
        outState.putBoolean("isVisiText", isVisiText);
        outState.putInt("lastColor", lastColor);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState == null)
            return;
        Picture1_ByteT = savedInstanceState.getByteArray("data");
        text = savedInstanceState.getString("text");
        isVisiImage = savedInstanceState.getBoolean("isVisiImage");
        isVisiText = savedInstanceState.getBoolean("isVisiText");
        lastColor=savedInstanceState.getInt("lastColor");
        if (isVisiText)
            setTextT(text);
        else if (isVisiImage)
            setPicture1_ByteT(Picture1_ByteT);
    }

    public void setPicture1_ByteT(byte[] picture1_ByteT) {
        Picture1_ByteT = picture1_ByteT;
        isVisiImage = true;
        isVisiText = false;
        for (View v : views) {
            SpinnerTopView spinnerTopView = (SpinnerTopView) v;
            spinnerTopView.setPicture1_ByteT(Picture1_ByteT);
        }
    }

    private void setColor(int color) {
        lastColor=color;
        for (View v : views) {
            SpinnerTopView spinnerTopView = (SpinnerTopView) v;
            spinnerTopView.setLastColor(lastColor);
        }
    }

    public void setTextT(String text) {
        this.text = text;
        isVisiText = true;
        isVisiImage = false;
        for (View v : views) {
            SpinnerTopView spinnerTopView = (SpinnerTopView) v;
            spinnerTopView.setText(text);
        }
    }

    public void changeToOne(String text) {
        fragment.setVisibility(View.GONE);
        fragment_mode.setVisibility(View.VISIBLE);
        if (!TextUtils.isEmpty(text))
            setTextT(text);
    }

    public void changeToOne(byte[] Picture1_ByteT) {
        fragment.setVisibility(View.GONE);
        fragment_mode.setVisibility(View.VISIBLE);
        if (Picture1_ByteT != null)
            setPicture1_ByteT(Picture1_ByteT);
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

}
