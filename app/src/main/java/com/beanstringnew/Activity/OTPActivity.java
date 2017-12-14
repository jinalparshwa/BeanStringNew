package com.beanstringnew.Activity;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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

public class OTPActivity extends AppCompatActivity implements View.OnClickListener, Thread.UncaughtExceptionHandler {

    EditText et_otp;
    RelativeLayout rr_submit;
    Activity_indicator obj_dialog;
    Context context = this;
    ArrayList<Model_user> Array_user = new ArrayList<>();
    Pref_Master pref;
    String get_otp;
    Message_Receiver mReceiver;
    RelativeLayout Resend_otp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.otp_activity);
        pref = new Pref_Master(context);
        et_otp = (EditText) findViewById(R.id.et_otp);
        rr_submit = (RelativeLayout) findViewById(R.id.rr_submit);
        rr_submit.setOnClickListener(this);
        Resend_otp = (RelativeLayout) findViewById(R.id.Resend_otp);
        Resend_otp.setOnClickListener(this);
        obj_dialog = new Activity_indicator(context);
        obj_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));

        IntentFilter intentFilter = new IntentFilter(
                "android.provider.Telephony.SMS_RECEIVED");
        mReceiver = new Message_Receiver();
        registerReceiver(mReceiver, intentFilter);
        Myapplication.getInstance().trackScreenView("OTP Screen");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rr_submit:
                Validate();
                break;
            case R.id.Resend_otp:
                LayoutInflater li = LayoutInflater.from(context);
                v = li.inflate(R.layout.alert_popup, null);
                final android.support.v7.app.AlertDialog alert = new android.support.v7.app.AlertDialog.Builder(context).setView(v).show();
                alert.setCancelable(false);
                alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                TextView ok = (TextView) v.findViewById(R.id.con_ok);
                TextView cancel = (TextView) v.findViewById(R.id.con_cancel);
                TextView txt = (TextView) v.findViewById(R.id.txt);
                txt.setText("Are you sure you want to Resend OTP?");

                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        pref.setotp("");
                        Array_user.clear();
                        Model_user model_user = new Model_user();
                        model_user.setMobile(pref.getStr_login_mobile());
                        Array_user.add(model_user);
                        Forgot_pwd();
                        alert.dismiss();
                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alert.dismiss();
                    }
                });


                break;

        }
    }

    private void Validate() {
        if (et_otp.getText().toString().equals("")) {
            et_otp.setError("Enter OTP Code");

        } else if
                (et_otp.getText().toString().equals(pref.getStr_otp())) {

            Intent i = new Intent(OTPActivity.this, Changepwd.class);
            startActivity(i);
            finish();
        }
    }

    private class Message_Receiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context paramContext, Intent paramIntent) {

            if (paramIntent.getAction().equals(
                    "android.provider.Telephony.SMS_RECEIVED")) {
                Bundle bundle = paramIntent.getExtras();
                // ---get the SMS message passed in---
                SmsMessage[] msgs = null;
                // String msg_from;
                if (bundle != null) {
                    // ---retrieve the SMS message received---
                    try {
                        Object[] pdus = (Object[]) bundle.get("pdus");
                        msgs = new SmsMessage[pdus.length];
                        for (int i = 0; i < msgs.length; i++) {
                            msgs[i] = SmsMessage
                                    .createFromPdu((byte[]) pdus[i]);
                            // msg_from = msgs[i].getOriginatingAddress();
                            get_otp = msgs[i].getMessageBody();
                            Log.i("Print it", "getOtp");
                        }
                    } catch (Exception e) {
                        // Log.d("Exception caught",e.getMessage());

                    }
                }
            }

            int end_point = get_otp.length();
            get_otp = get_otp.substring((end_point - 4), end_point);
            et_otp.setText(get_otp);

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
                        Log.e("otp_check", "Enter" + pref.getStr_otp());
                    } else {
                        DialogBox.alert_popup(context, "Invalid OTP");
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
    public void uncaughtException(Thread t, Throwable e) {
        FirebaseCrash.report(e);
    }
}
