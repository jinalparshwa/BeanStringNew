package com.beanstringnew.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Changepwd extends AppCompatActivity implements View.OnClickListener{

    EditText et_new,et_confirm;
    RelativeLayout rr_change;
    Activity_indicator obj_dialog;
    Context context = this;
    ArrayList<Model_user> Array_user = new ArrayList<>();
    Pref_Master pref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.changepwd);
        pref = new Pref_Master(context);
        et_new=(EditText)findViewById(R.id.et_new);
        et_confirm=(EditText)findViewById(R.id.et_confirm);
        rr_change=(RelativeLayout)findViewById(R.id.rr_change);
        rr_change.setOnClickListener(this);
        obj_dialog = new Activity_indicator(context);
        obj_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        Myapplication.getInstance().trackScreenView("Change Password Screen");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rr_change:
                Validate();
                break;
        }
    }

    private void Validate() {
        if (et_new.getText().toString().equals("")) {
            et_new.setError("Enter New Password");

        }  else if (!et_new.getText().toString().equals(et_confirm.getText().toString())) {
            et_confirm.setError("Password does not match");

        }  else {
            Array_user.clear();
            Model_user user = new Model_user();
            user.setMobile(pref.getStr_login_mobile());
            user.setPassword(et_new.getText().toString().trim());
            Array_user.add(user);
            Change_pwd();

        }
    }

    public void Change_pwd() {

        obj_dialog.show();
        String url = Configr.app_url + "changepassword";
        String json = "";

        json = JSON.add_json(Array_user, pref, "changepass");

        HashMap<String, String> param = new HashMap<>();
        param.put("data", json);

        Response.Listener<String> lis_pat = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response", response);

                String toastMsg = "";
                toastMsg = "";
                obj_dialog.dismiss();

                try {
                    JSONObject jobj = new JSONObject(response);
                    String res_flag = jobj.getString("status");
                    String res_msg = jobj.getString("message");
                    String otp = "", userid = "";

                    if (res_flag.equals("200")) {
                        Intent i = new Intent(Changepwd.this, Login.class);
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
        Connection.postconnection(url, param, Configr.getHeaderParam(), context, lis_pat, lis_error);
    }
}
