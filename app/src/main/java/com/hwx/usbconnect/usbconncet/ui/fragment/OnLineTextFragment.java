package com.hwx.usbconnect.usbconncet.ui.fragment;


import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;

import com.hwx.usbconnect.usbconncet.R;
import com.hwx.usbconnect.usbconncet.ui.activity.SimpleFragment;
import com.hwx.usbconnect.usbconncet.utils.HorizontalPicker;
import com.hwx.usbconnect.usbconncet.utils.IClickListener;
import com.xw.repo.XEditText;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class OnLineTextFragment extends SimpleFragment {

    @BindView(R.id.biucontainer)
    XEditText biucontainer;
    @BindView(R.id.send)
    Button send;
    @BindView(R.id.hpicker_font)
    HorizontalPicker hpickerFont;

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
        biucontainer.setMaxLines(2);
//        biucontainer.setFilters(new InputFilter[]{new InputFilter.LengthFilter(8)});
        send.setOnClickListener(new IClickListener() {
            @Override
            protected void onIClick(View v) {
                OnLineFragment fragment = (OnLineFragment) getParentFragment();
                fragment.changeToOne(biucontainer.getText().toString(),hpickerFont.getSelectedIndex());
            }
        });
        List<HorizontalPicker.PickerItem> textItems = new ArrayList<>();
        textItems.add(new HorizontalPicker.TextItem("宋体"));
        textItems.add(new HorizontalPicker.TextItem("粗体"));
        hpickerFont.setItems(textItems,0);
    }

}
