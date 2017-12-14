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
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.beanstringnew.Activity.Photo_activity;
import com.beanstringnew.Controller.Configr;
import com.beanstringnew.Controller.Connection;
import com.beanstringnew.Json_model.JSON;
import com.beanstringnew.Model.Model_Product;
import com.beanstringnew.Model.Model_profile;
import com.beanstringnew.Model.Model_user;
import com.beanstringnew.R;
import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Abc on 11/8/2016.
 */

public class Adapter_Image_viewpager extends PagerAdapter {

    Context myContext;
    ArrayList<Model_profile> imageArray;
    LayoutInflater mLayoutInflater;
    Photo_activity Photo;


    public Adapter_Image_viewpager(Context context, ArrayList<Model_profile> arrayList, Photo_activity photo_activity) {
        myContext = context;
        imageArray = arrayList;
        Photo = photo_activity;
        Log.e("vikas_image", ":" + imageArray.size());
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
        Log.e("vikas_mmmmmm", ":" + imageArray.size());
        View itemView = mLayoutInflater.inflate(R.layout.activity_image_viewpager, container, false);
        ImageView img1 = (ImageView) itemView.findViewById(R.id.img1);
        Log.e("vikas_ima", ":" + imageArray.get(position));
        Glide.with(myContext).load(imageArray.get(position).getUrl()).into(img1);
        String postid = imageArray.get(position).getPostid();
        Log.e("position of post id", postid);
        container.addView(itemView);
        return itemView;
    }

    public void destroyItem(View container, int position, Object object) {
        ((ViewPager) container).removeView((RelativeLayout) object);

    }

}