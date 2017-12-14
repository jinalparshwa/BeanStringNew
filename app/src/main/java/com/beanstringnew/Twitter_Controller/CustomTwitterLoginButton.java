package com.beanstringnew.Twitter_Controller;

import android.content.Context;
import android.util.AttributeSet;

import com.beanstringnew.R;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

/**
 * Created by Nirav on 11/8/2016.
 */

public class CustomTwitterLoginButton extends TwitterLoginButton {
    public CustomTwitterLoginButton(Context context) {
        super(context);
        init();
    }

    public CustomTwitterLoginButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomTwitterLoginButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        if (isInEditMode()){
            return;
        }
        setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.twitter), null, null, null);
        //setBackgroundResource(R.drawable.twitter);
//        setTextSize(20);
        //setPadding(0);
    }
}