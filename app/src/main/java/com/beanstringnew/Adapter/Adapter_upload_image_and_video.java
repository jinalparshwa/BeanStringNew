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
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.beanstringnew.Activity.Add_photo_activity;
import com.beanstringnew.Controller.Configr;
import com.beanstringnew.Controller.Connection;
import com.beanstringnew.Controller.DialogBox;
import com.beanstringnew.Json_model.JSON;
import com.beanstringnew.Model.Model_upload_image_and_video;
import com.beanstringnew.Model.Model_user;
import com.beanstringnew.R;
import com.beanstringnew.Shared.Pref_Master;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Abc on 10/24/2016.
 */

public class Adapter_upload_image_and_video extends BaseAdapter {

    private final Context context;
    ArrayList<Model_upload_image_and_video> array;
    Runnable api_f;
    Pref_Master pref;

    public Adapter_upload_image_and_video(Context context, ArrayList<Model_upload_image_and_video> array, Runnable api_f, Pref_Master pref) {
        super();
        this.context = context;
        this.array = array;
        this.api_f = api_f;
        this.pref = pref;
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
            v = LayoutInflater.from(context).inflate(R.layout.row_upload_video_and_image, null);
            holder = new ViewHolder(v);
            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }


        final Model_upload_image_and_video model = array.get(position);

        if (model.getStatus().equals("1")) {
            holder.iv_play.setVisibility(View.VISIBLE);
        } else {
            holder.iv_play.setVisibility(View.GONE);
        }

        Log.e("lenght of vikas", ":" + array.size());
        if (position == 0) {
            holder.del_post.setVisibility(View.GONE);
            holder.second_image_view.setVisibility(View.VISIBLE);
            holder.main_image_view.setVisibility(View.GONE);
            holder.ll.setVisibility(View.GONE);
            holder.progressBar.setVisibility(View.GONE);
        } else {
            holder.del_post.setVisibility(View.VISIBLE);
            holder.ll.setVisibility(View.VISIBLE);
            holder.second_image_view.setVisibility(View.GONE);
            holder.main_image_view.setVisibility(View.VISIBLE);
            if (model.getStatus().equals("0")) {
                Log.e("Enter_0_status", "0");
                Glide.with(context).load(model.getUrl()).centerCrop().listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        holder.progressBar.setVisibility(View.GONE);
                        Log.e("Exception", "Exception");
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        holder.progressBar.setVisibility(View.GONE);
                        Log.e("Suceess", "Success");
                        return false;
                    }
                }).into(holder.main_image_view);
            } else if (model.getStatus().equals("1")) {
                Log.e("Enter_1_status", "1");
                holder.progressBar.setVisibility(View.GONE);
                Glide.with(context).load(model.getThumb()).centerCrop().into(holder.main_image_view);
            }
            //holder.rr_main.setVisibility((model.getStatus().equals("2") ? View.GONE : View.VISIBLE));
            holder.tot_beans.setText(model.getTot_beans());
        }
        holder.second_image_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, Add_photo_activity.class);
                context.startActivity(i);
            }
        });

        holder.del_post.setOnClickListener(new View.OnClickListener() {
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
        return v;
    }


    private class ViewHolder {
        ImageView second_image_view;
        ImageView main_image_view;
        TextView tot_beans;
        LinearLayout ll;
        RelativeLayout rr_main;
        ProgressBar progressBar;
        ImageView iv_play;
        ImageView del_post;


        public ViewHolder(View v) {

            second_image_view = (ImageView) v.findViewById(R.id.second_image_view);
            main_image_view = (ImageView) v.findViewById(R.id.main_image_view);

            iv_play = (ImageView) v.findViewById(R.id.iv_play);

            rr_main = (RelativeLayout) v.findViewById(R.id.rr_main);
            tot_beans = (TextView) v.findViewById(R.id.tot_beans);
            ll = (LinearLayout) v.findViewById(R.id.ll);
            progressBar = (ProgressBar) v.findViewById(R.id.progressBar);
            del_post = (ImageView) v.findViewById(R.id.del_post);

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
                       // Toast.makeText(context, res_msg, Toast.LENGTH_SHORT).show();
                        api_f.run();

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