package com.beanstringnew.Activity;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.beanstringnew.Adapter.Adapter_upload_image_and_video;
import com.beanstringnew.Controller.Activity_indicator;
import com.beanstringnew.Controller.Configr;
import com.beanstringnew.Controller.Connection;
import com.beanstringnew.Controller.DialogBox;
import com.beanstringnew.Controller.Fb_invites_beans;
import com.beanstringnew.Json_model.JSON;
import com.beanstringnew.Model.Model_Play_video;
import com.beanstringnew.Model.Model_profile;
import com.beanstringnew.Model.Model_upload_image_and_video;
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
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.crash.FirebaseCrash;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import bolts.AppLinks;

/**
 * Created by Abc on 10/24/2016.
 */

public class Upload_image_and_video_activity extends AppCompatActivity implements Thread.UncaughtExceptionHandler {

    Context context = this;
    ImageView search, facebook_invite, drawer, imageView, notification;
    Adapter_upload_image_and_video adapter;
    ArrayList<String> array = new ArrayList<>();
    LinearLayout llmybean;
    String res_message;
    TextView upload_count;
    Activity_indicator obj_dialog;
    TextView noti_count;
    public static Pref_Master pref;

    ArrayList<Model_upload_image_and_video> array_upload = new ArrayList<>();
    static ArrayList<Model_profile> Array_user = new ArrayList<>();
    CallbackManager callbackManager;
    AppInviteDialog appInviteDialog;
    String image_url = "";

