package com.beanstringnew.Fragment;


import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.TextView;
import android.widget.Toast;

import com.ToxicBakery.viewpager.transforms.CubeOutTransformer;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.beanstringnew.Adapter.Adapter_home_viewpager;
import com.beanstringnew.Controller.Activity_indicator;
import com.beanstringnew.Controller.Configr;
import com.beanstringnew.Controller.Connection;
import com.beanstringnew.Controller.DialogBox;
import com.beanstringnew.Controller.FixedSpeedScroller;
import com.beanstringnew.Model.Model_Product;
import com.beanstringnew.Myapplication;
import com.beanstringnew.R;
import com.beanstringnew.Shared.Pref_Master;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by admin on 11/18/2016.
 */

public class Main_mall_fragment extends Fragment {

    Pref_Master pref;
    Context context;
    ViewPager viewpager;
    Activity_indicator obj_dialog;

    Handler handler;
    Runnable Update;


    private int currentPage = 0;
    Interpolator sInterpolator;

    ArrayList<Model_Product> array = new ArrayList<>();
    ArrayList<Model_Product> array1 = new ArrayList<>();
    ViewPager viewpager_second;
    TabLayout tabs;
    ViewPagerAdapter view_adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main_mall, container, false);
        context = getActivity();
        pref = new Pref_Master(context);
        obj_dialog = new Activity_indicator(context);
        obj_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));

        viewpager = (ViewPager) v.findViewById(R.id.viewpager);

        viewpager_second = (ViewPager) v.findViewById(R.id.viewpager_second);
        setupViewPager(viewpager_second);

        tabs = (TabLayout) v.findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewpager_second);

        get_product();

        createTabIcons();

        tabs.setTabTextColors(
                getResources().getColor(R.color.black),
                getResources().getColor(R.color.white)
        );
        Myapplication.getInstance().trackScreenView("Main Mall Screen");
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        Myapplication.getInstance().trackScreenView("Main Mall Screen");
    }

    private void createTabIcons() {

        TextView tabOne = (TextView) LayoutInflater.from(context).inflate(R.layout.custom_tab, null);
        tabOne.setText("Featured Products");
        tabs.getTabAt(0).setCustomView(tabOne);

        TextView tabTwo = (TextView) LayoutInflater.from(context).inflate(R.layout.custom_tab, null);
        tabTwo.setText("Top Sale");
        tabs.getTabAt(1).setCustomView(tabTwo);

    }

    private void setupViewPager(ViewPager viewPager) {
        view_adapter = new ViewPagerAdapter(getChildFragmentManager());
        view_adapter.addFragment(new Featured_product_fragment(), "Featured Products");
        view_adapter.addFragment(new Top_sale_fragment(), "Top Sale");
        viewPager.setAdapter(view_adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }

    }


    public void init() {

        viewpager.setAdapter(new Adapter_home_viewpager(context, array));
        viewpager.setPageTransformer(true, new CubeOutTransformer());
        viewpager.setCurrentItem(0);
        // to automatic rotation of the viewpager

        handler = new Handler();
        Update = new Runnable() {
            public void run() {
                if (currentPage == (array.size())) currentPage = 0;
                viewpager.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(
                new TimerTask() {
                    @Override
                    public void run() {
                        handler.post(Update);
                    }
                }, 5000, 3000);

        try {
            Field mScroller;

            mScroller = ViewPager.class.getDeclaredField("mScroller");

            mScroller.setAccessible(true);
            FixedSpeedScroller scroller = new FixedSpeedScroller(viewpager.getContext(), sInterpolator);
            // scroller.setFixedDuration(5000);
            mScroller.set(viewpager, scroller);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }

    public void get_product() {

        //   obj_dialog.show();

        String url = Configr.app_url + "getfeaturedproduct";
        String json = "";

        // json = JSON.add_json(array, pref, "mybeans");

        HashMap<String, String> param = new HashMap<>();
        param.put("data", json);
        Log.e("jinal", ":" + param.toString());

        Response.Listener<String> lis_pat = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response", response);
                //        obj_dialog.dismiss();
                try {
                    JSONObject jobj = new JSONObject(response);
                    String res_flag = jobj.getString("status");
                    String res_msg = jobj.getString("message");
                    if (res_flag.equals("200")) {


                        JSONObject job = new JSONObject(jobj.getString("data"));

                        JSONArray jarray1 = new JSONArray(job.getString("img"));
                        JSONArray jarray2 = new JSONArray(job.getString("product"));
                        array.clear();
                        array1.clear();

                        for (int i = 0; i < jarray1.length(); i++) {
                            Model_Product model = new Model_Product();
                            JSONObject jobj1 = jarray1.getJSONObject(i);
                            model.setImgurl(jobj1.getString("url"));
                            array.add(model);
                        }

                        for (int i = 0; i < jarray2.length(); i++) {
                            Model_Product model = new Model_Product();
                            JSONObject jobj1 = jarray2.getJSONObject(i);
                            model.setId(jobj1.getString("id"));
                            model.setCatid(jobj1.getString("catid"));
                            model.setName(jobj1.getString("name"));
                            model.setImg(jobj1.getString("image"));
                            model.setBeans(jobj1.getString("beans"));
                            array1.add(model);
                        }
                        Log.e("array_length", ":" + array.size());

                        init();
                    } else {
                        DialogBox.alert_popup(context, res_msg);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        };

        Response.ErrorListener lis_error = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // obj_dialog.dismiss();
                Toast.makeText(context, "" + Configr.Message, Toast.LENGTH_SHORT).show();
            }
        };
        Connection.postconnection(url, param, Configr.getHeaderParam(), context, lis_pat, lis_error);
    }
}
