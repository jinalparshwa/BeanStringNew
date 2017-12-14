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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.beanstringnew.Adapter.Adapter_search_layout;
import com.beanstringnew.Controller.Activity_indicator;
import com.beanstringnew.Controller.Configr;
import com.beanstringnew.Controller.Connection;
import com.beanstringnew.Controller.DialogBox;
import com.beanstringnew.Controller.Fb_invites_beans;
import com.beanstringnew.Json_model.JSON;
import com.beanstringnew.Model.Model_State_city;
import com.beanstringnew.Model.Model_profile;
import com.beanstringnew.Model.Model_search;
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

public class Search_activity extends AppCompatActivity {

    ListView search_list;
    Adapter_search_layout adapter;
    Context context = this;
    EditText etsearch;
    TextView tvsearch;
    Pref_Master pref;
    ArrayList<Model_search> Search = new ArrayList<>();
    Activity_indicator obj_dialog;
    ArrayList<Model_State_city> array1 = new ArrayList<>();
    ArrayList<Model_State_city> array2 = new ArrayList<>();
    ImageView facebook_invite, drawer, imageView, notification;
    ArrayList<Model_profile> Array_user = new ArrayList<>();
    LinearLayout llmybean;
    TextView upload_count, noti_count;
    CallbackManager callbackManager;
    AppInviteDialog appInviteDialog;
    String image_url = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_layout);
        pref = new Pref_Master(context);
        etsearch = (EditText) findViewById(R.id.etsearch);
        tvsearch = (TextView) findViewById(R.id.tvsearch);

        search_list = (ListView) findViewById(R.id.search_list);
        adapter = new Adapter_search_layout(context, Search, search_call);
        search_list.setAdapter(adapter);

        tvsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (etsearch.getText().toString().equals("")) {
                    etsearch.setError("Enter Something");
                } else {
                    Search.clear();
                    Model_search user = new Model_search();
                    user.setFname(etsearch.getText().toString());
                    Search.add(user);
                    Search_api();
                }
            }
        });
        obj_dialog = new Activity_indicator(context);
        obj_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));

        FacebookSdk.sdkInitialize(context);
        callbackManager = CallbackManager.Factory.create();
        Uri targetUrl = AppLinks.getTargetUrlFromInboundIntent(this, getIntent());
        if (targetUrl != null) {
            Log.i("Activity", "App Link Target URL: " + targetUrl.toString());
        }
        upload_count = (TextView) findViewById(R.id.upload_count);
        noti_count = (TextView) findViewById(R.id.noti_count);

        imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Search_activity.this, Home_Social.class);
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
                noti_count.setVisibility(View.GONE);
                Intent intent = new Intent(Search_activity.this, Notification_activity.class);
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
                Intent intent = new Intent(Search_activity.this, My_profile_activity.class);
                startActivity(intent);
                finish();
            }
        });
        llmybean = (LinearLayout) findViewById(R.id.llmybean);
        llmybean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Search_activity.this, My_Beans_Activity_new.class);
                startActivity(intent);
                finish();
            }
        });

        Myapplication.getInstance().trackScreenView("Search Screen");
        get_state_city();
        beans_Count();

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
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(Search_activity.this, Home_Social.class);
        i.putExtra("fragmentcode", Configr.Fragmentt_ID.Main);
        startActivity(i);
        finish();
    }

    Runnable search_call = new Runnable() {
        @Override
        public void run() {
            Search.clear();
            Model_search user = new Model_search();
            user.setFname(etsearch.getText().toString());
            Search.add(user);
            Search_api();
        }
    };

    public void Search_api() {

        obj_dialog.show();
        final String url = Configr.app_url + "searchpeople";
        String json = "";

        json = JSON.add_jsonn(Search, pref, "serch");

        HashMap<String, String> param = new HashMap<>();
        param.put("data", json);


        Response.Listener<String> lis_pat = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("vikas url", ":" + url);
                Log.e("Response", response);
                obj_dialog.dismiss();

                String toastMsg = "";
                toastMsg = "";

                try {
                    JSONObject jobj = new JSONObject(response);
                    String res_flag = jobj.getString("status");
                    String res_msg = jobj.getString("message");
                    String otp = "", userid = "";
                    Search.clear();
                    if (res_flag.equals("200")) {
                        JSONArray jarray = new JSONArray(jobj.getString("data"));


                        for (int i = 0; i < jarray.length(); i++) {
                            Model_search model = new Model_search();
                            JSONObject jobj1 = jarray.getJSONObject(i);

                            Log.e("json_id", ":" + jobj1.getString("id"));
                            model.setUserid(jobj1.getString("id"));

                            Log.e("Fnammme", ":" + jobj1.getString("fname"));
                            model.setFname(jobj1.getString("fname"));

                            Log.e("Lnammee", ":" + jobj1.getString("lname"));
                            model.setLname(jobj1.getString("lname"));

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


                            model.setPic(jobj1.getString("propic"));


                            Log.e("profilecc", ":" + jobj1.getString("propic"));

                            Log.e("statuss", ":" + jobj1.getString("status"));
                            model.setStatus(jobj1.getString("status"));

                            Search.add(model);


                        }


                    } else {
                        DialogBox.alert_popup(context, res_msg);
                    }

                    if (Search.size() != 0) {
                        search_list.setVisibility(View.VISIBLE);
                        adapter = new Adapter_search_layout(context, Search, search_call);
                        search_list.setAdapter(adapter);
                    } else {
                        search_list.setVisibility(View.GONE);
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
                // obj_dialog.dismiss();
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
