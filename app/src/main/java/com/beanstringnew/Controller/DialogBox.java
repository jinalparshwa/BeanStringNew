package com.beanstringnew.Controller;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.beanstringnew.Model.Model_user;
import com.beanstringnew.R;
import com.beanstringnew.Shared.Pref_Master;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.share.Sharer;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;


public class DialogBox extends Activity {
    Context context = this;
    public static Pref_Master pref;
    public static CallbackManager callbackManager;


    public void Share_facebook(String img) {
        FacebookSdk.sdkInitialize(context);
        callbackManager = CallbackManager.Factory.create();

        FacebookCallback<Sharer.Result> sharecallback = new FacebookCallback<Sharer.Result>() {
            @Override
            public void onSuccess(Sharer.Result result) {
                Log.e("Success", "Success");
            }

            @Override
            public void onCancel() {
                Log.e("Cancel", "Cancel");

            }

            @Override
            public void onError(FacebookException error) {
                Log.e("Error", "Error");

            }

        };


        ShareDialog shareDialog = new ShareDialog((Activity) context);
        SharePhoto photo = new SharePhoto.Builder()
                .setBitmap(Configr.onShareItemBitmap(img, context))
                .build();
        SharePhotoContent content = new SharePhotoContent.Builder()
                .addPhoto(photo)
                .build();
        shareDialog.registerCallback(callbackManager, sharecallback);
        shareDialog.show(content, ShareDialog.Mode.AUTOMATIC);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

//


    public static void alert_popup(final Context context, String msg) {
        LayoutInflater li = LayoutInflater.from(context);
        View v = li.inflate(R.layout.alert_popup_ok, null);
        final android.support.v7.app.AlertDialog alert = new android.support.v7.app.AlertDialog.Builder(context).setView(v).show();
        alert.setCancelable(false);
        alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView ok = (TextView) v.findViewById(R.id.con_ok);
        TextView txt = (TextView) v.findViewById(R.id.txt);
        txt.setText(msg);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert.dismiss();
            }
        });

    }

}