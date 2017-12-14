package com.beanstringnew.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.MediaController;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.beanstringnew.Controller.Acitvity_comunator;
import com.beanstringnew.Controller.Activity_indicator;
import com.beanstringnew.Controller.Configr;
import com.beanstringnew.Controller.Connection;
import com.beanstringnew.Controller.DialogBox;
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
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.google.android.gms.plus.PlusShare;
import com.google.firebase.crash.FirebaseCrash;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Abc on 10/25/2016.
 */

public class Play_video_activity extends AppCompatActivity implements Thread.UncaughtExceptionHandler {

    VideoView video_view;
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
    ImageView img_delete;
    PopupMenu menu_delete;
    CallbackManager callbackManager;
    public int GOOGLE_PLUS_REQUEST_CODE = 2;
    public int TWEET_COMPOSER_REQUEST_CODE = 3;
    String post_url = "";
    MediaController mc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.play_video_activity);
        pref = new Pref_Master(context);

        model = (Model_Play_video) getIntent().getSerializableExtra("url");
        //  }
        obj_dialog = new Activity_indicator(context);
        obj_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));

        FacebookSdk.sdkInitialize(context);
        callbackManager = CallbackManager.Factory.create();


        video_view = (VideoView) findViewById(R.id.video_view);
        img_share_bean = (ImageView) findViewById(R.id.img_share_bean);
        img_share_fb = (ImageView) findViewById(R.id.img_share_fb);
        img_share_twitter = (ImageView) findViewById(R.id.img_share_twitter);
        img_share_gplus = (ImageView) findViewById(R.id.img_share_gplus);
        back_button = (ImageView) findViewById(R.id.back_button);
        img_delete = (ImageView) findViewById(R.id.img_delete);

        if (model.getDel_valu().equals("1")) {
            img_delete.setVisibility(View.VISIBLE);
        } else {
            img_delete.setVisibility(View.INVISIBLE);
        }

        img_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                menu_delete.show();
            }
        });

        menu_delete = new PopupMenu(context, img_delete);
        createPopup();


        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        MediaController mediaController = new MediaController(Play_video_activity.this);
        mediaController.setAnchorView(video_view);
        String vpath = model.getUrl().replaceAll(" ", "%20");
        video_view.setVideoPath(vpath);
        video_view.requestFocus();
        //video_view.setMediaController(mediaController);
        obj_dialog.show();
        video_view.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                obj_dialog.dismiss();
//                mp.start();

                mp.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                    @Override
                    public void onVideoSizeChanged(MediaPlayer mp,
                                                   int width, int height) {
                            /*
                             * add media controller
                             */
                        mc = new MediaController(Play_video_activity.this);
                        video_view.setMediaController(mc);
                            /*
                             * and set its position on screen
                             */
                        mc.setAnchorView(video_view);

                        ((ViewGroup) mc.getParent()).removeView(mc);

                        ((FrameLayout) findViewById(R.id.controllerAnchor))
                                .addView(mc);
                        mc.setVisibility(View.INVISIBLE);
                    }
                });
                video_view.start();


            }
        });


        video_view.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                if (mc != null) {
                    mc.setVisibility(View.VISIBLE);
                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            mc.setVisibility(View.INVISIBLE);
                        }
                    }, 2000);
                }

                return false;
            }
        });


        video_view.setOnInfoListener(new MediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(MediaPlayer mp, int what, int i1) {
                if (what == MediaPlayer.MEDIA_INFO_BUFFERING_START) {

                    obj_dialog.show();
                }
                if (what == MediaPlayer.MEDIA_INFO_BUFFERING_END) {
                    obj_dialog.dismiss();
                }
                return true;
            }
        });

        video_view.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                Toast.makeText(context, "Video Format Error", Toast.LENGTH_LONG).show();
                return true;
            }
        });

        img_share_bean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Bean_image", model.getUrl());
                Check_share(model.getPostid(), model.getPostuserid(), "4", model.getDescription(), model.getUrl());
                //api_f.run();
            }
        });

        img_share_fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Faceboook_image", model.getUrl());
                boolean installed = appInstalledOrNot("com.facebook.katana");
                if (installed) {
                    getposturl(model.getPostid(), model.getPostuserid(), "1", model.getDescription(), model.getUrl());
                } else {
                    Toast.makeText(context, "App is not currently installed on your phone", Toast.LENGTH_LONG).show();
                }
            }
        });

        img_share_twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Twittter_image", model.getUrl());
                boolean installed = appInstalledOrNot("com.twitter.android");
                if (installed) {
                    getposturl(model.getPostid(), model.getPostuserid(), "3", model.getDescription(), model.getUrl());
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
                    getposturl(model.getPostid(), model.getPostuserid(), "2", model.getDescription(), model.getUrl());
                } else {
                    Toast.makeText(context, "App is not currently installed on your phone", Toast.LENGTH_LONG).show();
                }
            }
        });
        Myapplication.getInstance().trackScreenView("Play Video Screen");
        //    }

    }

    public void createPopup() {
        menu_delete = new PopupMenu(context, img_delete);
        menu_delete.getMenu().add(Menu.NONE, 0, Menu.NONE, "Delete");
        menu_delete.setOnMenuItemClickListener(onClick_State_popupmenu);
    }

    PopupMenu.OnMenuItemClickListener onClick_State_popupmenu = new PopupMenu.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            LayoutInflater li = LayoutInflater.from(context);
            View v = li.inflate(R.layout.alert_popup, null);
            final android.support.v7.app.AlertDialog alert = new android.support.v7.app.AlertDialog.Builder(context).setView(v).show();
            alert.setCancelable(false);
            alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            TextView ok = (TextView) v.findViewById(R.id.con_ok);
            TextView cancel = (TextView) v.findViewById(R.id.con_cancel);
            TextView txt = (TextView) v.findViewById(R.id.txt);
            txt.setText("Are you sure you want to Delete?");

            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Delete_post(model.getPostid());
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

    public void Delete_post(String postid) {

        ArrayList<Model_user> array1 = new ArrayList<>();
        Model_user model = new Model_user();
        model.setPostid(postid);
        array1.add(model);

        // obj_dialog.show();
        String url = Configr.app_url + "deletepost";
        String json = "";

        json = JSON.add_json(array1, pref, "deletepost");

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
                        //   Toast.makeText(context, res_msg, Toast.LENGTH_SHORT).show();
                        finish();
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

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        FirebaseCrash.report(e);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
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


    public void Check_share(final String postid, final String postUid, final String status, final String des, final String v_url) {
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
                                        .setContentUrl(Uri.parse(v_url))
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
                                sendIntent.putExtra(Intent.EXTRA_TEXT, v_url);
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
                                sendIntent.putExtra(Intent.EXTRA_TEXT, v_url);
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
                        // Toast.makeText(context, res_msg, Toast.LENGTH_SHORT).show();
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
