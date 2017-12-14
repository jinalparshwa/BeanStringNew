package com.beanstringnew.Activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsMessage;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.beanstringnew.Amazon.Utils;
import com.beanstringnew.Controller.Activity_indicator;
import com.beanstringnew.Controller.Configr;
import com.beanstringnew.Controller.Connection;
import com.beanstringnew.Controller.DialogBox;
import com.beanstringnew.Controller.Fb_invites_beans;
import com.beanstringnew.Json_model.JSON;
import com.beanstringnew.Model.Model_State_city;
import com.beanstringnew.Model.Model_profile;
import com.beanstringnew.Model.Model_user;
import com.beanstringnew.Myapplication;
import com.beanstringnew.R;
import com.beanstringnew.Shared.Pref_Master;
import com.bumptech.glide.Glide;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.share.model.AppInviteContent;
import com.facebook.share.widget.AppInviteDialog;
import com.google.firebase.crash.FirebaseCrash;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Random;

import bolts.AppLinks;

public class Change_password extends AppCompatActivity implements Thread.UncaughtExceptionHandler {

    RelativeLayout rr_submit;
    RelativeLayout rr_save;
    RelativeLayout Resend_otp;
    LinearLayout ll_otp;
    TextView txteditprofile, txtchange;
    LinearLayout llchange_pwd, llmybean;
    ScrollView edit_profile;
    ImageView search, facebook_invite, drawer, imageView, realimage, removeimage, notification;
    TextView save_it_change_password;
    EditText edt_retype_password;
    EditText edt_enter_new_password;
    EditText edt_enter_oldpassword;
    EditText et_otp;
    Context context = this;
    ArrayList<Model_user> Array_user = new ArrayList<>();
    Pref_Master pref;
    EditText etfname, etlname, editemail, editmobile, editdob;
    TextView picname, tvchoose;
    Spinner spinner_state, spinner_city, spinner_college;
    Button btnedit;
    String otp;
    String get_otp;
    TextView noti_count;
    ArrayList<Model_user> Array = new ArrayList<>();
    ArrayList<Model_user> update = new ArrayList<>();

    ArrayList<String> Array_city = new ArrayList<>();
    ArrayList<String> Array_city_id = new ArrayList<>();
    ArrayList<String> Array_cityid = new ArrayList<>();
    ArrayList<String> Array_state = new ArrayList<>();
    ArrayList<String> Array_state_id = new ArrayList<>();
    ArrayList<String> Array_college = new ArrayList<>();
    ArrayList<String> Array_college_id = new ArrayList<>();
    ArrayList<Model_State_city> array = new ArrayList<>();
    ArrayList<Model_State_city> array1 = new ArrayList<>();
    ArrayList<Model_State_city> array_clg = new ArrayList<>();
    ArrayList<Model_profile> Array_use = new ArrayList<>();
    CallbackManager callbackManager;
    AppInviteDialog appInviteDialog;

    ArrayAdapter<String> adapter2;

    String userid;
    String selectedStateid, selectedcityid, selectedcollege;
    private static final int SELECT_PICTURE = 100;
    private static final String TAG = "Registration";
    public static Bitmap bitmap;
    String var = "";
    String Profile;
    Activity_indicator obj_dialog;
    TextView change_count;
    String new_pwd;

