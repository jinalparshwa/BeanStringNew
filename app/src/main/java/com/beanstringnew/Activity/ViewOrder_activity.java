package com.beanstringnew.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.beanstringnew.Adapter.Adapter_order_details;
import com.beanstringnew.Controller.Activity_indicator;
import com.beanstringnew.Controller.Configr;
import com.beanstringnew.Controller.Connection;
import com.beanstringnew.Controller.DialogBox;
import com.beanstringnew.Json_model.JSON;
import com.beanstringnew.Model.Model_Product;
import com.beanstringnew.Myapplication;
import com.beanstringnew.R;
import com.beanstringnew.Shared.Pref_Master;
import com.google.firebase.crash.FirebaseCrash;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;

public class ViewOrder_activity extends AppCompatActivity implements Thread.UncaughtExceptionHandler {

    RecyclerView recycle_order;
    TextView order_no;
    TextView txt_date;
    TextView txt_time;
    Activity_indicator obj_dialog;
    Pref_Master pref;
    Context context = this;
    ImageView back_button;
    Adapter_order_details adapter;
    ArrayList<Model_Product> array_list = new ArrayList<>();
    String id = "";

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_order_activity);
        pref = new Pref_Master(context);

        obj_dialog = new Activity_indicator(context);
        obj_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));

        Intent i = getIntent();
        id = i.getStringExtra("Order_no");


        back_button = (ImageView) findViewById(R.id.back_button);
        order_no = (TextView) findViewById(R.id.order_no);
        txt_date = (TextView) findViewById(R.id.txt_date);
        txt_time = (TextView) findViewById(R.id.txt_time);

        recycle_order = (RecyclerView) findViewById(R.id.recycle_order);
        int numberOfColumns = 1;
        recycle_order.setLayoutManager(new GridLayoutManager(context, numberOfColumns));


        array_list.clear();
        Model_Product model_product = new Model_Product();
        model_product.setId(id);
        array_list.add(model_product);
        get_order_details();

        adapter = new Adapter_order_details(context, array_list, pref);
        recycle_order.setAdapter(adapter);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        Myapplication.getInstance().trackScreenView("View Order Screen");


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void get_order_details() {

        obj_dialog.show();

        String url = Configr.app_url + "beansspenddetail";
        String json = "";

        json = JSON.json_product(array_list, pref, "beansspenddetail");

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
                        array_list.clear();


                        for (int i = 0; i < jarray1.length(); i++) {
                            Model_Product model = new Model_Product();
                            JSONObject jobj1 = jarray1.getJSONObject(i);
                            model.setId(jobj1.getString("id"));
                            order_no.setText(jobj1.getString("orderno"));
                            txt_date.setText(jobj1.getString("date"));
                            txt_time.setText(jobj1.getString("time"));
                            model.setName(jobj1.getString("name"));
                            model.setImg(jobj1.getString("image"));
                            model.setBeans(jobj1.getString("beans"));
                            array_list.add(model);
                        }


                        adapter.notifyDataSetChanged();
                        Log.e("array_length", ":" + array_list.size());


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


    @Override
    public void uncaughtException(Thread t, Throwable e) {
        FirebaseCrash.report(e);
    }
}
