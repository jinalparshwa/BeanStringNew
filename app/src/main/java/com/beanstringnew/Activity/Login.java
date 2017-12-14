package com.beanstringnew.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.beanstringnew.Controller.Activity_indicator;
import com.beanstringnew.Controller.Configr;
import com.beanstringnew.Controller.Connection;
import com.beanstringnew.Controller.DialogBox;
import com.beanstringnew.Facebook_Controller.PrefUtils;
import com.beanstringnew.Facebook_Controller.User;
import com.beanstringnew.Json_model.JSON;
import com.beanstringnew.Model.Model_user;
import com.beanstringnew.Myapplication;
import com.beanstringnew.R;
import com.beanstringnew.Shared.Pref_Master;
import com.beanstringnew.Twitter_Controller.CustomTwitterLoginButton;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.iid.FirebaseInstanceId;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Abc on 10/17/2016.
 */

public class Login extends AppCompatActivity implements View.OnClickListener, Thread.UncaughtExceptionHandler, GoogleApiClient.OnConnectionFailedListener {

    EditText etemail, etpwd;
    TextView txtforgot;
    RelativeLayout rr_login;
    RelativeLayout rr_register;
    Context context = this;
    Activity_indicator obj_dialog;
    ArrayList<Model_user> Array_user = new ArrayList<>();
    Pref_Master pref;
    private int FB_SIGN_IN = 64206;
    public static String reg_id = "";
    //Facebook Login Inititalize Componant

    private SignInButton signInButton;
    private GoogleSignInOptions gso;

    //google api client
    private GoogleApiClient mGoogleApiClient;

    //Signin constant to check the activity result
    private int RC_SIGN_IN = 100;
    //TextViews

    //Image Loader
    // Facebook Login Inititalize Componant
    private CallbackManager callbackManager;
    private LoginButton loginButton;
    private ImageView btnLogin;
    private ProgressDialog progressDialog;
    User user;


    //Twitter Login Initialization

    TwitterAuthClient mTwitterAuthClient;
    ImageView img_twitter;
    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "iIFlb2EvAeudvUE6BDadtTUj2";
    private static final String TWITTER_SECRET = "DQMTGdZ0qOnNxAkKOgIpkZ8ZqBE8xMc7xGZXlIIIOHM0imtaFj";

