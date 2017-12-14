package com.beanstringnew.Fragment;


import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.ExifInterface;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import com.beanstringnew.Activity.Add_photo_activity;
import com.beanstringnew.Myapplication;
import com.beanstringnew.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static android.content.Context.SENSOR_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 */
public class PostCamera_Activity extends Fragment implements SurfaceHolder.Callback, SensorEventListener {


    android.hardware.Camera camera;
    SurfaceView surfaceView;
    Bitmap bitmap;
    private android.hardware.Camera.Size mSize;
    SurfaceHolder surfaceHolder;
    android.hardware.Camera.PictureCallback rawCallback;
    private SensorManager sensorManager = null;
    android.hardware.Camera.ShutterCallback shutterCallback;
    private File sdRoot;
    private int orientation;
    String fileName;
    String newfile;
    private String dir;
    ImageView clickmee;
    android.hardware.Camera.PictureCallback jpegCallback;
    private ExifInterface exif;
    private static final int STORAGE_PERMISSION_CODE = 123;
    private static final int STORAGE_PERMISSION_CODE_Write = 22;
    private int degrees = -1;


    @Override
    public void onResume() {
        super.onResume();

        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);

        Myapplication.getInstance().trackScreenView("Post Camera Screen");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_post_camera_2, container, false);

        sdRoot = Environment.getExternalStorageDirectory();
        dir = "/Beanstring/Camera/";
        surfaceView = (SurfaceView) v.findViewById(R.id.surfaceView);
        clickmee = (ImageView) v.findViewById(R.id.clickmeee);

        sensorManager = (SensorManager) getActivity().getSystemService(SENSOR_SERVICE);
        surfaceHolder = surfaceView.getHolder();

        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        surfaceHolder.addCallback(this);

        // deprecated setting, but required on Android versions prior to 3.0
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        clickmee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                camera.takePicture(null, null, jpegCallback);
            }
        });

        jpegCallback = new android.hardware.Camera.PictureCallback() {
            public void onPictureTaken(byte[] data, Camera camera) {

                fileName = "IMG_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()).toString() + ".jpg";

                // Creating the directory where to save the image. Sadly in older
                // version of Android we can not get the Media catalog name


                File mkDir = new File(sdRoot, dir);
                mkDir.mkdirs();

                // Main file where to save the data that we recive from the camera
                File pictureFile = new File(sdRoot, dir + fileName);

                newfile = pictureFile.getAbsolutePath();
                Log.e("new_file_jinal", newfile);

                FileOutputStream outStream = null;
                try {
                    outStream = new FileOutputStream(pictureFile);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }


                try {
                    outStream.write(data);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    outStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    exif = new ExifInterface("/sdcard/" + dir + fileName);
                    exif.setAttribute(ExifInterface.TAG_ORIENTATION, "" + orientation);
                    exif.saveAttributes();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                refreshCamera();

                Intent i = new Intent(getActivity(), Add_photo_activity.class);
                i.putExtra("camera", newfile);
                i.putExtra("api", "1");
                startActivity(i);
            }
        };
        Myapplication.getInstance().trackScreenView("Post Camera Screen");

        return v;
    }


    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        try {
            // open the camera
            camera = Camera.open();
        } catch (RuntimeException e) {
            // check for exceptions
            System.err.println(e);
            return;
        }
        Camera.Parameters param;
        param = camera.getParameters();

        List<Camera.Size> sizes = param.getSupportedPictureSizes();
        int w = 0, h = 0;
        for (Camera.Size size : sizes) {
            if (size.width > w || size.height > h) {
                w = size.width;
                h = size.height;
            }

        }
        param.setPictureSize(w, h);

        // modify parameter


        param.getSupportedPictureSizes();
        param.getSupportedPictureFormats();
        param.setPreviewSize(640, 480);
        camera.setParameters(param);
        try {
            // The Surface has been created, now tell the camera where to draw
            // the preview.
            camera.setPreviewDisplay(surfaceHolder);
            camera.startPreview();

            camera.startPreview();
        } catch (Exception e) {
            // check for exceptions
            System.err.println(e);
            return;
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        refreshCamera();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        camera.stopPreview();
        camera.release();
        camera = null;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        synchronized (this) {
            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                RotateAnimation animation = null;
                if (event.values[0] < 4 && event.values[0] > -4) {
                    if (event.values[1] > 0 && orientation != ExifInterface.ORIENTATION_ROTATE_90) {
                        // UP
                        orientation = ExifInterface.ORIENTATION_ROTATE_90;
                        animation = getRotateAnimation(270);
                        degrees = 270;
                    } else if (event.values[1] < 0 && orientation != ExifInterface.ORIENTATION_ROTATE_270) {
                        // UP SIDE DOWN
                        orientation = ExifInterface.ORIENTATION_ROTATE_270;
                        animation = getRotateAnimation(90);
                        degrees = 90;
                    }
                } else if (event.values[1] < 4 && event.values[1] > -4) {
                    if (event.values[0] > 0 && orientation != ExifInterface.ORIENTATION_NORMAL) {
                        // LEFT
                        orientation = ExifInterface.ORIENTATION_NORMAL;
                        animation = getRotateAnimation(0);
                        degrees = 0;
                    } else if (event.values[0] < 0 && orientation != ExifInterface.ORIENTATION_ROTATE_180) {
                        // RIGHT
                        orientation = ExifInterface.ORIENTATION_ROTATE_180;
                        animation = getRotateAnimation(180);
                        degrees = 180;
                    }
                }
                if (animation != null) {

                }
            }

        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }


    public void refreshCamera() {
        if (surfaceHolder.getSurface() == null) {
            // preview surface does not exist
            return;
        }

        // stop preview before making changes
        try {
            camera.stopPreview();
        } catch (Exception e) {
            // ignore: tried to stop a non-existent preview
        }

        // set preview size and make any resize, rotate or
        // reformatting changes here
        // start preview with new settings
        try {
            camera.setPreviewDisplay(surfaceHolder);
            Camera.Parameters params = camera.getParameters();
            if (params.getSupportedFocusModes().contains(
                    Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
                params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
            }
            camera.setParameters(params);

            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                camera.setDisplayOrientation(90);
            }
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {

            }

            camera.startPreview();
        } catch (Exception e) {

        }
    }

    private RotateAnimation getRotateAnimation(float toDegrees) {
        float compensation = 0;

        if (Math.abs(degrees - toDegrees) > 180) {
            compensation = 360;
        }

        // When the device is being held on the left side (default position for
        // a camera) we need to add, not subtract from the toDegrees.
        if (toDegrees == 0) {
            compensation = -compensation;
        }

        // Creating the animation and the RELATIVE_TO_SELF means that he image
        // will rotate on it center instead of a corner.
        RotateAnimation animation = new RotateAnimation(degrees, toDegrees - compensation, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

        // Adding the time needed to rotate the image
        animation.setDuration(250);

        // Set the animation to stop after reaching the desired position. With
        // out this it would return to the original state.
        animation.setFillAfter(true);

        return animation;
    }
}
