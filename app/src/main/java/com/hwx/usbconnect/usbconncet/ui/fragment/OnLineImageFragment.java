package com.hwx.usbconnect.usbconncet.ui.fragment;


import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.hwx.usbconnect.usbconncet.R;
import com.hwx.usbconnect.usbconncet.ui.activity.SimpleFragment;
import com.hwx.usbconnect.usbconncet.utils.IClickListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class OnLineImageFragment extends SimpleFragment {


    @BindView(R.id.toedit_t)
    TextView toeditT;
    @BindView(R.id.rv_list)
    RecyclerView rvList;
    @BindView(R.id.send)
    Button send;

    public OnLineImageFragment() {
        // Required empty public constructor
    }

    public static OnLineImageFragment newInstance() {
        OnLineImageFragment fragment = new OnLineImageFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_online_mode_image;
    }

    @Override
    protected void initEventAndData() {
        initView();
    }

    private void initView() {
        int a = (int) (Math.random() * 5);
        int[] ass = new int[]{R.color.colormain1, R.color.colormain2, R.color.colormain3, R.color.colormain4, R.color.colormain5};
        //Glide.with(getActivity()).load(new ColorDrawable()).into(imageView);
        send.setOnClickListener(new IClickListener() {
            @Override
            protected void onIClick(View v) {
                OnLineFragment fragment= (OnLineFragment) getParentFragment();
                fragment.changeToOne();
            }
        });
    }

}
