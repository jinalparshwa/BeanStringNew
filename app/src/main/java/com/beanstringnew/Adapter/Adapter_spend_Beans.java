package com.beanstringnew.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beanstringnew.Activity.Product_details_activity;
import com.beanstringnew.Activity.ViewOrder_activity;
import com.beanstringnew.Model.Model_Product;
import com.beanstringnew.R;
import com.beanstringnew.Shared.Pref_Master;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by admin on 12/16/2016.
 */

public class Adapter_spend_Beans extends BaseAdapter {
    Pref_Master pref;
    Context context;
    LayoutInflater layoutInflater;
    ViewHolder holder;
    ArrayList<Model_Product> array;
    ArrayList<Model_Product> Array_user = new ArrayList<>();
    Typeface typeFace_Rupee;

    public Adapter_spend_Beans(Context context, ArrayList<Model_Product> array, Pref_Master pref) {
        this.context = context;
        this.pref = pref;
        this.array = array;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        typeFace_Rupee = Typeface.createFromAsset(context.getAssets(), "Rupee.ttf");

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
        final Model_Product model = array.get(position);

        LayoutInflater inflater = LayoutInflater.from(context);
        if (v == null) {
            v = LayoutInflater.from(context).inflate(R.layout.row_spend_list_product, null);

            holder.id = (TextView) v.findViewById(R.id.id);
            holder.order_no = (TextView) v.findViewById(R.id.order_no);
            holder.txt_beans = (TextView) v.findViewById(R.id.txt_beans);
            holder.txt_date = (TextView) v.findViewById(R.id.txt_date);
            holder.txt_time = (TextView) v.findViewById(R.id.txt_time);
            holder.ll_order_list = (LinearLayout) v.findViewById(R.id.ll_order_list);

            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }

        holder.order_no.setText(model.getOrder_id());
        holder.txt_beans.setText(model.getBeans());
        holder.txt_date.setText(model.getDate());
        holder.txt_time.setText(model.getTime());
        holder.id.setText("" + (position + 1) + ".");

        holder.ll_order_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, ViewOrder_activity.class);
                i.putExtra("Order_no", model.getId());
                context.startActivity(i);
            }
        });

        return v;
    }


    private class ViewHolder {

        TextView order_no;
        TextView txt_beans;
        TextView txt_date;
        TextView txt_time;
        TextView id;
        LinearLayout ll_order_list;

    }

}
