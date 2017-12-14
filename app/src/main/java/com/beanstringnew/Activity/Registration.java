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
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsMessage;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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
import com.beanstringnew.Json_model.JSON;
import com.beanstringnew.Model.Model_State_city;
import com.beanstringnew.Model.Model_user;
import com.beanstringnew.Myapplication;
import com.beanstringnew.R;
import com.beanstringnew.Shared.Pref_Master;
import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONObject;

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


/**
 * Created by Abc on 10/17/2016.
 */

public class Registration extends AppCompatActivity implements View.OnClickListener, Thread.UncaughtExceptionHandler {

    String var = "";
    private static final int SELECT_PICTURE = 100;
    private static final String TAG = "Registration";
    Button submit;
    EditText fname, lname, email, mobile, date, pwd, confpwd;
    TextView upload, imagename;
    Spinner spinner_state, spinner_city;
    Context context = this;
    Activity_indicator obj_dialog;
    ArrayList<Model_user> Array_user = new ArrayList<>();
    ArrayList<String> Array_state = new ArrayList<>();
    ArrayList<String> Array_city = new ArrayList<>();
    ArrayList<Model_State_city> array = new ArrayList<>();
    ArrayList<Model_State_city> array1 = new ArrayList<>();
    Pref_Master pref;
    public static Bitmap bitmap;
    private static final int RECORD_REQUEST_CODE = 1;
    String reg_id;
    String st_id, city_id;
    EditText et_otp;
    RelativeLayout rr_submit;
    RelativeLayout Resend_otp;
    String otp;
    ScrollView scroll;
    LinearLayout ll_otp;
    String get_otp;
    Message_Receiver mReceiver;
    private static final int RECORD_READ_SMS = 2;
    private int GALLERY_INTENT_CALLED = 1;
    private int GALLERY_KITKAT_INTENT_CALLED = 3;
    private TransferUtility transferUtility;
    private String path;
    Uri uri;
    String getpostone = "";
    private NotificationManager mNotifyManager;
    private NotificationCompat.Builder mBuilder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.social_registration);
        pref = new Pref_Master(context);
        showPermiosssion();
        try {
            reg_id = FirebaseInstanceId.getInstance().getToken();
        } catch (Exception e) {
            e.printStackTrace();
            reg_id = FirebaseInstanceId.getInstance().getToken();
        }
        submit = (Button) findViewById(R.id.submit);
        submit.setOnClickListener(this);
        fname = (EditText) findViewById(R.id.fname);
        lname = (EditText) findViewById(R.id.lname);
        email = (EditText) findViewById(R.id.email);
        mobile = (EditText) findViewById(R.id.mobile);
        date = (EditText) findViewById(R.id.date);
        //collegename = (EditText) findViewById(R.id.collegename);
        pwd = (EditText) findViewById(R.id.pwd);
        confpwd = (EditText) findViewById(R.id.confpwd);
        spinner_state = (Spinner) findViewById(R.id.spinner_state);
        spinner_city = (Spinner) findViewById(R.id.spinner_city);
        date.setFocusableInTouchMode(false);
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDateTimeField(date);
            }
        });
        imagename = (TextView) findViewById(R.id.imagename);
        upload = (TextView) findViewById(R.id.upload);
        obj_dialog = new Activity_indicator(context);
        obj_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        upload.setOnClickListener(this);
        et_otp = (EditText) findViewById(R.id.et_otp);
        rr_submit = (RelativeLayout) findViewById(R.id.rr_submit);
        rr_submit.setOnClickListener(this);
        scroll = (ScrollView) findViewById(R.id.scroll);
        ll_otp = (LinearLayout) findViewById(R.id.ll_otp);
        Resend_otp = (RelativeLayout) findViewById(R.id.Resend_otp);
        Resend_otp.setOnClickListener(this);


        IntentFilter intentFilter = new IntentFilter(
                "android.provider.Telephony.SMS_RECEIVED");
        mReceiver = new Message_Receiver();

        registerReceiver(mReceiver, intentFilter);

        get_state_city();
        Myapplication.getInstance().trackScreenView("Registration Screen");
        //getamezon();

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


    public void showPermiosssion() {
        if (Build.VERSION.SDK_INT >= 19) {
            String[] s = new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE};

            if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    ActivityCompat.requestPermissions(this, s, RECORD_REQUEST_CODE);
                } else {
                    ActivityCompat.requestPermissions(this, s, RECORD_REQUEST_CODE);
                }
            }
        }
    }

    public void showPermiosssionn() {
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


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {

        if (requestCode == RECORD_REQUEST_CODE) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

            }

        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (data != null) {
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
            File file = new File(extStorageDirectory, getSaltString() + ".jpg");
            String pathh = file.getAbsolutePath();


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

            Bitmap myBitmap = BitmapFactory.decodeFile(f.getAbsolutePath());
            //Drawable d = new BitmapDrawable(getResources(), myBitmap);

            var = f.getAbsolutePath();
            imagename.setText(var.substring(var.lastIndexOf("/") + 1));
            //mainFile = imagename.getText().toString().trim();


//        SetImage(myBitmap, copyimagepath);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit:
                Validate();
                break;
            case R.id.upload:
                openImageChooser();
                break;
            case R.id.rr_submit:

                if (et_otp.getText().toString().equals("")) {
                    et_otp.setError("Enter Otp");
                } else if (!et_otp.getText().toString().equals(otp)) {
                    Toast.makeText(context, "Invalid Otp", Toast.LENGTH_SHORT).show();

                } else {
                    Registration_api();
                }
                break;
            case R.id.Resend_otp:

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
                        send_otp();
                        alert.dismiss();
                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alert.dismiss();
                    }
                });
                break;
        }
    }

    public void Spinner_state_change() {

        spinner_state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                for (Model_State_city model_state : array) {
                    if (model_state.getState_name().equals(spinner_state.getSelectedItem())) {
                        Array_city.clear();
                        for (Model_State_city model_city : array1) {
                            if (model_city.getS_id().equals(model_state.getStateid())) {
                                Array_city.add(model_city.getCityname());
                            }
                        }
                        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(context, R.layout.spinner_textview, Array_city);
                        adapter1.setDropDownViewResource(R.layout.spinner_textview);
                        spinner_city.setAdapter(adapter1);

                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void Validate() {
        if (fname.getText().toString().equals("")) {
            fname.setError("Enter First Name");

        } else if (lname.getText().toString().equals("")) {
            lname.setError("Enter Last Name");

        } else if (email.getText().toString().equals("")) {
            email.setError("Enter Email Address");

        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(
                email.getText().toString()).matches()) {
            email.setError("Invalid Email");

        } else if (mobile.getText().toString().equals("")) {
            mobile.setError("Enter Mobile Number");

        } else if (mobile.getText().toString().length() != 10) {
            mobile.setError("Invalid Mobile");

        } else if (date.getText().toString().equals("")) {
            date.setError("Choose Date");

        } else if (spinner_state.getSelectedItem().toString().equals("Select State")) {
            date.setError(null);
            TextView errorText = (TextView) spinner_state.getSelectedView();
            errorText.setError("Select State");
            errorText.setTextColor(Color.RED);

        } else if (spinner_city.getSelectedItem().toString().equals("Select City")) {
            TextView errorText = (TextView) spinner_city.getSelectedView();
            errorText.setError("Select City");
            errorText.setTextColor(Color.RED);


        } else if (pwd.getText().toString().equals("")) {
            pwd.setError("Enter Password");
        } else if (confpwd.getText().toString().equals("")) {
            confpwd.setError("Enter Confirm Password");


        } else if (!pwd.getText().toString().equals(confpwd.getText().toString())) {
            confpwd.setError("Password does not match");


        } else {

            for (Model_State_city model : array) {
                if (model.getState_name().equals(spinner_state.getSelectedItem())) {
                    Log.e("City", model.getState_name());
                    st_id = "" + model.getStateid();
                }
            }
            for (Model_State_city model : array1) {
                if (model.getCityname().equals(spinner_city.getSelectedItem())) {
                    Log.e("City", model.getCityname());
                    city_id = "" + model.getCityid();
                }
            }
            Array_user.clear();
            Model_user user = new Model_user();
            user.setFname(fname.getText().toString().trim());
            user.setLname(lname.getText().toString().trim());
            user.setEmail(email.getText().toString().trim());
            user.setMobile(mobile.getText().toString().trim());
            user.setDate(date.getText().toString().trim());
            // user.setCname(collegename.getText().toString().trim());
            user.setCity(city_id);
            user.setState(st_id);
            if (imagename.getText().toString().equals("No File Chosen")) {
                user.setPic("");
            } else {
                user.setPic(imagename.getText().toString());
            }
            user.setPassword(pwd.getText().toString().trim());
            user.setDeviceid("" + Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID));
            user.setDevicetoken(reg_id);
            Array_user.add(user);
            send_otp();

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void setDateTimeField(final EditText et) {
        final SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Calendar newCalendar = Calendar.getInstance();

        DatePickerDialog fromDatePickerDialog = new DatePickerDialog(Registration.this,
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

    public void Registration_api() {

        obj_dialog.show();

        String url = Configr.app_url + "registration";
        String json = "";

        json = JSON.add_json(Array_user, pref, "registration");

        HashMap<String, String> param = new HashMap<>();
        param.put("data", json);
        // param.put("image", var);

        Response.Listener<String> lis_res = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response", response);

                obj_dialog.dismiss();

                try {
                    JSONObject jobj = new JSONObject(response);
                    String res_flag = jobj.getString("status");
                    String res_msg = jobj.getString("message");
                    if (res_flag.equals("200")) {
                        JSONArray jarray = new JSONArray(jobj.getString("data"));

                        for (int i = 0; i < jarray.length(); i++) {
                            Model_user model = new Model_user();
                            JSONObject jobj1 = jarray.getJSONObject(i);
                            model.setUserid(jobj1.getString("id"));
                            model.setFname(jobj1.getString("fname"));
                            model.setLname(jobj1.getString("lname"));
                            model.setMobile(jobj1.getString("mobile"));
                            model.setEmail(jobj1.getString("email"));
                            model.setPic(jobj1.getString("propic"));
                            model.setFollower(jobj1.getString("followers"));
                            model.setFollowing(jobj1.getString("following"));

                            pref.setUserid(jobj1.getString("id"));
                            pref.setEmail(jobj1.getString("email"));
                            pref.setMobile(jobj1.getString("mobile"));
                            pref.setfname(jobj1.getString("fname"));
                            pref.setlname(jobj1.getString("lname"));
                            pref.setprofile(jobj1.getString("propic"));
                            pref.setStr_bucketname(jobj1.getString("bucketname"));
                            pref.setStr_Bucket_url(jobj1.getString("url"));
                            pref.setStr_poolid(jobj1.getString("poolid"));
                            pref.setStr_poolregion(jobj1.getString("poolregion"));
                            pref.setStr_bucketregion(jobj1.getString("bucketregion"));
                            Array_user.add(model);
                            pref.setUserid(jobj1.getString("id"));

                            if (!jobj1.getString("propic").equals("")) {
                                beginUpload(var);
                            }


                        }
                        pref.setLogin_Flag("login");
                        Intent i = new Intent(Registration.this, Home_Social.class);
                        startActivity(i);
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


    private void beginUpload(String filePath) {
        transferUtility = Utils.getTransferUtility(this);

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
            mBuilder = new NotificationCompat.Builder(Registration.this);

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


            } else if (newState.equals(TransferState.FAILED)) {
                Toast.makeText(getApplicationContext(), "Fail", Toast.LENGTH_SHORT).show();
            } else if (newState.equals(TransferState.IN_PROGRESS)) {
                Toast.makeText(getApplicationContext(), "Progress", Toast.LENGTH_SHORT).show();
            }
        }
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


                        Model_State_city model1 = new Model_State_city();
                        model1.setStateid("-1");
                        model1.setState_name("Select State");
                        array.add(model1);

                        Model_State_city model2 = new Model_State_city();
                        model2.setCityid("-1");
                        model2.setS_id("-1");
                        model2.setCityname("Select City");
                        array1.add(model2);


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

                        Array_city.clear();
                        for (Model_State_city model : array1) Array_city.add(model.getCityname());

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, R.layout.spinner_textview, Array_state);
                        adapter.setDropDownViewResource(R.layout.spinner_textview);
                        spinner_state.setAdapter(adapter);


                        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(context, R.layout.spinner_textview, Array_city);
                        adapter1.setDropDownViewResource(R.layout.spinner_textview);
                        spinner_city.setAdapter(adapter1);

                        Spinner_state_change();
                        // HTTP_API_getdata();
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

    public void send_otp() {

        obj_dialog.show();

        String url = Configr.app_url + "sendotp";
        String json = "";

        json = JSON.add_json(Array_user, pref, "sendotp");

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


                    if (res_flag.equals("200")) {

                        JSONObject jsonObject = jobj.getJSONObject("data");
                        otp = (jsonObject.getString("OTP"));
                        Log.e("otp_check", "Enter" + otp);

                        scroll.setVisibility(View.GONE);
                        ll_otp.setVisibility(View.VISIBLE);
                        showPermiosssionn();

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