package com.beanstringnew.Fragment;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.beanstringnew.Activity.Thank_youActivity;
import com.beanstringnew.Adapter.Adapter_cart_list;
import com.beanstringnew.Controller.Activity_cart_count;
import com.beanstringnew.Controller.Activity_indicator;
import com.beanstringnew.Controller.Configr;
import com.beanstringnew.Controller.Connection;
import com.beanstringnew.Controller.DialogBox;
import com.beanstringnew.Json_model.JSON;
import com.beanstringnew.Model.Model_Product;
import com.beanstringnew.Model.Model_mycart;
import com.beanstringnew.Myapplication;
import com.beanstringnew.R;
import com.beanstringnew.Shared.Pref_Master;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by admin on 11/18/2016.
 */

public class My_cart extends Fragment {

    Pref_Master pref;
    ListView cart_list;
    Adapter_cart_list adapter;
    Context context;
    Activity_indicator obj_dialog;
    ArrayList<Model_mycart> array = new ArrayList<>();
    ArrayList<Model_mycart> array1 = new ArrayList<>();
    Typeface typeFace_Rupee;
    TextView beans;
    LinearLayout ll_bottom;
    ImageView refresh;
    ImageView back;
    TextView notext;
    String access_code = "";
    String merchant_id = "";
    String redirect_url = "";
    String rsa_url = "";
    String cancelurl = "";
    ArrayList<Model_Product> product = new ArrayList<>();
    String order_no = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.my_cart, container, false);
        context = getActivity();
        pref = new Pref_Master(context);
        obj_dialog = new Activity_indicator(context);
        notext = (TextView) v.findViewById(R.id.notext);
        beans = (TextView) v.findViewById(R.id.beans);
        ll_bottom = (LinearLayout) v.findViewById(R.id.ll_bottom);
        ll_bottom.setOnClickListener(new View.OnClickListener() {
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
                txt.setText("Are you sure you want to Checkout?");

                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Check_out();
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
        refresh = (ImageView) v.findViewById(R.id.refresh);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                get_my_cart();
            }
        });
        back = (ImageView) v.findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });


        cart_list = (ListView) v.findViewById(R.id.cart_list);
        adapter = new Adapter_cart_list(context, array, api_cc);
        obj_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        cart_list.setAdapter(adapter);
        get_my_cart();
        Myapplication.getInstance().trackScreenView("My Cart Screen");
        return v;

    }

    Runnable api_cc = new Runnable() {
        @Override
        public void run() {
            array.clear();
            get_my_cart();
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        array.clear();
        get_my_cart();
        Myapplication.getInstance().trackScreenView("My Cart Screen");
    }

    public void get_my_cart() {

        obj_dialog.show();

        String url = Configr.app_url + "getmycart";
        String json = "";

        json = JSON.addd(array, pref, "getmycart");

        HashMap<String, String> param = new HashMap<>();
        param.put("data", json);
        Log.e("jinal", ":" + param.toString());

        Response.Listener<String> lis_pat = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response", response);
                obj_dialog.dismiss();
                try {
                    JSONObject jobj = new JSONObject(response);
                    String res_flag = jobj.getString("status");
                    String res_msg = jobj.getString("message");
                    if (res_flag.equals("200")) {


                        JSONObject job = new JSONObject(jobj.getString("data"));

                        JSONArray jarray1 = new JSONArray(job.getString("product"));
                        JSONArray jarray2 = new JSONArray(job.getString("tot"));
                        array.clear();
                        array1.clear();

                        for (int i = 0; i < jarray1.length(); i++) {
                            Model_mycart model = new Model_mycart();
                            JSONObject jobj1 = jarray1.getJSONObject(i);
                            model.setCatname(jobj1.getString("categoryname"));
                            model.setName(jobj1.getString("name"));
                            model.setBeans(jobj1.getString("beans"));
                            model.setQty(jobj1.getString("qty"));
                            model.setImage(jobj1.getString("image"));
                            model.setCartid(jobj1.getString("cartid"));
                            model.setProductid(jobj1.getString("productid"));
                            model.setCatid(jobj1.getString("catid"));
                            array.add(model);
                        }
                        for (int i = 0; i < jarray2.length(); i++) {
                            Model_mycart model = new Model_mycart();
                            JSONObject jobj1 = jarray2.getJSONObject(i);
                            model.setTot_beans(jobj1.getString("tot_beans"));
                            array1.add(model);
                        }

                        if (array1.size() != 0) {
                            beans.setText(array1.get(0).getTot_beans());

                        } else {
                            beans.setText("");
                        }


                        adapter.notifyDataSetChanged();
                        if (array.size() == 0) {
                            notext.setVisibility(View.VISIBLE);
                            cart_list.setVisibility(View.GONE);
                        } else {
                            notext.setVisibility(View.GONE);
                            cart_list.setVisibility(View.VISIBLE);
                        }

                    } else {
                        if (array.size() == 0) {
                            notext.setVisibility(View.VISIBLE);
                            cart_list.setVisibility(View.GONE);
                        } else {
                            notext.setVisibility(View.GONE);
                            cart_list.setVisibility(View.VISIBLE);
                        }
                    }
                    adapter.notifyDataSetChanged();
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

    public void Check_out() {

        obj_dialog.show();

        String url = Configr.app_url + "checkout";

        String json = "";

        json = JSON.json_product(product, pref, "checkout");

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
                        order_no = job.getString("orderno");

                        //Toast.makeText(context, res_msg, Toast.LENGTH_SHORT).show();
                        Activity_cart_count.cart_count(context, pref);

                        Intent i = new Intent(context, Thank_youActivity.class);
                        i.putExtra("message", res_msg);
                        i.putExtra("orderno", order_no);
                        startActivity(i);


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


}
