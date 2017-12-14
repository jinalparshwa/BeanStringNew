package com.beanstringnew.Activity;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.NotificationCompat;
import android.util.DisplayMetrics;
import android.util.Log;

import com.beanstringnew.R;
import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

//Created by Dharvik on 6/25/2016.

public class MyFirebaseMessagingService extends FirebaseMessagingService implements Thread.UncaughtExceptionHandler {

    private static final String TAG = "MyFirebaseMsgService";
    public static String Noty_type;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        remoteMessage.getCollapseKey();
        sendNotification(remoteMessage.getData());
    }

    private void sendNotification(Map<String, String> messageBody) {
        Log.i("Message Body", "" + messageBody);
        String message = "", postid = "", postuserid = "", Userid = "", noti_url = "";
        String type, status = "";
        try {

            status = messageBody.get("status");

            switch (status) {
                case "1":
                    message = messageBody.get("message");
                    postid = messageBody.get("postid");

                    postuserid = messageBody.get("postuserid");
                    Log.e("postiddd", postid);
                    Log.e("Postueridd", postuserid);
                    break;

                case "2":

                    message = messageBody.get("message");
                    Userid = messageBody.get("userid");
                    Log.e("Useridd_follow", Userid);
                    break;

                case "3":

                    message = messageBody.get("message");
                    Userid = messageBody.get("userid");
                    Log.e("Useridd_unfollow", Userid);
                    break;
                case "4":

                    message = messageBody.get("message");
                    noti_url = messageBody.get("url");
                    //Userid = messageBody.get("userid");
                    Log.e("Useridd_unfollow", Userid);
                    break;
                case "5":

                    message = messageBody.get("message");
                    noti_url = messageBody.get("url");
                    //Userid = messageBody.get("userid");
                    break;
                case "9":
                    message = messageBody.get("message");
                    postid = messageBody.get("postid");
                    Log.e("postiddd", postid);
                    break;


            }

        } catch (Exception e) {
            e.printStackTrace();
            message = "";
        }
        Log.e("hellloo", "msg aayoo ");
        Log.i("Message Body", "" + messageBody);

        Intent noti_Intent = null;
        switch (status) {
            case "1":

                //{status=3, userid=1, bedge=3, title=message send, message=Mansi Patel Like Your Post}

                noti_Intent = new Intent(this, Open_photo_video_activity.class);
                Log.i("intent", "done");
                noti_Intent.putExtra("postid", postid);
                noti_Intent.putExtra("postuserid", postuserid);
                noti_Intent.putExtra("del_value", "0");

                break;
            case "2":
                noti_Intent = new Intent(this, Other_Profile_activity.class);
                Log.i("intent", "done");
                noti_Intent.putExtra("id", Userid);
                break;

            case "3":
                noti_Intent = new Intent(this, Other_Profile_activity.class);
                Log.i("intent", "done");
                noti_Intent.putExtra("id", Userid);
                break;
            case "4":
                noti_Intent = new Intent(this, Mall_home_activity.class);
                Log.i("intent", "done");
                //noti_Intent.putExtra("id", Userid);
                break;
            case "5":
                noti_Intent = new Intent(this, Mall_home_activity.class);
                Log.i("intent", "done");
                //noti_Intent.putExtra("id", Userid);
                break;
            case "9":
                noti_Intent = new Intent(this, Open_photo_video_activity.class);
                Log.i("intent", "done");
                noti_Intent.putExtra("postid", postid);
                noti_Intent.putExtra("del_value", "0");
        }

        noti_Intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent intent = PendingIntent.getActivity(this, 0, noti_Intent, PendingIntent.FLAG_ONE_SHOT);

        if (status.equals("5")) {
            notification(message, intent, noti_url);
        } else {
            noti_one(message, intent);
        }

    }

    public void noti_one(String message, PendingIntent intent) {
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                .setContentTitle("BeanString")
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(intent)
                .setWhen(System.currentTimeMillis());
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int color = getResources().getColor(R.color.green_text);
            builder.setSmallIcon(R.drawable.mybeans);
            builder.setColor(color);
        } else {
            builder.setSmallIcon(R.drawable.app_icon);
        }
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, builder.build());
    }

    public void notification(String message, PendingIntent intent, String noti_url) {
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder nb = new NotificationCompat.Builder(this);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int color = getResources().getColor(R.color.green_text);
            nb.setSmallIcon(R.drawable.mybeans);
            nb.setColor(color);
        } else {
            nb.setSmallIcon(R.drawable.app_icon);
        }
        nb.setContentTitle("BeanString");
        nb.setContentText(message);
        nb.setContentIntent(intent);
        nb.setSound(defaultSoundUri);
        nb.setAutoCancel(true);
        Bitmap bitmap_image = getBitmapFromURL(noti_url);

        bitmap_image = Bitmap.createScaledBitmap(bitmap_image, 500, 500, false);
        NotificationCompat.BigPictureStyle s = new NotificationCompat.BigPictureStyle().bigPicture(bitmap_image);
        s.setSummaryText(message);
        nb.setStyle(s);
        NotificationManager mNotificationManager =
                (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.
        mNotificationManager.notify(11221, nb.build());


    }


    public Bitmap getBitmapFromURL(String strURL) {
        try {
            URL url = new URL(strURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        FirebaseCrash.report(e);
    }
}
