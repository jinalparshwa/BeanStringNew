package com.beanstringnew.Fragment;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.beanstringnew.Adapter.Adapter_home_product_list;
import com.beanstringnew.Controller.Activity_indicator;
import com.beanstringnew.Controller.Configr;
import com.beanstringnew.Controller.Connection;
import com.beanstringnew.Controller.DialogBox;
import com.beanstringnew.Model.Model_Product;
import com.beanstringnew.Myapplication;
import com.beanstringnew.R;
import com.beanstringnew.Shared.Pref_Master;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Admin on 8/23/2017.
 */

public class Featured_product_fragment extends Fragment {

    Adapter_home_product_list adapter;
    Pref_Master pref;
    Context context;
    ArrayList<Model_Product> array1 = new ArrayList<>();
    Activity_indicator obj_dialog;
    RecyclerView recycle_product;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.featured_product_fragment, container, false);
        context = getActivity();
        pref = new Pref_Master(context);
        obj_dialog = new Activity_indicator(context);
        obj_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));

        recycle_product = (RecyclerView) v.findViewById(R.id.recycle_product);
        int numberOfColumns = 1;
        recycle_product.setLayoutManager(new GridLayoutManager(context, numberOfColumns));
        adapter = new Adapter_home_product_list(context, array1, pref);
        recycle_product.setAdapter(adapter);

        get_product();

        Myapplication.getInstance().trackScreenView("Featured Product Screen");
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        Myapplication.getInstance().trackScreenView("Featured Product Screen");
    }

    public void get_product() {

        //   obj_dialog.show();

        String url = Configr.app_url + "getfeaturedproduct";
        String json = "";

        // json = JSON.add_json(array, pref, "mybeans");

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


                        JSONObject job = new JSONObject(jobj.getString("data"));

                        JSONArray jarray1 = new JSONArray(job.getString("img"));
                        JSONArray jarray2 = new JSONArray(job.getString("product"));
                        array1.clear();


                        for (int i = 0; i < jarray2.length(); i++) {
                            Model_Product model = new Model_Product();
                            JSONObject jobj1 = jarray2.getJSONObject(i);
                            model.setId(jobj1.getString("id"));
                            model.setCatid(jobj1.getString("catid"));
                            model.setName(jobj1.getString("name"));
                            model.setImg(jobj1.getString("image"));
                            model.setBeans(jobj1.getString("beans"));
                            array1.add(model);
                        }


                        adapter.notifyDataSetChanged();

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
}
