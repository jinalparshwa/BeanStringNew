package com.beanstringnew.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.beanstringnew.Controller.Activity_indicator;
import com.beanstringnew.Controller.Configr;
import com.beanstringnew.Controller.Connection;
import com.beanstringnew.Controller.DialogBox;
import com.beanstringnew.Controller.Fb_invites_beans;
import com.beanstringnew.Json_model.JSON;
import com.beanstringnew.Model.Model_State_city;
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
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import bolts.AppLinks;

/**
 * Created by Admin on 7/22/2017.
 */

public class My_profile_activity extends AppCompatActivity {

    LinearLayout editprofile;
    TextView fname, lname;
    Pref_Master pref;
    Context context = this;
    LinearLayout ll_my_followers;
    LinearLayout ll_my_following;
    LinearLayout ll_my_higest_beans;
    LinearLayout ll_upload_photo_and_video;
    LinearLayout ll_my_beans;
    LinearLayout ll_post;
    LinearLayout logout;
    ImageView profile;
    TextView mybeans_count, highest_count, follower_count, following_count;
    ArrayList<Model_profile> Array_user = new ArrayList<>();
    ArrayList<Model_State_city> array = new ArrayList<>();
    ArrayList<Model_State_city> array1 = new ArrayList<>();
    TextView location;
    Activity_indicator obj_dialog;
    LinearLayout ll_advertise;
    LinearLayout ll_platform;
    LinearLayout ll_page_guideline;
    LinearLayout ll_privacy_policy;
    LinearLayout ll_terms_condition;
    LinearLayout ll_beanstring;
    ImageView facebook_invite, drawer, imageView, notification, search, close;
    LinearLayout llmybean;
    TextView upload_count, noti_count;
    CallbackManager callbackManager;
    AppInviteDialog appInviteDialog;
    String image_url = "";


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_profile);

        FacebookSdk.sdkInitialize(context);
        callbackManager = CallbackManager.Factory.create();

        pref = new Pref_Master(context);
        obj_dialog = new Activity_indicator(context);
        obj_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        FacebookSdk.sdkInitialize(context);
        callbackManager = CallbackManager.Factory.create();
        Uri targetUrl = AppLinks.getTargetUrlFromInboundIntent(this, getIntent());
        if (targetUrl != null) {
            Log.i("Activity", "App Link Target URL: " + targetUrl.toString());
        }
        editprofile = (LinearLayout) findViewById(R.id.editprofile);
        fname = (TextView) findViewById(R.id.fname);
        lname = (TextView) findViewById(R.id.lname);
        profile = (ImageView) findViewById(R.id.profile);

        logout = (LinearLayout) findViewById(R.id.logout);
        ll_my_followers = (LinearLayout) findViewById(R.id.ll_my_followers);
        ll_my_following = (LinearLayout) findViewById(R.id.ll_my_following);
        ll_my_higest_beans = (LinearLayout) findViewById(R.id.ll_my_higest_beans);
        ll_upload_photo_and_video = (LinearLayout) findViewById(R.id.ll_upload_photo_and_video);
        ll_my_beans = (LinearLayout) findViewById(R.id.ll_my_beans);
        ll_post = (LinearLayout) findViewById(R.id.ll_post);
        mybeans_count = (TextView) findViewById(R.id.mybeans_count);
        highest_count = (TextView) findViewById(R.id.highest_count);
        follower_count = (TextView) findViewById(R.id.follower_count);
        following_count = (TextView) findViewById(R.id.following_count);
        location = (TextView) findViewById(R.id.location);

        ll_advertise = (LinearLayout) findViewById(R.id.ll_advertise);
        ll_platform = (LinearLayout) findViewById(R.id.ll_platform);
        ll_page_guideline = (LinearLayout) findViewById(R.id.ll_page_guideline);
        ll_privacy_policy = (LinearLayout) findViewById(R.id.ll_privacy_policy);
        ll_terms_condition = (LinearLayout) findViewById(R.id.ll_terms_condition);
        ll_beanstring = (LinearLayout) findViewById(R.id.ll_beanstring);
        close = (ImageView) findViewById(R.id.close);

        editprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, Change_password.class);
                startActivity(i);

            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater li = LayoutInflater.from(context);
                v = li.inflate(R.layout.alert_popup, null);
                final android.support.v7.app.AlertDialog alert = new android.support.v7.app.AlertDialog.Builder(context).setView(v).show();
                alert.setCancelable(false);
                alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                TextView ok = (TextView) v.findViewById(R.id.con_ok);
                TextView cancel = (TextView) v.findViewById(R.id.con_cancel);
                TextView txt = (TextView) v.findViewById(R.id.txt);
                txt.setText("Are you sure you want to exit?");

                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Sign_out();
                        alert.dismiss();
                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alert.dismiss();
                    }
                });

            }
        });

        ll_my_followers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, Follower_list_activity.class);
                i.putExtra("id", pref.getStr_Userid());
                startActivity(i);
            }
        });


        ll_my_following.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, Following_list_activity.class);
                i.putExtra("id", pref.getStr_Userid());
                startActivity(i);

            }
        });

        ll_my_higest_beans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(context, Higest_beans_list.class);
                startActivity(i);


            }
        });

        ll_my_beans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, My_Beans_Activity_new.class);
                startActivity(i);
            }
        });


        ll_upload_photo_and_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, Upload_image_and_video_activity.class);
                startActivity(i);


            }
        });
        ll_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, MyPost_activity.class);
                startActivity(i);

            }
        });
        ll_beanstring.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, About_Beanstring.class);
                startActivity(i);

            }
        });
        ll_advertise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, Advertise_guidelines.class);
                startActivity(i);

            }
        });
        ll_platform.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, Beanstring_policy.class);
                startActivity(i);

            }
        });
        ll_page_guideline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, Page_guidelines.class);
                startActivity(i);

            }
        });
        ll_privacy_policy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, Privacy_policy.class);
                startActivity(i);

            }
        });
        ll_terms_condition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, Terms_conditions.class);
                startActivity(i);

            }
        });
        upload_count = (TextView) findViewById(R.id.upload_count);
        noti_count = (TextView) findViewById(R.id.noti_count);


        imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(My_profile_activity.this, Home_Social.class);
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
                Intent intent = new Intent(My_profile_activity.this, Notification_activity.class);
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
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(My_profile_activity.this, Home_Social.class);
                intent.putExtra("fragmentcode", Configr.Fragment_ID.MainFragment);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
        llmybean = (LinearLayout) findViewById(R.id.llmybean);
        llmybean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(My_profile_activity.this, My_Beans_Activity_new.class);
                startActivity(intent);
                finish();
            }
        });

        search = (ImageView) findViewById(R.id.search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(My_profile_activity.this, Search_activity.class);
                startActivity(i);
                finish();
            }
        });

        Myapplication.getInstance().trackScreenView("My Profile Screen");
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
        Intent i = new Intent(My_profile_activity.this, Home_Social.class);
        i.putExtra("fragmentcode", Configr.Fragmentt_ID.Main);
        startActivity(i);
        finish();
    }

    @Override
    public void onResume() {
        super.onResume();
        beans_Count();
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
                            model.setFname(jobj1.getString("fname"));
                            model.setLname(jobj1.getString("lname"));
                            model.setCity(jobj1.getString("city"));
                            model.setCollege(jobj1.getString("collage"));
                            model.setFollowing(jobj1.getString("following"));
                            model.setFollower(jobj1.getString("followers"));

                            fname.setText(jobj1.getString("fname"));
                            lname.setText(jobj1.getString("lname"));
                            Log.e("Cityy", jobj1.getString("city"));
                            location.setText(jobj1.getString("city"));
                            image_url = jobj1.getString("url");

                            Log.e("Profile", jobj1.getString("propic"));

                            if (jobj1.getString("propic").equals("")) {
                                profile.setImageResource(R.drawable.default_imggg);
                            } else {
                                Picasso.with(context).load(jobj1.getString("propic")).skipMemoryCache().into(profile);
                            }

                            if (jobj1.getString("following").equals("0")) {
                                following_count.setText("");
                            } else {
                                following_count.setText(jobj1.getString("following"));
                            }

                            if (jobj1.getString("followers").equals("0")) {
                                follower_count.setText("");
                            } else {
                                follower_count.setText(jobj1.getString("followers"));
                            }
                            model.setHighest(jobj1.getString("highestbeans"));
                            mybeans_count.setText(jobj1.getString("beans"));
                            highest_count.setText(jobj1.getString("highestbeans"));
                            model.setNoti_count(jobj1.getString("notification"));

                            if (model.getNoti_count().equals("0")) {
                                noti_count.setVisibility(View.GONE);
                            } else {
                                noti_count.setVisibility(View.VISIBLE);
                                noti_count.setText(jobj1.getString("notification"));
                            }
                            upload_count.setText(jobj1.getString("beans"));


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

    public void Sign_out() {

        obj_dialog.show();

        String url = Configr.app_url + "signout";

        String json = "";

        json = JSON.add(Array_user, pref, "signout");

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

                        pref.clear_pref();
                        Intent i = new Intent(context, Login.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                        System.exit(0);


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
                obj_dialog.dismiss();
                Toast.makeText(context, "" + Configr.Message, Toast.LENGTH_SHORT).show();
            }
        };
        Connection.postconnection(url, param, Configr.getHeaderParam(), context, lis_pat, lis_error);

    }


}
