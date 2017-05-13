package com.hwx.usbconnect.usbconncet.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hwx.usbconnect.usbconncet.R;
import com.hwx.usbconnect.usbconncet.bluetooth.BluetoothService;
import com.hwx.usbconnect.usbconncet.font.FontUtilsTest;

/**
 * Created by Administrator on 2017/1/9.
 */

public class ControlFragment extends Fragment {

    private View rootView;
    private FontUtilsTest fontUtilsTest;

    public static ControlFragment newInstance() {
        ControlFragment fragment = new ControlFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_main, container, false);
        fontUtilsTest=new FontUtilsTest(getContext());
        final EditText editText= (EditText) rootView.findViewById(R.id.editText);
        Button button= (Button) rootView.findViewById(R.id.set_btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ttt= editText.getText().toString().trim();
                if (TextUtils.isEmpty(ttt))
                    return;
                BluetoothService.getInstance().sendData((byte) 0x01, ttt.getBytes(), false);
                BluetoothService.getInstance().sendData((byte) 0x02, fontUtilsTest.getWordsGen(ttt), false);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(),"ok",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        return rootView;
    }
}
