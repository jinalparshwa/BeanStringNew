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
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.beanstringnew.Adapter.Adapter_MyProduct;
import com.beanstringnew.Controller.Activity_indicator;
import com.beanstringnew.Controller.Configr;
import com.beanstringnew.Controller.Connection;
import com.beanstringnew.Controller.DialogBox;
import com.beanstringnew.Model.Model_Product;
import com.beanstringnew.Myapplication;
import com.beanstringnew.R;
import com.beanstringnew.Shared.Pref_Master;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by admin on 11/29/2016.
 */

public class My_product_fragment extends Fragment {

    Pref_Master pref;
    Context context;
    Activity_indicator obj_dialog;

    Adapter_MyProduct adapter;

    ArrayList<Model_Product> array = new ArrayList<>();

    GridView product;
    String id = "";

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.my_product_fragment, container, false);
        context = getActivity();
        pref = new Pref_Master(context);
        obj_dialog = new Activity_indicator(context);
        Bundle extras = getActivity().getIntent().getExtras();

        if (extras != null) {
            id = extras.getString("id");
            Log.e("id", ":" + id);
        }


        product = (GridView) view.findViewById(R.id.product);
        adapter = new Adapter_MyProduct(context, array, pref);
        obj_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        product.setAdapter(adapter);
        get_product();
        Myapplication.getInstance().trackScreenView("My Product Screen");
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Myapplication.getInstance().trackScreenView("My Product Screen");
    }

    public void get_product() {

        obj_dialog.show();

        String url = Configr.app_url + "getproduct";

        JSONObject jobj_loginuser = new JSONObject();
        obj_dialog.show();
        try {

            JSONObject jobj_row = new JSONObject();

            jobj_row.put("id", id);


            JSONArray jarray_loginuser = new JSONArray();
            jarray_loginuser.put(jobj_row);

            jobj_loginuser.put("getproduct", jarray_loginuser);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        HashMap<String, String> param = new HashMap<>();
        param.put("data", "" + jobj_loginuser.toString().replaceAll("\\\\", ""));
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
                            model.setCatid(jobj1.getString("catid"));
                            model.setName(jobj1.getString("name"));
                            model.setBeans(jobj1.getString("beans"));
                            model.setImg(jobj1.getString("image"));
                            array.add(model);
                        }


                        adapter.notifyDataSetChanged();
                        Log.e("array_length", ":" + array.size());

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
