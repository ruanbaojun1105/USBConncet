package com.hwx.usbconnect.usbconncet.ui.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hwx.usbconnect.usbconncet.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UseFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private View rootView;
    private TextView name;
    private TextView this_info;


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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_info, container, false);
        initView(rootView);
        return rootView;
    }

    private void initView(View rootView) {
        name = (TextView) rootView.findViewById(R.id.name);
        this_info = (TextView) rootView.findViewById(R.id.this_info);
        name.setText(mParam1);
        name.setTextColor(getResources().getColor(R.color.colorPrimary));
        this_info.setText(mParam2);
    }
}
