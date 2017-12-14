package com.beanstringnew.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.beanstringnew.Activity.Comment_Display_activity;
import com.beanstringnew.Activity.Other_Profile_activity;
import com.beanstringnew.Activity.Photo_activity;
import com.beanstringnew.Activity.Play_video_activity;
import com.beanstringnew.Controller.Acitvity_comunator;
import com.beanstringnew.Controller.Activity_indicator;
import com.beanstringnew.Controller.Configr;
import com.beanstringnew.Controller.Connection;
import com.beanstringnew.Controller.DialogBox;
import com.beanstringnew.Fragment.Latest_Fragment;
import com.beanstringnew.Json_model.JSON;
import com.beanstringnew.Model.Model_Play_video;
import com.beanstringnew.Model.Model_Product;
import com.beanstringnew.Model.Model_Suggest;
import com.beanstringnew.Model.Model_profile;
import com.beanstringnew.Model.Model_user;
import com.beanstringnew.R;
import com.beanstringnew.Shared.Pref_Master;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.model.ShareVideo;
import com.facebook.share.model.ShareVideoContent;
import com.facebook.share.widget.ShareDialog;
import com.google.android.gms.plus.PlusShare;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Nirav on 10/29/2016.
 */

public class Adapter_Latest_post extends BaseAdapter {

    private final Context context;
    ArrayList<Model_user> array;
    Pref_Master pref;

    LayoutInflater layoutInflater;
    ViewHolder holder;
    Latest_Fragment latest_fragment;
    int value;
    Drawable perk_active, perk_like;
    String post_url = "";
    Adapter_suggested_post adapter_suggested_post;
    ArrayList<Model_Suggest> array_suggest = new ArrayList<>();
    Activity_indicator obj_dialog;

