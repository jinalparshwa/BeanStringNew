package com.beanstringnew.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.beanstringnew.Activity.Other_Profile_activity;
import com.beanstringnew.Controller.Configr;
import com.beanstringnew.Controller.Connection;
import com.beanstringnew.Model.Model_higest_beans_list;
import com.beanstringnew.Model.Model_user;
import com.beanstringnew.R;
import com.beanstringnew.Shared.Pref_Master;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Abc on 10/21/2016.
 */

public class Adapter_my_higest_beans_list extends BaseAdapter {

    private final Context context;
    ArrayList<Model_higest_beans_list> array;
    String res_message;

    Pref_Master pref;
    LayoutInflater layoutInflater;
    ViewHolder holder;
    String id;
    Runnable api;

    public Adapter_my_higest_beans_list(Context context, ArrayList<Model_higest_beans_list> array, Pref_Master pref, Runnable api) {
        super();
        this.context = context;
        this.array = array;
        this.pref = pref;
        this.api = api;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        notifyDataSetChanged();

    }

    @Override
    public int getCount() {
        return array.size();
    }

    @Override
    public Object getItem(int position) {
        return array.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View v, ViewGroup parent) {
        holder = new ViewHolder();
        final Model_higest_beans_list model = array.get(position);

        LayoutInflater inflater = LayoutInflater.from(context);

        if (v == null) {
            v = LayoutInflater.from(context).inflate(R.layout.row_list_of_higest_beans, null);

            holder.high_profile = (ImageView) v.findViewById(R.id.high_profile);
            holder.high_fname = (TextView) v.findViewById(R.id.high_fname);
            holder.high_lname = (TextView) v.findViewById(R.id.high_lname);
            holder.high_beans = (TextView) v.findViewById(R.id.high_beans);
            holder.high_card = (CardView) v.findViewById(R.id.high_card);
            holder.folunfol = (TextView) v.findViewById(R.id.folunfol);

            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }
        Log.e("URL", model.getProf_pic());
        holder.high_fname.setText(model.getFname());
        holder.high_lname.setText(model.getLname());
        holder.high_beans.setText(model.getBeans());

        if (model.getProf_pic().equals("")) {
            holder.high_profile.setImageResource(R.drawable.default_imggg);
        } else {
            Glide.with(context).load(model.getProf_pic()).into(holder.high_profile);
        }


        if (model.getStatus().equals("0")) {
            holder.folunfol.setText("+Follow");
        } else {
            holder.folunfol.setText("UnFollow");

        }
        holder.folunfol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (model.getStatus().equals("0")) {
                    Log.e("if_follow",holder.folunfol.getText().toString());
                    HTTP_API_follow_Call(model.getId());
                }
                else {
                    Log.e("else follow",holder.folunfol.getText().toString());
                    HTTP_API_unfollow_Call(model.getId());
                }
            }
        });
        holder.high_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, Other_Profile_activity.class);
                id = array.get(position).getId();
                i.putExtra("id", id);
                context.startActivity(i);
            }
        });

        return v;
    }


    private class ViewHolder {
        ImageView high_profile;
        TextView high_fname, high_lname, high_beans;
        CardView high_card;
        TextView folunfol;

    }
    private void HTTP_API_unfollow_Call(String id) {

        res_message = "Server Error...";

        String url = Configr.app_url + "unfollow";
        JSONObject jobj_loginuser = new JSONObject();
        try {

            JSONObject jobj_row = new JSONObject();

            jobj_row.put("sender", pref.getStr_Userid());
            jobj_row.put("receiver", id);

            JSONArray jarray_loginuser = new JSONArray();
            jarray_loginuser.put(jobj_row);

            jobj_loginuser.put("unfollow", jarray_loginuser);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Map<String, String> params = new HashMap<>();
        params.put("data", "" + jobj_loginuser.toString().replaceAll("\\\\", ""));
        Response.Listener<String> lis_res = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("response", " : " + response);
                api.run();
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


    private void HTTP_API_follow_Call(String id) {

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
                api.run();
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
