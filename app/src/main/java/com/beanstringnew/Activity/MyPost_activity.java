package com.beanstringnew.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.beanstringnew.Adapter.Adapter_Notification_list;
import com.beanstringnew.Adapter.Adapter_my_post;
import com.beanstringnew.Adapter.Adapter_upload_image_and_video;
import com.beanstringnew.Controller.Activity_indicator;
import com.beanstringnew.Controller.Configr;
import com.beanstringnew.Controller.Connection;
import com.beanstringnew.Controller.DialogBox;
import com.beanstringnew.Controller.Fb_invites_beans;
import com.beanstringnew.Json_model.JSON;
import com.beanstringnew.Model.Model_profile;
import com.beanstringnew.Model.Model_upload_image_and_video;
import com.beanstringnew.Myapplication;
import com.beanstringnew.R;
import com.beanstringnew.Shared.Pref_Master;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.share.model.AppInviteContent;
import com.facebook.share.widget.AppInviteDialog;
import com.google.firebase.crash.FirebaseCrash;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import bolts.AppLinks;

public class MyPost_activity extends AppCompatActivity implements Thread.UncaughtExceptionHandler {

    Context context = this;
    ImageView search, facebook_invite, drawer, imageView, notification;
    Adapter_my_post adapter;
    ArrayList<String> array = new ArrayList<>();
    LinearLayout llmybean;
    String res_message;
    TextView upload_count;
    Activity_indicator obj_dialog;
    TextView noti_count;

    Pref_Master pref;
    ArrayList<Model_upload_image_and_video> array_upload = new ArrayList<>();
    ArrayList<Model_profile> Array_user = new ArrayList<>();

