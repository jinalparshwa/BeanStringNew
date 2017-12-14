package com.beanstringnew.Fragment;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.beanstringnew.Adapter.Adaptertop_list;
import com.beanstringnew.Controller.Acitvity_comunator;
import com.beanstringnew.Controller.Activity_indicator;
import com.beanstringnew.Controller.Configr;
import com.beanstringnew.Controller.Connection;
import com.beanstringnew.Controller.DialogBox;
import com.beanstringnew.Interface.Share;
import com.beanstringnew.Json_model.JSON;
import com.beanstringnew.Model.Model_Comment;
import com.beanstringnew.Model.Model_profile;
import com.beanstringnew.Model.Model_user;
import com.beanstringnew.Myapplication;
import com.beanstringnew.R;
import com.beanstringnew.Shared.Pref_Master;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Abc on 10/20/2016.
 */

public class Top_Post_Fragment extends Fragment {

    Pref_Master pref;
    Context context;
    ListView toppost;
    Activity_indicator obj_dialog;
    Adaptertop_list adapter;
    ArrayList<Model_user> array = new ArrayList<>();
    ArrayList<Model_user> array_j = new ArrayList<>();
    boolean flag_loading = false;
    ArrayList<Model_profile> array_liist = new ArrayList<>();
    Share share;
    private static final int RECORD_REQUEST_CODE = 1;

