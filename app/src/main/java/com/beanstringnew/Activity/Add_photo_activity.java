package com.beanstringnew.Activity;

import android.Manifest;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

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
import com.beanstringnew.Controller.FileUtils;
import com.beanstringnew.Controller.MediaController;
import com.beanstringnew.Json_model.JSON;
import com.beanstringnew.Model.Model_profile;
import com.beanstringnew.Model.Model_user;
import com.beanstringnew.Myapplication;
import com.beanstringnew.R;
import com.beanstringnew.Shared.Pref_Master;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.share.model.AppInviteContent;
import com.facebook.share.widget.AppInviteDialog;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.channels.FileChannel;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import bolts.AppLinks;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO;
import static com.beanstringnew.Controller.MediaController.path_value;

/**
 * Created by Admin on 8/10/2017.
 */

public class Add_photo_activity extends AppCompatActivity {

    EditText etpost;
    public static ArrayList<Model_user> Array_user = new ArrayList<>();
    Pref_Master pref;
    Context context = this;
    Activity_indicator obj_dialog;
    private static String TAG = "Add_photo_fragment";
    ImageView image_photo;
    ArrayList<Model_profile> Array_userr = new ArrayList<>();
    int abc = 0;
    private TransferUtility transferUtility;
    Uri uri;
    LinearLayout ll_progress;
    TextView progress_percentage;
    ProgressBar progresbar;
    LinearLayout camera;
    LinearLayout gallery;
    LinearLayout postbtn;
    LinearLayout video;
    LinearLayout tabupload;
    public static int videocount = 0;
    String api = "vhgkkv";
    private static int RESULT_LOAD_IMG = 1;
    private static final int REQUEST_TAKE_GALLERY_VIDEO = 2;
    String path = "";
    Uri selectedImageUri;
    String var_thumb = "";
    String var_video = "";
    String var_video_thumb = "";
    private File tempFile;
    String var = "";
    String copythumb = "";
    String PTh = "";
    String getpostone = "";
    String pre_path = "";
    ImageView postimg1;
    Bitmap risized;
    String video_path = "";
    TextView txt_path;
    ProgressBar loading;
    VideoView videopost;
    Uri fileUri;
    public static String pathee = "";


