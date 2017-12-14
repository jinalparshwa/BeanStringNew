package com.beanstringnew.Fragment;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.beanstringnew.Adapter.Adapter_spend_Beans;
import com.beanstringnew.Controller.Activity_indicator;
import com.beanstringnew.Controller.Configr;
import com.beanstringnew.Controller.Connection;
import com.beanstringnew.Json_model.JSON;
import com.beanstringnew.Model.Model_Product;
import com.beanstringnew.Myapplication;
import com.beanstringnew.R;
import com.beanstringnew.Shared.Pref_Master;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Abc on 10/24/2016.
 */

public class Beans_Spend_Fragment extends Fragment {

    Pref_Master pref;
    Context context;
    Activity_indicator obj_dialog;
    ListView spend_list;
    Adapter_spend_Beans adapter;
    TextView notext;


    ArrayList<Model_Product> array = new ArrayList<>();

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.beans_spend_layout, container, false);
        context = getActivity();
        pref = new Pref_Master(context);
        obj_dialog = new Activity_indicator(context);
        obj_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        notext = (TextView) v.findViewById(R.id.notext);

        spend_list = (ListView) v.findViewById(R.id.spend_list);
        adapter = new Adapter_spend_Beans(context, array, pref);
        spend_list.setAdapter(adapter);
        get_spend_product();
        Myapplication.getInstance().trackScreenView("Beans Spend Screen");
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        Myapplication.getInstance().trackScreenView("Beans Spend Screen");
    }

    public void get_spend_product() {

        obj_dialog.show();

        String url = Configr.app_url + "beansspend";
        String json = "";

        json = JSON.json_product(array, pref, "beansspend");

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

                        JSONArray jarray1 = new JSONArray(job.getString("product"));
                        array.clear();


                        for (int i = 0; i < jarray1.length(); i++) {
                            Model_Product model = new Model_Product();
                            JSONObject jobj1 = jarray1.getJSONObject(i);
                            model.setId(jobj1.getString("id"));
                            model.setOrder_id(jobj1.getString("orderno"));
                            model.setBeans(jobj1.getString("beans"));
                            model.setDate(jobj1.getString("date"));
                            model.setTime(jobj1.getString("time"));
                            array.add(model);
                        }


                        adapter.notifyDataSetChanged();
                        Log.e("array_length", ":" + array.size());

                        if (array.size() == 0) {
                            notext.setVisibility(View.VISIBLE);
                            spend_list.setVisibility(View.GONE);
                        } else {
                            notext.setVisibility(View.GONE);
                            spend_list.setVisibility(View.VISIBLE);
                        }

                    } else {
                        if (array.size() == 0) {
                            notext.setVisibility(View.VISIBLE);
                            spend_list.setVisibility(View.GONE);
                        } else {
                            notext.setVisibility(View.GONE);
                            spend_list.setVisibility(View.VISIBLE);
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