    public Adapter_Latest_post(Context context, ArrayList<Model_user> array, Pref_Master pref, Latest_Fragment latest_fragment) {
        super();
        this.context = context;
        this.array = array;
        this.pref = pref;
        this.latest_fragment = latest_fragment;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        perk_active = context.getResources().getDrawable(R.drawable.unlike);
        perk_like = context.getResources().getDrawable(R.drawable.like);
        obj_dialog = new Activity_indicator(context);
        obj_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));

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
        Log.e("Size of array", "" + array.size());


        final Model_user model = array.get(position);


        LayoutInflater inflater = LayoutInflater.from(context);
        if (v == null) {
            v = LayoutInflater.from(context).inflate(R.layout.row_latest_post_list, null);

            holder.lat_displayimage = (ImageView) v.findViewById(R.id.lat_displayimage);
            holder.lat_profile = (ImageView) v.findViewById(R.id.lat_profile);
            holder.lat_list_upload = (ImageView) v.findViewById(R.id.lat_list_upload);
            holder.lat_comment_image = (ImageView) v.findViewById(R.id.lat_comment_image);


            holder.img_share_bean = (ImageView) v.findViewById(R.id.img_share_bean);
            holder.img_share_fb = (ImageView) v.findViewById(R.id.img_share_fb);
            holder.img_share_twitter = (ImageView) v.findViewById(R.id.img_share_twitter);
            holder.img_share_gplus = (ImageView) v.findViewById(R.id.img_share_gplus);

            holder.lat_fname = (TextView) v.findViewById(R.id.lat_fname);
            holder.lat_time = (TextView) v.findViewById(R.id.lat_time);
            holder.lat_post = (TextView) v.findViewById(R.id.lat_post);
            holder.lat_comment = (TextView) v.findViewById(R.id.lat_comment);
            holder.tot_beans = (TextView) v.findViewById(R.id.tot_beans);
            holder.profile_first = (ImageView) v.findViewById(R.id.profile_first);
            holder.profile_second = (ImageView) v.findViewById(R.id.profile_second);
            holder.profile_third = (ImageView) v.findViewById(R.id.profile_third);


            holder.first_comment = (TextView) v.findViewById(R.id.first_comment);
            holder.time_first = (TextView) v.findViewById(R.id.time_first);


            holder.second_comment = (TextView) v.findViewById(R.id.second_comment);
            holder.time_second = (TextView) v.findViewById(R.id.time_second);


            holder.third_comment = (TextView) v.findViewById(R.id.third_comment);
            holder.time_third = (TextView) v.findViewById(R.id.time_third);

            holder.ll_first = (LinearLayout) v.findViewById(R.id.ll_first);
            holder.ll_second = (LinearLayout) v.findViewById(R.id.ll_second);
            holder.ll_third = (LinearLayout) v.findViewById(R.id.ll_third);
            holder.open_comment = (LinearLayout) v.findViewById(R.id.open_comment);

            holder.comment_count = (TextView) v.findViewById(R.id.comment_count);
            holder.share_count = (TextView) v.findViewById(R.id.share_count);
            holder.rr_lat = (RelativeLayout) v.findViewById(R.id.rr_lat);
            holder.ll_beans = (LinearLayout) v.findViewById(R.id.ll_beans);
            holder.ll_comment = (LinearLayout) v.findViewById(R.id.ll_comment);
            holder.tott_beans = (TextView) v.findViewById(R.id.tott_beans);
            holder.likes = (TextView) v.findViewById(R.id.likes);
            holder.img_like = (ImageView) v.findViewById(R.id.img_like);

            holder.fname = (TextView) v.findViewById(R.id.fname);
            holder.sname = (TextView) v.findViewById(R.id.sname);
            holder.tname = (TextView) v.findViewById(R.id.tname);


            holder.iv_play = (ImageView) v.findViewById(R.id.iv_play);

            holder.progressBar = (ProgressBar) v.findViewById(R.id.progressBar);

            holder.suggested_post = (RecyclerView) v.findViewById(R.id.suggested_post);
            holder.ll_suggest = (LinearLayout) v.findViewById(R.id.ll_suggest);
            holder.textss = (TextView) v.findViewById(R.id.textss);

            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }

        holder.ll_first.setVisibility(View.GONE);
        holder.ll_second.setVisibility(View.GONE);
        holder.ll_third.setVisibility(View.GONE);
        holder.ll_comment.setVisibility(View.GONE);


        if (position == 1) {
            getSponsor();
            holder.suggested_post.setVisibility(View.VISIBLE);
            holder.textss.setVisibility(View.VISIBLE);
            holder.suggested_post.setHasFixedSize(true);
            holder.suggested_post.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            holder.suggested_post.setNestedScrollingEnabled(false);

        } else {
            holder.suggested_post.setVisibility(View.GONE);
            holder.textss.setVisibility(View.GONE);
        }


        Log.e("vikas_status", "::::::::" + model.getStatus());
        if (model.getStatus().equals("1")) {
            holder.iv_play.setVisibility(View.VISIBLE);
            Log.e("vikas_status", "if::::" + model.getStatus());
        } else {
            holder.iv_play.setVisibility(View.GONE);
            Log.e("vikas_status", "else::::" + model.getStatus());
        }
        Log.e("fname", model.getFname());

        if (model.getDescription().equals("")) {
            holder.lat_post.setVisibility(View.GONE);
        } else {
            holder.lat_post.setVisibility(View.VISIBLE);
        }
        holder.lat_fname.setText(model.getFname() + " " + model.getLname());

