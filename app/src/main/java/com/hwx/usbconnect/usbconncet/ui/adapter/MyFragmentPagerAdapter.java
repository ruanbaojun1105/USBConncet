package com.hwx.usbconnect.usbconncet.ui.adapter;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class MyFragmentPagerAdapter extends FragmentStatePagerAdapter{

	private String[] mTitles;
	private List<Fragment> mFragments;

	public MyFragmentPagerAdapter(FragmentManager fm, String[] mTitles, List<Fragment> mFragments) {
		super(fm);
		this.mTitles = mTitles;
		this.mFragments = mFragments;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return mTitles[position];
	}

	@Override
	public Fragment getItem(int position) {
		return mFragments.get(position);
	}

	@Override
	public int getCount() {
		return mFragments.size();
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		super.destroyItem(container, position, object);
	}

}
