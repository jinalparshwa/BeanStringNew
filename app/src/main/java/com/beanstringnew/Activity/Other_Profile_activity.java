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
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
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
import com.beanstringnew.Controller.Acitvity_comunator;
import com.beanstringnew.Controller.Activity_indicator;
import com.beanstringnew.Controller.Configr;
import com.beanstringnew.Controller.Connection;
import com.beanstringnew.Controller.DialogBox;
import com.beanstringnew.Controller.Fb_invites_beans;
import com.beanstringnew.Fragment.Other_profile_latestpost;
import com.beanstringnew.Fragment.Other_profile_top_post;
import com.beanstringnew.Interface.Share;
import com.beanstringnew.Json_model.JSON;
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
import com.facebook.share.Sharer;
import com.facebook.share.model.AppInviteContent;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.AppInviteDialog;
import com.facebook.share.widget.ShareDialog;
import com.google.android.gms.plus.PlusShare;
import com.google.firebase.crash.FirebaseCrash;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bolts.AppLinks;


/**
 * Created by Abc on 10/28/2016.
 */

public class Other_Profile_activity extends AppCompatActivity implements Thread.UncaughtExceptionHandler, Share {

    Pref_Master pref;
    Context context = this;
    ImageView search, facebook_invite, drawer, imageView, notification;
    LinearLayout llmybean;
    String Userid;
    ImageView pics;
    TextView fname, lname, location;
    TextView beans_total, follower_total, following_total;
    ArrayList<Model_profile> Array_user = new ArrayList<>();
    ArrayList<Model_State_city> array = new ArrayList<>();
    ArrayList<Model_State_city> array1 = new ArrayList<>();
    ArrayList<Model_user> array111 = new ArrayList<>();
    public static TextView other;
    Activity_indicator obj_dialog;
    TextView folunfol;
    String foll = "";
    String blockun = "";
    String res_message;
    String selectedcityid;
    public static TextView noti_count;
    String image;
    LinearLayout ll_follower;
    LinearLayout ll_following;
    TextView txt_block;
    Model_user model_user;
    CallbackManager callbackManager;
    AppInviteDialog appInviteDialog;
    String image_url = "";
    public int GOOGLE_PLUS_REQUEST_CODE = 2;
    public int TWITTER_REQUEST_CODE = 3;
    TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.other_profile_layout);
        pref = new Pref_Master(context);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Userid = extras.getString("id");
            Log.e("uuuuuuuuuuuuuuuuuuu", "" + Userid);

        } else {
            Log.e("elseeeeeee", "Enter");
        }


        final Model_profile model = new Model_profile();
        model.setUserid(Userid);
        Log.e("vvvvvvvvvvvv", ":" + model.getUserid());
        Array_user.add(model);


        obj_dialog = new Activity_indicator(context);
        obj_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));

        FacebookSdk.sdkInitialize(context);
        callbackManager = CallbackManager.Factory.create();
        Uri targetUrl = AppLinks.getTargetUrlFromInboundIntent(this, getIntent());
        if (targetUrl != null) {
            Log.i("Activity", "App Link Target URL: " + targetUrl.toString());
        }

        beans_Count();
        get_state_city();
        HTTP_API_getdata();


        pics = (ImageView) findViewById(R.id.pics);
        fname = (TextView) findViewById(R.id.fname);
        lname = (TextView) findViewById(R.id.lname);
        location = (TextView) findViewById(R.id.location);
        //college = (TextView) findViewById(R.id.college);
        beans_total = (TextView) findViewById(R.id.beans_total);
        follower_total = (TextView) findViewById(R.id.follower_total);
        following_total = (TextView) findViewById(R.id.following_total);
        other = (TextView) findViewById(R.id.other);
        folunfol = (TextView) findViewById(R.id.folunfol);
        noti_count = (TextView) findViewById(R.id.noti_count);
        txt_block = (TextView) findViewById(R.id.txt_block);

        ll_follower = (LinearLayout) findViewById(R.id.ll_follower);
        ll_follower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Other_Profile_activity.this, Profile_follower_list.class);
                i.putExtra("id", model_user.getUserid());
                startActivity(i);

            }
        });

        ll_following = (LinearLayout) findViewById(R.id.ll_following);
        ll_following.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Other_Profile_activity.this, Profile_Following_list.class);
                i.putExtra("id", model_user.getUserid());
                startActivity(i);
            }
        });


        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        createTabIcons();


        imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Other_Profile_activity.this, Home_Social.class);
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
                Intent intent = new Intent(Other_Profile_activity.this, Notification_activity.class);
                noti_count.setVisibility(View.GONE);
                startActivity(intent);
                finish();
            }
        });
        search = (ImageView) findViewById(R.id.search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Other_Profile_activity.this, Search_activity.class);
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
                Intent intent = new Intent(Other_Profile_activity.this, My_profile_activity.class);
                startActivity(intent);
                finish();
            }
        });

        llmybean = (LinearLayout) findViewById(R.id.llmybean);
        llmybean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Other_Profile_activity.this, My_Beans_Activity_new.class);
                intent.putExtra("fragmentcode", Configr.Fragment_ID.MyBean);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });


        folunfol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (foll.equals("0")) {
                    Log.e("if_follow", folunfol.getText().toString());

                    LayoutInflater li = LayoutInflater.from(context);
                    v = li.inflate(R.layout.alert_popup, null);
                    final android.support.v7.app.AlertDialog alert = new android.support.v7.app.AlertDialog.Builder(context).setView(v).show();
                    alert.setCancelable(false);
                    alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    TextView ok = (TextView) v.findViewById(R.id.con_ok);
                    TextView cancel = (TextView) v.findViewById(R.id.con_cancel);
                    TextView txt = (TextView) v.findViewById(R.id.txt);
                    txt.setText("Are you sure you want to Follow?");

                    ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            HTTP_API_follow_Call(Userid);
                            alert.dismiss();
                        }
                    });
                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            alert.dismiss();
                        }
                    });


                } else {
                    Log.e("else follow", folunfol.getText().toString());
                    LayoutInflater li = LayoutInflater.from(context);
                    v = li.inflate(R.layout.alert_popup, null);
                    final android.support.v7.app.AlertDialog alert = new android.support.v7.app.AlertDialog.Builder(context).setView(v).show();
                    alert.setCancelable(false);
                    alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    TextView ok = (TextView) v.findViewById(R.id.con_ok);
                    TextView cancel = (TextView) v.findViewById(R.id.con_cancel);
                    TextView txt = (TextView) v.findViewById(R.id.txt);
                    txt.setText("Are you sure you want to UnFollow?");

                    ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            HTTP_API_unfollow_Call(Userid);
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
            }
        });
        txt_block.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (blockun.equals("0")) {
                    Log.e("if_follow", folunfol.getText().toString());

                    LayoutInflater li = LayoutInflater.from(context);
                    v = li.inflate(R.layout.alert_popup, null);
                    final android.support.v7.app.AlertDialog alert = new android.support.v7.app.AlertDialog.Builder(context).setView(v).show();
                    alert.setCancelable(false);
                    alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    TextView ok = (TextView) v.findViewById(R.id.con_ok);
                    TextView cancel = (TextView) v.findViewById(R.id.con_cancel);
                    TextView txt = (TextView) v.findViewById(R.id.txt);
                    txt.setText("Are you sure you want to Block?");

                    ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Http_Block(Userid);
                            alert.dismiss();
                        }
                    });
                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            alert.dismiss();
                        }
                    });


                } else {
                    Log.e("else follow", folunfol.getText().toString());

                    LayoutInflater li = LayoutInflater.from(context);
                    v = li.inflate(R.layout.alert_popup, null);
                    final android.support.v7.app.AlertDialog alert = new android.support.v7.app.AlertDialog.Builder(context).setView(v).show();
                    alert.setCancelable(false);
                    alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    TextView ok = (TextView) v.findViewById(R.id.con_ok);
                    TextView cancel = (TextView) v.findViewById(R.id.con_cancel);
                    TextView txt = (TextView) v.findViewById(R.id.txt);
                    txt.setText("Are you sure you want to Unblock?");

                    ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Http_UnBlock(Userid);
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

            }
        });

        Myapplication.getInstance().trackScreenView("Other Profile Screen");


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
        if (requestCode == GOOGLE_PLUS_REQUEST_CODE) {

            if (resultCode == Activity.RESULT_OK) {
                Log.e("Success", "success");

                Login_social(pref.getStr_postid(), pref.getStr_post_uid(), "2");


                // onTwitterSuccess();

            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.e("Failure", "Failure");

                // onTwitterCancel();
            }
        } else if (requestCode == TWITTER_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Log.e("twitter_succes", "twitter_succes");

            } else {
                Log.e("twitter_failure", "twitter_failure");
            }
        }

    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Other_Profile_activity.this, Home_Social.class);
        intent.putExtra("fragmentcode", Configr.Fragment_ID.MainFragment);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    public void HTTP_API_getdata() {

        obj_dialog.show();
        Log.e("getprofilllll", "Enter");

        String url = Configr.app_url + "getprofile";

        Log.e("url", ":" + url);

        Array_user.clear();

        final Model_profile model = new Model_profile();
        model.setUserid(Userid);
        Log.e("vvvvvvvvvvvv", ":" + model.getUserid());
        Array_user.add(model);

        String json = "";
        Log.e("vikas data userid", ":" + Array_user.get(0).getUserid());

        json = JSON.add(Array_user, pref, "getprofile");

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
                        JSONObject jarray = new JSONObject(jobj.getString("data"));
                        JSONObject jobjuser = new JSONObject(jarray.getString("user"));

                        model_user = new Model_user();
                        model_user.setUserid(jobjuser.getString("id"));
                        Log.e("jinal_id", ":" + model_user.getUserid());
                        model_user.setFname(jobjuser.getString("fname"));
                        model_user.setLname(jobjuser.getString("lname"));


                        fname.setText(jobjuser.getString("id"));
                        fname.setText(jobjuser.getString("fname"));
                        lname.setText(jobjuser.getString("lname"));
                        selectedcityid = jobjuser.getString("city");


                        if (jobjuser.getString("propic").equals("")) {
                            pics.setImageResource(R.drawable.default_imggg);
                        } else {
                            Picasso.with(context).load(jobjuser.getString("propic")).into(pics);
                            image = jobjuser.getString("propic");
                        }

                        beans_total.setText(jobjuser.getString("beans"));
                        following_total.setText(jobjuser.getString("following"));
                        follower_total.setText(jobjuser.getString("followers"));

                        for (Model_State_city model : array1) {
                            if (model.getCityid().equals(selectedcityid)) {
                                location.setText(model.getCityname());
                            }
                        }

                        foll = jobjuser.getString("status");
                        if (foll.equals("0")) {
                            Log.e("if_status", foll);
                            folunfol.setText("+Follow");
                        } else {
                            Log.e("else_status", foll);
                            folunfol.setText("Unfollow");
                        }

                        blockun = jobjuser.getString("blockstatus");
                        if (blockun.equals("0")) {
                            txt_block.setText("Block");
                        } else {
                            txt_block.setText("Unblock");
                        }


                        array111.add(model_user);

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


    private void createTabIcons() {

        TextView tabOne = (TextView) LayoutInflater.from(context).inflate(R.layout.custom_tab, null);
        tabOne.setText("Latest Post");
        tabLayout.getTabAt(0).setCustomView(tabOne);

        TextView tabTwo = (TextView) LayoutInflater.from(context).inflate(R.layout.custom_tab, null);
        tabTwo.setText("Top Post");
        tabLayout.getTabAt(1).setCustomView(tabTwo);
    }


    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.addFragment(new Other_profile_latestpost(Userid, this), "Latest Post");
        adapter.addFragment(new Other_profile_top_post(Userid, this), "Top Post");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void fb_share(final String postid, final String postUid, final String status, final String des, final String img, final String modelStatus, final String v_url) {
        if (status.equals("1")) {
            Log.e("FB_share", "fb_share");

            callbackManager = CallbackManager.Factory.create();

            FacebookCallback<Sharer.Result> sharecallback = new FacebookCallback<Sharer.Result>() {
                @Override
                public void onSuccess(Sharer.Result result) {
                    Login_social(postid, postUid, status);
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

            ShareLinkContent shareLinkContent = new ShareLinkContent.Builder()
                    .setContentTitle("Beanstring")
                    .setContentDescription(des)
                    .setContentUrl(Uri.parse(v_url))
                    //.setImageUrl(Configr.onShareItem(img, context))
                    .build();
            ShareDialog shareDialog = new ShareDialog((Activity) context);
            shareDialog.registerCallback(callbackManager, sharecallback);
            shareDialog.show(shareLinkContent, ShareDialog.Mode.AUTOMATIC);

            //  }
        } else if (status.equals("2")) {

            Intent sendIntent = new Intent();
            sendIntent.setType("text/plain");
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, v_url);
            sendIntent.setPackage("com.google.android.apps.plus");
            startActivityForResult(sendIntent, GOOGLE_PLUS_REQUEST_CODE);

        } else if (status.equals("3")) {
            Intent sendIntent = new Intent();
            sendIntent.setType("text/plain");
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, v_url);
            sendIntent.setPackage("com.twitter.android");
            startActivityForResult(sendIntent, TWITTER_REQUEST_CODE);
            Login_social(postid, postUid, "3");

        } else if (status.equals("4")) {
            Login_social(postid, postUid, "4");
        }
    }

    public void Login_social(String postid, String postUid, final String status) {

        obj_dialog.show();

        ArrayList<Model_profile> Array_user = new ArrayList<>();
        final Model_profile model = new Model_profile();
        model.setUserid(postid);
        model.setProfile(postUid);
        model.setStatus(status);
        Array_user.add(model);

        String url = Configr.app_url + "share";

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
                                beans_Count();
                                break;
                            }
                            case "2": {


                                break;
                            }
                            case "3": {


                                break;
                            }
                            case "4": {
                                // DialogBox.alert_popup(context, res_msg);
                                break;
                            }
                        }
                        DialogBox.alert_popup(context, res_msg);
                        beans_Count();
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


    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
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

                            other.setText(jobj1.getString("beans"));
                            // noti_count.setText(jobj1.getString("notification"));
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

    private void HTTP_API_unfollow_Call(String id) {

        obj_dialog.show();

        res_message = "Server Error...";

        String url = Configr.app_url + "unfollow";
        JSONObject jobj_loginuser = new JSONObject();
        try {

            JSONObject jobj_row = new JSONObject();

            jobj_row.put("sender", pref.getStr_Userid());
            jobj_row.put("receiver", id);

            JSONArray jarray_loginuser = new JSONArray();
            jarray_loginuser.put(jobj_row);

            jobj_loginuser.put("unfollow", jarray_loginuser);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Map<String, String> params = new HashMap<>();
        params.put("data", "" + jobj_loginuser.toString().replaceAll("\\\\", ""));
        Response.Listener<String> lis_res = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                obj_dialog.dismiss();
                Log.e("Response", response);


                String toastMsg = "";
                toastMsg = "";


                try {
                    JSONObject jobj = new JSONObject(response);
                    String res_flag = jobj.getString("status");
                    String res_msg = jobj.getString("message");
                    String otp = "", userid = "";

                    if (res_flag.equals("200")) {
                        Log.e("unfollow_Profileeeeeee", "Enterr");
                        HTTP_API_getdata();
                     /*   Model_profile model = new Model_profile();
                        model.setProfile(Userid);
                        Array_user.add(model);
                        HTTP_API_getdata();*/

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
                Log.e("response error", "" + error);
                obj_dialog.dismiss();
                Toast.makeText(context, "" + Configr.Message, Toast.LENGTH_SHORT).show();
            }
        };
        Connection.postconnection(url, params, Configr.getHeaderParam(), context, lis_res, lis_error);
    }


    private void HTTP_API_follow_Call(String id) {

        obj_dialog.show();

        res_message = "Server Error...";

        String url = Configr.app_url + "follow";
        JSONObject jobj_loginuser = new JSONObject();
        try {

            JSONObject jobj_row = new JSONObject();

            jobj_row.put("sender", pref.getStr_Userid());
            jobj_row.put("receiver", id);

            JSONArray jarray_loginuser = new JSONArray();
            jarray_loginuser.put(jobj_row);

            jobj_loginuser.put("follow", jarray_loginuser);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Map<String, String> params = new HashMap<>();
        params.put("data", "" + jobj_loginuser.toString().replaceAll("\\\\", ""));
        Response.Listener<String> lis_res = new Response.Listener<String>() {
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
                        Log.e("follow_Profileeeeeee", "Enterr");
                        HTTP_API_getdata();
                      /*  Model_profile model = new Model_profile();
                        model.setProfile(Userid);
                        Array_user.add(model);
                        HTTP_API_getdata();
*/
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
                Log.e("response error", "" + error);
                obj_dialog.dismiss();
                Toast.makeText(context, "" + Configr.Message, Toast.LENGTH_SHORT).show();
            }
        };
        Connection.postconnection(url, params, Configr.getHeaderParam(), context, lis_res, lis_error);
    }

    private void Http_Block(String id) {

        obj_dialog.show();

        res_message = "Server Error...";

        String url = Configr.app_url + "blockuser";
        JSONObject jobj_loginuser = new JSONObject();
        try {

            JSONObject jobj_row = new JSONObject();

            jobj_row.put("userid", pref.getStr_Userid());
            jobj_row.put("blockuserid", id);

            JSONArray jarray_loginuser = new JSONArray();
            jarray_loginuser.put(jobj_row);

            jobj_loginuser.put("blockuser", jarray_loginuser);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Map<String, String> params = new HashMap<>();
        params.put("data", "" + jobj_loginuser.toString().replaceAll("\\\\", ""));
        Response.Listener<String> lis_res = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                obj_dialog.dismiss();
                Log.e("Response", response);


                String toastMsg = "";
                toastMsg = "";


                try {
                    JSONObject jobj = new JSONObject(response);
                    String res_flag = jobj.getString("status");
                    String res_msg = jobj.getString("message");
                    String otp = "", userid = "";

                    if (res_flag.equals("200")) {
                        Log.e("Block", "Enterr");
                        HTTP_API_getdata();
                     /*   Model_profile model = new Model_profile();
                        model.setProfile(Userid);
                        Array_user.add(model);
                        HTTP_API_getdata();*/

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
                Log.e("response error", "" + error);
                obj_dialog.dismiss();
                Toast.makeText(context, "" + Configr.Message, Toast.LENGTH_SHORT).show();
            }
        };
        Connection.postconnection(url, params, Configr.getHeaderParam(), context, lis_res, lis_error);
    }

    private void Http_UnBlock(String id) {

        obj_dialog.show();

        res_message = "Server Error...";

        String url = Configr.app_url + "unblockuser";
        JSONObject jobj_loginuser = new JSONObject();
        try {

            JSONObject jobj_row = new JSONObject();

            jobj_row.put("userid", pref.getStr_Userid());
            jobj_row.put("unblockuserid", id);

            JSONArray jarray_loginuser = new JSONArray();
            jarray_loginuser.put(jobj_row);

            jobj_loginuser.put("unblockuser", jarray_loginuser);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Map<String, String> params = new HashMap<>();
        params.put("data", "" + jobj_loginuser.toString().replaceAll("\\\\", ""));
        Response.Listener<String> lis_res = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                obj_dialog.dismiss();
                Log.e("Response", response);


                String toastMsg = "";
                toastMsg = "";


                try {
                    JSONObject jobj = new JSONObject(response);
                    String res_flag = jobj.getString("status");
                    String res_msg = jobj.getString("message");
                    String otp = "", userid = "";

                    if (res_flag.equals("200")) {
                        Log.e("UnBlock", "Enterr");
                        HTTP_API_getdata();
                     /*   Model_profile model = new Model_profile();
                        model.setProfile(Userid);
                        Array_user.add(model);
                        HTTP_API_getdata();*/

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
                Log.e("response error", "" + error);
                obj_dialog.dismiss();
                Toast.makeText(context, "" + Configr.Message, Toast.LENGTH_SHORT).show();
            }
        };
        Connection.postconnection(url, params, Configr.getHeaderParam(), context, lis_res, lis_error);
    }

    public void get_state_city() {

        obj_dialog.show();

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
                obj_dialog.dismiss();
                try {
                    JSONObject jobj = new JSONObject(response);
                    String res_flag = jobj.getString("status");
                    String res_msg = jobj.getString("message");
                    if (res_flag.equals("200")) {


                        JSONObject job = new JSONObject(jobj.getString("data"));

                        JSONArray jarray1 = new JSONArray(job.getString("state"));
                        JSONArray jarray2 = new JSONArray(job.getString("city"));
                        array.clear();
                        array1.clear();

                        for (int i = 0; i < jarray1.length(); i++) {
                            Model_State_city model = new Model_State_city();
                            JSONObject jobj1 = jarray1.getJSONObject(i);
                            model.setStateid(jobj1.getString("id"));
                            model.setState_name(jobj1.getString("name"));
                            array.add(model);
                        }

                        for (int i = 0; i < jarray2.length(); i++) {
                            Model_State_city model = new Model_State_city();
                            JSONObject jobj1 = jarray2.getJSONObject(i);
                            model.setCityid(jobj1.getString("id"));
                            model.setS_id(jobj1.getString("stateid"));
                            model.setCityname(jobj1.getString("name"));
                            array1.add(model);
                        }

                        //Model_State_city model=new Model_State_city();


                        HTTP_API_getdata();

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
