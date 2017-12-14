package com.beanstringnew.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.beanstringnew.Controller.Configr;
import com.beanstringnew.Myapplication;
import com.beanstringnew.R;
import com.beanstringnew.Shared.Pref_Master;

public class Thank_youActivity extends AppCompatActivity {

    ImageView img_close;
    TextView txt_msg;
    TextView txt_orderno;
    String order_no = "";
    String message = "";
    Pref_Master pref;
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thank_you);
        pref = new Pref_Master(context);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            order_no = extras.getString("orderno");
            message = extras.getString("message");

        }
        img_close = (ImageView) findViewById(R.id.img_close);
        txt_msg = (TextView) findViewById(R.id.txt_msg);
        txt_orderno = (TextView) findViewById(R.id.txt_orderno);

        txt_msg.setText(message);
        txt_orderno.setText(order_no);

        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, Mall_home_activity.class);
                startActivity(i);
                finish();
            }
        });
        Myapplication.getInstance().trackScreenView("Thank You Screen");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(context, Mall_home_activity.class);
        startActivity(i);
        finish();
    }
}
