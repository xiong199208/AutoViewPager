package com.itheima.viewpager02;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.itheima.viewpager02.adapter.MyPagerAdapter;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener, View.OnTouchListener {

    int[] mIcons = {
            R.drawable.guide_1,
            R.drawable.guide_2,
            R.drawable.guide_3
    };

    List<View> mList;
    @Bind(R.id.viewpager)
    ViewPager mViewpager;
    @Bind(R.id.ll_dot)
    LinearLayout mLlDot;
    @Bind(R.id.iv_red)
    ImageView mIvRed;

    int mPitch;
    private int mPosition;
    private MyHandler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initData();

        initListener();

        autoPager();


    }

    /**
     * 开启ViewPager的自动轮播
     */
    private void autoPager() {

        if (mHandler==null) {
            mHandler = new MyHandler();
        }

        mHandler.startAuto();

    }

    /**
     * 在触摸事件里判断，down状态停止轮播，up状态开启轮播
     * 但是不能拦截事件，不然就无法拖动轮播图了
     * @param v
     * @param event
     * @return
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mHandler.stopAuto();
                break;
            case MotionEvent.ACTION_UP:
                mHandler.startAuto();
                break;
        }

        return false;
    }

    /**
     * 自定义一个Handler,内部用递归实现无限自动轮播，并添加了开始和结束方法。
     */
    class MyHandler extends Handler implements Runnable {

        @Override
        public void run() {

            mViewpager.setCurrentItem(mViewpager.getCurrentItem()+1);
            //每过2秒钟切换一次，递归
            postDelayed(this,2000);

        }

        public void startAuto() {
            //防止开启两次task
            removeCallbacks(this);

            postDelayed(this,2000);
        }

        public void stopAuto() {
            removeCallbacks(this);
        }
    }

    private void initListener() {
        //ViewPager的滑动监听
        mViewpager.addOnPageChangeListener(this);
        //ViewPager触摸监听
        mViewpager.setOnTouchListener(this);
    }

    private void initData() {
        //初始化小黑点
        initDot();

        //initPager();

        MyPagerAdapter adapter = new MyPagerAdapter(this, mIcons);

        mViewpager.setAdapter(adapter);

        int middle = Integer.MAX_VALUE / 2 ;

        //偏移Viewpager,显示第一张图片
        int extra = middle % mIcons.length;

        mViewpager.setCurrentItem(middle - extra );

        //ViewPager初始化不会走onPageSelected方法，所以需要手动赋值
        mPosition = middle - extra;

    }


    /**
     * 传入Adapter的是ImageView的集合，就会报
     * The specified child already has a parent. You must call removeView() on the child's parent first.
     *//*
    private void initPager() {

        mList = new ArrayList<>();

        for (int i = 0; i < mIcons.length; i++) {

            ImageView iv = new ImageView(this);
            iv.setImageResource(mIcons[i]);
            iv.setScaleType(ImageView.ScaleType.FIT_XY);
            mList.add(iv);

        }
    }*/


    private void initDot() {

        //屏幕适配，将dp值转换成Pixel值
        int size = getResources().getDimensionPixelSize(R.dimen.dot_size);
        //根据轮播图的数量添加小黑点
        for (int i = 0; i < mIcons.length; i++) {

            ImageView image = new ImageView(this);
            image.setImageResource(R.drawable.dot_normal);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(size,size);

            if (i!=0) {
                //小黑点的间距
                params.leftMargin = size;
            }
            image.setLayoutParams(params);
            mLlDot.addView(image);

            //当添加到第二个小黑点时，计算两个小黑点间的距离，为小红点的移动做准备
            if (mLlDot.getChildCount()==2) {
                mLlDot.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        //通过取两个小黑点左边距来
                        mPitch = mLlDot.getChildAt(1).getLeft()-mLlDot.getChildAt(0).getLeft();
                        mLlDot.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                });

            }

        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        System.out.println("onPageScrolled"+position);
        int pixel = Math.round(mPitch*positionOffset)+(position%mIcons.length)*mPitch;

        //小红点左右边界判断
        if (position%mIcons.length== mIcons.length-1) {
            pixel=(position%mIcons.length)*mPitch;
        }

        if (mPosition%mIcons.length==0) {

            if (pixel>mPitch) {
                pixel = (mPosition%mIcons.length)*mPitch;
            }

        }

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mIvRed.getLayoutParams();
        //通过改变小红点左边距，来移动小红点
        params.leftMargin = pixel;

        mIvRed.setLayoutParams(params);
    }

    @Override
    public void onPageSelected(int position) {

        mPosition=position;

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
