package com.kdp.tabviewpagerindicator;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by kangdongpu on 2017/2/17.
 */
public class MyPagerAdapter extends FragmentPagerAdapter {

    private List<String> mTitles;

    public MyPagerAdapter(FragmentManager fm, List<String> mTitles) {
        super(fm);
        this.mTitles = mTitles;
    }

    @Override
    public Fragment getItem(int position) {
        return TestFragment.newInstance(mTitles.get(position));
    }

    @Override
    public int getCount() {
        return mTitles.size();
    }
}