    ImageView facebook_invite, drawer, imageView, notification, search;
    LinearLayout llmybean;
    TextView upload_count, noti_count;
    CallbackManager callbackManager;
    AppInviteDialog appInviteDialog;
    String image_url = "";
    private static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 55;
    public static int Select_value = 0;
    LinearLayout ll_one;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_photo);
        pref = new Pref_Master(context);
        transferUtility = Utils.getTransferUtility(context);
        // showPermiosssion();
        checkAndRequestPermissions();


        camera = (LinearLayout) findViewById(R.id.camera);
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAndRequestPermissions();
                Intent i = new Intent(context, PostCameraViewpager.class);
                startActivity(i);
                Select_value = 1;
            }
        });
        gallery = (LinearLayout) findViewById(R.id.gallery);
        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAndRequestPermissions();

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), RESULT_LOAD_IMG);
                Select_value = 2;
            }
        });
        txt_path = (TextView) findViewById(R.id.txt_path);
        postbtn = (LinearLayout) findViewById(R.id.postbtn);
        postbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (etpost.getText().toString().equals("") && txt_path.getText().toString().equals("")) {
                    Toast.makeText(context, "Please Add photo/Video/Text", Toast.LENGTH_SHORT).show();
                } else {
                    if (!etpost.getText().toString().equals("") && txt_path.getText().toString().equals("")) {
                        Array_user.clear();
                        Model_user user = new Model_user();
                        user.setPost(etpost.getText().toString().trim());
                        Array_user.add(user);
                        Select_value = 3;
                        add_photo();

                    } else if (videocount == 1) {
                        Array_user.clear();
                        Model_user user = new Model_user();
                        user.setPost(etpost.getText().toString().trim());
                        user.setVideo_upload(video_path);
                        Array_user.add(user);

                        Log.e("Path_jinal", path_value);
                        UploadVideo(MediaController.path_value);
                        Upload_video_thumb(var_video_thumb);
                    } else if (Select_value == 1) {
                        Array_user.clear();
                        Model_user user = new Model_user();
                        user.setPost(etpost.getText().toString().trim());
                        user.setImage_upload(pre_path);
                        Array_user.add(user);

                        UploadImage(var);
                        Upload_thumb(copythumb);
                    } else if (Select_value == 2) {

                        Array_user.clear();
                        Model_user user = new Model_user();
                        user.setPost(etpost.getText().toString().trim());
                        user.setImage_upload(pre_path);
                        Array_user.add(user);

                        UploadImage(var);
                        Upload_thumb(copythumb);
                    }
                }
            }
        });

        videopost = (VideoView) findViewById(R.id.videopost);
        loading = (ProgressBar) findViewById(R.id.loading);
        video = (LinearLayout) findViewById(R.id.video);
        video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAndRequestPermissions();
                Intent intent = new Intent();
                intent.setType("video/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Video"), REQUEST_TAKE_GALLERY_VIDEO);
                Select_value = 4;
            }
        });
        postimg1 = (ImageView) findViewById(R.id.postimg1);
        etpost = (EditText) findViewById(R.id.etpost);

        tabupload = (LinearLayout) findViewById(R.id.tabupload);
        image_photo = (ImageView) findViewById(R.id.image_photo);
        obj_dialog = new Activity_indicator(context);
        obj_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        ll_progress = (LinearLayout) findViewById(R.id.ll_progress);
        progress_percentage = (TextView) findViewById(R.id.progress_percentage);
        progresbar = (ProgressBar) findViewById(R.id.progresbar);
        ll_one = (LinearLayout) findViewById(R.id.ll_one);

        Log.e("image_jinal", pref.getStr_profile());
        FacebookSdk.sdkInitialize(context);
        callbackManager = CallbackManager.Factory.create();
        Uri targetUrl = AppLinks.getTargetUrlFromInboundIntent(this, getIntent());
        if (targetUrl != null) {
            Log.i("Activity", "App Link Target URL: " + targetUrl.toString());
        }
        upload_count = (TextView) findViewById(R.id.upload_count);
        noti_count = (TextView) findViewById(R.id.noti_count);

        imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Add_photo_activity.this, Home_Social.class);
                intent.putExtra("fragmentcode", Configr.Fragment_ID.MainFragment);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
        notification = (ImageView) findViewById(R.id.notification);
        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                noti_count.setVisibility(View.GONE);
                Intent intent = new Intent(Add_photo_activity.this, Notification_activity.class);
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
                Intent intent = new Intent(Add_photo_activity.this, My_profile_activity.class);
                startActivity(intent);
                finish();
            }
        });
        llmybean = (LinearLayout) findViewById(R.id.llmybean);
        llmybean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Add_photo_activity.this, My_Beans_Activity_new.class);
                startActivity(intent);
                finish();
            }
        });
        search = (ImageView) findViewById(R.id.search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Add_photo_activity.this, Search_activity.class);
                startActivity(i);
                finish();
            }
        });

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            var = extras.getString("camera");
            api = extras.getString("api");
