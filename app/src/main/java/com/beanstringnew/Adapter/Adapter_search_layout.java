package com.beanstringnew.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.beanstringnew.Activity.Other_Profile_activity;
import com.beanstringnew.Controller.Configr;
import com.beanstringnew.Controller.Connection;
import com.beanstringnew.Controller.DialogBox;
import com.beanstringnew.Model.Model_State_city;
import com.beanstringnew.Model.Model_search;
import com.beanstringnew.R;
import com.beanstringnew.Shared.Pref_Master;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by admin on 10/24/2016.
 */

public class Adapter_search_layout extends BaseAdapter {

    private final Context context;
    ArrayList<Model_search> array_sub;
    ViewHolder holder;
    String res_message;
    Pref_Master pref;
    Runnable search_call;
    ArrayList<Model_State_city> array = new ArrayList<>();
    ArrayList<Model_State_city> array1 = new ArrayList<>();


    public Adapter_search_layout(Context context, ArrayList<Model_search> array_sub, Runnable search_call) {
        super();
        this.context = context;
        this.array_sub = array_sub;
        this.search_call = search_call;
        pref = new Pref_Master(context);

    }

    @Override
    public int getCount() {
        return array_sub.size();
    }

    @Override
    public Object getItem(int position) {
        return array_sub.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View v, ViewGroup parent) {
        holder = new ViewHolder();
        final Model_search model = array_sub.get(position);
        if (v == null) {
            v = LayoutInflater.from(context).inflate(R.layout.row_search_list_layout, null);
            holder.profile_img = (ImageView) v.findViewById(R.id.profile_img);
            holder.tvfname = (TextView) v.findViewById(R.id.tvfname);
            holder.tvlname = (TextView) v.findViewById(R.id.tvlname);
            holder.city = (TextView) v.findViewById(R.id.city);
            holder.follow = (TextView) v.findViewById(R.id.follow);
            holder.ll_search = (LinearLayout) v.findViewById(R.id.ll_search);

            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }

        try {
            if (model.getStatus().equals("0")) {
                holder.follow.setText("+Follow");
            } else {
                holder.follow.setText("Following");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (model.getStatus().equals("0")) {
                    Log.e("ifffffff_follow", holder.follow.getText().toString());

                    LayoutInflater li = LayoutInflater.from(context);
                    v = li.inflate(R.layout.alert_popup, null);
                    final android.support.v7.app.AlertDialog alert = new android.support.v7.app.AlertDialog.Builder(context).setView(v).show();
                    alert.setCancelable(false);
                    alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    TextView ok = (TextView) v.findViewById(R.id.con_ok);
                    TextView cancel = (TextView) v.findViewById(R.id.con_cancel);
                    TextView txt = (TextView) v.findViewById(R.id.txt);
                    txt.setText("Are you sure you want to Follow?");

                    ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            serach_follow(model.getUserid());
                            alert.dismiss();
                        }
                    });
                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            alert.dismiss();
                        }
                    });


                } else {
                    Log.e("elsefffff follow", holder.follow.getText().toString());
                    DialogBox.alert_popup(context, "Already Follow");
                }
            }
        });

        holder.ll_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, Other_Profile_activity.class);
                i.putExtra("id", model.getUserid());
                context.startActivity(i);
            }
        });

        holder.tvfname.setText(model.getFname());
        holder.tvlname.setText(model.getLname());

//        if (model.getCity().equals("")) {
//            holder.city.setText("From" + "");
//        } else {
        holder.city.setText(model.getCity());
        //  }
        Log.e("jinal", "" + model.getPic());


        Glide.with(context).load(model.getPic()).into(holder.profile_img);


        return v;
    }


    private class ViewHolder {
        ImageView profile_img;
        TextView tvfname, tvlname, city, follow;
        LinearLayout ll_search;

    }

    private void serach_follow(String id) {

        res_message = "Server Error...";

        String url = Configr.app_url + "follow";
        JSONObject jobj_loginuser = new JSONObject();
        try {

            JSONObject jobj_row = new JSONObject();

            jobj_row.put("sender", pref.getStr_Userid());
            jobj_row.put("receiver", id);

            JSONArray jarray_loginuser = new JSONArray();
            jarray_loginuser.put(jobj_row);

            jobj_loginuser.put("follow", jarray_loginuser);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Map<String, String> params = new HashMap<>();
        params.put("data", "" + jobj_loginuser.toString().replaceAll("\\\\", ""));
        Response.Listener<String> lis_res = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("response", " : " + response);

                String toastMsg = "";
                toastMsg = "";


                try {
                    JSONObject jobj = new JSONObject(response);
                    String res_flag = jobj.getString("status");
                    String res_msg = jobj.getString("message");
                    String otp = "", userid = "";

                    if (res_flag.equals("200")) {
                        Log.e("unfollow_Profileeeeeee", "Enterr");
                        search_call.run();

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
                Log.e("response error", "" + error);
                Toast.makeText(context, "" + Configr.Message, Toast.LENGTH_SHORT).show();
            }
        };
        Connection.postconnection(url, params, Configr.getHeaderParam(), context, lis_res, lis_error);
    }
}
