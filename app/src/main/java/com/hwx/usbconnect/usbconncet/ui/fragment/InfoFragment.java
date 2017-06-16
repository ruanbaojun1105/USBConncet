package com.hwx.usbconnect.usbconncet.ui.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

import com.hwx.usbconnect.usbconncet.R;
import com.hwx.usbconnect.usbconncet.ui.activity.SimpleFragment;

import butterknife.BindView;

public class InfoFragment extends SimpleFragment {

    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.this_info)
    TextView this_info;

    public InfoFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static InfoFragment newInstance() {
        InfoFragment fragment = new InfoFragment();
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_info;
    }

    @Override
    protected void initEventAndData() {
        this_info.setText(getString(R.string.dvaf));
        this_info.append(getString(R.string.about_me));
        this_info.setMovementMethod(LinkMovementMethod.getInstance());
    }

    public void setThis_info(String this_info) {
        if (TextUtils.isEmpty(this_info))
            return;
        this.this_info.setText(this_info);
    }
}