//            Log.e("APi", api);
        }


        Intent intent = getIntent();
        // Get the action of the intent
        String action = intent.getAction();
        // Get the type of intent (Text or Image)
        String type = intent.getType();

        if (Intent.ACTION_SEND.equals(action) && type != null) {
            // When tyoe is 'text/plain'
            if (type.startsWith("image/")) { // When type is 'image/*'
                // handleSendImage(intent); // Handle single image being sent
                Select_value = 2;
                Uri imageUri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
                // When image URI is not null
                if (imageUri != null) {
                    // Update UI to reflect image being shared
                    uri = imageUri;

                    try {
                        var = getPath(uri);
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }

                    File file = new File(var);
                    Log.e("File", "File");

                    File folder = new File(Environment.getExternalStorageDirectory().toString() + "/Beanstring/GalleryImage");
                    folder.mkdirs();
                    String extStorageDirectory = folder.toString();


                    File filee = new File(extStorageDirectory, getSaltString() + ".jpg");
                    String pathh = filee.getAbsolutePath();
                    Log.e("PAthhh", pathh);

                    File f = new File(pathh);

                    if (!f.exists()) {
                        try {
                            f.createNewFile();
                            copyFile(file, f);
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
//
                    Bitmap myBitmap = decodeSampledBitmapFromFile(f.getAbsolutePath(), 1000, 700);
                    try {
                        myBitmap.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(f.getAbsolutePath()));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    var = f.getAbsolutePath();
                    Log.e("var_jinal", var);
                    pre_path = var.substring(var.lastIndexOf("/") + 1);
                    txt_path.setText(var.substring(var.lastIndexOf("/") + 1));
                    Log.e("pre_path", pre_path);

                    Bitmap bitmap = decodeSampledBitmapFromFile(file.getAbsolutePath(), 1000, 700);
                    try {
                        risized = rotateImage(bitmap, 360);
                        risized.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(var));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                    SetCameraImage(risized, var);

                    Bitmap ThumbImage = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(f.getAbsolutePath()), 512, 512);
                    Uri uri = getImageUri(context, ThumbImage);

                    try {
                        var_thumb = getPath(uri);
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                    File source_file = new File(var_thumb);
                    File folder_thumb = new File(Environment.getExternalStorageDirectory().toString() + "/Beanstring/Imagethumb");
                    folder_thumb.mkdirs();
                    String extStorageDirectory_thumb = folder_thumb.toString();

                    File file_thumb = new File(extStorageDirectory_thumb, pre_path);
                    String pathh_thumb = file_thumb.getAbsolutePath();
                    File f1 = new File(pathh_thumb);
                    if (!f1.exists()) {
                        try {
                            f1.createNewFile();
                            copyFile(source_file, f1);
                        } catch (IOException e) {
// TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }

                    copythumb = f1.getAbsolutePath();
                    Log.e("copythumb", copythumb);
                }


            }

        } else {

            if (api.equals("1")) {
                File file = new File(var);
                Log.e("File", "File");

                File folder = new File(Environment.getExternalStorageDirectory().toString() + "/Beanstring/GalleryImage");
                folder.mkdirs();
                String extStorageDirectory = folder.toString();


                File filee = new File(extStorageDirectory, getSaltString() + ".jpg");
                String pathh = filee.getAbsolutePath();
                Log.e("PAthhh", pathh);

                File f = new File(pathh);

                if (!f.exists()) {
                    try {
                        f.createNewFile();
                        copyFile(file, f);
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
//
                Bitmap myBitmap = decodeSampledBitmapFromFile(f.getAbsolutePath(), 1000, 700);
                try {
                    myBitmap.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(f.getAbsolutePath()));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                var = f.getAbsolutePath();
                Log.e("var_jinal", var);
                pre_path = var.substring(var.lastIndexOf("/") + 1);
                txt_path.setText(var.substring(var.lastIndexOf("/") + 1));
                Log.e("pre_path", pre_path);

                Bitmap bitmap = decodeSampledBitmapFromFile(file.getAbsolutePath(), 1000, 700);
                try {
                    risized = rotateImage(bitmap, 90);
                    risized.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(var));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                SetCameraImage(risized, var);

                Bitmap ThumbImage = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(f.getAbsolutePath()), 512, 512);
                Uri uri = getImageUri(context, ThumbImage);

                try {
                    var_thumb = getPath(uri);
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
                File source_file = new File(var_thumb);
                File folder_thumb = new File(Environment.getExternalStorageDirectory().toString() + "/Beanstring/Imagethumb");
                folder_thumb.mkdirs();
                String extStorageDirectory_thumb = folder_thumb.toString();

                File file_thumb = new File(extStorageDirectory_thumb, pre_path);
                String pathh_thumb = file_thumb.getAbsolutePath();
                File f1 = new File(pathh_thumb);
                if (!f1.exists()) {
                    try {
                        f1.createNewFile();
                        copyFile(source_file, f1);
                    } catch (IOException e) {
// TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }

                copythumb = f1.getAbsolutePath();
                Log.e("copythumb", copythumb);

            } else if (api.equals("2")) {

                Intent ii = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

                // create a file to save the video
                fileUri = getOutputMediaFileUri(MEDIA_TYPE_VIDEO);

                // set the image file name
                intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

                // set the video image quality to high
                intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);

                // start the Video Capture Intent
                startActivityForResult(ii, REQUEST_TAKE_GALLERY_VIDEO);
            }
        }

        beans_Count();

    }

    private Uri getOutputMediaFileUri(int type) {

        return FileProvider.getUriForFile(Add_photo_activity.this, "com.beanstringnew.fileprovider",
                getOutputMediaFile(type));
    }

    private static File getOutputMediaFile(int type) {

        // Check that the SDCard is mounted
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory().toString() + "/Beanstring/video");


        // Create the storage directory(MyCameraVideo) if it does not exist
        if (!mediaStorageDir.exists()) {

            if (!mediaStorageDir.mkdirs()) {


                Log.d("MyCameraVideo", "Failed to create directory MyCameraVideo.");
                return null;
            }
        }

        java.util.Date date = new java.util.Date();
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
                .format(date.getTime());

        File mediaFile;

        if (type == MEDIA_TYPE_VIDEO) {

            // For unique video file name appending current timeStamp with file name
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + "VID_" + timeStamp + ".mp4");
            pathee = mediaFile.getAbsolutePath();


        } else {
            return null;
        }
        Myapplication.getInstance().trackScreenView("Add Photo/Video Screen");
        return mediaFile;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK && null != data) {
            // Get the Image from data
            uri = data.getData();
            Log.e("uri", data.getData().toString());

            try {
                path = getPath(uri);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }

            File source = new File(path);

            File folder = new File(Environment.getExternalStorageDirectory().toString() + "/Beanstring/GalleryImage");
            folder.mkdirs();
            String extStorageDirectory = folder.toString();


            File file = new File(extStorageDirectory, getSaltString() + ".jpg");
            String pathh = file.getAbsolutePath();
            Log.e("PAthhh", pathh);

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
//
            Bitmap myBitmap = decodeSampledBitmapFromFile(f.getAbsolutePath(), 1000, 700);
            try {
                myBitmap.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(f.getAbsolutePath()));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            var = f.getAbsolutePath();
            Log.e("var_jinal", var);
            pre_path = var.substring(var.lastIndexOf("/") + 1);
            txt_path.setText(var.substring(var.lastIndexOf("/") + 1));
            Log.e("pre_path", pre_path);

            Bitmap ThumbImage = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(f.getAbsolutePath()), 512, 512);
            Uri uri = getImageUri(context, ThumbImage);

            try {
                var_thumb = getPath(uri);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            File source_file = new File(var_thumb);
            File folder_thumb = new File(Environment.getExternalStorageDirectory().toString() + "/Beanstring/Imagethumb");
            folder_thumb.mkdirs();
            String extStorageDirectory_thumb = folder_thumb.toString();

            File file_thumb = new File(extStorageDirectory_thumb, pre_path);
            String pathh_thumb = file_thumb.getAbsolutePath();
            File f1 = new File(pathh_thumb);
            if (!f1.exists()) {
                try {
                    f1.createNewFile();
                    copyFile(source_file, f1);
                } catch (IOException e) {
// TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            copythumb = f1.getAbsolutePath();
            Log.e("copythumb", copythumb);

            Bitmap bitmap = decodeSampledBitmapFromFile(file.getAbsolutePath(), 1000, 700);


            try {
                risized = rotateImage(bitmap, 360);
                risized.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(var));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            SetCameraImage(risized, var);

        } else if (requestCode == REQUEST_TAKE_GALLERY_VIDEO && resultCode == RESULT_OK && null != data) {


            selectedImageUri = data.getData();
            Log.e("SelectedImageUri", "" + selectedImageUri);

            if (selectedImageUri != null) {
                Cursor cursor = context.getContentResolver().query(selectedImageUri, null, null, null, null, null);

                try {
                    if (cursor != null && cursor.moveToFirst()) {

                        String displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                        Log.i(TAG, "Display Name: " + displayName);

                        int sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE);
                        String size = null;
                        if (!cursor.isNull(sizeIndex)) {
                            size = cursor.getString(sizeIndex);
                        } else {
                            size = "Unknown";
                        }
                        Log.i(TAG, "Size: " + size);

                        tempFile = FileUtils.saveTempFile(displayName, context, selectedImageUri);
                        new VideoCompressor().execute();


                    }
                } finally {
                    if (cursor != null) {
                        cursor.close();
                    }
                }
            }


        }
    }


    public void SetCameraImage(Bitmap bitmapff, String s) {
        BitmapDrawable bdrawable = new BitmapDrawable(context.getResources(), bitmapff);

        Array_user.clear();
        Model_user user = new Model_user();
        user.setPost(etpost.getText().toString().trim());
        user.setImage_upload(pre_path);
        Array_user.add(user);

        postimg1.setVisibility(View.VISIBLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            postimg1.setBackground(bdrawable);

            pre_path = var.substring(var.lastIndexOf("/") + 1);
            txt_path.setText(var.substring(var.lastIndexOf("/") + 1));
        }
    }


    public void UploadImage(String filePath) {
        CannedAccessControlList cannedAccessControlList;
        cannedAccessControlList = CannedAccessControlList.PublicRead;

        if (filePath == null) {
            Toast.makeText(context, "Could not find the filepath of the selected files", Toast.LENGTH_LONG).show();
            return;
        }
        File file = new File(filePath);


        getpostone = file.getName();
//        Toast.makeText(getApplicationContext(),file.getName(), Toast.LENGTH_LONG).show();
        Log.e("User_id", pref.getStr_Userid());
        final TransferObserver observer = transferUtility.upload(pref.getStr_bucketname() + "/" + pref.getStr_Bucket_url() + "/post/image", file.getName(), file, new ObjectMetadata());

        /*
         * Note that usually we set the transfer listener after initializing the
         * transfer. However it isn't required in this sample app. The flow is
         * click upload button -> start an activity for image selection
         * startActivityForResult -> onActivityResult -> beginUpload -> onResume
         * -> set listeners to in progress transfers.
         */
        // observer.setTransferListener(new UploadListener());

        observer.setTransferListener(new TransferListener() {
            @Override
            public void onStateChanged(int id, TransferState state) {
                if (state.COMPLETED.equals(observer.getState())) {

                    // Toast.makeText(context, "File Upload Complete", Toast.LENGTH_SHORT).show();

                    File file = new File(var);
                    file.delete();

                    add_photo();

                }
                if (state.IN_PROGRESS.equals(observer.getState())) {
                    ll_progress.setVisibility(View.VISIBLE);
                    tabupload.setVisibility(View.INVISIBLE);
                }

            }

            @Override
            public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {

                long _bytesCurrent = bytesCurrent;
                long _bytesTotal = bytesTotal;

                double percentage = ((double) _bytesCurrent / (double) _bytesTotal * 100);
                Log.d("percentage", "" + percentage);
                percentage = Double.parseDouble(new DecimalFormat("##.#").format(percentage));

                progresbar.setProgress((int) percentage);
                progress_percentage.setText(percentage + "%");
            }

            @Override
            public void onError(int id, Exception ex) {
                Toast.makeText(context, "" + ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // add_photo();
        beans_Count();
    }

    public void Upload_thumb(String fPath) {
        CannedAccessControlList cannedAccessControlList;
        cannedAccessControlList = CannedAccessControlList.PublicRead;

        if (fPath == null) {
            Toast.makeText(context, "Could not find the filepath of the selected file", Toast.LENGTH_LONG).show();
            return;
        }
        File file = new File(fPath);


        getpostone = file.getName();
//        Toast.makeText(getApplicationContext(),file.getName(), Toast.LENGTH_LONG).show();
        Log.e("User_id", pref.getStr_Userid());
        TransferObserver observer = transferUtility.upload(pref.getStr_bucketname() + "/" + pref.getStr_Bucket_url() + "/post/image/thumb", file.getName(), file, new ObjectMetadata());

        /*
         * Note that usually we set the transfer listener after initializing the
         * transfer. However it isn't required in this sample app. The flow is
         * click upload button -> start an activity for image selection
         * startActivityForResult -> onActivityResult -> beginUpload -> onResume
         * -> set listeners to in progress transfers.
         */
        // observer.setTransferListener(new UploadListener());

        //observer.setTransferListener(new UploadListener());
        // add_photo();
        beans_Count();
    }

    public void UploadVideo(String filePath) {
        CannedAccessControlList cannedAccessControlList;
        cannedAccessControlList = CannedAccessControlList.PublicRead;

        if (filePath == null) {
            Toast.makeText(context, "Could not find the filepath of the selected file", Toast.LENGTH_LONG).show();
            return;
        }
        File file = new File(filePath);


        getpostone = file.getName();
//        Toast.makeText(getApplicationContext(),file.getName(), Toast.LENGTH_LONG).show();
        Log.e("User_id", pref.getStr_Userid());
        final TransferObserver observer = transferUtility.upload(pref.getStr_bucketname() + "/" + pref.getStr_Bucket_url() + "/post/video", file.getName(), file, new ObjectMetadata());

        /*
         * Note that usually we set the transfer listener after initializing the
         * transfer. However it isn't required in this sample app. The flow is
         * click upload button -> start an activity for image selection
         * startActivityForResult -> onActivityResult -> beginUpload -> onResume
         * -> set listeners to in progress transfers.
         */
        // observer.setTransferListener(new UploadListener());

        observer.setTransferListener(new TransferListener() {
            @Override
            public void onStateChanged(int id, TransferState state) {
                if (state.COMPLETED.equals(observer.getState())) {

                    Toast.makeText(context, "File Upload Complete", Toast.LENGTH_SHORT).show();

                    File file = new File(path_value);
                    file.delete();

                    add_photo();

                }
                if (state.IN_PROGRESS.equals(observer.getState())) {
                    ll_progress.setVisibility(View.VISIBLE);
                    tabupload.setVisibility(View.INVISIBLE);
                }

            }

            @Override
            public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {

                long _bytesCurrent = bytesCurrent;
                long _bytesTotal = bytesTotal;

                double percentage = ((double) _bytesCurrent / (double) _bytesTotal * 100);
                Log.d("percentage", "" + percentage);
                percentage = Double.parseDouble(new DecimalFormat("##.#").format(percentage));

                progresbar.setProgress((int) percentage);
                progress_percentage.setText(percentage + "%");
            }

            @Override
            public void onError(int id, Exception ex) {
                Toast.makeText(context, "" + ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        // add_photo();
        beans_Count();
    }

    public void Upload_video_thumb(String filePath) {
        CannedAccessControlList cannedAccessControlList;
        cannedAccessControlList = CannedAccessControlList.PublicRead;

        if (filePath == null) {
            Toast.makeText(context, "Could not find the filepath of the selected file", Toast.LENGTH_LONG).show();
            return;
        }
        File file = new File(filePath);


        getpostone = file.getName();
//        Toast.makeText(getApplicationContext(),file.getName(), Toast.LENGTH_LONG).show();
        Log.e("User_id", pref.getStr_Userid());
        TransferObserver observer = transferUtility.upload(pref.getStr_bucketname() + "/" + pref.getStr_Bucket_url() + "/post/video/thumb", file.getName(), file, new ObjectMetadata());

        /*
         * Note that usually we set the transfer listener after initializing the
         * transfer. However it isn't required in this sample app. The flow is
         * click upload button -> start an activity for image selection
         * startActivityForResult -> onActivityResult -> beginUpload -> onResume
         * -> set listeners to in progress transfers.
         */
        // observer.setTransferListener(new UploadListener());

        //observer.setTransferListener(new UploadListener());
        // add_photo();
        beans_Count();
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

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String pathhh = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(pathhh);
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

    public static Bitmap decodeSampledBitmapFromFile(String path, int reqWidth, int reqHeight) { // BEST QUALITY MATCH

//First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

// Calculate inSampleSize, Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        int inSampleSize = 1;

        if (height > reqHeight) {
            inSampleSize = Math.round((float) height / (float) reqHeight);
        }
        int expectedWidth = width / inSampleSize;

        if (expectedWidth > reqWidth) {
//if(Math.round((float)width / (float)reqWidth) > inSampleSize) // If bigger SampSize..
            inSampleSize = Math.round((float) width / (float) reqWidth);
        }

        options.inSampleSize = inSampleSize;

// Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(path, options);
    }


    private boolean checkAndRequestPermissions() {
        int permissionSendMessage = ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS);
        int locationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (locationPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.RECORD_AUDIO);
        }
        if (permissionSendMessage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }


        if (permissionSendMessage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAPTURE_VIDEO_OUTPUT);
        }


        if (permissionSendMessage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }


        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }


    @Override
    protected void onResume() {
        super.onResume();
        beans_Count();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(Add_photo_activity.this, Home_Social.class);
        i.putExtra("fragmentcode", Configr.Fragmentt_ID.Main);
        startActivity(i);
        finish();
        deleteTempFile();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        deleteTempFile();
    }

    class VideoCompressor extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading.setVisibility(View.VISIBLE);
            Log.d(TAG, "Start video compression");
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            Log.e("Compress", tempFile.getPath());
            return MediaController.getInstance().convertVideo(tempFile.getPath());
        }

        @Override
        protected void onPostExecute(Boolean compressed) {
            super.onPostExecute(compressed);
            loading.setVisibility(View.GONE);
            if (compressed) {
                Log.d(TAG, "Compression successfully!");
                Setvideo(MediaController.path_value);
                PTh = path_value.substring(path_value.lastIndexOf("/") + 1);
                video_path = PTh;
                txt_path.setText(PTh);

                File source = new File(path_value);

                File folder_thumb_video = new File(Environment.getExternalStorageDirectory().toString() + "/Beanstring/Video/thumb");
                folder_thumb_video.mkdirs();

                String extStorageDirectory = folder_thumb_video.toString();

                File file_thumb_video = new File(extStorageDirectory, PTh.substring(0, PTh.length() - 4) + ".jpg");
                Log.e("File_thumb_Video", ":" + file_thumb_video);

                var_video_thumb = file_thumb_video.getAbsolutePath();
                Log.e("Var_video_thumb", var_video_thumb);

                File fname = new File(var_video_thumb);
                if (!fname.exists()) {
                    try {
                        fname.createNewFile();
                        copyFile(source, fname);
                    } catch (IOException e) {
// TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }


                Bitmap myBitmap = createVideoThumbnail(fname.getAbsolutePath(), 100);
                try {
                    myBitmap.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(fname.getAbsolutePath()));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                var_video_thumb = fname.getAbsolutePath();


            }
        }
    }

    private void Setvideo(String path) {
        videocount = 1;

        postimg1.setVisibility(View.GONE);
        videopost.setVisibility(View.VISIBLE);

        videopost.setVideoPath(path);

        videopost.start();

    }

    private void deleteTempFile() {
        if (tempFile != null && tempFile.exists()) {
            tempFile.delete();
        }
    }


    public static Bitmap createVideoThumbnail(String filePath, int kind) {
        Bitmap bitmap = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            retriever.setDataSource(filePath);
            bitmap = retriever.getFrameAtTime(-1);
        } catch (IllegalArgumentException ex) {
            // Assume this is a corrupt video file
        } catch (RuntimeException ex) {
            // Assume this is a corrupt video file.
        } finally {
            try {
                retriever.release();
            } catch (RuntimeException ex) {
                // Ignore failures while cleaning up.
            }
        }

        if (bitmap == null) return null;

        if (kind == MediaStore.Video.Thumbnails.MICRO_KIND) {
            // Scale down the bitmap if it's too large.
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            int max = Math.max(width, height);
            if (max > 512) {
                float scale = 512f / max;
                int w = Math.round(scale * width);
                int h = Math.round(scale * height);
                bitmap = Bitmap.createScaledBitmap(bitmap, w, h, true);
            }
        } else if (kind == MediaStore.Video.Thumbnails.MICRO_KIND) {
            bitmap = ThumbnailUtils.createVideoThumbnail(filePath, MediaStore.Video.Thumbnails.MICRO_KIND);
        }
        return bitmap;
    }


    public void open_dialog() {


        appInviteDialog = new AppInviteDialog(this);
        appInviteDialog.registerCallback(callbackManager,
                new FacebookCallback<AppInviteDialog.Result>() {

                    @Override
                    public void onSuccess(AppInviteDialog.Result result) {
                        Fb_invites_beans.beans_Count(context, obj_dialog, pref, Array_userr);
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


   /* public void showPermiosssion() {
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
    }*/

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_ID_MULTIPLE_PERMISSIONS) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Displaying a toast

            } else {
                //Displaying another toast if permission is not granted

            }
        }
    }


    public void add_photo() {


        String url = Configr.app_url + "uploadph_video";
        String json = "";

        json = JSON.add_json(Array_user, pref, "upload");

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
                    String otp = "", userid = "";

                    if (res_flag.equals("200")) {
                        videocount = 0;
                        Intent i = new Intent(context, Upload_image_and_video_activity.class);
                        startActivity(i);
                        etpost.setText("");
//                        uploadimage.setText("");
//                        uploadvideo.setText("");

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

    public void beans_Count() {

        obj_dialog.show();

        String url = Configr.app_url + "beanscount";

        String json = "";

        json = JSON.add(Array_userr, pref, "beanscount");

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

                        for (int i = 0; i < jarray.length(); i++) {
                            Model_profile model = new Model_profile();
                            JSONObject jobj1 = jarray.getJSONObject(i);

                            if (jobj1.getString("propic").equals("")) {
                                image_photo.setImageResource(R.drawable.default_imggg);
                            } else {
                                Picasso.with(context).load(jobj1.getString("propic")).skipMemoryCache().into(image_photo);
                            }
                            model.setBeans(jobj1.getString("beans"));
                            model.setNoti_count(jobj1.getString("notification"));
                            image_url = jobj1.getString("url");

                            if (model.getNoti_count().equals("0")) {
                                noti_count.setVisibility(View.GONE);
                            } else {
                                noti_count.setVisibility(View.VISIBLE);
                                noti_count.setText(jobj1.getString("notification"));
                            }
                            upload_count.setText(jobj1.getString("beans"));
                            //  noti_count.setText(jobj1.getString("notification"));
                            Array_userr.add(model);


                        }

                    } else {
                        Toast.makeText(context, res_msg, Toast.LENGTH_SHORT).show();
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

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }
}
