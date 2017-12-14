package com.beanstringnew.Controller;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by admin on 10/1/2016.
 */

public class Configr {

    public static String app_url = "http://beanstring.com/api/";
    //  public static String app_url = "http://beanstring.com/beta/api/";

    public static String app_link = "https://fb.me/1353571058031874";

    public static String headunm = "Admin";
    public static String headkey = "123";

    public static String Key_status = "";
    public static String Message = "Server Error....Please try again...!!!";

    public static final String VIDEO_COMPRESSOR_APPLICATION_DIR_NAME = "BeanString";

    /**
     * Application folder for video files
     */
    public static final String VIDEO_COMPRESSOR_COMPRESSED_VIDEOS_DIR = "/Compressed Videos/";

    /**
     * Application folder for video files
     */
    public static final String VIDEO_COMPRESSOR_TEMP_DIR = "/Temp/";

//    public static final String FTP_HOST = "www.beanstring.com";
//    public static final String FTP_USER = "beanstring@beanstring.com";
//    public static final String FTP_PASS = "123456789987654";


//    public static final String FTP_HOST = "52.77.243.85";
//    public static final String FTP_USER = "abitwlwar";
//    public static final String FTP_PASS = "adm!n@abitwlwar";

    //    public static final String FTP_HOST = "5.189.141.180";
//    public static final String FTP_USER = "root";
//    public static final String FTP_PASS = "e8hHZ7vbONG2rF";
//
//
    public static final String FTP_HOST = "ftp.beanstring.com";
    public static final String FTP_USER = "beanftp@beanstring.com";
    public static final String FTP_PASS = "Ws2P@uBi]Haw";


    // code to define map header
    public static Map<String, String> getHeaderParam() {
        Map<String, String> header = new HashMap<>();
        header.put("username", Configr.headunm);
        header.put("apikey", Configr.headkey);
        return header;
    }

    public final static class Fragment_ID {
        public final static int MainFragment = 1;
        public final static int Notification = 2;
        public final static int Search = 3;
        public final static int Add_photo = 4;
        public final static int Myprofile = 5;
        public final static int MyBean = 6;
    }

    public final static class Fragmentt_ID {
        public final static int Main = 1;
        public final static int searchh = 2;
        public final static int cart = 3;
        public final static int MainmallFragment = 4;
        public final static int Product = 5;

    }

    public static Uri onShareItem(String uu, Context context) {
        String path = "";
        try {
            URL url = new URL(uu);
            //Bitmap image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            Bitmap image = BitmapFactory.decodeStream((InputStream) new URL(uu).getContent());
            path = MediaStore.Images.Media.insertImage(context.getContentResolver(),
                    image, "Image Description", null);

        } catch (IOException e) {
            System.out.println(e);
        }
        return Uri.parse(path);
    }

    public static Bitmap onShareItemBitmap(String uu, Context context) {
        Bitmap image = null;
        try {
            image = BitmapFactory.decodeStream((InputStream) new URL(uu).getContent());
        } catch (IOException e) {
            System.out.println(e);
        }
        return image;
    }
}
