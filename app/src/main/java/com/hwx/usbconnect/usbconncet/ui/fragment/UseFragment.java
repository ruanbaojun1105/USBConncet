package com.hwx.usbconnect.usbconncet.ui.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.TextView;

import com.hwx.usbconnect.usbconncet.R;
import com.hwx.usbconnect.usbconncet.ui.activity.SimpleFragment;
import com.joanzapata.iconify.widget.IconTextView;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UseFragment extends SimpleFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.this_info)
    IconTextView this_info;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public UseFragment() {
        // Required empty public constructor
    }

    public static UseFragment newInstance(String param1, String param2) {
        UseFragment fragment = new UseFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_use;
    }

    @Override
    protected void initEventAndData() {
        initView();
    }

    private void initView() {
        name.setText(mParam1);
        name.setTextColor(getResources().getColor(R.color.colorPrimary));
        this_info.setText(mParam2);
    }
}
