package com.beanstringnew.Adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.beanstringnew.Model.Model_Product;
import com.beanstringnew.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Abc on 11/8/2016.
 */

public class Adapter_home_advertise extends PagerAdapter {

    Context myContext;
    ArrayList<Model_Product> imageArray;
    LayoutInflater mLayoutInflater;
    ImageView gestureImageView;


    public Adapter_home_advertise(Context context, ArrayList<Model_Product> arrayList) {
        myContext = context;
        imageArray = arrayList;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }



    @Override
    public int getCount() {
        return imageArray.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        View itemView = mLayoutInflater.inflate(R.layout.activity_gallery_image, container, false);
        gestureImageView = (ImageView) itemView.findViewById(R.id.gimageview);
        Picasso.with(myContext).load(imageArray.get(position).getImgurl()).fit().into(gestureImageView);
        Log.i("vikas_image", ":" + imageArray.get(position));
        container.addView(itemView);

        return itemView;
    }

    public void destroyItem(View container, int position, Object object) {
        ((ViewPager) container).removeView((LinearLayout) object);


    }

}