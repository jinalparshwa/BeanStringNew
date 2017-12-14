package com.beanstringnew.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.beanstringnew.Activity.Open_photo_video_activity;
import com.beanstringnew.Activity.Play_video_activity;
import com.beanstringnew.Controller.Configr;
import com.beanstringnew.Controller.Connection;
import com.beanstringnew.Json_model.JSON;
import com.beanstringnew.Model.Model_Play_video;
import com.beanstringnew.Model.Model_user;
import com.beanstringnew.R;
import com.beanstringnew.Shared.Pref_Master;
import com.bumptech.glide.Glide;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Admin on 7/22/2017.
 */

public class Adapter_grid_beans_earned extends BaseAdapter {

    private final Context context;
    ArrayList<Model_user> array = new ArrayList<>();

    Pref_Master pref;
    LayoutInflater layoutInflater;
    ViewHolder holder;
    Runnable api_cc;


    public Adapter_grid_beans_earned(Context context, ArrayList<Model_user> array, Pref_Master pref, Runnable api_cc) {
        super();
        this.context = context;
        this.array = array;
        this.api_cc = api_cc;
        this.pref = pref;
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
        final Model_user model = array.get(position);

        LayoutInflater inflater = LayoutInflater.from(context);

        if (v == null) {
            v = LayoutInflater.from(context).inflate(R.layout.row_grid_beans_earned, null);

            holder.main_image_view = (ImageView) v.findViewById(R.id.main_image_view);
            holder.tot_beans = (TextView) v.findViewById(R.id.tot_beans);
            holder.iv_play = (ImageView) v.findViewById(R.id.iv_play);


            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }

        if (model.getStatus().equals("1")) {
            holder.iv_play.setVisibility(View.VISIBLE);
        } else {
            holder.iv_play.setVisibility(View.GONE);
        }


        Log.e("Beans_posttt", ":" + model.getBeans_post());


        holder.main_image_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (model.getStatus().equals("0")) {
                    Intent i = new Intent(context, Open_photo_video_activity.class);
                    i.putExtra("postid", model.getPostid());
                    i.putExtra("postuserid", model.getPost_userid());
                    i.putExtra("del_value", "1");
                    context.startActivity(i);
                } else {
                    Log.e("Click on video", "video");
                    Model_Play_video model_play_video = new Model_Play_video();
                    model_play_video.setDescription(model.getDescription());
                    model_play_video.setPostid(model.getPostid());
                    model_play_video.setPostuserid(model.getPost_userid());
                    model_play_video.setStatus(model.getStatus());
                    model_play_video.setUrl(model.getUrl());
                    model_play_video.setDel_valu("1");
                    Intent i = new Intent(context, Play_video_activity.class);
                    i.putExtra("url", model_play_video);
                    context.startActivity(i);
                }

            }
        });

        if (model.getThumb().equals("")) {
            holder.main_image_view.setImageResource(R.drawable.no_image);
        } else {
            Glide.with(context).load(model.getThumb()).centerCrop().into(holder.main_image_view);
        }

        holder.tot_beans.setText(model.getTotal_beans());


        return v;
    }


    private class ViewHolder {
        ImageView main_image_view;
        TextView tot_beans;
        ImageView iv_play;


    }

}
