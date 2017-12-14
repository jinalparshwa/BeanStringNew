package com.beanstringnew.Fragment;


import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.beanstringnew.Adapter.Adapter_Other_profile_latesttop;
import com.beanstringnew.Controller.Acitvity_comunator;
import com.beanstringnew.Controller.Activity_indicator;
import com.beanstringnew.Controller.Activity_other_profile_like;
import com.beanstringnew.Controller.Configr;
import com.beanstringnew.Controller.Connection;
import com.beanstringnew.Controller.DialogBox;
import com.beanstringnew.Interface.Share;
import com.beanstringnew.Json_model.JSON;
import com.beanstringnew.Model.Model_Comment;
import com.beanstringnew.Model.Model_profile;
import com.beanstringnew.Myapplication;
import com.beanstringnew.R;
import com.beanstringnew.Shared.Pref_Master;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Other_profile_latestpost extends Fragment {

    Pref_Master pref;
    Context context;
    ListView list_latset;
    Activity_indicator obj_dialog;
    Adapter_Other_profile_latesttop adapter;
    ArrayList<Model_profile> array = new ArrayList<>();
    ArrayList<Model_profile> array_j = new ArrayList<>();
    boolean flag_loading = false;
    ArrayList<Model_profile> array_liist = new ArrayList<>();
    String userid = "";
    TextView notext;
    Share share;
    CallbackManager callbackManager;

    public Other_profile_latestpost(String Userid, Share share) {
        userid = Userid;
        this.share = share;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.latest_fragment_layout, container, false);
        context = getActivity();
        pref = new Pref_Master(context);
        pref.setStr_latest("0");
        obj_dialog = new Activity_indicator(context);
        obj_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        FacebookSdk.sdkInitialize(context);
        callbackManager = CallbackManager.Factory.create();
        notext = (TextView) v.findViewById(R.id.notext);

        list_latset = (ListView) v.findViewById(R.id.list_latset);
        list_latset.setItemsCanFocus(true);

        adapter = new Adapter_Other_profile_latesttop(context, array, pref, Other_profile_latestpost.this);
        list_latset.setAdapter(adapter);
        array.clear();
        Model_profile model = new Model_profile();
        model.setLimit("0");
        model.setUserid(userid);
        array_j.add(model);
        latest_post_api();
        Acitvity_comunator.beans_Count(context, obj_dialog, pref, array_liist);


        list_latset.setOnScrollListener(new AbsListView.OnScrollListener() {

            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                if (firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount != 0) {
                    Log.e("Flaggggggggggg", "-->" + flag_loading);
                    if (!flag_loading) {
                        flag_loading = true;
                        Model_profile model = new Model_profile();
                        model.setLimit("" + array.size());
                        model.setUserid(userid);
                        array_j.add(model);
                        latest_post_api();
                        Log.e("Refresh", "Refresh");
                    }
                }
            }
        });
        Myapplication.getInstance().trackScreenView("Other Profile Latest Post Screen");
        return v;
    }

    @Override
    public void onResume() {
//        obj_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        super.onResume();

        if (pref.getStr_latest().equals("1")) {
            array.clear();
            Model_profile model = new Model_profile();
            model.setLimit("" + array.size());
            model.setUserid(userid);
            array_j.add(model);
            latest_post_api();
            pref.setStr_latest("0");
        }
        Myapplication.getInstance().trackScreenView("Other Profile Latest Post Screen");
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
                Activity_other_profile_like.beans_Count(context, obj_dialog, pref, array_liist);
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

    public void latest_post_api() {

        // obj_dialog.show();

        String url = Configr.app_url + "profilelatestpost";
        String json = "";

        json = JSON.add(array_j, pref, "profilelatestpost");

        HashMap<String, String> param = new HashMap<>();
        param.put("data", json);
        Log.e("jinal", ":" + param.toString());

        Response.Listener<String> lis_pat = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response", response);
                //   obj_dialog.dismiss();

                try {
                    JSONObject jobj = new JSONObject(response);
                    String res_flag = jobj.getString("status");
                    String res_msg = jobj.getString("message");
                    if (res_flag.equals("200")) {

                        JSONObject job = new JSONObject(jobj.getString("data"));

                        JSONArray jarray = new JSONArray(job.getString("user"));


                        for (int i = 0; i < jarray.length(); i++) {
                            Model_profile model = new Model_profile();
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
                            model.setStatus(jobj1.getString("status"));
                            model.setUserid(jobj1.getString("userid"));
                            model.setUrl(jobj1.getString("url"));
                            model.setThumb(jobj1.getString("thumb"));
                            model.setFname(jobj1.getString("fname"));
                            model.setLname(jobj1.getString("lname"));
                            model.setDescription(jobj1.getString("description"));
                            model.setProfile(jobj1.getString("propic"));
                            model.setTime(jobj1.getString("time"));
                            model.setPostid(jobj1.getString("postid"));
                            model.setTot_beans(jobj1.getString("beans"));
                            model.setComment_count(jobj1.getString("comment_count"));
                            model.setShare_count(jobj1.getString("share_count"));
                            model.setLike(jobj1.getString("like"));
                            model.setMylike(jobj1.getString("mylike"));
                            array.add(model);
                        }
                        adapter.notifyDataSetChanged();
                        flag_loading = false;
                        Log.e("Flaggggggggggg", "-->" + flag_loading);
                        if (array.size() == 0) {
                            notext.setVisibility(View.VISIBLE);
                            list_latset.setVisibility(View.GONE);
                        } else {
                            notext.setVisibility(View.GONE);
                            list_latset.setVisibility(View.VISIBLE);
                        }

                    } else {
                        if (array.size() == 0) {
                            notext.setVisibility(View.VISIBLE);
                            list_latset.setVisibility(View.GONE);
                        } else {
                            notext.setVisibility(View.GONE);
                            list_latset.setVisibility(View.VISIBLE);
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        };

        Response.ErrorListener lis_error = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // obj_dialog.dismiss();
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
                            case "2": {

                                share.fb_share(postid, postUid, status, des, img, modelStatus, v_url);
                                pref.setStr_postid(postid);
                                pref.setStr_post_uid(postUid);
                                break;

                            }
                            case "3": {

                                share.fb_share(postid, postUid, status, des, img, modelStatus, v_url);
                                break;
                            }
                            case "4": {
                                share.fb_share(postid, postUid, status, des, img, modelStatus, v_url);
                                break;
                            }
                        }
                        //Toast.makeText(context, res_msg, Toast.LENGTH_SHORT).show();
                        Acitvity_comunator.beans_Count(context, obj_dialog, pref, array_liist);
                    } else {
                        DialogBox.alert_popup(context, res_msg);
                    }
                    obj_dialog.dismiss();
                } catch (Exception e) {
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