    ListView list_post;
    TextView notext;
    CallbackManager callbackManager;
    AppInviteDialog appInviteDialog;
    String image_url = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_post_activity);
        pref = new Pref_Master(context);
        obj_dialog = new Activity_indicator(context);
        obj_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));

        FacebookSdk.sdkInitialize(context);
        callbackManager = CallbackManager.Factory.create();
        Uri targetUrl = AppLinks.getTargetUrlFromInboundIntent(this, getIntent());
        if (targetUrl != null) {
            Log.i("Activity", "App Link Target URL: " + targetUrl.toString());
        }
        notext = (TextView) findViewById(R.id.notext);

        HTTP_API_get_my_post_Call();

        upload_count = (TextView) findViewById(R.id.upload_count);
        noti_count = (TextView) findViewById(R.id.noti_count);
        imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyPost_activity.this, Home_Social.class);
                intent.putExtra("fragmentcode", Configr.Fragment_ID.MainFragment);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
        notification = (ImageView) findViewById(R.id.notification);
        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyPost_activity.this, Notification_activity.class);
                noti_count.setVisibility(View.GONE);
                startActivity(intent);
                finish();
            }
        });
        search = (ImageView) findViewById(R.id.search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyPost_activity.this, Search_activity.class);
                startActivity(intent);
                finish();
            }
        });
        facebook_invite = (ImageView) findViewById(R.id.facebook_invite);
        facebook_invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open_dialog();
            }
        });
        drawer = (ImageView) findViewById(R.id.drawer);
        drawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyPost_activity.this, My_profile_activity.class);
                startActivity(intent);
                finish();
            }
        });

        llmybean = (LinearLayout) findViewById(R.id.llmybean);
        llmybean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyPost_activity.this, My_Beans_Activity_new.class);
                startActivity(intent);
                finish();
            }
        });
        Myapplication.getInstance().trackScreenView("My Post Screen");
        beans_Count();

        list_post = (ListView) findViewById(R.id.list_post);
        adapter = new Adapter_my_post(context, array_upload, api_cc);
        list_post.setAdapter(adapter);


    }

    @Override
    protected void onResume() {
        super.onResume();
        beans_Count();
    }

    public void open_dialog() {


        appInviteDialog = new AppInviteDialog(this);
        appInviteDialog.registerCallback(callbackManager,
                new FacebookCallback<AppInviteDialog.Result>() {

                    @Override
                    public void onSuccess(AppInviteDialog.Result result) {
                        Fb_invites_beans.beans_Count(context, obj_dialog, pref, Array_user);
                        Log.d("Succes", "Success");
                    }

                    @Override
                    public void onCancel() {
                        Log.d("Result", "Cancelled");


                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Log.d("Result", "Error " + exception.getMessage());
                    }
                });


        if (AppInviteDialog.canShow()) {
            AppInviteContent content = new AppInviteContent.Builder()
                    .setApplinkUrl(Configr.app_link)
                    .setPreviewImageUrl(image_url)
                    .build();

            appInviteDialog.show(content);

        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

    }


    Runnable api_cc = new Runnable() {
        @Override
        public void run() {
            array.clear();
            HTTP_API_get_my_post_Call();
        }
    };


    private void HTTP_API_get_my_post_Call() {
        //   spinner.setVisibility(View.VISIBLE);
        res_message = "Server Error...";

        final String url = Configr.app_url + "getmyphoto";

        JSONObject jobj_row = new JSONObject();
        JSONObject jobj_loginuser = new JSONObject();
        try {

            jobj_row.put("id", pref.getStr_Userid());

            JSONArray jarray_loginuser = new JSONArray();
            jarray_loginuser.put(jobj_row);
            jobj_loginuser.put("getmyphoto", jarray_loginuser);

        } catch (Exception e) {
            e.printStackTrace();
        }

        Map<String, String> params = new HashMap<>();
        params.put("data", "" + jobj_loginuser.toString().replaceAll("\\\\", ""));

        Response.Listener<String> lis_res = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("login_Response", " : " + response);
                try {
                    JSONObject jobj1 = new JSONObject(response);
                    if (jobj1.getInt("status") == 200) {

                        JSONArray jsonArray = jobj1.getJSONArray("data");
                        Log.e("size of array", ":" + jsonArray.length());
                        array_upload.clear();

                        for (int i = 0; i < jsonArray.length(); i++) {
                            Model_upload_image_and_video model1 = new Model_upload_image_and_video();
                            JSONObject jobj2 = jsonArray.getJSONObject(i);
                            if (jobj2.getString("status").equals("2")) {

                                model1.setPostid(jobj2.getString("postid"));
                                model1.setStatus(jobj2.getString("status"));
                                model1.setTot_beans(jobj2.getString("beans"));
                                model1.setDescription(jobj2.getString("description"));
                                model1.setTime(jobj2.getString("time"));

                                array_upload.add(model1);
                            }
                        }
                        adapter.notifyDataSetChanged();

                        if (array_upload.size() == 0) {
                            notext.setVisibility(View.VISIBLE);
                            list_post.setVisibility(View.GONE);
                        } else {
                            notext.setVisibility(View.GONE);
                            list_post.setVisibility(View.VISIBLE);
                        }

                    } else {
                        if (array_upload.size() == 0) {
                            notext.setVisibility(View.VISIBLE);
                            list_post.setVisibility(View.GONE);
                        } else {
                            notext.setVisibility(View.GONE);
                            list_post.setVisibility(View.VISIBLE);
                        }
                    }
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        Response.ErrorListener lis_error = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("response error", "" + error);
                //   spinner.setVisibility(View.GONE);
                Toast.makeText(context, "" + Configr.Message, Toast.LENGTH_SHORT).show();
            }
        };
        Connection.postconnection(url, params, Configr.getHeaderParam(), context, lis_res, lis_error);
    }


    public void beans_Count() {

        obj_dialog.show();

        String url = Configr.app_url + "beanscount";

        String json = "";

        json = JSON.add(Array_user, pref, "beanscount");

        HashMap<String, String> param = new HashMap<>();
        param.put("data", json);

        Response.Listener<String> lis_pat = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response", response);
                obj_dialog.dismiss();

                String toastMsg = "";
                toastMsg = "";


                try {
                    JSONObject jobj = new JSONObject(response);
                    String res_flag = jobj.getString("status");
                    String res_msg = jobj.getString("message");
                    String otp = "", userid = "";

                    if (res_flag.equals("200")) {
                        JSONArray jarray = new JSONArray(jobj.getString("data"));
                        Array_user.clear();

                        for (int i = 0; i < jarray.length(); i++) {
                            Model_profile model = new Model_profile();
                            JSONObject jobj1 = jarray.getJSONObject(i);
                            model.setBeans(jobj1.getString("beans"));
                            model.setNoti_count(jobj1.getString("notification"));
                            image_url = jobj1.getString("url");

                            if (model.getNoti_count().equals("0")) {
                                noti_count.setVisibility(View.GONE);
                            } else {
                                noti_count.setVisibility(View.VISIBLE);
                                noti_count.setText(jobj1.getString("notification"));
                            }


                            upload_count.setText(jobj1.getString("beans"));
                            //   noti_count.setText(jobj1.getString("notification"));
                            Array_user.add(model);


                        }

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
                //  signup.setClickable(true);
                obj_dialog.dismiss();
                Toast.makeText(context, "" + Configr.Message, Toast.LENGTH_SHORT).show();
            }
        };
        Connection.postconnection(url, param, Configr.getHeaderParam(), context, lis_pat, lis_error);

    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        FirebaseCrash.report(e);
    }
}
