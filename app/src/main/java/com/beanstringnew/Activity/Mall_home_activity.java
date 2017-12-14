package com.beanstringnew.Activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ToxicBakery.viewpager.transforms.CubeOutTransformer;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.beanstringnew.Adapter.Adapter_home_advertise;
import com.beanstringnew.Controller.Activity_cart_count;
import com.beanstringnew.Controller.Activity_indicator;
import com.beanstringnew.Controller.Configr;
import com.beanstringnew.Controller.Connection;
import com.beanstringnew.Controller.DialogBox;
import com.beanstringnew.Controller.FixedSpeedScroller;
import com.beanstringnew.Fragment.Main_mall_fragment;
import com.beanstringnew.Fragment.My_account_fragment;
import com.beanstringnew.Fragment.My_cart;
import com.beanstringnew.Fragment.My_product_fragment;
import com.beanstringnew.Fragment.Search_mall_fragment;
import com.beanstringnew.Json_model.JSON;
import com.beanstringnew.Model.Model_Product;
import com.beanstringnew.Model.Model_profile;
import com.beanstringnew.Myapplication;
import com.beanstringnew.R;
import com.beanstringnew.Shared.Pref_Master;
import com.bumptech.glide.Glide;
import com.google.firebase.crash.FirebaseCrash;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by admin on 11/17/2016.
 */

public class Mall_home_activity extends AppCompatActivity implements Thread.UncaughtExceptionHandler {

    FrameLayout rl_m_main;
    RecyclerView list;
    Context context = this;
    U_NavigationAdapter adapter;
    Activity_indicator obj_dialog;
    ArrayList<Model_Product> Array_user = new ArrayList<>();
    ArrayList<Model_profile> array_usr = new ArrayList<>();
    ArrayList<Model_Product> arrayList = new ArrayList<>();
    ArrayList<Model_Product> array = new ArrayList<>();

    int fragmentcode = 0;
    Intent i;

    DrawerLayout drawer;
    TextView title;

    Pref_Master pref;