//

        holder.lat_post.setText(model.getDescription());
        holder.lat_time.setText(model.getTime() + " ago");
        holder.tot_beans.setText(model.getTotal_beans());
        holder.progressBar.setVisibility(View.VISIBLE);

        if (model.getStatus().equals("2")) {
            holder.rr_lat.setVisibility(View.GONE);
            holder.ll_beans.setVisibility(View.VISIBLE);
            holder.tott_beans.setText(model.getTotal_beans());
        } else {
            holder.rr_lat.setVisibility(View.VISIBLE);
            holder.ll_beans.setVisibility(View.GONE);
        }

        try {
            if (model.getComments().size() != 0) {
                if (model.getComments().size() == 1) {
                    holder.fname.setText(model.getComments().get(0).getComment_fname() + " " + model.getComments().get(0).getComment_lname());
                    holder.first_comment.setText(model.getComments().get(0).getComment());
                    holder.time_first.setText(model.getComments().get(0).getCommenttime() + "ago");
                    if (model.getComments().get(0).getComment_propic().equals("")) {
                        holder.profile_first.setImageResource(R.drawable.default_imggg);
                    } else {
                        Glide.with(context).load(model.getComments().get(0).getComment_propic()).into(holder.profile_first);
                    }

                } else if (model.getComments().size() == 2) {
                    holder.fname.setText(model.getComments().get(0).getComment_fname() + " " + model.getComments().get(0).getComment_lname());
                    holder.first_comment.setText(model.getComments().get(0).getComment());
                    holder.time_first.setText(model.getComments().get(0).getCommenttime() + "ago");

                    if (model.getComments().get(0).getComment_propic().equals("")) {
                        holder.profile_first.setImageResource(R.drawable.default_imggg);
                    } else {
                        Glide.with(context).load(model.getComments().get(0).getComment_propic()).into(holder.profile_first);
                    }
                    holder.sname.setText(model.getComments().get(1).getComment_fname() + " " + model.getComments().get(1).getComment_lname());
                    holder.second_comment.setText(model.getComments().get(1).getComment());
                    holder.time_second.setText(model.getComments().get(1).getCommenttime() + "ago");

                    if (model.getComments().get(1).getComment_propic().equals("")) {
                        holder.profile_second.setImageResource(R.drawable.default_imggg);
                    } else {
                        Glide.with(context).load(model.getComments().get(1).getComment_propic()).into(holder.profile_second);
                    }

                } else if (model.getComments().size() == 3) {
                    holder.fname.setText(model.getComments().get(0).getComment_fname() + " " + model.getComments().get(0).getComment_lname());
                    holder.first_comment.setText(model.getComments().get(0).getComment());
                    holder.time_first.setText(model.getComments().get(0).getCommenttime() + "ago");
                    if (model.getComments().get(0).getComment_propic().equals("")) {
                        holder.profile_first.setImageResource(R.drawable.default_imggg);
                    } else {
                        Glide.with(context).load(model.getComments().get(0).getComment_propic()).into(holder.profile_first);
                    }
                    holder.sname.setText(model.getComments().get(1).getComment_fname() + " " + model.getComments().get(1).getComment_lname());
                    holder.second_comment.setText(model.getComments().get(1).getComment());
                    holder.time_second.setText(model.getComments().get(1).getCommenttime() + "ago");
                    if (model.getComments().get(1).getComment_propic().equals("")) {
                        holder.profile_second.setImageResource(R.drawable.default_imggg);
                    } else {
                        Glide.with(context).load(model.getComments().get(1).getComment_propic()).into(holder.profile_second);
                    }

                    holder.tname.setText(model.getComments().get(2).getComment_fname() + " " + model.getComments().get(2).getComment_lname());
                    holder.third_comment.setText(model.getComments().get(2).getComment());
                    holder.time_third.setText(model.getComments().get(2).getCommenttime() + "ago");
                    if (model.getComments().get(2).getComment_propic().equals("")) {
                        holder.profile_third.setImageResource(R.drawable.default_imggg);
                    } else {
                        Glide.with(context).load(model.getComments().get(2).getComment_propic()).into(holder.profile_third);
                    }
                }

            } else {
                holder.ll_first.setVisibility(View.GONE);
                holder.ll_second.setVisibility(View.GONE);
                holder.ll_third.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.comment_count.setText(model.getComment_cunt());
        holder.share_count.setText(model.getShare_cunt());
        holder.likes.setText(model.getLike());


        if (pref.getStr_profile().equals("")) {
            holder.lat_comment_image.setImageResource(R.drawable.default_imggg);
        } else {
            Glide.with(context).load(pref.getStr_profile()).into(holder.lat_comment_image);
        }

        if (model.getPic().equals("")) {
            holder.lat_profile.setImageResource(R.drawable.default_imggg);
        } else {
            Glide.with(context).load(model.getPic()).into(holder.lat_profile);
        }

        holder.lat_fname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, Other_Profile_activity.class);
                i.putExtra("id", model.getUserid());
                context.startActivity(i);
            }
        });
        holder.fname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, Other_Profile_activity.class);
                i.putExtra("id", model.getComments().get(0).getUserid());
                context.startActivity(i);
            }
        });
        holder.sname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, Other_Profile_activity.class);
                i.putExtra("id", model.getComments().get(1).getUserid());
                context.startActivity(i);
            }
        });
        holder.tname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, Other_Profile_activity.class);
                i.putExtra("id", model.getComments().get(2).getUserid());
                context.startActivity(i);
            }
        });


        holder.lat_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, Other_Profile_activity.class);
                i.putExtra("id", model.getUserid());
                context.startActivity(i);
            }
        });


        Log.e("Thumbbbb", ":" + model.getThumb());

        if (model.getThumb().equals("")) {
            holder.lat_list_upload.setImageResource(R.drawable.no_image);
            holder.progressBar.setVisibility(View.GONE);
        } else {
            Glide.with(context).load(model.getThumb()).centerCrop().listener(new RequestListener<String, GlideDrawable>() {
                @Override
                public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                    holder.progressBar.setVisibility(View.GONE);
                    return false;
                }

                @Override
                public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                    holder.progressBar.setVisibility(View.GONE);
                    return false;
                }
            }).into(holder.lat_list_upload);
        }

        if (array.get(position).getMylike().equals("1")) {
            holder.img_like.setImageDrawable(perk_like);
        } else if (array.get(position).getMylike().equals("0")) {
            holder.img_like.setImageDrawable(perk_active);
        }


        holder.img_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (array.get(position).getMylike().equals("0")) {

                    if (value == 1) {

                        value = 0;


                    } else {

                        ImageView imageView = (ImageView) view.findViewById(R.id.img_like);
                        int likcountsss = Integer.parseInt(array.get(position).getLike());
                        int finalLikcountsss = likcountsss + 1;
                        latest_fragment.Likes(model.getPost_id());
                        model.setLike(Integer.toString(finalLikcountsss));
                        model.setMylike("1");
                        notifyDataSetChanged();
                        value = 1;
                        holder.likes.setText(array.get(position).getLike());
                        imageView.setImageDrawable(perk_active);
                    }


                } else if (array.get(position).getMylike().equals("1")) {

                    ImageView imageView = (ImageView) view.findViewById(R.id.img_like);
                    int likcountsss = Integer.parseInt(array.get(position).getLike());
                    int finalLikcountsss = likcountsss - 1;
                    model.setLike(Integer.toString(finalLikcountsss));
                    model.setMylike("0");
                    notifyDataSetChanged();
                    imageView.setImageDrawable(perk_like);
                    latest_fragment.Likes(model.getPost_id());
                }
            }
        });


        holder.lat_list_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (model.getStatus().equals("0")) {
                    Model_Play_video model_play_video = new Model_Play_video();
                    model_play_video.setDescription(model.getDescription());
                    model_play_video.setPostid(model.getPost_id());
                    model_play_video.setPostuserid(model.getUserid());
                    model_play_video.setStatus(model.getStatus());
                    model_play_video.setUrl(model.getUrl());
                    Intent i = new Intent(context, Photo_activity.class);
                    i.putExtra("url", model_play_video);
                    context.startActivity(i);
                    View_post(model.getPost_id());


                } else {
                    Log.e("Click on video", "video");
                    Model_Play_video model_play_video = new Model_Play_video();
                    model_play_video.setDescription(model.getDescription());
                    model_play_video.setPostid(model.getPost_id());
                    model_play_video.setPostuserid(model.getUserid());
                    model_play_video.setStatus(model.getStatus());
                    model_play_video.setUrl(model.getUrl());
                    model_play_video.setDel_valu("0");
                    Intent i = new Intent(context, Play_video_activity.class);
                    i.putExtra("url", model_play_video);
                    context.startActivity(i);
                    View_post(model.getPost_id());
                }
            }
        });


        holder.open_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, Comment_Display_activity.class);
                i.putExtra("post_id", model.getPost_id());
                i.putExtra("post_user_id", model.getUserid());
                context.startActivity(i);
            }
        });
        holder.lat_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(context, Comment_Display_activity.class);
                i.putExtra("post_id", model.getPost_id());
                i.putExtra("post_user_id", model.getUserid());
                context.startActivity(i);

            }
        });

        holder.img_share_bean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                latest_fragment.Check_share(model.getPost_id(), model.getUserid(), "4", model.getDescription(), model.getThumb(), model.getStatus(), model.getUrl());

            }
        });

        holder.img_share_fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean installed = appInstalledOrNot("com.facebook.katana");
                if (installed) {
                    // if (!model.getStatus().equals("2")) {
                    getposturl(model.getPost_id(), model.getUserid(), "1", model.getDescription(), model.getThumb(), model.getStatus(), model.getUrl());

                    //  }
                } else {
                    Toast.makeText(context, "App is not currently installed on your phone", Toast.LENGTH_LONG).show();
                    // System.out.println("App is not currently installed on your phone");
                }
            }
        });

        holder.img_share_gplus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean installed = appInstalledOrNot("com.google.android.apps.plus");
                if (installed) {
                    getposturl(model.getPost_id(), model.getUserid(), "2", model.getDescription(), model.getThumb(), model.getStatus(), model.getUrl());
                } else {
                    Toast.makeText(context, "App is not currently installed on your phone", Toast.LENGTH_LONG).show();
                }
            }


        });

        holder.img_share_twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean installed = appInstalledOrNot("com.twitter.android");
                if (installed) {
                    getposturl(model.getPost_id(), model.getUserid(), "3", model.getDescription(), model.getThumb(), model.getStatus(), model.getUrl());
                } else {
                    Toast.makeText(context, "App is not currently installed on your phone", Toast.LENGTH_LONG).show();
                }
            }
        });

        return v;
    }


    private class ViewHolder {
        ImageView lat_profile;
        ImageView lat_list_upload;
        ImageView lat_displayimage;
        ImageView lat_comment_image;

        ImageView iv_play;

        ImageView profile_first;
        ImageView profile_second;
        ImageView profile_third;

        TextView first_comment;
        TextView time_first;
        TextView fname;
        TextView sname;
        TextView tname;

        TextView second_comment;
        TextView time_second;

        TextView third_comment;
        TextView time_third;


        ImageView img_share_bean;
        ImageView img_share_fb;
        ImageView img_share_twitter;
        ImageView img_share_gplus;


        TextView lat_fname;
        TextView lat_lname;
        TextView lat_time;
        TextView lat_post;
        TextView lat_comment;
        TextView tot_beans;
        LinearLayout ll_first;
        LinearLayout ll_second;
        LinearLayout ll_third;
        LinearLayout open_comment;
        LinearLayout ll_beans;
        LinearLayout ll_comment;

        RelativeLayout rr_lat;

        TextView comment_count;
        TextView share_count;
        TextView tott_beans;
        TextView likes;
        ImageView img_like;

        ProgressBar progressBar;

        LinearLayout ll_suggest;
        RecyclerView suggested_post;
        TextView textss;
    }

    private boolean appInstalledOrNot(String uri) {
        PackageManager pm = context.getPackageManager();
        boolean app_installed;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed;
    }


    public void View_post(String postid) {

        ArrayList<Model_user> array_j = new ArrayList<>();
        Model_user model = new Model_user();
        model.setPost_id(postid);
        // model.setUserid(userid);
        array_j.add(model);

        //obj_dialog.show();

        String url = Configr.app_url + "viewpost";

        String json = "";

        json = JSON.add_json(array_j, pref, "viewpost");

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
                        // api_cc.run();

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

    public void getposturl(final String postid, final String userid, final String share_value, final String description, final String thumb, final String status, final String urll) {


        //obj_dialog.show();

        String url = Configr.app_url + "getposturl";
        JSONObject jobj_loginuser = new JSONObject();
        try {

            JSONObject jobj_row = new JSONObject();

            jobj_row.put("id", postid);

            JSONArray jarray_loginuser = new JSONArray();
            jarray_loginuser.put(jobj_row);

            jobj_loginuser.put("getposturl", jarray_loginuser);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Map<String, String> params = new HashMap<>();
        params.put("data", "" + jobj_loginuser.toString().replaceAll("\\\\", ""));
        Response.Listener<String> lis_res = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("response", " : " + response);
                try {
                    JSONObject jobj = new JSONObject(response);
                    String res_flag = jobj.getString("status");
                    String res_msg = jobj.getString("message");
                    if (res_flag.equals("200")) {


                        JSONArray jarray2 = new JSONArray(jobj.getString("data"));

                        JSONObject jsonObject = jarray2.getJSONObject(0);
                        post_url = jsonObject.getString("url");

                        if (share_value.equals("1")) {

                            latest_fragment.Check_share(postid, userid, "1", description, thumb, status, post_url);
                        } else if (share_value.equals("2")) {
                            latest_fragment.Check_share(postid, userid, "2", description, thumb, status, post_url);

                        } else if (share_value.equals("3")) {
                            latest_fragment.Check_share(postid, userid, "3", description, thumb, status, post_url);
                        }

                    } else {
                        DialogBox.alert_popup(context, res_msg);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }


                // obj_dialog.dismiss();
            }
        };

        Response.ErrorListener lis_error = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("response error", "" + error);
                Toast.makeText(context, "" + Configr.Message, Toast.LENGTH_SHORT).show();
            }
        };
        Connection.postconnection(url, params, Configr.getHeaderParam(), context, lis_res, lis_error);
    }

    public void getSponsor() {

        obj_dialog.show();

        String url = Configr.app_url + "getsponsore";
        String json = "";

        HashMap<String, String> param = new HashMap<>();
        param.put("data", json);
        Log.e("jinal", ":" + param.toString());

        Response.Listener<String> lis_pat = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response", response);
                obj_dialog.dismiss();
                try {
                    JSONObject jobj = new JSONObject(response);
                    String res_flag = jobj.getString("status");
                    String res_msg = jobj.getString("message");
                    if (res_flag.equals("200")) {


                        JSONArray job = new JSONArray(jobj.getString("data"));

                        array_suggest.clear();


                        for (int i = 0; i < job.length(); i++) {
                            Model_Suggest model = new Model_Suggest();
                            JSONObject jobj1 = job.getJSONObject(i);
                            model.setAdv_id(jobj1.getString("advid"));
                            model.setStatus(jobj1.getString("status"));
                            model.setName(jobj1.getString("name"));
                            model.setImage(jobj1.getString("imagename"));
                            model.setVideo(jobj1.getString("videoname"));
                            array_suggest.add(model);
                        }


                        adapter_suggested_post = new Adapter_suggested_post(context, array_suggest);
                        holder.suggested_post.setAdapter(adapter_suggested_post);

                    } else {
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
                obj_dialog.dismiss();
                Toast.makeText(context, "" + Configr.Message, Toast.LENGTH_SHORT).show();
            }
        };
        Connection.postconnection(url, param, Configr.getHeaderParam(), context, lis_pat, lis_error);
    }


}
