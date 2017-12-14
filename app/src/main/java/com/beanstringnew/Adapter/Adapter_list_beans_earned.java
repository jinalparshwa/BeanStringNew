package com.beanstringnew.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.beanstringnew.Activity.Open_photo_video_activity;
import com.beanstringnew.Controller.Configr;
import com.beanstringnew.Controller.Connection;
import com.beanstringnew.Controller.DialogBox;
import com.beanstringnew.Json_model.JSON;
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

public class Adapter_list_beans_earned extends BaseAdapter {

    private final Context context;
    ArrayList<Model_user> array = new ArrayList<>();

    Pref_Master pref;
    LayoutInflater layoutInflater;
    ViewHolder holder;
    Runnable api_cc;


    public Adapter_list_beans_earned(Context context, ArrayList<Model_user> array, Pref_Master pref, Runnable api_cc) {
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
            v = inflater.inflate(R.layout.row_list_beans_earned, null);

            holder.bean_profile = (ImageView) v.findViewById(R.id.bean_profile);
            holder.beans_comment = (TextView) v.findViewById(R.id.beans_comment);
            holder.comm_beans = (TextView) v.findViewById(R.id.comm_beans);
            holder.ll_earned = (LinearLayout) v.findViewById(R.id.ll_earned);
            holder.comment_time = (TextView) v.findViewById(R.id.comment_time);
            holder.del_comm = (ImageView) v.findViewById(R.id.del_comm);


            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }

        holder.comment_time.setText(model.getLast_comment_time() + "ago");

        holder.ll_earned.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, Open_photo_video_activity.class);
                i.putExtra("postid", model.getPostid());
                i.putExtra("del_value", "0");
                //i.putExtra("postuserid", model.getPostuserid());
                context.startActivity(i);
            }
        });

        holder.beans_comment.setText(model.getBeans_comment());
        holder.comm_beans.setText(model.getTotal_beans());
        if (model.getBeans_profile().equals("")) {
            holder.bean_profile.setImageResource(R.drawable.default_imggg);
        } else {
            Glide.with(context).load(model.getBeans_profile()).into(holder.bean_profile);
        }

        holder.del_comm.setOnClickListener(new View.OnClickListener() {
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
                txt.setText("Are you sure you want to Delete?");

                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Delete_post(model.getCommentid());
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
        ImageView bean_profile;
        TextView beans_comment;
        TextView comm_beans;
        LinearLayout ll_earned;
        ImageView del_comm;
        TextView comment_time;

    }

    public void Delete_post(String postid) {

        ArrayList<Model_user> array1 = new ArrayList<>();
        Model_user model = new Model_user();
        model.setCommentid(postid);
        array1.add(model);

        // obj_dialog.show();
        String url = Configr.app_url + "deletecomment";
        String json = "";

        json = JSON.add_json(array1, pref, "deletecomment");

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
                      //  Toast.makeText(context, res_msg, Toast.LENGTH_SHORT).show();
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
