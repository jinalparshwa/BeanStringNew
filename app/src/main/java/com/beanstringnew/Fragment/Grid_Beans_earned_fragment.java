package com.beanstringnew.Fragment;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.beanstringnew.Adapter.Adapter_grid_beans_earned;
import com.beanstringnew.Controller.Activity_indicator;
import com.beanstringnew.Controller.Configr;
import com.beanstringnew.Controller.Connection;
import com.beanstringnew.Controller.DialogBox;
import com.beanstringnew.Json_model.JSON;
import com.beanstringnew.Model.Model_profile;
import com.beanstringnew.Model.Model_user;
import com.beanstringnew.Myapplication;
import com.beanstringnew.R;
import com.beanstringnew.Shared.Pref_Master;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Admin on 7/22/2017.
 */

public class Grid_Beans_earned_fragment extends Fragment {

    Pref_Master pref;
    Context context;
    public static GridView img_grid;
    public static TextView notext1;
    Activity_indicator obj_dialog;
    public static Adapter_grid_beans_earned adapter_grid;
    public static ArrayList<Model_user> array = new ArrayList<>();
    ArrayList<Model_profile> Array_userr = new ArrayList<>();
    ArrayList<Model_user> Array_user = new ArrayList<>();


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.grid_beans_earned, container, false);
        context = getActivity();
        pref = new Pref_Master(context);
        obj_dialog = new Activity_indicator(context);
        obj_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));

        notext1 = (TextView) view.findViewById(R.id.notext1);

        img_grid = (GridView) view.findViewById(R.id.img_grid);
        adapter_grid = new Adapter_grid_beans_earned(context, array, pref, api_cc);
        obj_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        img_grid.setAdapter(adapter_grid);

        img_grid.setLongClickable(true);
        Beans_earned();
        Myapplication.getInstance().trackScreenView("Beans Earned Photo/Video Screen");
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Beans_earned();
        Myapplication.getInstance().trackScreenView("Beans Earned Photo/Video Screen");
    }

    Runnable api_cc = new Runnable() {
        @Override
        public void run() {
            array.clear();
            Beans_earned();
        }
    };

    public void Beans_earned() {

        obj_dialog.show();

        String url = Configr.app_url + "mybeans";
        String json = "";

        json = JSON.add_json(array, pref, "mybeans");

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

                        JSONArray jarray2 = new JSONArray(job.getString("post"));
                        array.clear();


//                        for (int i = 0; i < jarray1.length(); i++) {
//                            Model_user model = new Model_user();
//                            JSONObject jobj1 = jarray1.getJSONObject(i);
//                            model.setCommentid(jobj1.getString("commentid"));
//                            model.setBeans_comment(jobj1.getString("comment"));
//                            model.setBeans_profile(jobj1.getString("propic"));
//                            model.setTotal_beans(jobj1.getString("beans"));
//                            model.setPostid(jobj1.getString("postid"));
//                            model.setPostuserid(jobj1.getString("postuserid"));
//                            array.add(model);
//                        }

                        for (int i = 0; i < jarray2.length(); i++) {
                            Model_user model = new Model_user();
                            JSONObject jobj1 = jarray2.getJSONObject(i);
                            model.setUrl(jobj1.getString("url"));
                            model.setThumb(jobj1.getString("thumb"));
                            model.setTotal_beans(jobj1.getString("beans"));
                            model.setStatus(jobj1.getString("status"));
                            model.setPostid(jobj1.getString("postid"));
                            model.setPost_userid(jobj1.getString("postuserid"));
                            array.add(model);
                        }


                        adapter_grid.notifyDataSetChanged();
                        if (array.size() == 0) {
                            notext1.setVisibility(View.VISIBLE);
                            img_grid.setVisibility(View.GONE);
                        } else {
                            notext1.setVisibility(View.GONE);
                            img_grid.setVisibility(View.VISIBLE);
                        }

                    } else {
                        if (array.size() == 0) {
                            notext1.setVisibility(View.VISIBLE);
                            img_grid.setVisibility(View.GONE);
                        } else {
                            notext1.setVisibility(View.GONE);
                            img_grid.setVisibility(View.VISIBLE);
                        }

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

    public void beans_Count() {

        obj_dialog.show();

        String url = Configr.app_url + "beanscount";

        String json = "";

        json = JSON.add(Array_userr, pref, "beanscount");

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
                        Array_userr.clear();

                        for (int i = 0; i < jarray.length(); i++) {
                            Model_profile model = new Model_profile();
                            JSONObject jobj1 = jarray.getJSONObject(i);

                            model.setBeans(jobj1.getString("beans"));

                            Array_userr.add(model);


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


}
