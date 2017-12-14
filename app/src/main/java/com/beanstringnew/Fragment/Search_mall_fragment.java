package com.beanstringnew.Fragment;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.beanstringnew.Adapter.Adapter_Serach_mall;
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
 * Created by admin on 12/1/2016.
 */

public class Search_mall_fragment extends Fragment {

    Pref_Master pref;
    Context context;
    Activity_indicator obj_dialog;

    Adapter_Serach_mall adapter;
    ArrayList<Model_Product> array_search = new ArrayList<>();


    GridView product;
    Button btn_search;
    EditText etsearch;
    TextView notext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.search_mall_layout, container, false);
        context = getActivity();
        pref = new Pref_Master(context);
        obj_dialog = new Activity_indicator(context);
        obj_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        etsearch = (EditText) v.findViewById(R.id.etsearch);
        btn_search = (Button) v.findViewById(R.id.btn_search);
        notext = (TextView) v.findViewById(R.id.notext);

        product = (GridView) v.findViewById(R.id.product);
        adapter = new Adapter_Serach_mall(context, array_search, pref);
        product.setAdapter(adapter);

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!etsearch.getText().toString().equals("")) {
                    array_search.clear();
                    Model_Product user = new Model_Product();
                    user.setName(etsearch.getText().toString());
                    array_search.add(user);
                    search_product();
                }
            }
        });

        Myapplication.getInstance().trackScreenView("Search mall Screen");
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        Myapplication.getInstance().trackScreenView("Search mall Screen");
    }

    public void search_product() {

        obj_dialog.show();

        String url = Configr.app_url + "searchproduct";
        String json = "";

        json = JSON.json_product(array_search, pref, "searchproduct");

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
                        array_search.clear();
                        for (int i = 0; i < jarray1.length(); i++) {
                            Model_Product model = new Model_Product();
                            JSONObject jobj1 = jarray1.getJSONObject(i);
                            model.setId(jobj1.getString("id"));
                            model.setCatid(jobj1.getString("catid"));
                            model.setName(jobj1.getString("name"));
                            model.setBeans(jobj1.getString("beans"));
                            model.setImg(jobj1.getString("image"));
                            array_search.add(model);
                        }
                        adapter.notifyDataSetChanged();
                        Log.e("array_length", ":" + array_search.size());
                        if (array_search.size() == 0) {
                            notext.setVisibility(View.VISIBLE);
                            product.setVisibility(View.GONE);
                        } else {
                            notext.setVisibility(View.GONE);
                            product.setVisibility(View.VISIBLE);
                        }

                    } else {
                        notext.setVisibility(View.VISIBLE);
                        product.setVisibility(View.GONE);
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
