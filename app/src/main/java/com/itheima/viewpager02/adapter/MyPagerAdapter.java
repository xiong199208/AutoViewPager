package com.itheima.viewpager02.adapter;
/*
 *  @项目名：  carousel 
 *  @包名：    com.itheima.carousel.adapter
 *  @文件名:   MyPagerAdapter
 *  @创建者:   Administrator
 *  @创建时间: 十月
 *  @描述：    TODO
 */

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class MyPagerAdapter extends PagerAdapter {

    int [] mIcons;
    Context mContext;
    //List<View> mList;

    public MyPagerAdapter(Context context, int[] icons) {
        mContext = context;
        mIcons = icons;

    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        int index = position%mIcons.length;

        ImageView imageView = new ImageView(mContext);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView.setImageResource(mIcons[index]);
/*
        ImageView imageView = (ImageView) mList.get(index);

        container.addView(imageView);*/

        if (imageView.getParent()==null) {
            container.addView(imageView);
        }


        return imageView;
    }
}
