package com.beanstringnew.Controller;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

import com.beanstringnew.R;


public class Activity_indicator extends Dialog {

    private String text = "";
    private int color_code;

    public Activity_indicator(Context context) {
        super(context);
    }

    public void setActivityIndicatorText(String text, int color_code) {
        this.text = text;
        this.color_code = color_code;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activityindicator);
        TextView txt_loading_text = (TextView) findViewById(R.id.txt_loading_text);
        txt_loading_text.setText(text);
        txt_loading_text.setTextColor(color_code);
    }
}
