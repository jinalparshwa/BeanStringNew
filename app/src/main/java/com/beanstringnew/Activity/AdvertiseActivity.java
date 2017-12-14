package com.beanstringnew.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.beanstringnew.Controller.Configr;
import com.beanstringnew.Controller.Connection;
import com.beanstringnew.Controller.DialogBox;
import com.beanstringnew.Model.Model_Product;
import com.beanstringnew.Myapplication;
import com.beanstringnew.R;
import com.beanstringnew.Shared.Pref_Master;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

public class AdvertiseActivity extends AppCompatActivity {

    Pref_Master pref;
    Context context = this;
    ImageView txt_skip;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_advertise);
        pref = new Pref_Master(context);

        imageView = (ImageView) findViewById(R.id.imageView);
        txt_skip = (ImageView) findViewById(R.id.txt_skip);
        txt_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pref.getStr_login_flag().equals("login")) {
                    Intent i = new Intent(AdvertiseActivity.this, Home_Social.class);
                    startActivity(i);
                    finish();
                } else {
                    Intent i1 = new Intent(AdvertiseActivity.this, Login.class);
                    startActivity(i1);
                    finish();
                }
            }
        });
        Myapplication.getInstance().trackScreenView("Advertise Screen");
        getsplasimage();

    }

    public void getsplasimage() {


        String url = Configr.app_url + "getsplasimage";
        String json = "";

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


                        JSONArray jarray1 = new JSONArray(jobj.getString("data"));
                        for (int i = 0; i < jarray1.length(); i++) {
                            Model_Product model = new Model_Product();
                            JSONObject jobj1 = jarray1.getJSONObject(i);
                            model.setImgurl(jobj1.getString("imagename"));
                            Glide.with(context).load(jobj1.getString("imagename")).skipMemoryCache(true).into(imageView);

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
                Toast.makeText(context, "" + Configr.Message, Toast.LENGTH_SHORT).show();
            }
        };
        Connection.postconnection(url, param, Configr.getHeaderParam(), context, lis_pat, lis_error);
    }


}
