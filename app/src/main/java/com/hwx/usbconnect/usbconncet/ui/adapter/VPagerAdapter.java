package com.hwx.usbconnect.usbconncet.ui.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.hwx.usbconnect.usbconncet.utils.SpinnerTopView;

/**
 * Created by donglua on 15/6/21.
 */
public class VPagerAdapter extends PagerAdapter {

  private int count;
  public VPagerAdapter(int count) {
    this.count = count;
  }

  @Override
  public Object instantiateItem(ViewGroup container, final int position) {
    final Context context = container.getContext();
    SpinnerTopView spinnerTopView=new SpinnerTopView(context);
    container.addView(spinnerTopView);//切记加此句
    return spinnerTopView;
  }

  @Override
  public int getCount() {
    return count;
  }

  @Override
  public boolean isViewFromObject(View view, Object object) {
    return view == object;
  }

  @Override
  public void destroyItem(ViewGroup container, int position, Object object) {
    container.removeView((View) object);
  }

}
