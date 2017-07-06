package com.hwx.usbconnect.usbconncet.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.hwx.usbconnect.usbconncet.R;
import com.hwx.usbconnect.usbconncet.ui.fragment.UseFragment;
import com.jaeger.library.StatusBarUtil;

public class UseInfoActivity extends SimpleActivity {

    @Override
    protected int getLayout() {
        return R.layout.activity_use_info;
    }

    @Override
    protected void initEventAndData() {
        setToolBar((Toolbar)findViewById(R.id.toolbar),getString(R.string.ddavv));
        setStatusBar(0);
        getSupportFragmentManager().beginTransaction().replace(R.id.container, UseFragment.newInstance(getString(R.string.ddavv), getString(R.string.vavdgsdgsa))).commit();
    }
}
