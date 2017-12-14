package com.beanstringnew.Controller;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.beanstringnew.Activity.Home_Social;
import com.beanstringnew.Activity.Mall_home_activity;
import com.beanstringnew.Json_model.JSON;
import com.beanstringnew.Model.Model_Product;
import com.beanstringnew.Model.Model_profile;
import com.beanstringnew.Shared.Pref_Master;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by admin on 11/30/2016.
 */

public class Activity_cart_count {

    public static void cart_count(final Context context, Pref_Master pref) {

        //obj_dialog.show();
        ArrayList<Model_Product> Array_user = new ArrayList<>();
        String url = Configr.app_url + "cartcount";

        String json = "";

        json = JSON.json_product(Array_user, pref, "cartcount");

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

                        JSONObject job = jobj.getJSONObject("data");
                        if (job.getString("total").equals("0")) {
                            Mall_home_activity.total_cart.setVisibility(View.GONE);
                        } else {
                            Mall_home_activity.total_cart.setVisibility(View.VISIBLE);
                            Mall_home_activity.total_cart.setText(job.getString("total"));
                        }
                    } else {
                        Toast.makeText(context, res_msg, Toast.LENGTH_SHORT).show();
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
                //  obj_dialog.dismiss();
                Toast.makeText(context, "" + Configr.Message, Toast.LENGTH_SHORT).show();
            }
        };
        Connection.postconnection(url, param, Configr.getHeaderParam(), context, lis_pat, lis_error);

    }

}
