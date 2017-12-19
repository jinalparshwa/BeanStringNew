package com.beanstringnew.Activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ToxicBakery.viewpager.transforms.CubeOutTransformer;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.beanstringnew.Adapter.Adapter_home_advertise;
import com.beanstringnew.Controller.Acitvity_comunator;
import com.beanstringnew.Controller.Activity_indicator;
import com.beanstringnew.Controller.Configr;
import com.beanstringnew.Controller.Connection;
import com.beanstringnew.Controller.DialogBox;
import com.beanstringnew.Controller.Fb_invites_beans;
import com.beanstringnew.Controller.FixedSpeedScroller;
import com.beanstringnew.Fragment.Followin_Fragment;
import com.beanstringnew.Fragment.Latest_Fragment;
import com.beanstringnew.Fragment.Top_Post_Fragment;
import com.beanstringnew.Interface.Share;
import com.beanstringnew.Json_model.JSON;
import com.beanstringnew.Model.Model_Product;
import com.beanstringnew.Model.Model_profile;
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
import com.facebook.share.widget.AppInviteDialog;
import com.facebook.share.widget.ShareDialog;
import com.google.firebase.crash.FirebaseCrash;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import bolts.AppLinks;


/**
 * Created by Abc on 10/18/2016.
 */

public class Home_Social extends AppCompatActivity implements Thread.UncaughtExceptionHandler, Share {

    public static final String TEST_DEVICE_ID = "37D01A552DF761E149607CA81BBA69E4";
    Context context = this;
    // FrameLayout frame_home;
    ImageView search, facebook_invite, drawer, close, imageView, notification;
    public static TextView noti_count;
    LinearLayout llmybean;
    Pref_Master pref;
    Activity_indicator obj_dialog;
    int fragmentcode = 0;
    ArrayList<Model_profile> Array_user = new ArrayList<>();
    public static TextView bean_total;
    boolean doubleBackToExitPressedOnce = false;
    private int currentPage = 0;
    Interpolator sInterpolator;
    Handler handler;
    Runnable Update;
    ArrayList<Model_Product> array = new ArrayList<>();
    RelativeLayout fab;
    ViewPager viewpager;
    CallbackManager callbackManager;
    AppInviteDialog appInviteDialog;
    String image_url = "";
    TabLayout tabLayout;
    ViewPager viewPager_one;
    ViewPagerAdapter adapter;
    public int GOOGLE_PLUS_REQUEST_CODE = 2;
    public int TWITTER_REQUEST_CODE = 3;
    private static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 55;
    public String DEFAULT_CACHE_DIR = "1";

    //private AdView mAdView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_social_activity);
        pref = new Pref_Master(context);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);


        obj_dialog = new Activity_indicator(context);
        obj_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));

        viewPager_one = (ViewPager) findViewById(R.id.viewpager_one);
        setupViewPager(viewPager_one);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager_one);

        FacebookSdk.sdkInitialize(context);
        callbackManager = CallbackManager.Factory.create();
        Uri targetUrl = AppLinks.getTargetUrlFromInboundIntent(this, getIntent());
        if (targetUrl != null) {
            Log.i("Activity", "App Link Target URL: " + targetUrl.toString());
        }
        imageView = (ImageView) findViewById(R.id.imageView);
        notification = (ImageView) findViewById(R.id.notification);
        search = (ImageView) findViewById(R.id.search);
        facebook_invite = (ImageView) findViewById(R.id.facebook_invite);
        llmybean = (LinearLayout) findViewById(R.id.llmybean);
        drawer = (ImageView) findViewById(R.id.drawer);
        close = (ImageView) findViewById(R.id.close);
        bean_total = (TextView) findViewById(R.id.bean_total);
        noti_count = (TextView) findViewById(R.id.noti_count);
        viewpager = (ViewPager) findViewById(R.id.viewpager);
        fab = (RelativeLayout) findViewById(R.id.fab);

        Model_Product model = new Model_Product();
        model.setId("0");
        array.add(model);
        get_product();

        createTabIcons();
        checkAndRequestPermissions();

        viewPager_one.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.e("onPageScrolled", "onPageScrolled: " + position);
            }

            @Override
            public void onPageSelected(int position) {
                Log.e("onPageSelected", "onPageSelected: " + position);

                //  adapter.getItem(position).onResume();

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                Log.e("pagescroll", "onPageScrollStateChanged: " + state);
            }
        });


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                beans_Count();
                Intent intent = new Intent(Home_Social.this, Home_Social.class);
                startActivity(intent);
                finish();
                close.setVisibility(View.GONE);
                drawer.setVisibility(View.VISIBLE);
            }
        });
        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Home_Social.this, Notification_activity.class);
                startActivity(i);