    ImageView imgview;
    RelativeLayout cart;
    ImageView search;
    public static TextView total_cart;
    boolean doubleBackToExitPressedOnce = false;
    LinearLayout ll_one;
    LinearLayout ll_two;
    LinearLayout ll_three;
    //    LinearLayout ll_four;
//    LinearLayout ll_five;
    LinearLayout ll_six;
    ViewPager viewpager;
    Handler handler;
    private int currentPage = 0;
    Interpolator sInterpolator;
    Runnable Update;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mall_home_activity);

        imgview = (ImageView) findViewById(R.id.imgview);
        cart = (RelativeLayout) findViewById(R.id.cart);
        total_cart = (TextView) findViewById(R.id.total_cart);
        search = (ImageView) findViewById(R.id.search);


        list = (RecyclerView) findViewById(R.id.list);
        pref = new Pref_Master(context);

        viewpager = (ViewPager) findViewById(R.id.viewpager);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        toggle.setHomeAsUpIndicator(R.drawable.menu_drower);
        toggle.syncState();
        obj_dialog = new Activity_indicator(context);
        obj_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));

        ll_one = (LinearLayout) findViewById(R.id.ll_one);
        ll_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ii = new Intent(Mall_home_activity.this, Home_Social.class);
                startActivity(ii);
                finish();
                drawer.closeDrawers();
            }
        });
        ll_two = (LinearLayout) findViewById(R.id.ll_two);
        ll_two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setfragment(new Main_mall_fragment());
                drawer.closeDrawers();
                viewpager.setVisibility(View.GONE);
                search.setVisibility(View.VISIBLE);
            }
        });
        ll_three = (LinearLayout) findViewById(R.id.ll_three);
        ll_three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setfragment(new My_account_fragment());
                drawer.closeDrawers();
                viewpager.setVisibility(View.VISIBLE);
                search.setVisibility(View.GONE);
            }
        });
        ll_six = (LinearLayout) findViewById(R.id.ll_six);
        ll_six.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater li = LayoutInflater.from(context);
                v = li.inflate(R.layout.alert_popup, null);
                final android.support.v7.app.AlertDialog alert = new android.support.v7.app.AlertDialog.Builder(context).setView(v).show();
                alert.setCancelable(false);
                alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                TextView ok = (TextView) v.findViewById(R.id.con_ok);
                TextView cancel = (TextView) v.findViewById(R.id.con_cancel);
                TextView txt = (TextView) v.findViewById(R.id.txt);
                txt.setText("Are you sure you want to exit?");

                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Sign_out();
                        alert.dismiss();
                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alert.dismiss();
                    }
                });
            }
        });

        drawer.closeDrawers();


        rl_m_main = (FrameLayout) findViewById(R.id.rl_m_main);

        if (getIntent().getExtras() != null) {
            fragmentcode = getIntent().getIntExtra("fragmentcode", 1);
            switch (fragmentcode) {
                case Configr.Fragmentt_ID.Main:
//                    fragmentcode = 1;
                    break;

                case Configr.Fragmentt_ID.cart:
                    fragmentcode = 3;
                    setfragment(new My_cart());
                    search.setVisibility(View.GONE);
                    viewpager.setVisibility(View.GONE);
                    break;
                case Configr.Fragmentt_ID.Product:
                    fragmentcode = 5;
                    setfragment(new My_product_fragment());
                    search.setVisibility(View.VISIBLE);
                    viewpager.setVisibility(View.VISIBLE);
                    break;
            }
        } else

        {
            setfragment(new Main_mall_fragment());
            search.setVisibility(View.VISIBLE);
            viewpager.setVisibility(View.GONE);

        }

        categorylist();

        Model_Product model = new Model_Product();
        model.setId("1");
        array.add(model);
        get_product();


        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setfragment(new My_cart());
                search.setVisibility(View.GONE);
                viewpager.setVisibility(View.GONE);
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setfragment(new Search_mall_fragment());
                search.setVisibility(View.VISIBLE);
                viewpager.setVisibility(View.VISIBLE);

            }
        });
        Myapplication.getInstance().trackScreenView("Mall Home Screen");

        adapter = new U_NavigationAdapter();
        list.setAdapter(adapter);
        list.setLayoutManager(new LinearLayoutManager(context));
        list.setHasFixedSize(true);

        cart_count();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Activity_cart_count.cart_count(context, pref);
    }

    public void setfragment(String title, Fragment fragment) {

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.rl_m_main, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {

        setfragment("", new Main_mall_fragment());
        search.setVisibility(View.VISIBLE);
        viewpager.setVisibility(View.GONE);
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            finish();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }


    public class U_NavigationAdapter extends RecyclerView.Adapter<U_NavigationHolder> {

        @Override
        public U_NavigationHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new U_NavigationHolder(LayoutInflater.from(context).inflate(R.layout.u_navigation_listrow, null));
        }

        @Override
        public void onBindViewHolder(final U_NavigationHolder holder, final int position) {

            final Model_Product model = arrayList.get(position);
            // holder.u_navigation_row_image.setImageResource(array_image.get(position));
            Log.e("Name_category", model.getName());


            holder.u_navigation_row_name.setText(model.getName());
            Glide.with(context).load(model.getImg()).into(holder.u_navigation_row_image);


            holder.ll_click.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent i = new Intent(Mall_home_activity.this, Mall_home_activity.class);
                    i.putExtra("fragmentcode", Configr.Fragmentt_ID.Product);
                    i.putExtra("id", model.getId());
                    startActivity(i);
                }
            });


        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }
    }

    public class U_NavigationHolder extends RecyclerView.ViewHolder {

        ImageView u_navigation_row_image;
        TextView u_navigation_row_name;
        LinearLayout ll_click;

        View v;

        public U_NavigationHolder(View itemView) {
            super(itemView);
            this.v = itemView;
            u_navigation_row_image = (ImageView) itemView.findViewById(R.id.u_navigation_row_image);
            u_navigation_row_name = (TextView) itemView.findViewById(R.id.u_navigation_row_name);
            ll_click = (LinearLayout) itemView.findViewById(R.id.ll_click);

        }
    }

    public void setfragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.rl_m_main, fragment);
        fragmentTransaction.commit();

    }

    public void cart_count() {

        obj_dialog.show();

        String url = Configr.app_url + "cartcount";

        String json = "";

        json = JSON.json_product(Array_user, pref, "cartcount");

        HashMap<String, String> param = new HashMap<>();
        param.put("data", json);

        Response.Listener<String> lis_pat = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response", response);
                obj_dialog.dismiss();

                String toastMsg = "";
                toastMsg = "";


                try {
                    JSONObject jobj = new JSONObject(response);
                    String res_flag = jobj.getString("status");
                    String res_msg = jobj.getString("message");
                    String otp = "", userid = "";

                    if (res_flag.equals("200")) {

                        JSONObject job = jobj.getJSONObject("data");
                        if (job.getString("total").equals("0")) {
                            total_cart.setVisibility(View.GONE);
                        } else {
                            total_cart.setVisibility(View.VISIBLE);
                            total_cart.setText(job.getString("total"));
                        }
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
                obj_dialog.dismiss();
                Toast.makeText(context, "" + Configr.Message, Toast.LENGTH_SHORT).show();
            }
        };
        Connection.postconnection(url, param, Configr.getHeaderParam(), context, lis_pat, lis_error);

    }

    public void Sign_out() {

        obj_dialog.show();

        String url = Configr.app_url + "signout";

        String json = "";

        json = JSON.add(array_usr, pref, "signout");

        HashMap<String, String> param = new HashMap<>();
        param.put("data", json);

        Response.Listener<String> lis_pat = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response", response);
                obj_dialog.dismiss();

                String toastMsg = "";
                toastMsg = "";


                try {
                    JSONObject jobj = new JSONObject(response);
                    String res_flag = jobj.getString("status");
                    String res_msg = jobj.getString("message");
                    String otp = "", userid = "";

                    if (res_flag.equals("200")) {

                        pref.clear_pref();
                        Intent i = new Intent(context, Login.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                        System.exit(0);


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
                obj_dialog.dismiss();
                Toast.makeText(context, "" + Configr.Message, Toast.LENGTH_SHORT).show();
            }
        };
        Connection.postconnection(url, param, Configr.getHeaderParam(), context, lis_pat, lis_error);

    }

    public void categorylist() {

        //   obj_dialog.show();

        String url = Configr.app_url + "categorylist";
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

                        // JSONArray jarray1 = new JSONArray(job.getString("img"));
                        JSONArray jarray2 = new JSONArray(job.getString("categorylist"));

                        arrayList.clear();


                        for (int i = 0; i < jarray2.length(); i++) {
                            Model_Product model = new Model_Product();
                            JSONObject jobj1 = jarray2.getJSONObject(i);
                            model.setId(jobj1.getString("id"));
                            model.setName(jobj1.getString("name"));
                            model.setImg(jobj1.getString("icon"));
                            arrayList.add(model);
                        }


                        adapter.notifyDataSetChanged();
                        Log.e("array_length", ":" + arrayList.size());

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

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        FirebaseCrash.report(e);
    }

    public void get_product() {

        //   obj_dialog.show();

        String url = Configr.app_url + "getinnerads";
        String json = "";

        json = JSON.json_product(array, pref, "getinnerads");

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
//                        JSONArray jarray2 = new JSONArray(job.getString("product"));
                        array.clear();
                        //array1.clear();

                        for (int i = 0; i < jarray1.length(); i++) {
                            Model_Product model = new Model_Product();
                            JSONObject jobj1 = jarray1.getJSONObject(i);
                            model.setImgurl(jobj1.getString("imagename"));
                            array.add(model);
                        }


                        init();
                        //  adapter.notifyDataSetChanged();
                        Log.e("array_length", ":" + array.size());

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

    public void init() {

        viewpager.setAdapter(new Adapter_home_advertise(context, array));
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
                }, 10000, 6000);

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


}
