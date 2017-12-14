package com.beanstringnew.Activity;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.beanstringnew.Adapter.Adapter_profile_my_followers;
import com.beanstringnew.Adapter.Adapter_profile_my_following;
import com.beanstringnew.Adapter.Profile_follower;
import com.beanstringnew.Controller.Activity_indicator;
import com.beanstringnew.Controller.Configr;
import com.beanstringnew.Controller.Connection;
import com.beanstringnew.Controller.DialogBox;
import com.beanstringnew.Json_model.JSON;
import com.beanstringnew.Model.Model_Follower_List;
import com.beanstringnew.Model.Model_State_city;
import com.beanstringnew.Model.Model_profile;
import com.beanstringnew.Model.Model_user;
import com.beanstringnew.Myapplication;
import com.beanstringnew.R;
import com.beanstringnew.Shared.Pref_Master;
import com.google.firebase.crash.FirebaseCrash;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Profile_follower_list extends AppCompatActivity implements Thread.UncaughtExceptionHandler {

    GridView grid_profile_follower;
    Context context;
    Activity_indicator obj_dialog;
    Pref_Master pref;
    String res_message;
    ArrayList<Model_user> array_lll = new ArrayList<>();
    ArrayList<Model_Follower_List> follower_array = new ArrayList<>();

    ArrayList<Model_profile> Array_user = new ArrayList<>();
    ArrayList<Model_State_city> array1 = new ArrayList<>();
    ArrayList<Model_State_city> array2 = new ArrayList<>();

    Profile_follower adapter;
    String Userid;
    ImageView back_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_follower_list);
        context = this;
        pref = new Pref_Master(context);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Userid = extras.getString("id");
            Log.e("uuuuuuuuuuuuuuuuuuu", "" + Userid);

        } else {
            Log.e("elseeeeeee", "Enter");
        }

        back_button = (ImageView) findViewById(R.id.back_button);
        obj_dialog = new Activity_indicator(context);
        obj_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        get_state_city();
        grid_profile_follower = (GridView) findViewById(R.id.grid_profile_follower);
        adapter = new Profile_follower(context, follower_array, api_c);
        grid_profile_follower.setAdapter(adapter);
        Myapplication.getInstance().trackScreenView("Profile Follower Screen");

    }

    Runnable api_c = new Runnable() {
        @Override
        public void run() {
            follower_array.clear();
            HTTP_API_Follower_list_Call();
        }
    };


    private void HTTP_API_Follower_list_Call() {
        //   spinner.setVisibility(View.VISIBLE);

        obj_dialog.show();
        res_message = "Server Error...";

        final String url = Configr.app_url + "profilefollowerslist";

        array_lll.clear();

        final Model_user model = new Model_user();
        model.setUserid(Userid);
        Log.e("vvvvvvvvvvvv", ":" + Userid);
        array_lll.add(model);
        String json_str = "";
        try {
            json_str = JSON.add_json(array_lll, pref, "profilefollowerslist");
        } catch (Exception e) {
            e.printStackTrace();
        }

        Map<String, String> params = new HashMap<>();
        params.put("data", json_str);


        Response.Listener<String> lis_res = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response", " : " + response);

                obj_dialog.dismiss();
                try {
                    JSONObject jobj = new JSONObject(response);
                    int status = jobj.getInt("status");
                    String resMessage = jobj.getString("message");
                    JSONArray jarray = new JSONArray(jobj.getString("data"));

                    if (status == 200) {
                        for (int i = 0; i < jarray.length(); i++) {
                            Model_Follower_List model = new Model_Follower_List();
                            JSONObject jobj1 = jarray.getJSONObject(i);
                            model.setF_name(jobj1.getString("fname"));
                            model.setUser_id(jobj1.getString("userid"));
                            model.setL_name(jobj1.getString("lname"));


                            if (jobj1.getString("state").equals("null")) {
                                model.setState("");
                            } else {
                                for (Model_State_city modelstate : array1) {
                                    if (modelstate.getStateid().equals(jobj1.getString("state"))) {
                                        model.setState(modelstate.getState_name());
                                        Log.e("State_name", modelstate.getState_name());
                                    }
                                }
                            }

                            if (jobj1.getString("city").equals("null")) {
                                model.setCity("");
                            } else {
                                for (Model_State_city modelstate : array2) {
                                    if (modelstate.getCityid().equals(jobj1.getString("city"))) {
                                        model.setCity(modelstate.getCityname());
                                        Log.e("City_name", modelstate.getCityname());
                                    }
                                }
                            }
                            model.setProf_image(jobj1.getString("propic"));
                            model.setStatus(jobj1.getString("status"));
                            follower_array.add(model);
                        }

                        adapter.notifyDataSetChanged();
                    } else {
                        DialogBox.alert_popup(context, resMessage);
                    }

                } catch (Exception e) {

                }
            }
        };
        Response.ErrorListener lis_error = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("response error", "" + error);
                //   spinner.setVisibility(View.GONE);
                obj_dialog.dismiss();
                Toast.makeText(context, "" + Configr.Message, Toast.LENGTH_SHORT).show();
            }
        };
        Connection.postconnection(url, params, Configr.getHeaderParam(), context, lis_res, lis_error);
    }


    public void get_state_city() {

        //obj_dialog.show();

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
                // obj_dialog.dismiss();
                try {
                    JSONObject jobj = new JSONObject(response);
                    String res_flag = jobj.getString("status");
                    String res_msg = jobj.getString("message");
                    if (res_flag.equals("200")) {


                        JSONObject job = new JSONObject(jobj.getString("data"));

                        JSONArray jarray1 = new JSONArray(job.getString("state"));
                        JSONArray jarray2 = new JSONArray(job.getString("city"));
                        array1.clear();
                        array2.clear();

                        for (int i = 0; i < jarray1.length(); i++) {
                            Model_State_city model = new Model_State_city();
                            JSONObject jobj1 = jarray1.getJSONObject(i);
                            model.setStateid(jobj1.getString("id"));
                            model.setState_name(jobj1.getString("name"));
                            array1.add(model);
                        }

                        for (int i = 0; i < jarray2.length(); i++) {
                            Model_State_city model = new Model_State_city();
                            JSONObject jobj1 = jarray2.getJSONObject(i);
                            model.setCityid(jobj1.getString("id"));
                            model.setS_id(jobj1.getString("stateid"));
                            model.setCityname(jobj1.getString("name"));
                            array2.add(model);
                        }

                        HTTP_API_Follower_list_Call();
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

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        FirebaseCrash.report(e);
    }

}
