package com.beanstringnew.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beanstringnew.Activity.Product_details_activity;
import com.beanstringnew.Model.Model_Product;
import com.beanstringnew.R;
import com.beanstringnew.Shared.Pref_Master;
import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Adapter_home_product_list extends RecyclerView.Adapter<Adapter_home_product_list.ViewHolder> {


    Pref_Master pref;
    Context context;
    LayoutInflater layoutInflater;
    ArrayList<Model_Product> array;

    public Adapter_home_product_list(Context context, ArrayList<Model_Product> array, Pref_Master pref) {
        this.context = context;
        this.pref = pref;
        this.array = array;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public Adapter_home_product_list.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_list_home_product, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Model_Product model = array.get(position);

        holder.product_name.setText(model.getName());
        holder.beans.setText(model.getBeans());
        if (model.getImg().equals("")) {
            holder.product_image.setImageResource(R.drawable.no_image);
        } else {
            Glide.with(context).load(model.getImg()).skipMemoryCache(true).into(holder.product_image);
        }

        holder.buynow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, Product_details_activity.class);
                i.putExtra("id", model.getId());
                i.putExtra("catid", model.getCatid());
                context.startActivity(i);
            }
        });

        holder.ll_home_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, Product_details_activity.class);
                i.putExtra("id", model.getId());
                i.putExtra("catid", model.getCatid());
                context.startActivity(i);
            }
        });


    }

    @Override
    public long getItemId(int i) {
        return 0;
    }


    @Override
    public int getItemCount() {
        return array.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        ImageView product_image;
        TextView beans;
        TextView product_name;
        ImageView buynow;
        LinearLayout ll_home_product;

        public ViewHolder(View v) {
            super(v);


            product_image = (ImageView) v.findViewById(R.id.product_image);
            product_name = (TextView) v.findViewById(R.id.product_name);
            beans = (TextView) v.findViewById(R.id.beans);
            buynow = (ImageView) v.findViewById(R.id.buynow);
            ll_home_product = (LinearLayout) v.findViewById(R.id.ll_home_product);

        }

    }


}