package com.beanstringnew.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.beanstringnew.Activity.Other_Profile_activity;
import com.beanstringnew.Controller.Configr;
import com.beanstringnew.Controller.Connection;
import com.beanstringnew.Model.Model_Follower_List;
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
 * Created by Abc on 10/21/2016.
 */

public class Adapter_profile_my_followers extends BaseAdapter {

    private final Context context;
    ArrayList<Model_Follower_List> array;
    String res_message;
    Pref_Master pref;
    String id;
    Runnable api_c;

    Model_Follower_List model;


    public Adapter_profile_my_followers(Context context, ArrayList<Model_Follower_List> array, Runnable api_c) {
        super();
        this.context = context;
        this.array = array;
        this.api_c = api_c;
        notifyDataSetChanged();
        pref = new Pref_Master(context);
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


        final ViewHolder holder;
        if (v == null) {
            v = LayoutInflater.from(context).inflate(R.layout.row_layout_for_following, null);
            holder = new ViewHolder(v);
            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }

        final Model_Follower_List model = array.get(position);
        if (model.getProf_image().equals("")) {
            holder.pic.setImageResource(R.drawable.default_imggg);

        } else {
            Glide.with(context).load(model.getProf_image()).into(holder.pic);
        }

        if (model.getState().equals("")) {
            holder.name.setText(model.getF_name() + " " + model.getL_name());
        } else {
            holder.name.setText(model.getF_name() + " " + model.getL_name());
            holder.info.setText(model.getCity() + " , " + model.getState());
        }

        if (model.getStatus().equals("0")) {
            holder.btn_follow.setText("+Follow");
        } else {
            holder.btn_follow.setText("Following");
        }

        holder.btn_follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (model.getStatus().equals("0")) {
                    HTTP_API_follow_Call(model.getUser_id());
                } else {
                    Toast.makeText(context, "Already Follow", Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.card_getprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, Other_Profile_activity.class);
                id = array.get(position).getUser_id();
                i.putExtra("id", id);
                context.startActivity(i);

            }
        });
        return v;
    }


    private class ViewHolder {

        ImageView pic;
        TextView name;
        TextView info;
        TextView btn_follow;
        LinearLayout card_getprofile;

        public ViewHolder(View v) {
            pic = (ImageView) v.findViewById(R.id.pic);
            name = (TextView) v.findViewById(R.id.name);
            info = (TextView) v.findViewById(R.id.info);
            btn_follow = (TextView) v.findViewById(R.id.btn_follow);
            card_getprofile = (LinearLayout) v.findViewById(R.id.card_getprofile);

        }
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
                api_c.run();
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
