package com.beanstringnew.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beanstringnew.Activity.ViewOrder_activity;
import com.beanstringnew.Model.Model_Paying_order_list;
import com.beanstringnew.Model.Model_user;
import com.beanstringnew.R;
import com.beanstringnew.Shared.Pref_Master;

import java.util.ArrayList;

/**
 * Created by admin on 11/28/2016.
 */

public class Paying_Order_adapter extends BaseAdapter {

    Context context;
    ArrayList<Model_Paying_order_list> array;

    Pref_Master pref;

    LayoutInflater layoutInflater;
    ViewHolder holder;

    public Paying_Order_adapter(Context context, ArrayList<Model_Paying_order_list> array, Pref_Master pref) {
        super();
        this.context = context;
        this.array = array;
        this.pref = pref;
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
        holder = new ViewHolder();
        final Model_Paying_order_list model = array.get(position);

        LayoutInflater inflater = LayoutInflater.from(context);
        if (v == null) {
            v = LayoutInflater.from(context).inflate(R.layout.row_paying_order_layout, null);

            holder.order_number = (TextView) v.findViewById(R.id.order_number);
            holder.order_date = (TextView) v.findViewById(R.id.order_date);
            holder.ll_orders = (LinearLayout) v.findViewById(R.id.ll_orders);
            holder.txtnumber = (TextView) v.findViewById(R.id.txtnumber);

            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }

        holder.txtnumber.setText("" + (position + 1) + ".");
        holder.order_number.setText(model.getOrder_number());
        holder.order_date.setText(model.getOrder_date());
        holder.ll_orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, ViewOrder_activity.class);
                i.putExtra("Order_no", model.getOrderid());
                context.startActivity(i);
            }
        });

        return v;
    }

    private class ViewHolder {

        TextView order_number, order_date;
        LinearLayout ll_orders;
        TextView txtnumber;

    }
}
