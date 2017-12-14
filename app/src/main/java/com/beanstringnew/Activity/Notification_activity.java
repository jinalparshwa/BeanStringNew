package com.beanstringnew.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.beanstringnew.Adapter.Adapter_Notification_list;
import com.beanstringnew.Controller.Acitvity_comunator;
import com.beanstringnew.Controller.Activity_indicator;
import com.beanstringnew.Controller.Configr;
import com.beanstringnew.Controller.Connection;
import com.beanstringnew.Controller.DialogBox;
import com.beanstringnew.Controller.Fb_invites_beans;
import com.beanstringnew.Json_model.JSON;
import com.beanstringnew.Model.Model_notification;
import com.beanstringnew.Model.Model_profile;
import com.beanstringnew.Myapplication;
import com.beanstringnew.R;
import com.beanstringnew.Shared.Pref_Master;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.share.model.AppInviteContent;
import com.facebook.share.widget.AppInviteDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import bolts.AppLinks;

/**
 * Created by Admin on 7/22/2017.
 */

public class Notification_activity extends AppCompatActivity {

    Pref_Master pref;
    Context context = this;
    Activity_indicator obj_dialog;
    public static boolean flag_loading = false;

    Adapter_Notification_list adapter;

    ArrayList<Model_notification> array = new ArrayList<>();
    ArrayList<Model_notification> array_j = new ArrayList<>();
    ListView list_notification;
    String res_message;
    TextView notext;
    ImageView facebook_invite, drawer, imageView, notification, search;
    ArrayList<Model_profile> Array_user = new ArrayList<>();
    LinearLayout llmybean;
    TextView upload_count, noti_count;
    CallbackManager callbackManager;
    AppInviteDialog appInviteDialog;
    String image_url = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_notification);
        pref = new Pref_Master(context);
        obj_dialog = new Activity_indicator(context);
        notext = (TextView) findViewById(R.id.notext);

        list_notification = (ListView) findViewById(R.id.list_notification);
        adapter = new Adapter_Notification_list(context, array, pref);
        obj_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));

        FacebookSdk.sdkInitialize(context);
        callbackManager = CallbackManager.Factory.create();
        Uri targetUrl = AppLinks.getTargetUrlFromInboundIntent(this, getIntent());
        if (targetUrl != null) {
            Log.i("Activity", "App Link Target URL: " + targetUrl.toString());
        }
        list_notification.setAdapter(adapter);

        upload_count = (TextView) findViewById(R.id.upload_count);
        noti_count = (TextView) findViewById(R.id.noti_count);

        imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Notification_activity.this, Home_Social.class);
                intent.putExtra("fragmentcode", Configr.Fragment_ID.MainFragment);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
        notification = (ImageView) findViewById(R.id.notification);
        search = (ImageView) findViewById(R.id.search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Notification_activity.this, Search_activity.class);
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
                Intent intent = new Intent(Notification_activity.this, My_profile_activity.class);
                startActivity(intent);
                finish();
            }
        });
        llmybean = (LinearLayout) findViewById(R.id.llmybean);
        llmybean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Notification_activity.this, My_Beans_Activity_new.class);
                startActivity(intent);
                finish();
            }
        });
        Myapplication.getInstance().trackScreenView("Notification Screen");
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


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(Notification_activity.this, Home_Social.class);
        i.putExtra("fragmentcode", Configr.Fragmentt_ID.Main);
        startActivity(i);
        finish();
    }

    @Override
    public void onResume() {

        //obj_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        super.onResume();
        array.clear();
        Model_notification model = new Model_notification();
        model.setLimit("0");
        array_j.add(model);
        get_notification();
        beans_Count();
    }


    public void get_notification() {
        obj_dialog.show();
        String url = Configr.app_url + "getnotification";
        String json = "";
        json = JSON.json_notification(array_j, pref, "getnotification");

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


                        JSONArray jarray = new JSONArray(jobj.getString("data"));

                        for (int i = 0; i < jarray.length(); i++) {
                            Model_notification model = new Model_notification();

                            JSONObject jobj1 = jarray.getJSONObject(i);
                            Log.e("Naughty id", jobj1.getString("notification_id"));
                            model.setNoti_id(jobj1.getString("notification_id"));
                            model.setDesc(jobj1.getString("desc"));
                            model.setProfile(jobj1.getString("propic"));
                            model.setTime(jobj1.getString("time"));
                            model.setNoti_status(jobj1.getString("notification_status"));
                            model.setUserid(jobj1.getString("userid"));
                            Log.e("Postuserid-->", jobj1.getString("userid"));

                            if (jobj1.has("postid")) {
                                model.setPostid(jobj1.getString("postid"));
                            } else {
                                model.setPostid("");
                            }

                            array.add(model);

                            Log.e("array_sizeee", "" + array.size());
                        }
                        adapter.notifyDataSetChanged();
                        flag_loading = false;
                        if (array.size() == 0) {
                            notext.setVisibility(View.VISIBLE);
                            list_notification.setVisibility(View.GONE);
                        } else {
                            notext.setVisibility(View.GONE);
                            list_notification.setVisibility(View.VISIBLE);
                        }

                    } else {
                        if (array.size() == 0) {
                            notext.setVisibility(View.VISIBLE);
                            list_notification.setVisibility(View.GONE);
                        } else {
                            notext.setVisibility(View.GONE);
                            list_notification.setVisibility(View.VISIBLE);
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
                //  signup.setClickable(true);
                obj_dialog.dismiss();
                Toast.makeText(context, "" + Configr.Message, Toast.LENGTH_SHORT).show();
            }
        };
        Connection.postconnection(url, param, Configr.getHeaderParam(), context, lis_pat, lis_error);

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
                            //  noti_count.setText(jobj1.getString("notification"));
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


}
