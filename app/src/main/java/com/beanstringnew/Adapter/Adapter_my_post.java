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
import com.beanstringnew.Model.Model_upload_image_and_video;
import com.beanstringnew.Model.Model_user;
import com.beanstringnew.R;
import com.beanstringnew.Shared.Pref_Master;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by admin on 12/20/2016.
 */

public class Adapter_my_post extends BaseAdapter {

    private final Context context;
    ArrayList<Model_upload_image_and_video> array;
    Pref_Master pref;
    LayoutInflater layoutInflater;
    Runnable api_cc;

    public Adapter_my_post(Context context, ArrayList<Model_upload_image_and_video> array, Runnable api_cc) {
        super();
        this.context = context;
        this.array = array;
        this.api_cc = api_cc;
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


        final ViewHolder holder;


        if (v == null) {
            v = LayoutInflater.from(context).inflate(R.layout.row_my_post_list, null);
            holder = new ViewHolder(v);
            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }

        final Model_upload_image_and_video model = array.get(position);


        holder.txtpost.setText(model.getDescription());
        holder.time_post.setText(model.getTime() + "ago");
        holder.tott_beans.setText(model.getTot_beans());

        holder.img_delete.setOnClickListener(new View.OnClickListener() {
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
                        Delete_post(model.getPostid());
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
        holder.ll_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, Open_photo_video_activity.class);
                i.putExtra("postid", model.getPostid());
                i.putExtra("del_value", "0");
                context.startActivity(i);
            }
        });

        return v;
    }

    private class ViewHolder {
        TextView txtpost;
        TextView time_post;
        TextView tott_beans;
        ImageView img_delete;
        LinearLayout ll_post;

        public ViewHolder(View v) {
            txtpost = (TextView) v.findViewById(R.id.txtpost);
            time_post = (TextView) v.findViewById(R.id.time_post);
            tott_beans = (TextView) v.findViewById(R.id.tott_beans);
            img_delete = (ImageView) v.findViewById(R.id.img_delete);
            ll_post = (LinearLayout) v.findViewById(R.id.ll_post);


        }
    }

    public void Delete_post(String postid) {

        ArrayList<Model_user> array1 = new ArrayList<>();
        Model_user model = new Model_user();
        model.setPostid(postid);
        array1.add(model);

        // obj_dialog.show();
        String url = Configr.app_url + "deletepost";
        String json = "";

        json = JSON.add_json(array1, pref, "deletepost");

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
