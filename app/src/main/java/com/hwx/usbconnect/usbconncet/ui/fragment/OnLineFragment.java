package com.hwx.usbconnect.usbconncet.ui.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import com.hwx.usbconnect.usbconncet.R;
import com.hwx.usbconnect.usbconncet.ui.activity.SimpleFragment;
import com.hwx.usbconnect.usbconncet.ui.adapter.VPagerAdapter;
import com.hwx.usbconnect.usbconncet.utils.IClickListener;
import com.hwx.usbconnect.usbconncet.utils.LogUtils;
import com.hwx.usbconnect.usbconncet.utils.SpinnerTopView;
import com.piotrek.customspinner.CustomSpinner;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import pro.alexzaitsev.freepager.library.view.core.VerticalPager;


public class OnLineFragment extends SimpleFragment {

//    @BindView(R.id.vertical_pager)
//    VerticalPager verticalPager;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.color_btn)
    Button colorBtn;
    @BindView(R.id.mode_spinner)
    CustomSpinner modeSpinner;
    @BindView(R.id.image_btn)
    Button imageBtn;
    @BindView(R.id.fragment_mode)
    View fragment_mode;
    @BindView(R.id.fragment)
    FrameLayout fragment;
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
        changePage(0);
        colorBtn.setOnClickListener(new IClickListener() {
            @Override
            protected void onIClick(View v) {
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
        modeSpinner.initializeStringValues(new String[]{
                        mContext.getString(R.string.vvag),
                        mContext.getString(R.string.vaga),
                        mContext.getString(R.string.vaggagkjoo)},
                mContext.getString(R.string.ajkjkk));

        try {
            InputStream in = getResources().getAssets().open("five.bin");
            in.read(fileData, 0, 240);
            in.close();
        } catch (Exception ex) {
        }
//        topPanel1.setPicture1_ByteT(data);
//        topPanel2.setPicture1_ByteT(data);
//        topPanel3.setPicture1_ByteT(data);
        VPagerAdapter vPagerAdapter=new VPagerAdapter(3);
        viewpager.setAdapter(vPagerAdapter);

        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
                SpinnerTopView spinnerTopView= (SpinnerTopView) viewpager.getChildAt(position);
                spinnerTopView.setPicture1_ByteT(fileData);
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    public void changeToOne() {
        fragment.setVisibility(View.GONE);
        fragment_mode.setVisibility(View.VISIBLE);
    }

    public void changePage(int page) {
        //currentPage = page;
        //getChildFragmentManager().beginTransaction().replace(R.id.fragment, fragments.get(page));
        //updateFragment(page);
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
