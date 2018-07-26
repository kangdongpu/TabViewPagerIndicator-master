package com.kdp.tabviewpagerindicator;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.ScaleAnimation;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by kangdongpu on 2018/7/24.
 */

public class TabPagerIndicatorLayout extends HorizontalScrollView {
    private static final String TAG = TabPagerIndicatorLayout.class.getSimpleName();
    //未选中的TabTitle字体大小
    private float mTabTitleSize;
    //选中的TabTitle字体大小
    private float mTabTitleSelectedSize;
    //未选中的TabTitle字体颜色
    private int mTabTitleColor;
    //选中的TabTitle字体颜色
    private int mTabTitleSelectedColor;
    //相邻TabTilte之间的间距大小
    private int mTabSpace;
    //下划线指示器的高度
    private float mIndicatorHeight;
    //下划线指示器的颜色
    private int mIndicatorColor;
    //默认TabTitle字体大小
    private final float mDefaultTabTitleSize = 14;
    //默认选中的TabTitle字体大小
    private final float mDefaultSelectedTabTitleSize = 16;
    //默认未选中TabTitle的字体颜色
    private final int mDefaultTabTitleColor = 0xFF000000;
    //默认选中TabTitle的字体颜色
    private final int mDefaultSelectedTabtitleColor = 0xFFFF0000;
    //默认TabTitle之间的间距
    private final int mDefaultTabSpace = (int) dp2px(10);
    //默认指示器的高度
    private final float mDefaultIndicatorHeight = dp2px(2);
    //默认指示器的颜色
    private final int mDefaultIndicatorColor = 0xFFFF0000;

    private LinearLayout mTabContainer;

    private Paint mPaint;

    private float mIndicatorStartX, mIndicatorStartY, mIndicatorEndX, mIndicatorEndY;

    //当前选中的Tab位置
    private int mCurrentPosition;
    private ViewPager mViewPager;

    public TabPagerIndicatorLayout(Context context) {
        this(context, null);
    }