    GridView grid;
    Boolean bool = true;
    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        public void run() {
            bool = isMyServiceRunning();
            if (!bool) {
                obj_dialog.dismiss();
                handler.removeCallbacksAndMessages(runnable);
            }
            handler.postDelayed(this, 2000);
        }
    };
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_video_and_image_layout);
        pref = new Pref_Master(context);
        obj_dialog = new Activity_indicator(this);
        obj_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));

        FacebookSdk.sdkInitialize(context);
        callbackManager = CallbackManager.Factory.create();
        Uri targetUrl = AppLinks.getTargetUrlFromInboundIntent(this, getIntent());
        if (targetUrl != null) {
            Log.i("Activity", "App Link Target URL: " + targetUrl.toString());
        }


        Log.e("Service_running", "----->" + bool);

        HTTP_API_get_my_post_Call();

        upload_count = (TextView) findViewById(R.id.upload_count);
        noti_count = (TextView) findViewById(R.id.noti_count);
        imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Upload_image_and_video_activity.this, Home_Social.class);
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
                Intent intent = new Intent(Upload_image_and_video_activity.this, Notification_activity.class);
                noti_count.setVisibility(View.GONE);
                startActivity(intent);
                finish();
            }
        });
        search = (ImageView) findViewById(R.id.search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Upload_image_and_video_activity.this, Search_activity.class);
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
                Intent intent = new Intent(Upload_image_and_video_activity.this, My_profile_activity.class);
                startActivity(intent);
                finish();
            }
        });
        llmybean = (LinearLayout) findViewById(R.id.llmybean);
        llmybean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Upload_image_and_video_activity.this, My_Beans_Activity_new.class);
                startActivity(intent);
                finish();
            }
        });

        beans_Count();

        grid = (GridView) findViewById(R.id.grid);
        adapter = new Adapter_upload_image_and_video(context, array_upload, api_f, pref);
        grid.setAdapter(adapter);
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Model_upload_image_and_video model = array_upload.get(position);

                if (position == 0) {
                    Intent intent = new Intent(context, Home_Social.class);
                    intent.putExtra("fragmentcode", Configr.Fragment_ID.Add_photo);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                } else {
                    OpenDialog(model);
                }


            }
        });

        grid.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e("Long_image", "Longg_image");
                return false;
            }
        });
        Myapplication.getInstance().trackScreenView("Upload Photo/Video Screen");
        handler.postDelayed(runnable, 2000);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        // client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    protected void onResume() {
        super.onResume();
        beans_Count();
    }

    private boolean isMyServiceRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if ("com.beanstringnew.Controller.S_Upload_Video".equals(service.service.getClassName()) || "com.beanstringnew.Controller.S_Upload_Image".equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
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


    public void OpenDialog(final Model_upload_image_and_video model) {
        if (model.getStatus().equals("0")) {
            Model_Play_video model_play_video = new Model_Play_video();
            model_play_video.setDescription(model.getDescription());
            model_play_video.setPostid(model.getPostid());
            model_play_video.setPostuserid(model.getPostuserid());
            model_play_video.setStatus(model.getStatus());
            model_play_video.setUrl(model.getUrl());
            Intent i = new Intent(context, Photo_activity.class);
            i.putExtra("url", model_play_video);
            context.startActivity(i);
            View_post(model.getPostid());


        } else {
            Model_Play_video model_play_video = new Model_Play_video();
            model_play_video.setDescription(model.getDescription());
            model_play_video.setPostid(model.getPostid());
            model_play_video.setPostuserid(model.getPostuserid());
            model_play_video.setStatus(model.getStatus());
            model_play_video.setUrl(model.getUrl());
            model_play_video.setDel_valu("0");
            Intent i = new Intent(context, Play_video_activity.class);
            i.putExtra("url", model_play_video);
            context.startActivity(i);
            View_post(model.getPostid());
        }
    }

    Runnable api_f = new Runnable() {
        @Override
        public void run() {
            array.clear();
            HTTP_API_get_my_post_Call();
        }
    };


    private void HTTP_API_get_my_post_Call() {
        //   spinner.setVisibility(View.VISIBLE);
        obj_dialog.show();
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
                Log.e("Response", " getmyphoto" + response);
                //obj_dialog.dismiss();
                try {
                    JSONObject jobj1 = new JSONObject(response);
                    if (jobj1.getInt("status") == 200) {

                        JSONArray jsonArray = jobj1.getJSONArray("data");
                        Log.e("size of array", ":" + jsonArray.length());
                        array_upload.clear();

                        Model_upload_image_and_video model = new Model_upload_image_and_video();
                        model.setStatus("");
                        model.setThumb("");
                        model.setUrl("");
                        model.setTot_beans("");
                        array_upload.add(model);

                        for (int i = 0; i < jsonArray.length(); i++) {
                            Model_upload_image_and_video model1 = new Model_upload_image_and_video();
                            JSONObject jobj2 = jsonArray.getJSONObject(i);

                            if (!jobj2.getString("status").equals("2")) {
                                model1.setPostid(jobj2.getString("postid"));
                                model1.setPostuserid(jobj2.getString("postuserid"));
                                model1.setDescription(jobj2.getString("description"));
                                model1.setUrl(jobj2.getString("url"));
                                model1.setThumb(jobj2.getString("thumb"));
                                model1.setStatus(jobj2.getString("status"));
                                model1.setTot_beans(jobj2.getString("beans"));
                                model1.setTime(jobj2.getString("time"));

                                array_upload.add(model1);
                            }

                        }

                        adapter.notifyDataSetChanged();
                    } else if (jobj1.getInt("status") == 400) {
                        DialogBox.alert_popup(context, "No Post Found");
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
                //   spinner.setVisibility(View.GONE);
                NetworkResponse networkResponse = error.networkResponse;
                if (networkResponse != null) {
                    Log.e("vikas Status code", String.valueOf(networkResponse.statusCode));
                }
                Toast.makeText(context, "" + Configr.Message, Toast.LENGTH_SHORT).show();
            }
        };
        Connection.postconnection(url, params, Configr.getHeaderParam(), context, lis_res, lis_error);
    }

    public void beans_Count() {

//        obj_dialog.show();

        String url = Configr.app_url + "beanscount";

        String json = "";

        json = JSON.add(Array_user, pref, "beanscount");

        HashMap<String, String> param = new HashMap<>();
        param.put("data", json);

        Response.Listener<String> lis_pat = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response", response);
                //  obj_dialog.dismiss();

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
                //   obj_dialog.dismiss();
                Toast.makeText(context, "" + Configr.Message, Toast.LENGTH_SHORT).show();
            }
        };
        Connection.postconnection(url, param, Configr.getHeaderParam(), context, lis_pat, lis_error);

    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        FirebaseCrash.report(e);
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
//    public Action getIndexApiAction() {
//        Thing object = new Thing.Builder()
//                .setName("Upload_image_and_video_activity Page") // TODO: Define a title for the content shown.
//                // TODO: Make sure this auto-generated URL is correct.
//                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
//                .build();
//        return new Action.Builder(Action.TYPE_VIEW)
//                .setObject(object)
//                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
//                .build();
//    }
    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        //  client.connect();
        // AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        // AppIndex.AppIndexApi.end(client, getIndexApiAction());
        // client.disconnect();
    }

    public void View_post(String postid) {

        ArrayList<Model_user> array_j = new ArrayList<>();
        Model_user model = new Model_user();
        model.setPost_id(postid);
        //  model.setUserid(userid);
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
                        // Toast.makeText(context, res_msg, Toast.LENGTH_SHORT).show();
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
}
