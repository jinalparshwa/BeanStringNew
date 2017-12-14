package com.beanstringnew.Activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.beanstringnew.Adapter.Adapter_comment;
import com.beanstringnew.Controller.Acitvity_comunator;
import com.beanstringnew.Controller.Activity_indicator;
import com.beanstringnew.Controller.Configr;
import com.beanstringnew.Controller.Connection;
import com.beanstringnew.Json_model.JSON;
import com.beanstringnew.Model.Model_profile;
import com.beanstringnew.Model.Model_user;
import com.beanstringnew.Model.Model_view_comment;
import com.beanstringnew.Myapplication;
import com.beanstringnew.R;
import com.beanstringnew.Shared.Pref_Master;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.firebase.crash.FirebaseCrash;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Abc on 10/26/2016.
 */

public class Comment_Display_activity extends AppCompatActivity implements Thread.UncaughtExceptionHandler {


    Context context = this;
    Pref_Master pref;

    EditText edit_comment;
    ListView comment_list;

    String comment;
    String post_id;
    String res_message;
    String post_user_id;

    ImageView send_post;
    ImageView bottom_pro;
    ImageView back_button;

    Adapter_comment adapter;
    Activity_indicator obj_dialog;

    ArrayList<Model_view_comment> array_comment = new ArrayList<>();
    static ArrayList<Model_profile> array_liist = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comment_layout);
        pref = new Pref_Master(context);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            post_id = extras.getString("post_id");
            post_user_id = extras.getString("post_user_id");
            Log.e("url for video", ":" + post_id);
            Log.e("url for video", ":" + post_user_id);
        }
        obj_dialog = new Activity_indicator(context);
        obj_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));

        HTTP_API_get_comment_Call();

        send_post = (ImageView) findViewById(R.id.send_post);
        bottom_pro = (ImageView) findViewById(R.id.bottom_pro);
        back_button = (ImageView) findViewById(R.id.back_button);
        edit_comment = (EditText) findViewById(R.id.edit_comment);


        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Acitvity_comunator.beans_Count(context, obj_dialog, pref, array_liist);
                pref.setpos("1");
                pref.setStr_pos_latest("1");
                pref.setStr_pos_top("1");
                finish();
            }
        });

        if (pref.getStr_profile().equals("")) {
            bottom_pro.setImageResource(R.drawable.default_imggg);
        } else {
            Glide.with(context).load(pref.getStr_profile()).into(bottom_pro);
        }
        send_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edit_comment.getText().toString().equals("")) {
                    edit_comment.setError("Enter Comment");
                } else {
                    comment = edit_comment.getText().toString();
                    HTTP_API_post_comment_Call();
                    Acitvity_comunator.beans_Count(context, obj_dialog, pref, array_liist);
                }
            }
        });
        Myapplication.getInstance().trackScreenView("Comment Display Screen");
        comment_list = (ListView) findViewById(R.id.comment_list);
        adapter = new Adapter_comment(context, array_comment);
        comment_list.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        pref.setpos("1");
        pref.setStr_pos_latest("1");
        pref.setStr_pos_top("1");
        //pref.setpos("1");
    }

    private void HTTP_API_post_comment_Call() {

        obj_dialog.show();

        final String url = Configr.app_url + "postcomment";

        JSONObject jobj_row = new JSONObject();
        JSONObject jobj_loginuser = new JSONObject();
        try {

            jobj_row.put("postid", post_id);
            jobj_row.put("userid", pref.getStr_Userid());
            jobj_row.put("comment", comment);

            Log.e("posttttusserrr", post_user_id);
            jobj_row.put("postuserid", post_user_id);


            JSONArray jarray_loginuser = new JSONArray();
            jarray_loginuser.put(jobj_row);
            jobj_loginuser.put("postcomment", jarray_loginuser);

        } catch (Exception e) {
            e.printStackTrace();
        }


        Map<String, String> params = new HashMap<>();
        params.put("data", "" + jobj_loginuser.toString().replaceAll("\\\\", ""));

        Response.Listener<String> lis_res = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response", " : " + response);
                obj_dialog.dismiss();
                try {
                    JSONObject jobj1 = new JSONObject(response);
                    Log.e("status code", ":" + jobj1.getInt("status"));
                    HTTP_API_get_comment_Call();
                    edit_comment.setText("");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Toast.makeText(context, "Comment successfully posted", Toast.LENGTH_SHORT).show();
            }
        };
        Response.ErrorListener lis_error = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("response error", "" + error);
                obj_dialog.dismiss();
                Toast.makeText(context, "" + Configr.Message, Toast.LENGTH_SHORT).show();
            }
        };
        Connection.postconnection(url, params, Configr.getHeaderParam(), context, lis_res, lis_error);
    }


    private void HTTP_API_get_comment_Call() {

        obj_dialog.show();
        res_message = "Server Error...";

        final String url = Configr.app_url + "getcomment";

        JSONObject jobj_row = new JSONObject();
        JSONObject jobj_loginuser = new JSONObject();

        try {
            jobj_row.put("postid", post_id);
            jobj_row.put("userid", pref.getStr_Userid());

            JSONArray jarray_loginuser = new JSONArray();
            jarray_loginuser.put(jobj_row);
            jobj_loginuser.put("getcomment", jarray_loginuser);

        } catch (Exception e) {
            e.printStackTrace();
        }
        Map<String, String> params = new HashMap<>();
        params.put("data", "" + jobj_loginuser.toString().replaceAll("\\\\", ""));

        Response.Listener<String> lis_res = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response", " : " + response);
                obj_dialog.dismiss();
                try {
                    JSONObject jobj1 = new JSONObject(response);

                    if (jobj1.getInt("status") == 200) {
                        JSONArray jsonArray = jobj1.getJSONArray("data");

                        array_comment.clear();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            Model_view_comment model = new Model_view_comment();
                            JSONObject jobj2 = jsonArray.getJSONObject(i);

                            model.setUserid(jobj2.getString("userid"));
                            model.setComment(jobj2.getString("comment"));
                            model.setTime(jobj2.getString("time"));
                            model.setFname(jobj2.getString("fname"));
                            model.setLname(jobj2.getString("lname"));
                            model.setPro_pic(jobj2.getString("propic"));

                            array_comment.add(model);
                        }
                        adapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        Response.ErrorListener lis_error = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("response error", "" + error);
                obj_dialog.dismiss();
                Toast.makeText(context, "" + Configr.Message, Toast.LENGTH_SHORT).show();
            }
        };
        Connection.postconnection(url, params, Configr.getHeaderParam(), context, lis_res, lis_error);
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        FirebaseCrash.report(e);
    }

}
