package com.beanstringnew.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
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
import com.beanstringnew.Json_model.JSON;
import com.beanstringnew.Model.Model_Product;
import com.beanstringnew.Myapplication;
import com.beanstringnew.R;
import com.beanstringnew.Shared.Pref_Master;
import com.google.firebase.crash.FirebaseCrash;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by admin on 11/29/2016.
 */

public class Product_details_activity extends AppCompatActivity implements Thread.UncaughtExceptionHandler {

    Pref_Master pref;
    Context context = this;
    String id;
    String catid;
    Activity_indicator obj_dialog;
    ArrayList<Model_Product> Array_user = new ArrayList<>();
    ArrayList<Model_Product> Array = new ArrayList<>();
    ArrayList<Model_Product> array1 = new ArrayList<>();
    ArrayList<String> color = new ArrayList<>();
    ArrayList<String> size = new ArrayList<>();
    ArrayList<Model_Product> array = new ArrayList<>();

    ImageView prod_imag, addcart;
    ImageView back_button;
    ImageView share;
    TextView prod_name, prod_beans, desc;
    Spinner spinner_color, spinner_size;
    Typeface typeFace_Rupee;
    LinearLayout ll_color_size;

    int count = 1;
    RelativeLayout cart;
    TextView total_cart;
    Handler handler;
    private int currentPage = 0;
    Interpolator sInterpolator;
    Runnable Update;
    ViewPager viewpager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_detail_layout);
        pref = new Pref_Master(context);

        Intent i = getIntent();
        id = i.getStringExtra("id");
        catid = i.getStringExtra("catid");

        Log.e("category_id", "-->" + catid);

        obj_dialog = new Activity_indicator(context);
        obj_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        typeFace_Rupee = Typeface.createFromAsset(context.getAssets(), "Rupee.ttf");
        viewpager = (ViewPager) findViewById(R.id.viewpager);


        cart = (RelativeLayout) findViewById(R.id.cart);
        total_cart = (TextView) findViewById(R.id.total_cart);
        share = (ImageView) findViewById(R.id.share);
        back_button = (ImageView) findViewById(R.id.back_button);
        prod_imag = (ImageView) findViewById(R.id.prod_imag);
        prod_name = (TextView) findViewById(R.id.prod_name);
        prod_beans = (TextView) findViewById(R.id.prod_beans);
        desc = (TextView) findViewById(R.id.desc);
        ll_color_size = (LinearLayout) findViewById(R.id.ll_color_size);
        spinner_color = (Spinner) findViewById(R.id.spinner_color);
        spinner_size = (Spinner) findViewById(R.id.spinner_size);
        addcart = (ImageView) findViewById(R.id.addcart);
        addcart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Validate();
            }
        });
        cart.setOnClickListener(new View.OnClickListener()

                                {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(Product_details_activity.this, Mall_home_activity.class);
                                        intent.putExtra("fragmentcode", Configr.Fragmentt_ID.cart);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                    }
                                }

        );


        back_button.setOnClickListener(new View.OnClickListener()

                                       {
                                           @Override
                                           public void onClick(View v) {
                                               finish();
                                               Activity_cart_count.cart_count(context, pref);

                                           }
                                       }

        );
        Model_Product model = new Model_Product();
        model.setId(id);
        model.setCatid(catid);
        Array_user.add(model);

        get_product_data();
        cart_count();

        Model_Product modell = new Model_Product();
        modell.setId("1");
        array.add(modell);
        get_product();

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Drawable mDrawable = prod_imag.getDrawable();
                Bitmap mBitmap = ((BitmapDrawable) mDrawable).getBitmap();

                String path = MediaStore.Images.Media.insertImage(getContentResolver(), mBitmap, "Image Description", null);
                Uri uri = Uri.parse(path);

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("image/jpeg");
                intent.putExtra(Intent.EXTRA_STREAM, uri);
                startActivity(Intent.createChooser(intent, "Share Image"));

            }
        });
        Myapplication.getInstance().trackScreenView("Product Detail Screen");

    }

    public void Validate() {


        if (Array.size() != 0) {
            if (Array.get(0).getCatid().equals("1")) {
                Array_user.clear();
                Model_Product modell = new Model_Product();
                modell.setCatid(Array.get(0).getCatid());
                modell.setId(Array.get(0).getId());
                modell.setQty("1");
                modell.setBeans(Array.get(0).getBeans());

                Array_user.add(modell);
                add_cart();
                //  }
            } else {
                Array_user.clear();
                Model_Product modell = new Model_Product();
                modell.setCatid(Array.get(0).getCatid());
                modell.setId(Array.get(0).getId());
                modell.setQty("1");
                modell.setBeans(Array.get(0).getBeans());
                modell.setSizeid("0");
                modell.setColorid("0");
                Array_user.add(modell);
                add_cart();
            }
        } else {
            get_product_data();
        }
    }

    public void get_product_data() {

        obj_dialog.show();

        String url = Configr.app_url + "getproductdetail";

        String json = "";

        json = JSON.json_product(Array_user, pref, "getproductdetail");

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


                    if (res_flag.equals("200")) {
                        JSONObject job = new JSONObject(jobj.getString("data"));

                        JSONArray jarray1 = new JSONArray(job.getString("product"));

                        Array.clear();
                        for (int i = 0; i < jarray1.length(); i++) {
                            Model_Product model = new Model_Product();
                            JSONObject jobj1 = jarray1.getJSONObject(i);

                            model.setName(jobj1.getString("name"));
                            model.setDesc(jobj1.getString("des"));
                            model.setBeans(jobj1.getString("beans"));
                            model.setCatid(jobj1.getString("catid"));
                            model.setId(jobj1.getString("id"));


                            prod_name.setText(jobj1.getString("name"));

                            if (jobj1.getString("image").equals("")) {
                                prod_imag.setImageResource(R.drawable.no_image);
                            } else {
                                Picasso.with(context).load(jobj1.getString("image")).skipMemoryCache().into(prod_imag);
                            }

                            desc.setText(jobj1.getString("des"));
                            prod_beans.setText(jobj1.getString("beans"));

                            Array.add(model);
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
                //  signup.setClickable(true);
                obj_dialog.dismiss();
                Toast.makeText(context, "" + Configr.Message, Toast.LENGTH_SHORT).show();
            }
        };
        Connection.postconnection(url, param, Configr.getHeaderParam(), context, lis_pat, lis_error);

    }

    public void add_cart() {

        obj_dialog.show();
        String url = Configr.app_url + "addtocart";
        String json = "";

        json = JSON.json_product(Array_user, pref, "addtocart");

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

                        LayoutInflater li = LayoutInflater.from(context);
                        View v = li.inflate(R.layout.alert_popup_ok, null);
                        final android.support.v7.app.AlertDialog alert = new android.support.v7.app.AlertDialog.Builder(context).setView(v).show();
                        alert.setCancelable(false);
                        alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        TextView ok = (TextView) v.findViewById(R.id.con_ok);
                        TextView txt = (TextView) v.findViewById(R.id.txt);
                        txt.setText(res_msg);

                        ok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                alert.dismiss();
                            }
                        });

                        cart_count();

                    } else if (res_flag.equals("201")) {
                        DialogBox.alert_popup(context, res_msg);
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
                //  signup.setClickable(true);
            }
        };
        Connection.postconnection(url, param, Configr.getHeaderParam(), context, lis_pat, lis_error);
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
                //  signup.setClickable(true);
                obj_dialog.dismiss();
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
                        Toast.makeText(context, res_msg, Toast.LENGTH_SHORT).show();
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
