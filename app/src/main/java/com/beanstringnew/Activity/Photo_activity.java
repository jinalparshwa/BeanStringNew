package com.beanstringnew.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.beanstringnew.Adapter.Adapter_Image_viewpager;
import com.beanstringnew.Controller.Acitvity_comunator;
import com.beanstringnew.Controller.Activity_indicator;
import com.beanstringnew.Controller.Configr;
import com.beanstringnew.Controller.Connection;
import com.beanstringnew.Controller.DialogBox;
import com.beanstringnew.Json_model.JSON;
import com.beanstringnew.Model.Model_Play_video;
import com.beanstringnew.Model.Model_profile;
import com.beanstringnew.Model.Model_user;
import com.beanstringnew.Myapplication;
import com.beanstringnew.R;
import com.beanstringnew.Shared.Pref_Master;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.google.firebase.crash.FirebaseCrash;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Photo_activity extends AppCompatActivity implements Thread.UncaughtExceptionHandler {

    Context context = this;
    Activity_indicator obj_dialog;
    ImageView img_share_bean;
    ImageView img_share_fb;
    ImageView img_share_twitter;
    ImageView img_share_gplus;
    Pref_Master pref;
    String intent;
    Model_Play_video model;
    ImageView back_button;
    ViewPager Image_slider;
    ArrayList<Model_profile> array = new ArrayList<>();
    ArrayList<Model_profile> array_post = new ArrayList<>();
    Adapter_Image_viewpager adapter;
    ImageView previous;
    ImageView next, img_report;
    PopupMenu menu_report;
    CallbackManager callbackManager;
    public int TWEET_COMPOSER_REQUEST_CODE = 1;
    public int GOOGLE_PLUS_REQUEST_CODE = 2;
    String post_url = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_activity);
        pref = new Pref_Master(context);
        Bundle extras = getIntent().getExtras();


        model = (Model_Play_video) getIntent().getSerializableExtra("url");
        Log.e("jinal_id", model.getPostuserid());
        //  }
        obj_dialog = new Activity_indicator(context);
        obj_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));

        FacebookSdk.sdkInitialize(context);
        callbackManager = CallbackManager.Factory.create();

        previous = (ImageView) findViewById(R.id.previous);
        next = (ImageView) findViewById(R.id.next);
        img_report = (ImageView) findViewById(R.id.img_report);
        Image_slider = (ViewPager) findViewById(R.id.image_slider);


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Image_slider.setCurrentItem(Image_slider.getCurrentItem() + 1, true);

            }
        });
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Image_slider.setCurrentItem(Image_slider.getCurrentItem() - 1, true);
            }
        });

        img_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menu_report.show();
            }
        });

        menu_report = new PopupMenu(context, img_report);
        createPopup();


        img_share_bean = (ImageView) findViewById(R.id.img_share_bean);
        img_share_fb = (ImageView) findViewById(R.id.img_share_fb);
        img_share_twitter = (ImageView) findViewById(R.id.img_share_twitter);
        img_share_gplus = (ImageView) findViewById(R.id.img_share_gplus);
        back_button = (ImageView) findViewById(R.id.back_button);


        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        img_share_bean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Log.e("Bean_image", "" + Image_slider.getCurrentItem());
                int position = Image_slider.getCurrentItem();
                Check_share(array.get(position).getPostid(), array.get(position).getUserid(), "4", array.get(position).getDescription(), array.get(position).getUrl());
                //Login_social(model.getPostid(), model.getPostuserid(), "4", model.getDescription(), model.getStatus(), model.getUrl());
                //api_f.run();
            }
        });

        img_share_fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Faceboook_image", model.getUrl());
                boolean installed = appInstalledOrNot("com.facebook.katana");
                if (installed) {
                    if (!model.getStatus().equals("2")) {
                        int position = Image_slider.getCurrentItem();
                        getposturl(array.get(position).getPostid(), array.get(position).getUserid(), "1", array.get(position).getDescription(), array.get(position).getUrl());

                    }
                } else {
                    Toast.makeText(context, "App is not currently installed on your phone", Toast.LENGTH_LONG).show();
                    // System.out.println("App is not currently installed on your phone");
                }
            }
        });

        img_share_twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Twittter_image", model.getUrl());
                boolean installed = appInstalledOrNot("com.twitter.android");
                if (installed) {
                    int position = Image_slider.getCurrentItem();
                    Login_social(array.get(position).getPostid(), array.get(position).getUserid(), "3", array.get(position).getDescription(), array.get(position).getStatus(), array.get(position).getUrl());
                } else {
                    Toast.makeText(context, "App is not currently installed on your phone", Toast.LENGTH_LONG).show();
                }
            }
        });

        img_share_gplus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("gooogle_plus_image", model.getUrl());
                boolean installed = appInstalledOrNot("com.google.android.apps.plus");
                if (installed) {
                    int position = Image_slider.getCurrentItem();
                    getposturl(array.get(position).getPostid(), array.get(position).getUserid(), "2", array.get(position).getDescription(), array.get(position).getUrl());
                } else {
                    Toast.makeText(context, "App is not currently installed on your phone", Toast.LENGTH_LONG).show();
                }
            }
        });

        Model_profile model_profile = new Model_profile();
        model_profile.setUserid(model.getPostuserid());
        model_profile.setPostid(model.getPostid());
        array_post.add(model_profile);
        Post_slider();

        Image_slider.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                int pos = Image_slider.getCurrentItem();
                View_post(array.get(pos).getPostid());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        Myapplication.getInstance().trackScreenView("Photo Screen");
        //    }
    }

    public void createPopup() {
        menu_report = new PopupMenu(context, img_report);
        menu_report.getMenu().add(Menu.NONE, 0, Menu.NONE, "Report Photo");
        menu_report.setOnMenuItemClickListener(onClick_State_popupmenu);
    }

    PopupMenu.OnMenuItemClickListener onClick_State_popupmenu = new PopupMenu.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            final int position = Image_slider.getCurrentItem();

            LayoutInflater li = LayoutInflater.from(context);
            View v = li.inflate(R.layout.alert_popup, null);
            final android.support.v7.app.AlertDialog alert = new android.support.v7.app.AlertDialog.Builder(context).setView(v).show();
            alert.setCancelable(false);
            alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            TextView ok = (TextView) v.findViewById(R.id.con_ok);
            TextView cancel = (TextView) v.findViewById(R.id.con_cancel);
            TextView txt = (TextView) v.findViewById(R.id.txt);
            txt.setText("Are you sure you want to Report this post?");

            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    post_report(array.get(position).getPostid(), array.get(position).getUserid());
                    alert.dismiss();
                }
            });
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alert.dismiss();
                }
            });

            return true;
        }
    };


    public boolean appInstalledOrNot(String uri) {
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

    private void Post_slider() {

        obj_dialog.show();


        final String url = Configr.app_url + "postslider";
        String json_str = "";
        try {
            json_str = JSON.add(array_post, pref, "postslider");
        } catch (Exception e) {
            e.printStackTrace();
        }
        Map<String, String> params = new HashMap<>();
        params.put("data", json_str);
        Log.e("request", ":" + params.toString());
        Response.Listener<String> lis_res = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("login_Response______", " : " + response);
                obj_dialog.dismiss();

                try {
                    JSONObject jobj = new JSONObject(response);
                    String status = jobj.getString("status");
                    String resMessage = jobj.getString("message");
                    JSONObject jsonObject = new JSONObject(jobj.getString("data"));
                    JSONArray jarray = new JSONArray(jsonObject.getString("user"));

                    if (status.equals("200")) {
                        for (int i = 0; i < jarray.length(); i++) {
                            Model_profile model = new Model_profile();
                            JSONObject jobj1 = jarray.getJSONObject(i);

                            if (jobj1.has("url")) {
                                model.setPostid(jobj1.getString("postid"));
                                model.setUrl(jobj1.getString("url"));
                                model.setDescription("");
                                model.setStatus("");
                                model.setUserid(jobj1.getString("userid"));
                            }
                            array.add(model);
                        }
                        adapter = new Adapter_Image_viewpager(context, array, Photo_activity.this);
                        Image_slider.setAdapter(adapter);
                        adapter.notifyDataSetChanged();


                    } else {
                        DialogBox.alert_popup(context, resMessage);
                    }
                    adapter.notifyDataSetChanged();

                } catch (Exception e) {

                }
            }
        };
        Response.ErrorListener lis_error = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("response error", "" + error);
                //   spinner.setVisibility(View.GONE);
                //Toast.makeText(context, res_message, Toast.LENGTH_SHORT).show();
                obj_dialog.dismiss();
                Toast.makeText(context, "" + Configr.Message, Toast.LENGTH_SHORT).show();
            }
        };
        Connection.postconnection(url, params, Configr.getHeaderParam(), context, lis_res, lis_error);
    }

    public void
    Login_social(final String postid, String postUid, final String status, final String des, final String modelStatus, final String img) {
        obj_dialog.show();
        final ArrayList<Model_profile> Array_user = new ArrayList<>();
        final Model_profile model = new Model_profile();
        model.setUserid(postid);
        model.setProfile(postUid);
        model.setStatus(status);
        Array_user.add(model);
        final String url = Configr.app_url + "share";
        String json = "";
        json = JSON.add(Array_user, pref, "share");
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
                        Log.e("Statusssst", status);
                        switch (status) {
                            case "1": {
                                break;
                            }
                            case "2": {
                                break;

                            }
                            case "3": {

                                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                                shareIntent.setType("text/plain");
                                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Hiiii");
                                shareIntent.putExtra(Intent.EXTRA_TEXT, des);
                                shareIntent.setPackage("com.twitter.android");
                                shareIntent.putExtra(Intent.EXTRA_STREAM, Configr.onShareItem(img, context));
                                // }
                                context.startActivity(shareIntent);
                                break;
                            }
                            case "4": {
                                DialogBox.alert_popup(context, res_msg);
                                break;
                            }
                        }
                        DialogBox.alert_popup(context, res_msg);
                        Acitvity_comunator.beans_Count(context, obj_dialog, pref, Array_user);
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


    @Override
    public void uncaughtException(Thread t, Throwable e) {
        FirebaseCrash.report(e);
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

    public void post_report(final String postid, final String postUid) {

        obj_dialog.show();
        final ArrayList<Model_profile> Array_report = new ArrayList<>();
        final Model_profile model = new Model_profile();
        model.setPostid(postid);
        model.setUserid(postUid);
        Array_report.add(model);


        final String url = Configr.app_url + "reportpost";
        String json_str = "";
        try {
            json_str = JSON.add(Array_report, pref, "reportpost");
        } catch (Exception e) {
            e.printStackTrace();
        }
        Map<String, String> params = new HashMap<>();
        params.put("data", json_str);
        Response.Listener<String> lis_res = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                obj_dialog.dismiss();

                try {
                    JSONObject jobj = new JSONObject(response);
                    String status = jobj.getString("status");
                    String resMessage = jobj.getString("message");

                    if (status.equals("200")) {
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
                //Toast.makeText(context, res_message, Toast.LENGTH_SHORT).show();
                obj_dialog.dismiss();
                Toast.makeText(context, "" + Configr.Message, Toast.LENGTH_SHORT).show();
            }
        };
        Connection.postconnection(url, params, Configr.getHeaderParam(), context, lis_res, lis_error);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("request_code", ":" + requestCode);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GOOGLE_PLUS_REQUEST_CODE) {

            if (resultCode == Activity.RESULT_OK) {
                Log.e("Success", "success");
                Share(pref.getStr_postid(), pref.getStr_post_uid(), "2");
                // onTwitterSuccess();

            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.e("Failure", "Failure");

                // onTwitterCancel();
            }
        } else if (requestCode == TWEET_COMPOSER_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Log.e("twitter_succes", "twitter_succes");

            } else {
                Log.e("twitter_failure", "twitter_failure");
            }
        }
    }

    public void Check_share(final String postid, final String postUid, final String status, final String des, final String img) {
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

                                callbackManager = CallbackManager.Factory.create();
                                FacebookCallback<Sharer.Result> sharecallback = new FacebookCallback<Sharer.Result>() {
                                    @Override
                                    public void onSuccess(Sharer.Result result) {
                                        Share(postid, postUid, status);
                                        Log.e("Success", "Success");
                                    }

                                    @Override
                                    public void onCancel() {
                                        Log.e("Cancel", "Cancel");

                                    }

                                    @Override
                                    public void onError(FacebookException error) {
                                        Log.e("Error", "Error");

                                    }

                                };
                                ShareDialog shareDialog = new ShareDialog((Activity) context);
                                ShareLinkContent shareLinkContent = new ShareLinkContent.Builder()
                                        .setContentTitle("Beanstring")
                                        .setContentDescription(des)
                                        .setContentUrl(Uri.parse(img))
                                        //.setImageUrl(Configr.onShareItem(img, context))
                                        .build();
                                shareDialog.registerCallback(callbackManager, sharecallback);
                                shareDialog.show(shareLinkContent, ShareDialog.Mode.AUTOMATIC);

                                break;
                            }
                            case "2": {

                                Intent sendIntent = new Intent();
                                sendIntent.setType("text/plain");
                                sendIntent.setAction(Intent.ACTION_SEND);
                                sendIntent.putExtra(Intent.EXTRA_TEXT, img);
                                sendIntent.setPackage("com.google.android.apps.plus");
                                startActivityForResult(sendIntent, GOOGLE_PLUS_REQUEST_CODE);
                                pref.setStr_postid(postid);
                                pref.setStr_post_uid(postUid);


                                break;
                            }
                            case "3":

                            {

                                Intent sendIntent = new Intent();
                                sendIntent.setType("text/plain");
                                sendIntent.setAction(Intent.ACTION_SEND);
                                sendIntent.putExtra(Intent.EXTRA_TEXT, img);
                                sendIntent.setPackage("com.twitter.android");
                                startActivityForResult(sendIntent, TWEET_COMPOSER_REQUEST_CODE);
                                Share(postid, postUid, status);


                                break;
                            }
                            case "4":

                            {
                                Share(postid, postUid, status);
                                break;
                            }

                        }
                        //Toast.makeText(context, res_msg, Toast.LENGTH_SHORT).show();
                        //Acitvity_comunator.beans_Count(context, obj_dialog, pref, array_liist);
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

    public void Share(String postid, String postUid, final String status) {
        obj_dialog.show();
        final ArrayList<Model_profile> Array_user = new ArrayList<>();
        final Model_profile model = new Model_profile();
        model.setUserid(postid);
        model.setProfile(postUid);
        model.setStatus(status);
        Array_user.add(model);
        final String url = Configr.app_url + "share";
        String json = "";
        json = JSON.add(Array_user, pref, "share");
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
                                break;
                            }
                            case "2": {


                                break;


                            }
                            case "3": {

                                break;
                            }
                            case "4": {
                                DialogBox.alert_popup(context, res_msg);
                                break;
                            }

                        }
                        DialogBox.alert_popup(context, res_msg);
                        // Acitvity_comunator.beans_Count(context, obj_dialog, pref, array_liist);
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

    public void getposturl(final String postid, final String userid, final String share_value, final String description, final String urll) {


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

                            Check_share(postid, userid, "1", description, post_url);
                        } else if (share_value.equals("2")) {
                            Check_share(postid, userid, "2", description, post_url);

                        } else if (share_value.equals("3")) {
                            Check_share(postid, userid, "3", description, post_url);
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


}
