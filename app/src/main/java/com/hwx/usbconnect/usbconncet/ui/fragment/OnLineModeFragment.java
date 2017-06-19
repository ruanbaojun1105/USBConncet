package com.hwx.usbconnect.usbconncet.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.hwx.usbconnect.usbconncet.R;
import com.hwx.usbconnect.usbconncet.ui.activity.SimpleFragment;
import com.hwx.usbconnect.usbconncet.utils.IClickListener;
import com.hwx.usbconnect.usbconncet.utils.LogUtils;
import com.piotrek.customspinner.CustomSpinner;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class OnLineModeFragment extends SimpleFragment {


    @BindView(R.id.color_btn)
    Button colorBtn;
    @BindView(R.id.mode_spinner)
    CustomSpinner modeSpinner;
    @BindView(R.id.image_btn)
    Button imageBtn;

    public OnLineModeFragment() {
        // Required empty public constructor
    }

    public static OnLineModeFragment newInstance() {
        OnLineModeFragment fragment = new OnLineModeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_online_modechange;
    }

    @Override
    protected void initEventAndData() {
        initView();
    }

    private void initView() {
        int a = (int) (Math.random() * 5);
        int[] ass = new int[]{R.color.colormain1, R.color.colormain2, R.color.colormain3, R.color.colormain4, R.color.colormain5};
        colorBtn.setOnClickListener(new IClickListener() {
            @Override
            protected void onIClick(View v) {
                OnLineFragment fragment= (OnLineFragment) getParentFragment();
                fragment.changePage(1);
            }
        });
        imageBtn.setOnClickListener(new IClickListener() {
            @Override
            protected void onIClick(View v) {
                OnLineFragment fragment= (OnLineFragment) getParentFragment();
                fragment.changePage(2);
            }
        });
        modeSpinner.initializeStringValues(new String[]{
                        mContext.getString(R.string.vvag),
                        mContext.getString(R.string.vaga),
                        mContext.getString(R.string.vaggagkjoo)},
                mContext.getString(R.string.ajkjkk));
    }

}
