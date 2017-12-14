package com.beanstringnew.Activity;

import android.util.Log;

import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import static com.beanstringnew.Activity.Login.reg_id;


// Created by Dharvik on 6/25/2016.

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService implements Thread.UncaughtExceptionHandler{

    private static final String TAG = "MyFirebaseIIDService";

    @Override
    public void onTokenRefresh() {

        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.e(TAG, "Refreshed token: " + refreshedToken);
        reg_id = refreshedToken;
        // TODO: Implement this method to send any registration to your app's servers.
        sendRegistrationToServer(refreshedToken);
    }

    private void sendRegistrationToServer(String token) {
        Log.e(TAG, "Refreshed token: " + token);
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        FirebaseCrash.report(e);
    }
}
