package com.kdp.tabviewpagerindicator;

import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<String> titles = new ArrayList<>();
    private TabPagerIndicatorLayout mPagerIndicatorLayout;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addTitles();
        FragmentManager mFragmentManager = getSupportFragmentManager();

        MyPagerAdapter adapter = new MyPagerAdapter(mFragmentManager, titles);
        mPagerIndicatorLayout = (TabPagerIndicatorLayout) findViewById(R.id.tabIndicatorLayout);
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        mViewPager.setAdapter(adapter);
        mPagerIndicatorLayout.setTabTitles(titles);
        mPagerIndicatorLayout.setViewPager(mViewPager);
    }


    private void addTitles() {
        titles.add("推荐");
        titles.add("热点资讯");
        titles.add("北京");
        titles.add("阳光宽频");
        titles.add("社会");
        titles.add("头条号");
        titles.add("问答");
        titles.add("娱乐");
        titles.add("图片");
        titles.add("汽车");
    }
}