//                setfragment("", new Notification_fragment());
                noti_count.setVisibility(View.GONE);
                close.setVisibility(View.GONE);
                drawer.setVisibility(View.VISIBLE);
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Home_Social.this, Search_activity.class);
                startActivity(i);
                close.setVisibility(View.GONE);
                drawer.setVisibility(View.VISIBLE);

            }
        });
        facebook_invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open_dialog(Home_Social.this);
            }
        });
        drawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Home_Social.this, My_profile_activity.class);
                startActivity(i);

            }
        });
        llmybean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Home_Social.this, My_Beans_Activity_new.class);
                startActivity(i);

            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Home_Social.this, Add_photo_activity.class);
                startActivity(i);
                finish();

            }
        });


        Myapplication.getInstance().trackScreenView("Home Screen");
        beans_Count();

    }

    private boolean checkAndRequestPermissions() {
        int permissionSendMessage = ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS);
        int locationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (locationPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.RECORD_AUDIO);
        }
        if (permissionSendMessage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }


        if (permissionSendMessage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAPTURE_VIDEO_OUTPUT);
        }


        if (permissionSendMessage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }


        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    private void createTabIcons() {

        TextView tabOne = (TextView) LayoutInflater.from(context).inflate(R.layout.custom_tab, null);
        tabOne.setText("Following");
        tabLayout.getTabAt(0).setCustomView(tabOne);

        TextView tabTwo = (TextView) LayoutInflater.from(context).inflate(R.layout.custom_tab, null);
        tabTwo.setText("Latest Post");
        tabLayout.getTabAt(1).setCustomView(tabTwo);

        TextView tabThree = (TextView) LayoutInflater.from(context).inflate(R.layout.custom_tab, null);
        tabThree.setText("Top Post");
        tabLayout.getTabAt(2).setCustomView(tabThree);
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new Followin_Fragment(this), "Following");
        adapter.addFragment(new Latest_Fragment(this), "Latest Post");
        adapter.addFragment(new Top_Post_Fragment(this), "Top Post");
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("request_code", ":" + requestCode);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GOOGLE_PLUS_REQUEST_CODE) {

            if (resultCode == Activity.RESULT_OK) {
                Log.e("Success", "success");

                Login_social(pref.getStr_postid(), pref.getStr_post_uid(), "2");


            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.e("Failure", "Failure");

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


    public void open_dialog(Activity activity) {


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

    public void init() {

        viewpager.setAdapter(new Adapter_home_advertise(context, array));
        viewpager.setPageTransformer(true, new CubeOutTransformer());
        viewpager.setCurrentItem(0);
        // to automatic rotation of the viewpager

        handler = new Handler();
        Update = new Runnable() {
            public void run() {
                if (currentPage == (array.size())) currentPage = 0;
                viewpager.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(
                new TimerTask() {
                    @Override
                    public void run() {
                        handler.post(Update);
                    }
                }, 10000, 6000);
        try {
            Field mScroller;

            mScroller = ViewPager.class.getDeclaredField("mScroller");

            mScroller.setAccessible(true);
            FixedSpeedScroller scroller = new FixedSpeedScroller(viewpager.getContext(), sInterpolator);
            // scroller.setFixedDuration(5000);
            mScroller.set(viewpager, scroller);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        Acitvity_comunator.beans_Count(context, obj_dialog, pref, Array_user);
//        if (mAdView != null) {
//            mAdView.resume();
//        }
    }

    @Override
    public void onBackPressed() {

        //setfragment("", new Main_fragment());
        if (doubleBackToExitPressedOnce) {
            // super.onBackPressed();
            finish();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
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
                // obj_dialog.dismiss();

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
                            Log.e("Jinal_image_url", image_url);

                            if (model.getNoti_count().equals("0")) {
                                noti_count.setVisibility(View.GONE);
                            } else {
                                noti_count.setVisibility(View.VISIBLE);
                                noti_count.setText(jobj1.getString("notification"));
                            }

//

                            Log.e("noti_count", jobj1.getString("notification"));
                            bean_total.setText(jobj1.getString("beans"));
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


    public void get_product() {

        //   obj_dialog.show();

        String url = Configr.app_url + "getinnerads";
        String json = "";

        json = JSON.json_product(array, pref, "getinnerads");

        HashMap<String, String> param = new HashMap<>();
        param.put("data", json);
        Log.e("jinal", ":" + param.toString());

        Response.Listener<String> lis_pat = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response", response);
                //        obj_dialog.dismiss();
                try {
                    JSONObject jobj = new JSONObject(response);
                    String res_flag = jobj.getString("status");
                    String res_msg = jobj.getString("message");
                    if (res_flag.equals("200")) {


                        JSONObject job = new JSONObject(jobj.getString("data"));

                        JSONArray jarray1 = new JSONArray(job.getString("img"));
//                        JSONArray jarray2 = new JSONArray(job.getString("product"));
                        array.clear();
                        //array1.clear();

                        for (int i = 0; i < jarray1.length(); i++) {
                            Model_Product model = new Model_Product();
                            JSONObject jobj1 = jarray1.getJSONObject(i);
                            model.setImgurl(jobj1.getString("imagename"));
                            array.add(model);
                        }


                        init();
                        //  adapter.notifyDataSetChanged();
                        Log.e("array_length", ":" + array.size());

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
}
