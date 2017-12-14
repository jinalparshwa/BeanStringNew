package com.beanstringnew.Activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.beanstringnew.Controller.Activity_indicator;
import com.beanstringnew.Controller.Configr;
import com.beanstringnew.Controller.Connection;
import com.beanstringnew.Controller.DialogBox;
import com.beanstringnew.Json_model.JSON;
import com.beanstringnew.Model.Model_user;
import com.beanstringnew.Myapplication;
import com.beanstringnew.R;
import com.beanstringnew.Shared.Pref_Master;
import com.google.firebase.crash.FirebaseCrash;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ForgotPasswordActivity extends AppCompatActivity implements View.OnClickListener, Thread.UncaughtExceptionHandler {

    EditText for_mobile;
    RelativeLayout rr_next;
    Activity_indicator obj_dialog;
    Context context = this;
    ArrayList<Model_user> Array_user = new ArrayList<>();
    Pref_Master pref;
    private static final int RECORD_READ_SMS = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot);
        showPermiosssion();
        pref = new Pref_Master(context);
        for_mobile = (EditText) findViewById(R.id.for_mobile);
        rr_next = (RelativeLayout) findViewById(R.id.rr_next);
        rr_next.setOnClickListener(this);
        obj_dialog = new Activity_indicator(context);
        obj_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        Myapplication.getInstance().trackScreenView("Forgot Password Screen");
    }
    public void showPermiosssion() {
        if (Build.VERSION.SDK_INT >= 19) {
            String[] s = new String[]{Manifest.permission.READ_SMS};

            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, android.Manifest.permission.READ_SMS)) {
                    ActivityCompat.requestPermissions((Activity) context, s, RECORD_READ_SMS);
                } else {
                    ActivityCompat.requestPermissions((Activity) context, s, RECORD_READ_SMS);
                }
            }
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rr_next:
                Validate();
                break;
        }

    }

    public void Validate() {
        if (for_mobile.getText().toString().equals("")) {
            for_mobile.setError("Enter Mobile");

        } else if (for_mobile.getText().toString().length() != 10) {
            for_mobile.setError("Invalid Mobile");

        } else {
            Array_user.clear();
            Model_user user = new Model_user();
            user.setMobile(for_mobile.getText().toString().trim());
            Array_user.add(user);
            Forgot_pwd();
        }

    }

    public void Forgot_pwd() {

        obj_dialog.show();

        String url = Configr.app_url + "forgotpassword";
        String json = "";

        json = JSON.add_json(Array_user, pref, "forgotpassword");

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
                        JSONObject jsonObject = jobj.getJSONObject("data");
                        pref.setotp(jsonObject.getString("OTP"));
                        pref.setUserid(jsonObject.getString("id"));
                        pref.setMobile(for_mobile.getText().toString());
                        Log.e("otp_check", "Enter" + pref.getStr_otp());
                        Intent i = new Intent(context, OTPActivity.class);
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
                obj_dialog.dismiss();
                Toast.makeText(context, "" + Configr.Message, Toast.LENGTH_SHORT).show();
            }
        };
        Connection.postconnection(url, param, Configr.getHeaderParam(), context, lis_res, lis_error);
    }
    @Override
    public void uncaughtException(Thread t, Throwable e) {
        FirebaseCrash.report(e);
    }
}