    public TabPagerIndicatorLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TabPagerIndicatorLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.TabPagerIndicatorLayout, defStyleAttr, 0);
        //获取自定义属性
        mTabTitleSize = mTypedArray.getDimension(R.styleable.TabPagerIndicatorLayout_mTabTitleSize, mDefaultTabTitleSize);
        mTabTitleSelectedSize = mTypedArray.getDimension(R.styleable.TabPagerIndicatorLayout_mTabTitleSelectedSize, mDefaultSelectedTabTitleSize);
        mTabTitleColor = mTypedArray.getColor(R.styleable.TabPagerIndicatorLayout_mTabTitleColor, mDefaultTabTitleColor);
        mTabTitleSelectedColor = mTypedArray.getColor(R.styleable.TabPagerIndicatorLayout_mTabTitleSelectedColor, mDefaultSelectedTabtitleColor);
        mTabSpace = (int) mTypedArray.getDimension(R.styleable.TabPagerIndicatorLayout_mTabSpace, mDefaultTabSpace);
        mIndicatorHeight = mTypedArray.getDimension(R.styleable.TabPagerIndicatorLayout_mIndicatorHeight, mDefaultIndicatorHeight);
        mIndicatorColor = mTypedArray.getColor(R.styleable.TabPagerIndicatorLayout_mIndicatorColor, mDefaultIndicatorColor);
        mTypedArray.recycle();
        //初始化TabContainer
        initTabContainer();
        //初始化画笔
        initPaint();
    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStrokeWidth(mIndicatorHeight);
        mPaint.setColor(mIndicatorColor);
    }

    private void initTabContainer() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mTabContainer = new LinearLayout(getContext());
        mTabContainer.setOrientation(LinearLayout.HORIZONTAL);
        mTabContainer.setGravity(Gravity.CENTER_VERTICAL);
        mTabContainer.setLayoutParams(params);
        this.addView(mTabContainer);
    }


    //设置Titles
    public void setTabTitles(List<String> mTitles) {
        if (mTabContainer == null)
            throw new NullPointerException("You need init mTabContainer ");
        if (mTitles == null || mTitles.size() == 0) return;
        mTabContainer.removeAllViews();
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        for (int i = 0; i < mTitles.size(); i++) {
            TextView textView = new TextView(getContext());
            textView.setText(mTitles.get(i));
            textView.setTextSize(px2sp(mTabTitleSize));
            textView.setTextColor(mTabTitleColor);
            textView.setGravity(Gravity.CENTER);
            textView.setSingleLine();
            textView.setWidth((int) (measureTabWidth(mTitles.get(i)) + mTabSpace));
            textView.setHeight((int) (measureTabHeight() + mTabSpace));
            setTabOnClickListener(i, textView);
            mTabContainer.addView(textView, lp);
        }
        //默认选中第一个
        updateSelectTabTitle(mCurrentPosition);
    }

    /**
     * 测量文本的宽
     *
     * @param title
     * @return
     */
    private float measureTabWidth(String title) {
        mPaint.setTextSize(Math.max(mTabTitleSize, mTabTitleSelectedSize));
        return mPaint.measureText(title);
    }

    /**
     * 测量文本的高
     *
     * @return
     */
    private float measureTabHeight() {
        mPaint.setTextSize(Math.max(mTabTitleSize, mTabTitleSelectedSize));
        Paint.FontMetrics metrics = mPaint.getFontMetrics();
        return metrics.bottom - metrics.top;
    }

    private void setTabOnClickListener(final int position, final TextView textView) {
        textView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position != mCurrentPosition) {
                    switchTab(position);
                }
            }
        });
    }

    /**
     * 切换Tab
     *
     * @param position
     */
    private void switchTab(int position) {
        this.mViewPager.setCurrentItem(position);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //初始化指示器位置
        mIndicatorStartX = getPaddingLeft();
        mIndicatorStartY = mIndicatorEndY = h - mIndicatorHeight;
        if (mTabContainer != null && mTabContainer.getChildCount() > 0)
            mIndicatorEndX = mTabContainer.getChildAt(0).getMeasuredWidth();
    }


    @Override
    protected void dispatchDraw(Canvas canvas) {
        canvas.drawLine(mIndicatorStartX, mIndicatorStartY, mIndicatorEndX, mIndicatorEndY, mPaint);
        super.dispatchDraw(canvas);
    }


    public void setViewPager(ViewPager mViewPager) {
        if (mViewPager == null) return;
        this.mViewPager = mViewPager;

        this.mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                scrollToChild(position, positionOffset);
            }

            @Override
            public void onPageSelected(int position) {
                updateCurrentTabTitle();
                updateSelectTabTitle(position);
                mCurrentPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    /**
     * 更新当前TabTitle
     */
    private void updateCurrentTabTitle() {
        TextView mLastTabTitleView = (TextView) mTabContainer.getChildAt(mCurrentPosition);
        mLastTabTitleView.setTextColor(mTabTitleColor);
        mLastTabTitleView.animate().scaleX(1.0f).scaleY(1.0f).setDuration(150).start();


    }

    /**
     * 更新选中TabTitle
     *
     * @param position
     */
    private void updateSelectTabTitle(int position) {
        TextView mCurrentTabTitleView = (TextView) mTabContainer.getChildAt(position);
        mCurrentTabTitleView.setTextColor(mTabTitleSelectedColor);
        mCurrentTabTitleView.animate().scaleX(Math.max(mTabTitleSize, mTabTitleSelectedSize) * 1.0f / Math.min(mTabTitleSize, mTabTitleSelectedSize)).scaleY(mTabTitleSelectedSize * 1.0f / mTabTitleSize).setDuration(150).start();
    }

    //滚动到指定Tab
    private void scrollToChild(int position, float positionOffset) {
        //0 - 1
        if (positionOffset <= 0) return;
        View mCurrentTab = mTabContainer.getChildAt(position);
        View mNextTab = mTabContainer.getChildAt(position + 1);
        //滚动指示器
        mIndicatorStartX = mCurrentTab.getLeft() + mCurrentTab.getWidth() * positionOffset;
        mIndicatorEndX = mCurrentTab.getRight() + mNextTab.getWidth() * positionOffset;
        invalidate();
        if (checkmTabTitleOffsetCenter(mNextTab)) {
            //滚动容器到最左侧
            if (getScrollX() != 0)
                this.scrollTo(0, 0);
        } else {
            int mScrollX = 0;
            if (!checkmTabTitleOffsetCenter(mCurrentTab)) {
                //当前TabTitle容器的总偏移量
                mScrollX = mCurrentTab.getLeft() - ((getWidth() / 2) - (mCurrentTab.getWidth() / 2));
            }
            //滑动到下一个tab的偏移量
            int scrollOffset = (int) ((mNextTab.getLeft() - mScrollX - (getWidth() / 2 - mNextTab.getWidth() / 2)) * positionOffset);
//            //滚动容器
            this.scrollTo(mScrollX + scrollOffset, 0);
        }
    }


    /**
     * 判断TabTitle的位置是否在中心位置以内
     *
     * @param view
     * @return
     */
    private boolean checkmTabTitleOffsetCenter(View view) {
        return (view.getLeft() + view.getWidth() / 2) < getWidth() / 2;
    }

    /**
     * px > sp
     *
     * @param pxValue
     * @return
     */
    private float px2sp(float pxValue) {
        float scale = getContext().getResources().getDisplayMetrics().scaledDensity;
        return pxValue / scale + 0.5f;
    }

    /**
     * dp > px
     *
     * @param dpValue
     * @return
     */
    private float dp2px(float dpValue) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, getContext().getResources().getDisplayMetrics());
    }
}
