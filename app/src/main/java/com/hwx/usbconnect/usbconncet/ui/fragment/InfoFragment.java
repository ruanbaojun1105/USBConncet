package com.hwx.usbconnect.usbconncet.ui.fragment;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hwx.usbconnect.usbconncet.R;

public class InfoFragment extends Fragment {

    TextView this_info;

    public InfoFragment() {
        // Required empty public constructor
    }
    // TODO: Rename and change types and number of parameters
    public static InfoFragment newInstance() {
        InfoFragment fragment = new InfoFragment();
        return fragment;
    }

    public void setThis_info(String str) {
        if (TextUtils.isEmpty(str))
            return;
        this_info.post(new Runnable() {
            @Override
            public void run() {
                this_info.setText(str);
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView=inflater.inflate(R.layout.fragment_info, container, false);
        this_info= (TextView) rootView.findViewById(R.id.this_info);
        return rootView;
    }

}