    //Tags to send the username and image url to next activity using intent
    public static final String KEY_USERNAME = "username";
    public static final String KEY_PROFILE_IMAGE_URL = "image_url";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_social);
        pref = new Pref_Master(context);
        getKeyHash();
        txtforgot = (TextView) findViewById(R.id.txtforgot);
        txtforgot.setOnClickListener(this);
        etemail = (EditText) findViewById(R.id.etemail);
        etpwd = (EditText) findViewById(R.id.etpwd);
        rr_login = (RelativeLayout) findViewById(R.id.rr_login);
        rr_register = (RelativeLayout) findViewById(R.id.rr_register);
        rr_login.setOnClickListener(this);
        rr_register.setOnClickListener(this);
        obj_dialog = new Activity_indicator(context);
        obj_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));


        try {
            reg_id = FirebaseInstanceId.getInstance().getToken();
        } catch (Exception e) {
            e.printStackTrace();
            reg_id = FirebaseInstanceId.getInstance().getToken();
        }


        //G+ Login
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_WIDE);
        signInButton.setScopes(gso.getScopeArray());

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, Login.this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        signInButton.setOnClickListener(this);


        //Twitter Login
        mTwitterAuthClient = new TwitterAuthClient();

        img_twitter = (ImageView) findViewById(R.id.twitter_login_img);
        img_twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTwitterAuthClient.authorize(Login.this, new Callback<TwitterSession>() {

                    @Override
                    public void success(Result<TwitterSession> result) {
                        // Success
                        // The TwitterSession is also available through:
                        // Twitter.getInstance().core.getSessionManager().getActiveSession()
                        TwitterSession session = result.data;
                        // TODO: Remove toast and use the TwitterSession's userID
                        // with your app's user model
                        String msg = "@" + session.getUserName() + " logged in! (#" + session.getUserId() + ")";
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();

                        Array_user.clear();
                        Model_user user1 = new Model_user();
                        user1.setStatus("3");
                        user1.setFname(session.getUserName());
                        user1.setEmail("");
                        user1.setUserid("" + session.getUserId());
                        user1.setDeviceid("" + Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID));
                        user1.setDevicetoken(reg_id);
                        Array_user.add(user1);
                        Social_Login_api();

                    }
                    @Override
                    public void failure(TwitterException e) {
                        e.printStackTrace();
                    }
                });


            }

        });
        Myapplication.getInstance().trackScreenView("Login Screen");
    }

    private void getKeyHash() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("Keyhash:",
                        Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
        } catch (NoSuchAlgorithmException e) {
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        //Facebook Login

        callbackManager = CallbackManager.Factory.create();

        loginButton = (LoginButton) findViewById(R.id.login_button);

        loginButton.setReadPermissions("public_profile", "email", "user_friends");

        btnLogin = (ImageView) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog = new ProgressDialog(Login.this);
                progressDialog.setMessage("Loading...");
                //progressDialog.show();

                loginButton.performClick();

                loginButton.setPressed(true);

                loginButton.invalidate();

                loginButton.registerCallback(callbackManager, mCallBack);

                loginButton.setPressed(false);

                loginButton.invalidate();

            }
        });


    }


    //This function will option signing intent
    private void signIn() {


        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            //mGoogleApiClient.disconnect();
            Auth.GoogleSignInApi.signOut(mGoogleApiClient);
        }
        //mGoogleApiClient.connect();
        //Creating an intent
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);

        //Starting intent for result
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    @Override
    public void onClick(View v) {
        Intent i;
        switch (v.getId()) {
            case R.id.rr_register:
                i = new Intent(Login.this, Registration.class);
                startActivity(i);
                break;
            case R.id.rr_login:
                Validate();
                break;
            case R.id.txtforgot:
                i = new Intent(Login.this, ForgotPasswordActivity.class);
                startActivity(i);
                break;
            case R.id.sign_in_button:
                signIn();
                break;

        }
    }

    public void Validate() {
        if (etemail.getText().toString().equals("")) {
            etemail.setError("Enter Email or Mobile");

        } else if (etpwd.getText().toString().equals("")) {
            etpwd.setError("Enter Password");
        } else {
            Array_user.clear();
            Model_user user = new Model_user();
            user.setEmail(etemail.getText().toString().trim());
            user.setPassword(etpwd.getText().toString().trim());
            user.setDeviceid("" + Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID));
            user.setDevicetoken(reg_id);
            Array_user.add(user);
            Login_api();

        }
    }

    public void Login_api() {

        obj_dialog.show();

        String url = Configr.app_url + "login";
        String json = "";

        json = JSON.add_json(Array_user, pref, "login");

        HashMap<String, String> param = new HashMap<>();
        param.put("data", json);

        Response.Listener<String> lis_res = new Response.Listener<String>() {
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
                            Model_user model = new Model_user();
                            JSONObject jobj1 = jarray.getJSONObject(i);
                            model.setUserid(jobj1.getString("id"));
                            model.setFname(jobj1.getString("fname"));
                            model.setLname(jobj1.getString("lname"));
                            model.setMobile(jobj1.getString("mobile"));
                            model.setEmail(jobj1.getString("email"));
                            model.setPic(jobj1.getString("propic"));

                            pref.setUserid(jobj1.getString("id"));
                            pref.setEmail(jobj1.getString("email"));
                            pref.setMobile(jobj1.getString("mobile"));
                            pref.setfname(jobj1.getString("fname"));
                            pref.setlname(jobj1.getString("lname"));
                            pref.setprofile(jobj1.getString("propic"));
                            pref.setStr_bucketname(jobj1.getString("bucketname"));
                            pref.setStr_Bucket_url(jobj1.getString("url"));
                            pref.setStr_poolid(jobj1.getString("poolid"));
                            pref.setStr_poolregion(jobj1.getString("poolregion"));
                            pref.setStr_bucketregion(jobj1.getString("bucketregion"));
                            Array_user.add(model);
                            pref.setUserid(jobj1.getString("id"));

                        }
                        pref.setLogin_Flag("login");
                        Intent i = new Intent(Login.this, Home_Social.class);
                        startActivity(i);
                        finish();

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
        Connection.postconnection(url, param, Configr.getHeaderParam(), context, lis_res, lis_error);
    }

    public void Social_Login_api() {

        obj_dialog.show();

        String url = Configr.app_url + "loginwithsocial";
        String json = "";

        json = JSON.add_json(Array_user, pref, "loginwithsocial");

        HashMap<String, String> param = new HashMap<>();
        param.put("data", json);

        Response.Listener<String> lis_res = new Response.Listener<String>() {
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
                            Model_user model = new Model_user();
                            JSONObject jobj1 = jarray.getJSONObject(i);
                            model.setUserid(jobj1.getString("id"));
                            model.setFname(jobj1.getString("fname"));
                            model.setLname(jobj1.getString("lname"));
                            model.setMobile(jobj1.getString("mobile"));
                            model.setEmail(jobj1.getString("email"));
                            model.setPic(jobj1.getString("propic"));

                            pref.setUserid(jobj1.getString("id"));
                            pref.setEmail(jobj1.getString("email"));
                            pref.setMobile(jobj1.getString("mobile"));
                            pref.setfname(jobj1.getString("fname"));
                            pref.setlname(jobj1.getString("lname"));
                            pref.setprofile(jobj1.getString("propic"));
                            pref.setStr_bucketname(jobj1.getString("bucketname"));
                            pref.setStr_Bucket_url(jobj1.getString("url"));
                            pref.setStr_poolid(jobj1.getString("poolid"));
                            pref.setStr_poolregion(jobj1.getString("poolregion"));
                            pref.setStr_bucketregion(jobj1.getString("bucketregion"));
                            Array_user.add(model);
                            pref.setUserid(jobj1.getString("id"));

                        }

                        pref.setLogin_Flag("login");
                        Intent i = new Intent(Login.this, Home_Social.class);
                        startActivity(i);
                        finish();

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
        Connection.postconnection(url, param, Configr.getHeaderParam(), context, lis_res, lis_error);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            //Calling a new function to handle signin
            handleSignInResult(result);
        } else if (requestCode == FB_SIGN_IN) {
            Log.e("RRRRRR", "" + requestCode);
            callbackManager.onActivityResult(requestCode, resultCode, data);
        } else {
            Log.e("TTTT", "" + requestCode);
            mTwitterAuthClient.onActivityResult(requestCode, resultCode, data);
        }


    }


    private FacebookCallback<LoginResult> mCallBack = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {

            progressDialog.dismiss();

            // App code
            GraphRequest request = GraphRequest.newMeRequest(
                    loginResult.getAccessToken(),
                    new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(
                                JSONObject object,
                                GraphResponse response) {

                            Log.e("response: ", response + "");
                            try {
                                user = new User();
                                user.facebookID = object.getString("id").toString();
                                user.email = object.getString("email").toString();
                                user.name = object.getString("name").toString();
                                user.gender = object.getString("gender").toString();
                                PrefUtils.setCurrentUser(user, Login.this);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            Toast.makeText(Login.this, "welcome " + user.name, Toast.LENGTH_LONG).show();
                            Array_user.clear();
                            Model_user user1 = new Model_user();
                            user1.setStatus("1");
                            user1.setFname(user.name);
                            user1.setEmail(user.email);
                            user1.setUserid(user.facebookID);
                            user1.setDeviceid("" + Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID));
                            user1.setDevicetoken(reg_id);
                            Array_user.add(user1);
                            Social_Login_api();
                        }

                    });

            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name,email,gender, birthday");
            request.setParameters(parameters);
            request.executeAsync();
        }

        @Override
        public void onCancel() {
            progressDialog.dismiss();
        }

        @Override
        public void onError(FacebookException e) {
            progressDialog.dismiss();
        }
    };

    //After the signing we are calling this function
    private void handleSignInResult(GoogleSignInResult result) {
        //If the login succeed
        if (result.isSuccess()) {
            //Getting google account
            GoogleSignInAccount acct = result.getSignInAccount();

            //Toast.makeText(this, "Welcome " + acct.getEmail(), Toast.LENGTH_LONG).show();
            Toast.makeText(this, "Welcome " + acct.getId(), Toast.LENGTH_LONG).show();
            Array_user.clear();
            Model_user user1 = new Model_user();
            user1.setStatus("2");
            user1.setFname(acct.getDisplayName());
            user1.setEmail(acct.getEmail());
            user1.setUserid(acct.getId());
            user1.setDeviceid("" + Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID));
            user1.setDevicetoken(reg_id);
            Array_user.add(user1);
            Social_Login_api();

        } else {
            //If login fails
            Toast.makeText(this, "Login Failed", Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        FirebaseCrash.report(e);
    }
}