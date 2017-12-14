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
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.beanstringnew.Adapter.Adapter_profile_my_followers;
import com.beanstringnew.Controller.Activity_indicator;
import com.beanstringnew.Controller.Configr;
import com.beanstringnew.Controller.Connection;
import com.beanstringnew.Controller.DialogBox;
import com.beanstringnew.Controller.Fb_invites_beans;
import com.beanstringnew.Json_model.JSON;
import com.beanstringnew.Model.Model_Follower_List;
import com.beanstringnew.Model.Model_State_city;
import com.beanstringnew.Model.Model_profile;
import com.beanstringnew.Model.Model_user;
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
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import bolts.AppLinks;

/**
 * Created by Abc on 10/20/2016.
 */

public class Follower_list_activity extends AppCompatActivity implements Thread.UncaughtExceptionHandler {

    GridView grid_my_followers;
    ImageView search, facebook_invite, drawer, imageView, notification;
    TextView noti_count;
    Context context;
    LinearLayout llmybean;
    TextView unfoll;
    Activity_indicator obj_dialog;
    Pref_Master pref;
    String res_message;
    ArrayList<Model_user> array_lll = new ArrayList<>();
    ArrayList<Model_Follower_List> follower_array = new ArrayList<>();

    ArrayList<Model_profile> Array_user = new ArrayList<>();
    ArrayList<Model_State_city> array1 = new ArrayList<>();
    ArrayList<Model_State_city> array2 = new ArrayList<>();
    CallbackManager callbackManager;
    AppInviteDialog appInviteDialog;

    Adapter_profile_my_followers adapter;
    String Userid;
    String image_url = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_followers_layout);

        context = this;

        pref = new Pref_Master(context);
        obj_dialog = new Activity_indicator(context);
        obj_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));

        FacebookSdk.sdkInitialize(context);
        callbackManager = CallbackManager.Factory.create();
        Uri targetUrl = AppLinks.getTargetUrlFromInboundIntent(this, getIntent());
        if (targetUrl != null) {
            Log.i("Activity", "App Link Target URL: " + targetUrl.toString());
        }
        unfoll = (TextView) findViewById(R.id.unfoll);
        noti_count = (TextView) findViewById(R.id.noti_count);

        imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Follower_list_activity.this, Home_Social.class);
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
                Intent intent = new Intent(Follower_list_activity.this, Notification_activity.class);
                noti_count.setVisibility(View.GONE);
                startActivity(intent);
                finish();
            }
        });
        search = (ImageView) findViewById(R.id.search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Follower_list_activity.this, Search_activity.class);
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
                Intent intent = new Intent(Follower_list_activity.this, My_profile_activity.class);
                startActivity(intent);
                finish();
            }
        });

        llmybean = (LinearLayout) findViewById(R.id.llmybean);
        llmybean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Follower_list_activity.this, My_Beans_Activity_new.class);
                startActivity(intent);
                finish();
            }
        });
        Myapplication.getInstance().trackScreenView("Follower Screen");
        get_state_city();
        beans_Count();
        grid_my_followers = (GridView) findViewById(R.id.grid_my_followers);
        adapter = new Adapter_profile_my_followers(context, follower_array, api_c);
        grid_my_followers.setAdapter(adapter);


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


    Runnable api_c = new Runnable() {
        @Override
        public void run() {
            follower_array.clear();
            HTTP_API_Follower_list_Call();
        }
    };

    private void HTTP_API_Follower_list_Call() {
        //   spinner.setVisibility(View.VISIBLE);

        obj_dialog.show();
        res_message = "Server Error...";

        String url = Configr.app_url + "followerslist";

        String json = "";

        json = JSON.add_json(array_lll, pref, "followerslist");

        HashMap<String, String> param = new HashMap<>();
        param.put("data", json);


        Response.Listener<String> lis_res = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response", " : " + response);

                obj_dialog.dismiss();
                try {
                    JSONObject jobj = new JSONObject(response);
                    int status = jobj.getInt("status");
                    String resMessage = jobj.getString("message");
                    JSONArray jarray = new JSONArray(jobj.getString("data"));

                    if (status == 200) {
                        for (int i = 0; i < jarray.length(); i++) {
                            Model_Follower_List model = new Model_Follower_List();
                            JSONObject jobj1 = jarray.getJSONObject(i);
                            model.setF_name(jobj1.getString("fname"));
                            model.setUser_id(jobj1.getString("userid"));
                            model.setL_name(jobj1.getString("lname"));


                            if (jobj1.getString("state").equals("")) {
                                model.setState("");
                            } else {
                                for (Model_State_city modelstate : array1) {
                                    if (modelstate.getStateid().equals(jobj1.getString("state"))) {
                                        model.setState(modelstate.getState_name());
                                        Log.e("State_name", modelstate.getState_name());
                                    }
                                }
                            }


                            if (jobj1.getString("city").equals("")) {
                                model.setCity("");
                            } else {
                                for (Model_State_city modelstate : array2) {
                                    if (modelstate.getCityid().equals(jobj1.getString("city"))) {
                                        model.setCity(modelstate.getCityname());
                                        Log.e("City_name", modelstate.getCityname());
                                    }
                                }
                            }
                            model.setProf_image(jobj1.getString("propic"));
                            model.setStatus(jobj1.getString("status"));
                            follower_array.add(model);

                        }

                        adapter.notifyDataSetChanged();
                    } else {
                        DialogBox.alert_popup(context, resMessage);
                    }

                } catch (Exception e) {

                }
            }
        };
        Response.ErrorListener lis_error = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("response error", "" + error);
                //   spinner.setVisibility(View.GONE);
                obj_dialog.dismiss();
                Toast.makeText(context, "" + Configr.Message, Toast.LENGTH_SHORT).show();
            }
        };
        Connection.postconnection(url, param, Configr.getHeaderParam(), context, lis_res, lis_error);
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


                            unfoll.setText(jobj1.getString("beans"));
                            //noti_count.setText(jobj1.getString("notification"));

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

    public void get_state_city() {

        //obj_dialog.show();

        String url = Configr.app_url + "getstate";
        String json = "";

        // json = JSON.add_json(array, pref, "mybeans");

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

                        JSONArray jarray1 = new JSONArray(job.getString("state"));
                        JSONArray jarray2 = new JSONArray(job.getString("city"));
                        array1.clear();
                        array2.clear();

                        for (int i = 0; i < jarray1.length(); i++) {
                            Model_State_city model = new Model_State_city();
                            JSONObject jobj1 = jarray1.getJSONObject(i);
                            model.setStateid(jobj1.getString("id"));
                            model.setState_name(jobj1.getString("name"));
                            array1.add(model);
                        }

                        for (int i = 0; i < jarray2.length(); i++) {
                            Model_State_city model = new Model_State_city();
                            JSONObject jobj1 = jarray2.getJSONObject(i);
                            model.setCityid(jobj1.getString("id"));
                            model.setS_id(jobj1.getString("stateid"));
                            model.setCityname(jobj1.getString("name"));
                            array2.add(model);
                        }

                        HTTP_API_Follower_list_Call();
                    } else {
                        Toast.makeText(context, res_msg, Toast.LENGTH_SHORT).show();
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

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        FirebaseCrash.report(e);
    }

}
