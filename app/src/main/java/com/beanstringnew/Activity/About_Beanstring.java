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
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.beanstringnew.Adapter.ExpandableListAdapter;
import com.beanstringnew.Controller.Acitvity_comunator;
import com.beanstringnew.Controller.Activity_indicator;
import com.beanstringnew.Controller.Configr;
import com.beanstringnew.Controller.Connection;
import com.beanstringnew.Controller.DialogBox;
import com.beanstringnew.Controller.Fb_invites_beans;
import com.beanstringnew.Json_model.JSON;
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
import com.google.firebase.crash.FirebaseCrash;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import bolts.AppLinks;

public class About_Beanstring extends AppCompatActivity implements Thread.UncaughtExceptionHandler {

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    ImageView search, facebook_invite, drawer, imageView, notification;
    TextView upload_count;
    Activity_indicator obj_dialog;
    Pref_Master pref;
    Context context = this;
    LinearLayout llmybean;
    TextView noti_count;
    CallbackManager callbackManager;
    AppInviteDialog appInviteDialog;

    ArrayList<Model_profile> Array_user = new ArrayList<>();
    String image_url = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about__beanstring);
        pref = new Pref_Master(context);

        obj_dialog = new Activity_indicator(context);
        obj_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));

        FacebookSdk.sdkInitialize(context);
        callbackManager = CallbackManager.Factory.create();
        Uri targetUrl = AppLinks.getTargetUrlFromInboundIntent(this, getIntent());
        if (targetUrl != null) {
            Log.i("Activity", "App Link Target URL: " + targetUrl.toString());
        }
        expListView = (ExpandableListView) findViewById(R.id.lvExp);
        prepareListData();
        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);
        expListView.setAdapter(listAdapter);

        upload_count = (TextView) findViewById(R.id.upload_count);
        noti_count = (TextView) findViewById(R.id.noti_count);
        imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(About_Beanstring.this, Home_Social.class);
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
                Intent intent = new Intent(About_Beanstring.this, Notification_activity.class);
                noti_count.setVisibility(View.GONE);
                startActivity(intent);
                finish();
            }
        });
        search = (ImageView) findViewById(R.id.search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(About_Beanstring.this, Search_activity.class);
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
                Intent intent = new Intent(About_Beanstring.this, My_profile_activity.class);
                startActivity(intent);
                finish();
            }
        });

        llmybean = (LinearLayout) findViewById(R.id.llmybean);
        llmybean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(About_Beanstring.this, My_Beans_Activity_new.class);
                startActivity(intent);
                finish();
            }
        });
        Myapplication.getInstance().trackScreenView("About Screen");
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

    }


    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        listDataHeader.add("What is Beanstring?");
        listDataHeader.add("How does it work?");
        listDataHeader.add("Why should I earn Beans?");
        listDataHeader.add("Can I only see what’s happening in my college on Beanstring?");
        listDataHeader.add("Do I have to pay to join Beanstring?");
        listDataHeader.add("Can I encash my Beans?");

        List<String> top250 = new ArrayList<String>();
        top250.add("Beanstring is YOUR space, it is an extension of your college & your friends to an online space where you can share, exchange, upload, comment on and discuss anything under the sun!");

        List<String> nowShowing = new ArrayList<String>();
        nowShowing.add("First, follow the instructions and register on beanstring.com\n" +
                "Once you are on Beanstring, EVERYONE who is registered from the same college as you is your ‘friend’ by default. Just like your college canteen, everyone from your college can see you, and you can see everyone – you may not know each other, but you can see each other!\n" +
                "Any discussion you start, pictures / videos you upload, groups you form, or events you talk about – EVERYONE from your college can see, and comment on these. Each time someone comments on any activity you do on beanstring, you earn ‘Beans’. Likewise, when you click on an image or video uploaded by someone or participate in a discussion started by them, they earn Beans!\n" +
                "Simple!\n");

        List<String> comingSoon = new ArrayList<String>();
        comingSoon.add(" Your Beans are like actual currency – you can visit the Bean Mall and buy cool merchandize and stuff, by using your beans! So, chat, discuss and upload more pictures and videos, to earn more Beans which can be used to buy kickass stuff, ranging from movie tickets, restaurant vouchers, stylish apparel to mobile phones at the Bean Mall!");

        List<String> top2 = new ArrayList<String>();
        top2.add("Not really – once a discussion topic / picture / video gains popularity = earns enough Beans in your college, it will be ‘visible’ to ALL users of ALL colleges in your city! So, the more people react to your discussions / pictures / videos on Beanstring, the more are your chances of a wider number of persons seeing what you do, and the more Beans you earn…\n" +
                " For example, if you start a discussion titled ‘I want shorter lectures and longer breaks’, and there are 15 persons from your college who comment on this, automatically the discussion will then be visible to all users of all colleges in your city! Once 50 persons from your city comment on this discussion, automatically All users ALL over India can view this discussion, and comment on it, thus tremendously increasing the number of Beans you can earn\n");

        List<String> top3 = new ArrayList<String>();
        top3.add(" Nope. Nah. Nada. Nahin. Vaenda. Nako. Nyet etc. etc…you get the drift – Beanstring is FREE!!! Not just that, you actually get a joining bonus of 5 Beans when you sign up. Also, refer a friend, and you earn a further 5 Beans, if your friend joins Beanstring!");

        List<String> top4 = new ArrayList<String>();
        top4.add(" You can use your Beans to buy stuff at the Bean Mall. You can gift Beans to another member on Beanstring. You can use your Beans to place a classified ad on Beanstring, or to promote your event across your city / across India. You can also donate Beans to charity, at the Beanstring Foundation, where equivalent amount of money will be donated towards education and healthcare of under – privileged persons! Point – you can do pretty much anything with your Beans on beanstring.com, except encashing them");

        listDataChild.put(listDataHeader.get(0), top250);
        listDataChild.put(listDataHeader.get(1), nowShowing);
        listDataChild.put(listDataHeader.get(2), comingSoon);
        listDataChild.put(listDataHeader.get(3), top2);
        listDataChild.put(listDataHeader.get(4), top3);
        listDataChild.put(listDataHeader.get(5), top4);
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
