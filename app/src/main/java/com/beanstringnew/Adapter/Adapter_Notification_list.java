package com.beanstringnew.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beanstringnew.Activity.Open_photo_video_activity;
import com.beanstringnew.Activity.Other_Profile_activity;
import com.beanstringnew.Model.Model_notification;
import com.beanstringnew.R;
import com.beanstringnew.Shared.Pref_Master;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by admin on 12/8/2016.
 */

public class Adapter_Notification_list extends BaseAdapter {

    private final Context context;
    ArrayList<Model_notification> array;

    Pref_Master pref;
    LayoutInflater layoutInflater;
    ViewHolder holder;

    public Adapter_Notification_list(Context context, ArrayList<Model_notification> array, Pref_Master pref) {
        super();
        this.context = context;
        this.array = array;
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
        final Model_notification model = array.get(position);

        LayoutInflater inflater = LayoutInflater.from(context);


        if (v == null) {
            v = LayoutInflater.from(context).inflate(R.layout.row_notification_list, null);

            holder.noti_profile = (ImageView) v.findViewById(R.id.noti_profile);
            holder.not_desc = (TextView) v.findViewById(R.id.not_desc);
            holder.time_noti = (TextView) v.findViewById(R.id.time_noti);
            holder.ll_notification = (LinearLayout) v.findViewById(R.id.ll_notification);

            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }

        if (model.getProfile().equals("")) {
            holder.noti_profile.setImageResource(R.drawable.default_imggg);

        } else {
            Glide.with(context).load(model.getProfile()).into(holder.noti_profile);
        }
        holder.not_desc.setText(model.getDesc());
        holder.time_noti.setText(model.getTime() + " " + "ago");

        holder.ll_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (model.getNoti_status().equals("1")) {
                    Intent i = new Intent(context, Other_Profile_activity.class);
                    i.putExtra("id", model.getUserid());
                    Log.e("following_id", model.getUserid());
                    context.startActivity(i);

                } else {

                    Intent i = new Intent(context, Open_photo_video_activity.class);
                    i.putExtra("postid", model.getPostid());
                    i.putExtra("del_value", "0");
                    //i.putExtra("postuserid", model.getUserid());
                    context.startActivity(i);

                }
            }
        });
        return v;
    }

    private class ViewHolder {
        ImageView noti_profile;
        TextView not_desc, time_noti;
        LinearLayout ll_notification;

    }
}

