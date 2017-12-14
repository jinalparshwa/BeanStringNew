package com.beanstringnew.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.beanstringnew.Activity.Other_Profile_activity;
import com.beanstringnew.Model.Model_view_comment;
import com.beanstringnew.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.util.ArrayList;

/**
 * Created by Abc on 10/26/2016.
 */

public class Adapter_comment extends BaseAdapter {

    private final Context context;
    ArrayList<Model_view_comment> array_comment;
    String post_id;


    public Adapter_comment(Context context, ArrayList<Model_view_comment> array_comment) {
        super();
        this.context = context;
        this.array_comment = array_comment;
        this.post_id = post_id;


    }

    @Override
    public int getCount() {
        return array_comment.size();
    }

    @Override
    public Object getItem(int position) {
        return array_comment.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View v, ViewGroup parent) {


        final ViewHolder holder;


        if (v == null) {
            v = LayoutInflater.from(context).inflate(R.layout.row_comment_view_layout, null);
            holder = new ViewHolder(v);
            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }
        final Model_view_comment model = array_comment.get(position);

        holder.txt_view_comment.setText(model.getComment());
        holder.time_comment.setText(model.getTime() + "ago");
        holder.txt_name.setText(model.getFname() + " " + model.getLname());

        holder.txt_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, Other_Profile_activity.class);
                Log.e("jinal_idd",model.getUserid());
                i.putExtra("id",model.getUserid());
                context.startActivity(i);
            }
        });

        if (model.getPro_pic().equals("")) {
            holder.propic.setImageResource(R.drawable.default_imggg);
        } else {
            Glide.with(context).load(model.getPro_pic()).into(holder.propic);
        }
        return v;
    }


    private class ViewHolder {
        TextView txt_view_comment;
        TextView time_comment;
        TextView txt_name;
        ImageView propic;

        public ViewHolder(View v) {
            txt_view_comment = (TextView) v.findViewById(R.id.txt_view_comment);
            time_comment = (TextView) v.findViewById(R.id.time_comment);
            propic = (ImageView) v.findViewById(R.id.propic);
            txt_name = (TextView) v.findViewById(R.id.txt_name);
        }
    }


}
