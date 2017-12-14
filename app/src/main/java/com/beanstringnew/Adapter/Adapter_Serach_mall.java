package com.beanstringnew.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
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
import com.beanstringnew.Activity.Product_details_activity;
import com.beanstringnew.Controller.Activity_cart_count;
import com.beanstringnew.Controller.Configr;
import com.beanstringnew.Controller.Connection;
import com.beanstringnew.Json_model.JSON;
import com.beanstringnew.Model.Model_Product;
import com.beanstringnew.R;
import com.beanstringnew.Shared.Pref_Master;
import com.bumptech.glide.Glide;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by admin on 12/1/2016.
 */

public class Adapter_Serach_mall extends BaseAdapter {

   Pref_Master pref;
   Context context;
    LayoutInflater layoutInflater;
    ViewHolder holder;
    ArrayList<Model_Product> array = new ArrayList<>();


    public Adapter_Serach_mall(Context context, ArrayList<Model_Product> array, Pref_Master pref) {
        this.context = context;
        this.pref = pref;
        this.array = array;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
        holder = new Adapter_Serach_mall.ViewHolder();
        final Model_Product model = array.get(position);

        LayoutInflater inflater = LayoutInflater.from(context);
        if (v == null) {
            v = LayoutInflater.from(context).inflate(R.layout.row_search_layout, null);

            holder.p_image = (ImageView) v.findViewById(R.id.p_image);
            holder.p_name = (TextView) v.findViewById(R.id.p_name);
            holder.p_beans = (TextView) v.findViewById(R.id.p_beans);
            holder.details = (TextView) v.findViewById(R.id.details);
            holder.search_mall = (LinearLayout) v.findViewById(R.id.search_mall);
            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }
        holder.p_name.setText(model.getName());
        holder.p_beans.setText(model.getBeans());

        Glide.with(context).load(model.getImg()).into(holder.p_image);
        holder.details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, Product_details_activity.class);
                i.putExtra("id", model.getId());
                i.putExtra("catid", model.getCatid());
                context.startActivity(i);
            }
        });
        holder.search_mall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, Product_details_activity.class);
                i.putExtra("id", model.getId());
                i.putExtra("catid", model.getCatid());
                context.startActivity(i);
            }
        });
        return v;
    }

    private class ViewHolder {
        ImageView p_image;
        TextView p_name, p_beans, details;
        LinearLayout search_mall;
    }
}
