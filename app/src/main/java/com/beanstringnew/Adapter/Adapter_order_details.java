package com.beanstringnew.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.beanstringnew.Model.Model_Product;
import com.beanstringnew.R;
import com.beanstringnew.Shared.Pref_Master;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by Admin on 8/31/2017.
 */

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

import java.util.ArrayList;


/**
 * Created by Admin on 5/31/2017.
 */

public class Adapter_order_details extends RecyclerView.Adapter<Adapter_order_details.Viewholder> {

    Context context;
    ArrayList<Model_Product> arrayList = new ArrayList<>();
    Pref_Master pref;


    public Adapter_order_details(Context context, ArrayList<Model_Product> arraylist, Pref_Master pref) {
        super();
        this.context = context;
        this.arrayList = arraylist;
        this.pref = pref;
    }

    @Override
    public Adapter_order_details.Viewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_order_details, parent, false);
        return new Viewholder(itemView);
    }

    @Override
    public void onBindViewHolder(Adapter_order_details.Viewholder holder, int position) {
        final Model_Product model = arrayList.get(position);

        Glide.with(context).load(model.getImg()).into(holder.order_image);
        holder.pro_name.setText(model.getName());
        holder.pro_beans.setText(model.getBeans());


    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    public class Viewholder extends RecyclerView.ViewHolder {

        ImageView order_image;
        TextView pro_name;
        TextView pro_beans;


        public Viewholder(View view) {
            super(view);

            order_image = (ImageView) view.findViewById(R.id.order_image);
            pro_name = (TextView) view.findViewById(R.id.pro_name);
            pro_beans = (TextView) view.findViewById(R.id.pro_beans);

        }
    }
}


