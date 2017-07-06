package com.hwx.usbconnect.usbconncet.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.andview.refreshview.XRefreshView;
import com.andview.refreshview.callback.IFooterCallBack;

public class CustomFooterView extends View implements IFooterCallBack {

    public CustomFooterView(Context context) {
        super(context);
        initView(context);
    }

    public CustomFooterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    @Override
    public void callWhenNotAutoLoadMore(final XRefreshView xRefreshView) {
    }

    @Override
    public void onStateReady() {
    }


    @Override
    public void onStateRefreshing() {
    }

    @Override
    public void onReleaseToLoadMore() {
    }

    @Override
    public void onStateFinish(boolean hideFooter) {
    }

    @Override
    public void onStateComplete() {
    }

    @Override
    public void show(final boolean show) {

    }

    @Override
    public boolean isShowing() {
        return false;
    }

    private void initView(Context context) {
    }

    @Override
    public int getFooterHeight() {
        return getMeasuredHeight();
    }
}
