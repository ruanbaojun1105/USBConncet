package com.hwx.usbconnect.usbconncet.ui.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.hwx.usbconnect.usbconncet.utils.IClickListener;
import com.hwx.usbconnect.usbconncet.utils.SpinnerTopView;

import java.util.List;

/**
 * Created by donglua on 15/6/21.
 */
public class VPagerAdapter extends PagerAdapter {

  private List<View> viewList;
  public VPagerAdapter( List<View> viewList) {
    this.viewList = viewList;
  }

  @Override
  public Object instantiateItem(ViewGroup container, final int position) {
    container.addView(viewList.get(position));//切记加此句
    return viewList.get(position);
  }


  @Override
  public int getCount() {
    return viewList.size();
  }

  @Override
  public boolean isViewFromObject(View view, Object object) {
    return view == object;
  }

  @Override
  public void destroyItem(ViewGroup container, int position, Object object) {
    container.removeView(viewList.get(position));
  }

}