    int img_status = 1;
    Message_Receiver mReceiver;
    private static final int RECORD_READ_SMS = 2;
    private int GALLERY_INTENT_CALLED = 1;
    private int GALLERY_KITKAT_INTENT_CALLED = 3;
    private NotificationManager mNotifyManager;
    private NotificationCompat.Builder mBuilder;
    private TransferUtility transferUtility;
    Uri uri;
    String getpostone = "";
    private String path = "";
    File file_jinal;
    String image_url = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_password_activity);
        pref = new Pref_Master(context);
        transferUtility = Utils.getTransferUtility(this);
        obj_dialog = new Activity_indicator(context);
        obj_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        FacebookSdk.sdkInitialize(context);
        callbackManager = CallbackManager.Factory.create();
        Uri targetUrl = AppLinks.getTargetUrlFromInboundIntent(this, getIntent());
        if (targetUrl != null) {
            Log.i("Activity", "App Link Target URL: " + targetUrl.toString());
        }
        rr_save = (RelativeLayout) findViewById(R.id.rr_save);
        txteditprofile = (TextView) findViewById(R.id.txteditprofile);
        txtchange = (TextView) findViewById(R.id.txtchange);
        llchange_pwd = (LinearLayout) findViewById(R.id.llchange_pwd);
        edit_profile = (ScrollView) findViewById(R.id.edit_profile);
        save_it_change_password = (TextView) findViewById(R.id.save_it_change_password);
        edt_retype_password = (EditText) findViewById(R.id.edt_retype_password);
        edt_enter_new_password = (EditText) findViewById(R.id.edt_enter_new_password);
        edt_enter_oldpassword = (EditText) findViewById(R.id.edt_enter_oldpassword);
        et_otp = (EditText) findViewById(R.id.et_otp);
        etfname = (EditText) findViewById(R.id.etfname);
        etlname = (EditText) findViewById(R.id.etlname);
        editemail = (EditText) findViewById(R.id.editemail);
        editmobile = (EditText) findViewById(R.id.editmobile);
        editdob = (EditText) findViewById(R.id.editdob);
        editdob.setFocusableInTouchMode(false);
        editdob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDateTimeField(editdob);
            }
        });
        picname = (TextView) findViewById(R.id.picname);
        tvchoose = (TextView) findViewById(R.id.tvchoose);
        btnedit = (Button) findViewById(R.id.btnedit);
        realimage = (ImageView) findViewById(R.id.realimage);
        removeimage = (ImageView) findViewById(R.id.removeimage);
        notification = (ImageView) findViewById(R.id.notification);
        change_count = (TextView) findViewById(R.id.change_count);
        noti_count = (TextView) findViewById(R.id.noti_count);
        rr_submit = (RelativeLayout) findViewById(R.id.rr_submit);
        rr_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Array_user.clear();
                Model_user user = new Model_user();
                user.setPassword(new_pwd);
                Array_user.add(user);

                if (et_otp.getText().toString().equals(otp)) {
                    Change_pwd();
                } else {
                    Toast.makeText(context, "Invalid Otp", Toast.LENGTH_SHORT).show();
                }

            }
        });
        Resend_otp = (RelativeLayout) findViewById(R.id.Resend_otp);
        Resend_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LayoutInflater li = LayoutInflater.from(context);
                v = li.inflate(R.layout.alert_popup, null);
                final android.support.v7.app.AlertDialog alert = new android.support.v7.app.AlertDialog.Builder(context).setView(v).show();
                alert.setCancelable(false);
                alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                TextView ok = (TextView) v.findViewById(R.id.con_ok);
                TextView cancel = (TextView) v.findViewById(R.id.con_cancel);
                TextView txt = (TextView) v.findViewById(R.id.txt);
                txt.setText("Are you sure you want to Resend OTP?");

                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        otp = "";
                        Check_old_password();
                        alert.dismiss();
                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alert.dismiss();
                    }
                });


            }
        });

        spinner_state = (Spinner) findViewById(R.id.spinner_state);
        spinner_city = (Spinner) findViewById(R.id.spinner_city);
        spinner_college = (Spinner) findViewById(R.id.spinner_college);
        ll_otp = (LinearLayout) findViewById(R.id.ll_otp);

        tvchoose = (TextView) findViewById(R.id.tvchoose);
        tvchoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImageChooser();
            }
        });

        removeimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater li = LayoutInflater.from(context);
                v = li.inflate(R.layout.alert_popup, null);
                final android.support.v7.app.AlertDialog alert = new android.support.v7.app.AlertDialog.Builder(context).setView(v).show();
                alert.setCancelable(false);
                alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                TextView ok = (TextView) v.findViewById(R.id.con_ok);
                TextView cancel = (TextView) v.findViewById(R.id.con_cancel);
                TextView txt = (TextView) v.findViewById(R.id.txt);
                txt.setText("Are you sure you want to Remove Your Profile Picture?");

                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        realimage.setImageResource(R.drawable.default_imggg);
                        removeimage.setVisibility(View.GONE);
                        img_status = 0;
                        alert.dismiss();
                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alert.dismiss();
                    }
                });


            }
        });

        txteditprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txteditprofile.setBackgroundColor(Color.parseColor("#000000"));
                txtchange.setBackgroundColor(Color.TRANSPARENT);
                txtchange.setTextColor(Color.parseColor("#000000"));
                txteditprofile.setTextColor(Color.parseColor("#ffffff"));
                llchange_pwd.setVisibility(View.GONE);
                edit_profile.setVisibility(View.VISIBLE);
            }
        });

        txtchange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                txtchange.setBackgroundColor(Color.parseColor("#000000"));
                txteditprofile.setBackgroundColor(Color.TRANSPARENT);
                txteditprofile.setTextColor(Color.parseColor("#000000"));
                txtchange.setTextColor(Color.parseColor("#ffffff"));
                edit_profile.setVisibility(View.GONE);
                llchange_pwd.setVisibility(View.VISIBLE);
            }
        });

        imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Change_password.this, Home_Social.class);
                intent.putExtra("fragmentcode", Configr.Fragment_ID.MainFragment);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Change_password.this, Notification_activity.class);
                noti_count.setVisibility(View.GONE);
                startActivity(intent);
                finish();
            }
        });
        search = (ImageView) findViewById(R.id.search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Change_password.this, Search_activity.class);
                startActivity(intent);
                finish();
            }
        });
        facebook_invite = (ImageView) findViewById(R.id.facebook_invite);
        facebook_invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open_dialog();
            }
        });
        drawer = (ImageView) findViewById(R.id.drawer);
        drawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Change_password.this, My_profile_activity.class);
                startActivity(intent);
                finish();
            }
        });
        llmybean = (LinearLayout) findViewById(R.id.llmybean);
        llmybean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Change_password.this, My_Beans_Activity_new.class);
                startActivity(intent);
                finish();
            }
        });
        rr_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Validate();
            }
        });

        adapter2 = new ArrayAdapter<String>(context, R.layout.spinner_textview, Array_city);
        adapter2.setDropDownViewResource(R.layout.spinner_textview);
        spinner_city.setAdapter(adapter2);

        btnedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValidateData();
            }
        });
        get_state_city();
        get_college();
        beans_Count();

        IntentFilter intentFilter = new IntentFilter(
                "android.provider.Telephony.SMS_RECEIVED");
        mReceiver = new Message_Receiver();
        registerReceiver(mReceiver, intentFilter);
        Myapplication.getInstance().trackScreenView("Change Password Screen");
    }

    @Override
    protected void onResume() {
        super.onResume();
        beans_Count();
    }

    public void open_dialog() {


        appInviteDialog = new AppInviteDialog(this);
        appInviteDialog.registerCallback(callbackManager,
                new FacebookCallback<AppInviteDialog.Result>() {

                    @Override
                    public void onSuccess(AppInviteDialog.Result result) {
                        Fb_invites_beans.beans_Count(context, obj_dialog, pref, Array_use);
                        Log.d("Succes", "Success");
                    }

                    @Override
                    public void onCancel() {
                        Log.d("Result", "Cancelled");


                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Log.d("Result", "Error " + exception.getMessage());
                    }
                });


        if (AppInviteDialog.canShow()) {
            AppInviteContent content = new AppInviteContent.Builder()
                    .setApplinkUrl(Configr.app_link)
                    .setPreviewImageUrl(image_url)
                    .build();

            appInviteDialog.show(content);

        }

    }


    void openImageChooser() {
        if (Build.VERSION.SDK_INT < 19) {
            Intent intent = new Intent();
            intent.setType("image/jpeg");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), GALLERY_INTENT_CALLED);
        } else {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/jpeg");
            startActivityForResult(intent, GALLERY_KITKAT_INTENT_CALLED);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (data != null) {
            callbackManager.onActivityResult(requestCode, resultCode, data);

            uri = data.getData();

            try {
                path = getPath(uri);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }

            File source = new File(path);

            File folder = new File(Environment.getExternalStorageDirectory().toString() + "/Beanstring/GalleryImage");
            folder.mkdirs();
            //Save the path as a string value
            String extStorageDirectory = folder.toString();
            //Create New file and name it Image2.PNG
//        file_jinal.delete();
            file_jinal = new File(extStorageDirectory, pref.getStr_Userid() + ".jpg");
            String pathh = file_jinal.getAbsolutePath();


            File f = new File(pathh);
            if (!f.exists()) {
                try {
                    f.createNewFile();
                    copyFile(source, f);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            var = f.getAbsolutePath();
            if (var.equals("")) {

            } else {
                picname.setVisibility(View.VISIBLE);
                picname.setText(var.substring(var.lastIndexOf("/") + 1));
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 1;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                realimage.setImageURI(uri);
                removeimage.setVisibility(View.VISIBLE);
                img_status = 2;

            }
        }
    }

    private void copyFile(File sourceFile, File destFile) throws IOException {
        if (!sourceFile.exists()) {
            return;
        }
        FileChannel source = null;
        FileChannel destination = null;
        source = new FileInputStream(sourceFile).getChannel();
        destination = new FileOutputStream(destFile).getChannel();
        if (destination != null && source != null) {
            destination.transferFrom(source, 0, source.size());
        }
        if (source != null) {
            source.close();
        }
        if (destination != null) {
            destination.close();
        }

    }

    private String getPath(Uri uri) throws URISyntaxException {
        final boolean needToCheckUri = Build.VERSION.SDK_INT >= 19;
        String selection = null;
        String[] selectionArgs = null;
        // Uri is different in versions after KITKAT (Android 4.4), we need to
        // deal with different Uris.
        if (needToCheckUri && DocumentsContract.isDocumentUri(getApplicationContext(), uri)) {
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                return Environment.getExternalStorageDirectory() + "/" + split[1];
            } else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                uri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
            } else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("image".equals(type)) {
                    uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                selection = "_id=?";
                selectionArgs = new String[]{
                        split[1]
                };
            }
        }
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {
                    MediaStore.Images.Media.DATA
            };
            Cursor cursor = null;
            try {
                cursor = getContentResolver()
                        .query(uri, projection, selection, selectionArgs, null);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /*
     * A TransferListener class that can listen to a upload task and be notified
     * when the status changes.
     */


    protected String getSaltString() {
        String SALTCHARS = "abcdefghijklmnopqrstuvwxyz1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 12) {
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }

        return salt.toString();
    }

    // By using this method get the Uri of Internal/External Storage for Media
    private Uri getUri() {
        String state = Environment.getExternalStorageState();
        if (!state.equalsIgnoreCase(Environment.MEDIA_MOUNTED))
            return MediaStore.Images.Media.INTERNAL_CONTENT_URI;

        return MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
    }

    public String getPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] imageBytes = baos.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }


    private void ValidateData() {
        update.clear();
        Model_user user = new Model_user();
        user.setUserid(userid);
        user.setFname(etfname.getText().toString().trim());
        user.setLname(etlname.getText().toString().trim());
        user.setEmail((editemail.getText().toString().trim()));
        user.setMobile(editmobile.getText().toString().trim());
        user.setDate(editdob.getText().toString().trim());
        for (Model_State_city modelcollege : array_clg) {
            if (modelcollege.getCollege_name().equals(spinner_college.getSelectedItem())) {
                user.setCname(modelcollege.getCollege_id());
                Log.e("stateeeee_name", modelcollege.getCollege_name());
            }
        }
        // user.setCname(editclg.getText().toString().trim());
        for (Model_State_city modelstate : array) {
            if (modelstate.getState_name().equals(spinner_state.getSelectedItem())) {
                user.setState(modelstate.getStateid());
                Log.e("stateeeee_name", modelstate.getState_name());
            }
        }
        for (Model_State_city modelstate : array1) {
            if (modelstate.getCityname().equals(spinner_city.getSelectedItem())) {
                user.setCity(modelstate.getCityid());
                Log.e("cityyyyy_name", modelstate.getCityname());
            }
        }
        if (img_status == 0) {
            user.setPic("0");
        } else if (img_status == 1) {
            user.setPic("1");
        } else {
            user.setPic(picname.getText().toString());
        }
        update.add(user);
//        if (img_status == 2) {
//            beginUpload(var);
//        } else {
        updatedata();
        //       }

    }

    private void beginUpload(String filePath) {

        CannedAccessControlList cannedAccessControlList;
        cannedAccessControlList = CannedAccessControlList.PublicRead;

        if (filePath == null) {
            Toast.makeText(this, "Could not find the filepath of the selected file",
                    Toast.LENGTH_LONG).show();
            return;
        }
        File file = new File(filePath);


        getpostone = file.getName();
//        Toast.makeText(getApplicationContext(),file.getName(), Toast.LENGTH_LONG).show();
        Log.e("User_id", pref.getStr_Userid());
        TransferObserver observer = transferUtility.upload(pref.getStr_bucketname() + "/" + pref.getStr_Bucket_url(), file.getName(), file, new ObjectMetadata());

        /*
         * Note that usually we set the transfer listener after initializing the
         * transfer. However it isn't required in this sample app. The flow is
         * click upload button -> start an activity for image selection
         * startActivityForResult -> onActivityResult -> beginUpload -> onResume
         * -> set listeners to in progress transfers.
         */
        // observer.setTransferListener(new UploadListener());

        observer.setTransferListener(new UploadListener());

    }

    private class UploadListener implements TransferListener {

        // Simply updates the UI list when notified.
        @Override
        public void onError(int id, Exception e) {
            Log.e(TAG, "Error during upload: " + id, e);
            //  updateList();
        }

        @Override
        public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
            Log.d(TAG, String.format("onProgressChanged: %d, total: %d, current: %d",
                    id, bytesTotal, bytesCurrent));
            //  updateList();


        }

        @Override
        public void onStateChanged(int id, TransferState newState) {
            Log.d(TAG, "onStateChanged: " + id + ", " + newState);
            // updateList();

            mNotifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            mBuilder = new NotificationCompat.Builder(Change_password.this);

            mBuilder = new NotificationCompat.Builder(getBaseContext())
                    .setContentTitle("Upload")
                    .setContentText("Uploading")
                    .setSmallIcon(R.drawable.app_icon)
                    .setOngoing(false)
                    .setProgress(0, 0, true);

            mNotifyManager.notify(id, mBuilder.build());


            if (newState.equals(TransferState.COMPLETED)) {
                Toast.makeText(getApplicationContext(), "Completed", Toast.LENGTH_SHORT).show();

                mBuilder.setContentText("Upload Completed");
                // Removes the progress bar
                mBuilder.setProgress(0, 0, false);
                mNotifyManager.notify(id, mBuilder.build());
                file_jinal.delete();
                //updatedata();


            } else if (newState.equals(TransferState.FAILED)) {
                Toast.makeText(getApplicationContext(), "Fail", Toast.LENGTH_SHORT).show();
            } else if (newState.equals(TransferState.IN_PROGRESS)) {
                Toast.makeText(getApplicationContext(), "Progress", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void Validate() {

        if (edt_enter_new_password.getText().toString().equals("")) {
            edt_enter_new_password.setError("Enter New Password");

        } else if (!edt_enter_new_password.getText().toString().equals(edt_retype_password.getText().toString())) {
            edt_retype_password.setError("Password does not match");

        } else {
            Array_user.clear();
            Model_user user = new Model_user();
            user.setSsuerid(edt_enter_oldpassword.getText().toString().trim());
            new_pwd = edt_enter_new_password.getText().toString().trim();
            Array_user.add(user);
            Check_old_password();

        }
    }

    public void showPermiosssion() {
        if (Build.VERSION.SDK_INT >= 19) {
            String[] s = new String[]{Manifest.permission.READ_SMS};

            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, android.Manifest.permission.READ_SMS)) {
                    ActivityCompat.requestPermissions((Activity) context, s, RECORD_READ_SMS);
                } else {
                    ActivityCompat.requestPermissions((Activity) context, s, RECORD_READ_SMS);
                }
            }
        }
    }


    public void Change_pwd() {

        obj_dialog.show();
        String url = Configr.app_url + "changepassword";
        String json = "";

        json = JSON.add_json(Array_user, pref, "changepass");

        HashMap<String, String> param = new HashMap<>();
        param.put("data", json);


        Response.Listener<String> lis_pat = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response", response);
                obj_dialog.dismiss();

                String toastMsg = "";
                toastMsg = "";


                try {
                    JSONObject jobj = new JSONObject(response);
                    String res_flag = jobj.getString("status");
                    String res_msg = jobj.getString("message");
                    String otp = "", userid = "";

                    if (res_flag.equals("200")) {

                       // Toast.makeText(context, res_msg, Toast.LENGTH_SHORT).show();
                        finish();

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
                //  signup.setClickable(true);
                Toast.makeText(context, "" + Configr.Message, Toast.LENGTH_SHORT).show();
            }
        };
        Connection.postconnection(url, param, Configr.getHeaderParam(), context, lis_pat, lis_error);
    }

    public void HTTP_API_getdata() {

        obj_dialog.show();

        String url = Configr.app_url + "getmyprofile";

        String json = "";

        json = JSON.add_json(Array_user, pref, "getmyprofile");

        HashMap<String, String> param = new HashMap<>();
        param.put("data", json);

        Response.Listener<String> lis_pat = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response", response);
                obj_dialog.dismiss();

                String toastMsg = "";
                toastMsg = "";


                try {
                    JSONObject jobj = new JSONObject(response);
                    String res_flag = jobj.getString("status");
                    String res_msg = jobj.getString("message");
                    String otp = "", userid = "";

                    if (res_flag.equals("200")) {
                        JSONArray jarray = new JSONArray(jobj.getString("data"));
                        Array.clear();

                        for (int i = 0; i < jarray.length(); i++) {
                            Model_user model = new Model_user();
                            JSONObject jobj1 = jarray.getJSONObject(i);

                            model.setUserid(jobj1.getString("id"));
                            Log.e(TAG, "Firstname " + jobj1.getString("fname"));
                            model.setFname(jobj1.getString("fname"));
                            model.setLname(jobj1.getString("lname"));
                            model.setEmail(jobj1.getString("email"));
                            model.setDate(jobj1.getString("dob"));
                            model.setMobile(jobj1.getString("mobile"));

                            model.setState(jobj1.getString("state"));
                            selectedStateid = jobj1.getString("state");
                            model.setCity(jobj1.getString("city"));
                            selectedcityid = jobj1.getString("city");

                            Log.e("College", jobj1.getString("collage"));
                            model.setCname(jobj1.getString("collage"));
                            selectedcollege = (jobj1.getString("collage"));

                            if (jobj1.getString("propic").equals("")) {
                                realimage.setImageResource(R.drawable.default_imggg);
                                removeimage.setVisibility(View.GONE);
                            } else {
                                Glide.with(context).load(jobj1.getString("propic")).into(realimage);
                                removeimage.setVisibility(View.VISIBLE);
                            }
                            Array.add(model);
                            Profile = jobj1.getString("propic");

                            etfname.setText(jobj1.getString("fname"));
                            etlname.setText(jobj1.getString("lname"));
                            editemail.setText(jobj1.getString("email"));
                            editmobile.setText(jobj1.getString("mobile"));
                            editdob.setText(jobj1.getString("dob"));
                            //editclg.setText(jobj1.getString("collage"));
                            // Glide.with(context).load(jobj1.getString("propic")).into(realimage);

                        }

                        Log.e("Selected State Id", selectedStateid);
                        int selected_state = Array_state_id.indexOf(selectedStateid);
                        spinner_state.setSelection(selected_state);


                        int selected_city = Array_cityid.indexOf(selectedcityid);
                        spinner_city.setSelection(selected_city);

                        int selected_college = Array_college_id.indexOf(selectedcollege);
                        spinner_college.setSelection(selected_college);

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

    public void updatedata() {

        obj_dialog.show();

        String url = Configr.app_url + "editprofile";
        String json = "";

        json = JSON.add_json(update, pref, "editprofile");

        HashMap<String, String> param = new HashMap<>();
        param.put("data", json);
        //  param.put("image", (img_status == 2) ? "" + var : (img_status == 0) ? "0" : "1");
        Log.e("llllllll", param.toString());
        Response.Listener<String> lis_res = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response", response);

                String toastMsg = "";
                toastMsg = "Sign up successfully";
                obj_dialog.dismiss();

                try {

                    JSONObject jobj = new JSONObject(response);
                    String res_flag = jobj.getString("status");
                    String res_msg = jobj.getString("message");
                    String otp = "", userid = "";

                    if (res_flag.equals("200")) {


                        JSONObject jobj1 = jobj.getJSONObject("data");
                        pref.setprofile(jobj1.getString("url"));
                        Toast.makeText(context, "Profile Successfully Updated", Toast.LENGTH_SHORT).show();

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (img_status == 2) {
                                    beginUpload(var);
                                }
                                //Do something after 100ms
                            }
                        }, 5000);

                        finish();


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
                //  signup.setClickable(true);
                obj_dialog.dismiss();
                Toast.makeText(context, "" + Configr.Message, Toast.LENGTH_SHORT).show();
            }
        };
        Connection.postconnection(url, param, Configr.getHeaderParam(), context, lis_res, lis_error);
    }

    public void setDateTimeField(final EditText et) {
        final SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Calendar newCalendar = Calendar.getInstance();

        DatePickerDialog fromDatePickerDialog = new DatePickerDialog(Change_password.this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        // TODO Auto-generated method stub
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, monthOfYear, dayOfMonth);
                        et.setText(dateFormatter.format(newDate.getTime()));
                    }
                }, newCalendar.get(Calendar.YEAR),
                newCalendar.get(Calendar.MONTH),
                newCalendar.get(Calendar.DAY_OF_MONTH));
        fromDatePickerDialog.getDatePicker().setMaxDate(newCalendar.getTimeInMillis());
        fromDatePickerDialog.show();
    }

    public void beans_Count() {

        obj_dialog.show();

        String url = Configr.app_url + "beanscount";

        String json = "";

        json = JSON.add(Array_use, pref, "beanscount");

        HashMap<String, String> param = new HashMap<>();
        param.put("data", json);

        Response.Listener<String> lis_pat = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response", response);
                obj_dialog.dismiss();

                String toastMsg = "";
                toastMsg = "";


                try {
                    JSONObject jobj = new JSONObject(response);
                    String res_flag = jobj.getString("status");
                    String res_msg = jobj.getString("message");
                    String otp = "", userid = "";

                    if (res_flag.equals("200")) {
                        JSONArray jarray = new JSONArray(jobj.getString("data"));
                        Array_use.clear();

                        for (int i = 0; i < jarray.length(); i++) {
                            Model_profile model = new Model_profile();
                            JSONObject jobj1 = jarray.getJSONObject(i);

                            model.setBeans(jobj1.getString("beans"));
                            model.setNoti_count(jobj1.getString("notification"));
                            image_url = jobj1.getString("url");
                            if (model.getNoti_count().equals("0")) {
                                noti_count.setVisibility(View.GONE);
                            } else {
                                noti_count.setVisibility(View.VISIBLE);
                                noti_count.setText(jobj1.getString("notification"));
                            }
                            change_count.setText(jobj1.getString("beans"));
                            // noti_count.setText(jobj1.getString("notification"));


                            Array_use.add(model);


                        }

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
                //  signup.setClickable(true);
                obj_dialog.dismiss();
                Toast.makeText(context, "" + Configr.Message, Toast.LENGTH_SHORT).show();
            }
        };
        Connection.postconnection(url, param, Configr.getHeaderParam(), context, lis_pat, lis_error);

    }

    public void get_state_city() {

        obj_dialog.show();

        String url = Configr.app_url + "getstate";
        String json = "";

        // json = JSON.add_json(array, pref, "mybeans");

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

                        JSONArray jarray1 = new JSONArray(job.getString("state"));
                        JSONArray jarray2 = new JSONArray(job.getString("city"));
                        array.clear();
                        array1.clear();

                        for (int i = 0; i < jarray1.length(); i++) {
                            Model_State_city model = new Model_State_city();
                            JSONObject jobj1 = jarray1.getJSONObject(i);
                            model.setStateid(jobj1.getString("id"));
                            model.setState_name(jobj1.getString("name"));
                            array.add(model);
                        }

                        for (int i = 0; i < jarray2.length(); i++) {
                            Model_State_city model = new Model_State_city();
                            JSONObject jobj1 = jarray2.getJSONObject(i);
                            model.setCityid(jobj1.getString("id"));
                            model.setS_id(jobj1.getString("stateid"));
                            model.setCityname(jobj1.getString("name"));
                            array1.add(model);
                        }

                        Array_state.clear();
                        for (Model_State_city model : array) Array_state.add(model.getState_name());

                        Array_state_id.clear();
                        for (Model_State_city model : array) Array_state_id.add(model.getStateid());

                        Array_city_id.clear();
                        for (Model_State_city model : array1) Array_city_id.add(model.getCityid());

                        Array_city.clear();
                        for (Model_State_city model : array1) Array_city.add(model.getCityname());

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, R.layout.spinner_textview, Array_state);
                        adapter.setDropDownViewResource(R.layout.spinner_textview);
                        spinner_state.setAdapter(adapter);

                        HTTP_API_getdata();
                        Spinner_state_change();

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


    public void get_college() {

        obj_dialog.show();

        String url = Configr.app_url + "getcollege";
        String json = "";

        // json = JSON.add_json(array, pref, "mybeans");

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

                        JSONArray jarray1 = new JSONArray(job.getString("college"));
                        array_clg.clear();

                        Model_State_city model2 = new Model_State_city();
                        model2.setCollege_id("0");
                        model2.setCollege_name("Select College");
                        array_clg.add(model2);

                        for (int i = 0; i < jarray1.length(); i++) {
                            Model_State_city model = new Model_State_city();
                            JSONObject jobj1 = jarray1.getJSONObject(i);
                            model.setCollege_id(jobj1.getString("id"));
                            model.setCollege_name(jobj1.getString("college"));
                            array_clg.add(model);
                        }

                        Array_college.clear();
                        for (Model_State_city model : array_clg)
                            Array_college.add(model.getCollege_name());

                        Array_college_id.clear();
                        for (Model_State_city model : array_clg)
                            Array_college_id.add(model.getCollege_id());


                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, R.layout.spinner_textview, Array_college);
                        adapter.setDropDownViewResource(R.layout.spinner_textview);
                        spinner_college.setAdapter(adapter);

                        HTTP_API_getdata();
                        // Spinner_state_change();

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

    public void Spinner_state_change() {

        spinner_state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Array_city.clear();
                Array_city_id.clear();
                Array_cityid.clear();
                for (Model_State_city model_state : array) {
                    if (model_state.getState_name().equals(spinner_state.getSelectedItem())) {

                        for (Model_State_city model_city : array1) {
                            if (model_city.getS_id().equals(model_state.getStateid())) {
                                Array_city.add(model_city.getCityname());
                                Array_city_id.add(model_city.getCityid());
                                Array_cityid.add(model_city.getCityid());
                            }
                        }

                        Log.e("City list", Array_city.toString());
                        Log.e("City id list", Array_city_id.toString());

                        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(context, R.layout.spinner_textview, Array_city);
                        adapter1.setDropDownViewResource(R.layout.spinner_textview);
                        spinner_city.setAdapter(adapter1);

                        int selected_city = Array_cityid.indexOf(selectedcityid);
                        spinner_city.setSelection(selected_city);

                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void Check_old_password() {
        obj_dialog.show();

        String url = Configr.app_url + "checkoldpassword";
        String json = "";

        json = JSON.add_json(Array_user, pref, "checkoldpassword");

        HashMap<String, String> param = new HashMap<>();
        param.put("data", json);

        Response.Listener<String> lis_res = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response", response);
                obj_dialog.dismiss();
                try {
                    JSONObject jobj = new JSONObject(response);
                    String res_flag = jobj.getString("status");
                    String res_msg = jobj.getString("message");
                    if (jobj.getInt("status") == 200) {
                        JSONObject jsonObject = jobj.getJSONObject("data");

                        otp = (jsonObject.getString("otp"));
                        Log.e("otp_check", "Enter" + otp);
                        ll_otp.setVisibility(View.VISIBLE);
                        showPermiosssion();
                        txteditprofile.setVisibility(View.GONE);
                        txtchange.setVisibility(View.GONE);
                        llchange_pwd.setVisibility(View.GONE);

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
                //  signup.setClickable(true);
                obj_dialog.dismiss();
                Toast.makeText(context, "" + Configr.Message, Toast.LENGTH_SHORT).show();
            }
        };
        Connection.postconnection(url, param, Configr.getHeaderParam(), context, lis_res, lis_error);
    }

    private class Message_Receiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context paramContext, Intent paramIntent) {

            if (paramIntent.getAction().equals(
                    "android.provider.Telephony.SMS_RECEIVED")) {
                Bundle bundle = paramIntent.getExtras();
                // ---get the SMS message passed in---
                SmsMessage[] msgs = null;
                // String msg_from;
                if (bundle != null) {
                    // ---retrieve the SMS message received---
                    try {
                        Object[] pdus = (Object[]) bundle.get("pdus");
                        msgs = new SmsMessage[pdus.length];
                        for (int i = 0; i < msgs.length; i++) {
                            msgs[i] = SmsMessage
                                    .createFromPdu((byte[]) pdus[i]);
                            // msg_from = msgs[i].getOriginatingAddress();
                            get_otp = msgs[i].getMessageBody();
                            Log.i("Print it", "getOtp");
                        }
                    } catch (Exception e) {
                        // Log.d("Exception caught",e.getMessage());

                    }
                }
            }

            int end_point = get_otp.length();
            get_otp = get_otp.substring((end_point - 4), end_point);
            et_otp.setText(get_otp);

        }

    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        FirebaseCrash.report(e);
    }

}