    public Top_Post_Fragment(Share share) {
        this.share = share;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.top_fragment, container, false);
        context = getActivity();
        pref = new Pref_Master(context);
        pref.setStr_pos_top("0");
        showPermiosssion();
        obj_dialog = new Activity_indicator(context);
        obj_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));

        toppost = (ListView) v.findViewById(R.id.toppost);
        toppost.setItemsCanFocus(true);

        adapter = new Adaptertop_list(context, array, pref, Top_Post_Fragment.this);
        toppost.setAdapter(adapter);
        Acitvity_comunator.beans_Count(context, obj_dialog, pref, array_liist);

        array.clear();
        Model_user model = new Model_user();
        model.setLimit("0");
        array_j.add(model);
        top_post_api();
        toppost.setOnScrollListener(new AbsListView.OnScrollListener() {

            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {

                if (firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount != 0) {
                    if (!flag_loading) {
                        flag_loading = true;
                        Model_user model = new Model_user();
                        model.setLimit("" + array.size());
                        array_j.add(model);
                        top_post_api();
                        Log.i("Refresh", "Refresh");
                    }
                }
            }
        });

        Myapplication.getInstance().trackScreenView("Top Post Screen");
        return v;
    }

    @Override
    public void onResume() {
        // obj_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));



        super.onResume();

        if (pref.getStr_pos_top().equals("1")) {
            array.clear();
            Model_user model = new Model_user();
            model.setLimit("0");
            array_j.add(model);
            top_post_api();
            pref.setStr_pos_top("0");
        }
        Myapplication.getInstance().trackScreenView("Top Post Screen");
    }

    public void Likes(String postid) {


        //obj_dialog.show();

        String url = Configr.app_url + "like";
        JSONObject jobj_loginuser = new JSONObject();
        try {

            JSONObject jobj_row = new JSONObject();

            jobj_row.put("userid", pref.getStr_Userid());
            jobj_row.put("postid", postid);

            JSONArray jarray_loginuser = new JSONArray();
            jarray_loginuser.put(jobj_row);

            jobj_loginuser.put("like", jarray_loginuser);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Map<String, String> params = new HashMap<>();
        params.put("data", "" + jobj_loginuser.toString().replaceAll("\\\\", ""));
        Response.Listener<String> lis_res = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("response", " : " + response);
                Acitvity_comunator.beans_Count(context, obj_dialog, pref, array_liist);
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

    public void showPermiosssion() {
        if (Build.VERSION.SDK_INT >= 19) {
            String[] s = new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    ActivityCompat.requestPermissions(getActivity(), s, RECORD_REQUEST_CODE);
                } else {
                    ActivityCompat.requestPermissions(getActivity(), s, RECORD_REQUEST_CODE);
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        if (requestCode == RECORD_REQUEST_CODE) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    public void top_post_api() {

        //  obj_dialog.show();

        String url = Configr.app_url + "usertoppost";
        String json = "";

        json = JSON.add_json(array_j, pref, "usertoppost");

        HashMap<String, String> param = new HashMap<>();
        param.put("data", json);
        Log.e("jinal", ":" + param.toString());

        Response.Listener<String> lis_pat = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response", response);
                // obj_dialog.dismiss();
                try {
                    JSONObject jobj = new JSONObject(response);
                    String res_flag = jobj.getString("status");
                    String res_msg = jobj.getString("message");
                    if (res_flag.equals("200")) {

                        JSONObject job = new JSONObject(jobj.getString("data"));

                        JSONArray jarray = new JSONArray(job.getString("user"));

                        for (int i = 0; i < jarray.length(); i++) {
                            Model_user model = new Model_user();
                            JSONObject jobj1 = jarray.getJSONObject(i);

                            if (jobj1.has("comment")) {
                                JSONArray jarraycomment = new JSONArray(jobj1.getString("comment"));
                                if (jarraycomment.length() != 0) {
                                    ArrayList<Model_Comment> array_comment = new ArrayList<>();
                                    for (int j = 0; j < jarraycomment.length(); j++) {
                                        Model_Comment model_comment = new Model_Comment();
                                        JSONObject jobj2 = jarraycomment.getJSONObject(j);
                                        model_comment.setUserid(jobj2.getString("userid"));
                                        model_comment.setComment(jobj2.getString("comment"));
                                        model_comment.setComment_fname(jobj2.getString("comment_fname"));
                                        model_comment.setComment_lname(jobj2.getString("comment_lname"));
                                        model_comment.setComment_propic(jobj2.getString("comment_propic"));
                                        model_comment.setCommenttime(jobj2.getString("commenttime"));
                                        array_comment.add(model_comment);

                                    }
                                    model.setComments(array_comment);
                                } else {
                                    Log.e("lalalalalal", "-->" + jarraycomment.length());

                                    ArrayList<Model_Comment> array_comment = new ArrayList<>();

                                    model.setComments(array_comment);
                                }
                            }

                            model.setSstatus(jobj1.getString("sstatus"));
                            model.setSfname(jobj1.getString("sfname"));
                            model.setSlname(jobj1.getString("slname"));
                            model.setSsuerid(jobj1.getString("suserid"));
                            model.setStatus(jobj1.getString("status"));
                            model.setUrl(jobj1.getString("url"));
                            model.setThumb(jobj1.getString("thumb"));
                            model.setFname(jobj1.getString("fname"));
                            model.setLname(jobj1.getString("lname"));
                            model.setDescription(jobj1.getString("description"));
                            model.setPic(jobj1.getString("propic"));
                            model.setTime(jobj1.getString("time"));
                            model.setPost_id(jobj1.getString("postid"));
                            model.setUserid(jobj1.getString("userid"));
                            model.setTotal_beans(jobj1.getString("beans"));
                            model.setComment_cunt(jobj1.getString("comment_count"));
                            model.setShare_cunt(jobj1.getString("share_count"));
                            model.setLike(jobj1.getString("like"));
                            model.setMylike(jobj1.getString("mylike"));

                            array.add(model);
                        }
                        adapter.notifyDataSetChanged();
                        flag_loading = false;
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
                //obj_dialog.dismiss();
                Toast.makeText(context, "" + Configr.Message, Toast.LENGTH_SHORT).show();
            }
        };
        Connection.postconnection(url, param, Configr.getHeaderParam(), context, lis_pat, lis_error);
    }
    public void Check_share(final String postid, final String postUid, final String status, final String des, final String img, final String modelStatus, final String v_url) {
        obj_dialog.show();
        final ArrayList<Model_profile> Array_user = new ArrayList<>();
        final Model_profile model = new Model_profile();
        model.setPostid(postid);
        model.setPost_userid(postUid);
        model.setStatus(status);
        Array_user.add(model);
        final String url = Configr.app_url + "checkshare";
        String json = "";
        json = JSON.add(Array_user, pref, "checkshare");
        HashMap<String, String> param = new HashMap<>();
        param.put("data", json);
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
                        switch (status) {
                            case "1": {

                                share.fb_share(postid, postUid, status, des, img, modelStatus, v_url);
                                break;
                            }

                            case "2":

                            {
                                share.fb_share(postid, postUid, status, des, img, modelStatus, v_url);
                                pref.setStr_postid(postid);
                                pref.setStr_post_uid(postUid);
                                break;


                            }
                            case "3":

                            {
                                share.fb_share(postid, postUid, status, des, img, modelStatus, v_url);
                                break;
                            }
                            case "4":

                            {
                                share.fb_share(postid, postUid, status, des, img, modelStatus, v_url);
                                break;
                            }

                        }
                        // Toast.makeText(context, res_msg, Toast.LENGTH_SHORT).show();
                        Acitvity_comunator.beans_Count(context, obj_dialog, pref, array_liist);
                    } else {
                        DialogBox.alert_popup(context, res_msg);
                    }
                    obj_dialog.dismiss();
                } catch (
                        Exception e
                        )

                {
                    e.printStackTrace();
                }
            }


        };
        Response.ErrorListener lis_error = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //  signup.setClickable(true);
                obj_dialog.dismiss();
                Toast.makeText(context, "" + Configr.Message, Toast.LENGTH_SHORT).show();
            }
        };
        Connection.postconnection(url, param, Configr.getHeaderParam(), context, lis_pat, lis_error);


    }


}
