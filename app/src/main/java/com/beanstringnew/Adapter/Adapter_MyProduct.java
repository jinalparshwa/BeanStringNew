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
import com.beanstringnew.Controller.Acitvity_comunator;
import com.beanstringnew.Controller.Configr;
import com.beanstringnew.Controller.Connection;
import com.beanstringnew.Json_model.JSON;
import com.beanstringnew.Model.Model_Product;
import com.beanstringnew.R;
import com.beanstringnew.Shared.Pref_Master;
import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by admin on 11/29/2016.
 */

public class Adapter_MyProduct extends BaseAdapter {

    Pref_Master pref;
    Context context;
    LayoutInflater layoutInflater;
    ViewHolder holder;
    ArrayList<Model_Product> array = new ArrayList<>();

    public Adapter_MyProduct(Context context, ArrayList<Model_Product> array, Pref_Master pref) {
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
        holder = new Adapter_MyProduct.ViewHolder();
        final Model_Product model = array.get(position);

        LayoutInflater inflater = LayoutInflater.from(context);
        if (v == null) {
            v = LayoutInflater.from(context).inflate(R.layout.row_myproduct_adapter, null);

            holder.p_image = (ImageView) v.findViewById(R.id.p_image);
            holder.buynow = (ImageView) v.findViewById(R.id.buynow);
            holder.p_name = (TextView) v.findViewById(R.id.p_name);
            holder.p_beans = (TextView) v.findViewById(R.id.p_beans);
            holder.ll_product = (LinearLayout) v.findViewById(R.id.ll_product);

            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }


        holder.p_name.setText(model.getName());
        holder.p_beans.setText(model.getBeans());

        if (model.getImg().equals("")) {
            holder.p_image.setImageResource(R.drawable.no_image);
        } else {
            Picasso.with(context).load(model.getImg()).skipMemoryCache().into(holder.p_image);
        }
        holder.ll_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, Product_details_activity.class);
                i.putExtra("id", model.getId());
                i.putExtra("catid", model.getCatid());
                context.startActivity(i);

            }
        });
        holder.buynow.setOnClickListener(new View.OnClickListener() {
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

        ImageView p_image, buynow;
        TextView p_name, p_beans;
        LinearLayout ll_product;


    }
}
