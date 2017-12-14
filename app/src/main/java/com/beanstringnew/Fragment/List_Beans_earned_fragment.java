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
import com.beanstringnew.Adapter.Adapter_list_beans_earned;
import com.beanstringnew.Controller.Activity_indicator;
import com.beanstringnew.Controller.Configr;
import com.beanstringnew.Controller.Connection;
import com.beanstringnew.Json_model.JSON;
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

public class List_Beans_earned_fragment extends Fragment {

    Pref_Master pref;
    Context context;
    public static GridView img_grid;
    public static TextView notext;
    Activity_indicator obj_dialog;
    public static Adapter_list_beans_earned adapter;
    public static ArrayList<Model_user> array1 = new ArrayList<>();


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_beans_earned, container, false);
        context = getActivity();
        pref = new Pref_Master(context);
        obj_dialog = new Activity_indicator(context);
        obj_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));

        notext = (TextView) view.findViewById(R.id.notext1);

        img_grid = (GridView) view.findViewById(R.id.img_grid);
        adapter = new Adapter_list_beans_earned(context, array1, pref, api_cc);
        obj_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        img_grid.setAdapter(adapter);

        Beans_earned();
        Myapplication.getInstance().trackScreenView("Beans Earned Comments Screen");
        return view;

    }

    Runnable api_cc = new Runnable() {
        @Override
        public void run() {
            array1.clear();
            Beans_earned();
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        Beans_earned();
        Myapplication.getInstance().trackScreenView("Beans Earned Comments Screen");
    }

    public void Beans_earned() {

        obj_dialog.show();

        String url = Configr.app_url + "mybeans";
        String json = "";

        json = JSON.add_json(array1, pref, "mybeans");

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

                        JSONArray jarray1 = new JSONArray(job.getString("comment"));
                        array1.clear();


                        for (int i = 0; i < jarray1.length(); i++) {
                            Model_user model = new Model_user();
                            JSONObject jobj1 = jarray1.getJSONObject(i);
                            model.setCommentid(jobj1.getString("commentid"));
                            model.setBeans_comment(jobj1.getString("comment"));
                            model.setBeans_profile(jobj1.getString("propic"));
                            model.setTotal_beans(jobj1.getString("beans"));
                            model.setPostid(jobj1.getString("postid"));
                            model.setPostuserid(jobj1.getString("postuserid"));
                            model.setLast_comment_time(jobj1.getString("time"));
                            array1.add(model);
                        }

                        adapter.notifyDataSetChanged();
                        if (array1.size() == 0) {
                            notext.setVisibility(View.VISIBLE);
                            img_grid.setVisibility(View.GONE);
                        } else {
                            notext.setVisibility(View.GONE);
                            img_grid.setVisibility(View.VISIBLE);
                        }

                    } else {
                        if (array1.size() == 0) {
                            notext.setVisibility(View.VISIBLE);
                            img_grid.setVisibility(View.GONE);
                        } else {
                            notext.setVisibility(View.GONE);
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


}
