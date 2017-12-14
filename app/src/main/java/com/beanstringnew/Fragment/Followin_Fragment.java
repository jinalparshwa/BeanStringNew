package com.beanstringnew.Fragment;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.beanstringnew.Adapter.AdapterFollowing_list;
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
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Abc on 10/20/2016.
 */

public class Followin_Fragment extends Fragment {

    Pref_Master pref;
    Context context;
    ListView list_following_post;
    Activity_indicator obj_dialog;
    AdapterFollowing_list adapter;
    ArrayList<Model_user> array = new ArrayList<>();
    ArrayList<Model_profile> array_liist = new ArrayList<>();
    ArrayList<Model_user> array_j = new ArrayList<>();
    boolean flag_loading = false;
    private static final int RECORD_REQUEST_CODE = 1;
    TextView notext;
    CallbackManager callbackManager;
    Share share;
    private static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 55;


    public Followin_Fragment(Share share) {
        this.share = share;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.following_fragment_layout, container, false);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        context = getActivity();
        //        checkAndRequestPermissions();
        pref = new Pref_Master(context);
        pref.setpos("0");
        obj_dialog = new Activity_indicator(context);
        obj_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        FacebookSdk.sdkInitialize(context);
        callbackManager = CallbackManager.Factory.create();
        notext = (TextView) v.findViewById(R.id.notext);

        list_following_post = (ListView) v.findViewById(R.id.list_following_post);
        list_following_post.setItemsCanFocus(true);

        adapter = new AdapterFollowing_list(context, array, pref, Followin_Fragment.this);
        list_following_post.setAdapter(adapter);
        Acitvity_comunator.beans_Count(context, obj_dialog, pref, array_liist);
        array.clear();


        Model_user model = new Model_user();
        model.setLimit("0");
        array_j.add(model);
        Log.e("First", "first");
        Updates();

        list_following_post.setOnScrollListener(new AbsListView.OnScrollListener() {
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
                        Log.e("Second", "Second");
                        Updates();

                    }
                }
            }
        });
        Myapplication.getInstance().trackScreenView("Following Post Screen");
        return v;
    }

//    Runnable api_f = new Runnable() {
//        @Override
//        public void run() {
//            array.clear();
//            Updates();
//        }
//    };

    @Override
    public void onResume() {
        super.onResume();
        if (pref.getStr_pos().equals("1")) {
            array.clear();
            Log.e("Refresh", "Refresh");
            Model_user model = new Model_user();
            model.setLimit("" + array.size());
            array_j.add(model);
            Log.e("Third", "Third");
            Updates();
            pref.setpos("0");
        }
        Myapplication.getInstance().trackScreenView("Following Post Screen");
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

    public void Updates() {
        //  obj_dialog.show();
        String url = Configr.app_url + "userfollowingpost";
        String json = "";
        json = JSON.add_json(array_j, pref, "userfollowingpost");
        HashMap<String, String> param = new HashMap<>();
        param.put("data", json);
        Log.e("jinal", ":" + param.toString());
        Response.Listener<String> lis_pat = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response", response);
                //obj_dialog.dismiss();
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
                            //like":"1","mylike":"0",
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
                            Log.e("array_sizeee", "" + array.size());
                        }
                        adapter.notifyDataSetChanged();
                        flag_loading = false;

                        if (array.size() == 0) {
                            notext.setVisibility(View.VISIBLE);
                            list_following_post.setVisibility(View.GONE);
                        } else {
                            notext.setVisibility(View.GONE);
                            list_following_post.setVisibility(View.VISIBLE);
                        }

                    } else {
                        // Toast.makeText(context, res_msg, Toast.LENGTH_SHORT).show();
                        if (array.size() == 0) {
                            notext.setVisibility(View.VISIBLE);
                            list_following_post.setVisibility(View.GONE);
                        } else {
                            notext.setVisibility(View.GONE);
                            list_following_post.setVisibility(View.VISIBLE);
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
