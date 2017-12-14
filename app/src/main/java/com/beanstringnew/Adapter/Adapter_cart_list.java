package com.beanstringnew.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
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
import com.beanstringnew.Controller.Activity_cart_count;
import com.beanstringnew.Controller.Configr;
import com.beanstringnew.Controller.Connection;
import com.beanstringnew.Controller.DialogBox;
import com.beanstringnew.Json_model.JSON;
import com.beanstringnew.Model.Model_mycart;
import com.beanstringnew.Model.Model_search;
import com.beanstringnew.R;
import com.beanstringnew.Shared.Pref_Master;
import com.bumptech.glide.Glide;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Abc on 11/9/2016.
 */

public class Adapter_cart_list extends BaseAdapter {

    Context context;
    LayoutInflater layoutInflater;
    Adapter_cart_list.ViewHolder holder;
    ArrayList<Model_mycart> array;
    Typeface typeFace_Rupee;
    Pref_Master pref;
    Runnable api_cc;

    public Adapter_cart_list(Context context, ArrayList<Model_mycart> array, Runnable api_cc) {
        this.context = context;
        this.array = array;
        this.api_cc = api_cc;
        pref = new Pref_Master(context);
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
        holder = new Adapter_cart_list.ViewHolder();
        final Model_mycart model = array.get(position);

        LayoutInflater inflater = LayoutInflater.from(context);
        if (v == null) {
            v = LayoutInflater.from(context).inflate(R.layout.row_cart_product_list, null);

            holder.cart_image = (ImageView) v.findViewById(R.id.cart_image);
            holder.cart_product_name = (TextView) v.findViewById(R.id.cart_product_name);
            holder.cart_beans = (TextView) v.findViewById(R.id.cart_beans);
            holder.remove = (ImageView) v.findViewById(R.id.remove);
            holder.subtotal = (TextView) v.findViewById(R.id.subtotal);
            v.setTag(holder);
        } else {
            holder = (Adapter_cart_list.ViewHolder) v.getTag();
        }

        holder.cart_product_name.setText(model.getName());
        holder.cart_beans.setText(model.getBeans());
        if (model.getImage().equals("")) {
            holder.cart_image.setImageResource(R.drawable.no_image);
        } else {
            Glide.with(context).load(model.getImage()).into(holder.cart_image);
        }

        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater li = LayoutInflater.from(context);
                v = li.inflate(R.layout.alert_popup, null);
                final android.support.v7.app.AlertDialog alert = new android.support.v7.app.AlertDialog.Builder(context).setView(v).show();
                alert.setCancelable(false);
                alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                TextView ok = (TextView) v.findViewById(R.id.con_ok);
                TextView cancel = (TextView) v.findViewById(R.id.con_cancel);
                TextView txt = (TextView) v.findViewById(R.id.txt);
                txt.setText("Are you sure you want to Remove Product from Cart?");

                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Remove_cart(model.getCartid());
                        alert.dismiss();
                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alert.dismiss();
                    }
                });

            }
        });

        return v;
    }


    private class ViewHolder {

        ImageView cart_image;
        TextView cart_product_name;
        TextView cart_beans;
        ImageView remove;
        TextView subtotal;

    }

    public void Remove_cart(String cartid) {

        ArrayList<Model_mycart> array1 = new ArrayList<>();
        Model_mycart model = new Model_mycart();
        model.setCartid(cartid);
        array1.add(model);

        // obj_dialog.show();
        String url = Configr.app_url + "removetocart";
        String json = "";

        json = JSON.addd(array1, pref, "removetocart");

        HashMap<String, String> param = new HashMap<>();
        param.put("data", json);


        Response.Listener<String> lis_pat = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response", response);
                // obj_dialog.dismiss();

                String toastMsg = "";
                toastMsg = "";


                try {
                    JSONObject jobj = new JSONObject(response);
                    String res_flag = jobj.getString("status");
                    String res_msg = jobj.getString("message");
                    String otp = "", userid = "";

                    if (res_flag.equals("200")) {
                        //Toast.makeText(context, res_msg, Toast.LENGTH_SHORT).show();
                        Activity_cart_count.cart_count(context, pref);
                        api_cc.run();

                    }
                    else {
                        DialogBox.alert_popup(context, res_msg);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        };


        Response.ErrorListener lis_error = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //  signup.setClickable(true);
                Toast.makeText(context, "" + Configr.Message, Toast.LENGTH_SHORT).show();
            }
        };
        Connection.postconnection(url, param, Configr.getHeaderParam(), context, lis_pat, lis_error);
    }


}
