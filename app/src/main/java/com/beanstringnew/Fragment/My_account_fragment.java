package com.beanstringnew.Fragment;


import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.beanstringnew.Adapter.Paying_Order_adapter;
import com.beanstringnew.Controller.Activity_indicator;
import com.beanstringnew.Controller.Configr;
import com.beanstringnew.Controller.Connection;
import com.beanstringnew.Controller.DialogBox;
import com.beanstringnew.Json_model.JSON;
import com.beanstringnew.Model.Model_Paying_order_list;
import com.beanstringnew.Model.Model_State_city;
import com.beanstringnew.Myapplication;
import com.beanstringnew.R;
import com.beanstringnew.Shared.Pref_Master;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by admin on 11/18/2016.
 */

public class My_account_fragment extends Fragment {

    Context context;
    LinearLayout ll_order_history;

    TextView txt_order;
    ImageView back, refresh;
    ListView list_paying;
    ArrayList<Model_State_city> array = new ArrayList<>();
    ArrayList<Model_Paying_order_list> array2 = new ArrayList<>();
    Pref_Master pref;
    Activity_indicator obj_dialog;
    Paying_Order_adapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.my_account, container, false);
        context = getActivity();
        pref = new Pref_Master(context);
        obj_dialog = new Activity_indicator(context);
        obj_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));

        ll_order_history = (LinearLayout) v.findViewById(R.id.ll_order_history);
        txt_order = (TextView) v.findViewById(R.id.txt_order);
        back = (ImageView) v.findViewById(R.id.back);
        refresh = (ImageView) v.findViewById(R.id.refresh);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Order_history();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        list_paying = (ListView) v.findViewById(R.id.list_paying);


        Order_history();
        Myapplication.getInstance().trackScreenView("My Account Screen");
        return v;


    }

    @Override
    public void onResume() {
        super.onResume();
        Myapplication.getInstance().trackScreenView("My Account Screen");
    }

    public void Order_history() {

        obj_dialog.show();

        String url = Configr.app_url + "orderhistory";
        String json = "";

        json = JSON.add_json_order(array2, pref, "orderhistory");

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
                        JSONArray jarray1 = new JSONArray(job.getString("order"));
                        array2.clear();

                        for (int i = 0; i < jarray1.length(); i++) {
                            Model_Paying_order_list model = new Model_Paying_order_list();
                            JSONObject jobj2 = jarray1.getJSONObject(i);
                            model.setOrderid(jobj2.getString("orderid"));
                            model.setOrder_number(jobj2.getString("orderno"));
                            model.setUser_id(jobj2.getString("userid"));
                            model.setOrder_date(jobj2.getString("date"));
                            model.setStatus(jobj2.getString("status"));
                            array2.add(model);
                        }

                        adapter = new Paying_Order_adapter(context, array2, pref);
                        list_paying.setAdapter(adapter);

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
