package com.hwx.usbconnect.usbconncet.ui.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.hwx.usbconnect.usbconncet.R;
import com.hwx.usbconnect.usbconncet.ui.activity.SimpleFragment;
import com.hwx.usbconnect.usbconncet.utils.IClickListener;
import com.hwx.usbconnect.usbconncet.utils.StateButton;
import com.xw.repo.XEditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class OnLineTextFragment extends SimpleFragment {

    @BindView(R.id.biucontainer)
    XEditText biucontainer;
    @BindView(R.id.font_default)
    StateButton fontDefault;
    @BindView(R.id.font_bold)
    StateButton fontBold;
    @BindView(R.id.color_select)
    View colorSelect;
    @BindView(R.id.send)
    Button send;

    public OnLineTextFragment() {
        // Required empty public constructor
    }

    public static OnLineTextFragment newInstance() {
        OnLineTextFragment fragment = new OnLineTextFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_online_mode_text;
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
