package com.beanstringnew.Shared;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by admin on 10/1/2016.
 */
public class Pref_Master {

    // code for construstor of : class - Pref_Master
    public Pref_Master(Context context) {
        this.context = context;
        pref = PreferenceManager.getDefaultSharedPreferences(context);
    }

    // code for define variable of : shared preference
    Context context;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    // code for define variable of : string

    private String str_login_flag = "loginflag";
    private String default_login_flag = "0";

    private String str_Userid = "userid";
    private String default_Userid = "0";

    private String str_Email = "email";
    private String default_Email = "0";


    private String str_Mobile = "mobile";
    private String default_Mobile = "0";

    private String str_Pwd = "password";
    private String default_pwd = "0";

    private String str_otp = "otpcode";
    private String default_otp = "0";

    private String str_fname = "fname";
    private String default_fname = " ";

    private String str_lname = "lname";
    private String default_lname = " ";


    private String str_profile = "profile";
    private String default_profile = " ";

    private String str_pos = "pos";
    private String default_pos = " ";

    private String str_pos_latest = "pos_latest";
    private String default_pos_latest = " ";

    private String str_pos_top = "pos_top";
    private String default_pos_top = " ";

    private String str_latest = "latest";
    private String default_latest = " ";

    private String str_top = "top";
    private String default_top = " ";


    private String str_Bucket_url = "Bucket_url";
    private String Bucket_url = " ";

    private String str_bucketname = "bucketname";
    private String bucketname = " ";

    private String str_poolid = "poolid";
    private String poolid = " ";

    private String str_poolregion = "poolregion";
    private String poolregion = " ";

    private String str_bucketregion = "bucketregion";
    private String bucketregion = " ";


    private String str_postid = "postid";
    private String postid = "";

    private String str_post_uid = "postuid";
    private String postuid = "";


    // *******************************************************************************************
    // *******************************************************************************************
    // *******************************************************************************************
    // code for define and initialize :  get methods
    // *******************************************************************************************
    // *******************************************************************************************
    // *******************************************************************************************

    public String getStr_login_flag() {
        return pref.getString(str_login_flag, default_login_flag);
    }

    public String getStr_login_email() {
        return pref.getString(str_Email, default_Email);
    }

    public String getStr_login_mobile() {
        return pref.getString(str_Mobile, default_Mobile);
    }

    public String getStr_Pwd() {
        return pref.getString(str_Pwd, default_pwd);
    }

    public String getStr_Userid() {
        return pref.getString(str_Userid, default_Userid);
    }

    public String getStr_otp() {
        return pref.getString(str_otp, default_otp);
    }

    public String getStr_fname() {
        return pref.getString(str_fname, default_fname);
    }

    public String getStr_lname() {
        return pref.getString(str_lname, default_lname);
    }

    public String getStr_profile() {
        return pref.getString(str_profile, default_profile);
    }

    public String getStr_pos() {
        return pref.getString(str_pos, default_pos);
    }

    public String getStr_pos_latest() {
        return pref.getString(str_pos_latest, default_pos_latest);
    }

    public String getStr_pos_top() {
        return pref.getString(str_pos_top, default_pos_top);
    }

    public String getStr_top() {
        return pref.getString(str_pos_top, default_pos_top);
    }

    public String getStr_latest() {
        return pref.getString(str_pos_top, default_pos_top);
    }

    public String getStr_Bucket_url() {
        return pref.getString(str_Bucket_url, Bucket_url);
    }

    public String getStr_bucketname() {
        return pref.getString(str_bucketname, bucketname);
    }

    public String getStr_poolid() {
        return pref.getString(str_poolid, poolid);
    }

    public String getStr_poolregion() {
        return pref.getString(str_poolregion, poolregion);
    }

    public String getStr_bucketregion() {
        return pref.getString(str_bucketregion, bucketregion);
    }

    public String getStr_postid() {
        return pref.getString(str_postid, postid);
    }

    public String getStr_post_uid() {
        return pref.getString(str_post_uid, postuid);
    }


    // *******************************************************************************************
    // *******************************************************************************************
    // ********************************************************************************************
    // code for define and initialize :  set methods

    public void setLogin_Flag(String name) {
        editor = pref.edit();
        editor.putString(str_login_flag, name);
        editor.apply();
    }

    public void setUserid(String name) {
        editor = pref.edit();
        editor.putString(str_Userid, name);
        editor.apply();
    }

    public void setEmail(String name) {
        editor = pref.edit();
        editor.putString(str_Email, name);
        editor.apply();
    }

    public void setMobile(String name) {
        editor = pref.edit();
        editor.putString(str_Mobile, name);
        editor.apply();
    }

    public void setPassword(String name) {
        editor = pref.edit();
        editor.putString(str_Pwd, name);
        editor.apply();
    }

    public void setotp(String name) {
        editor = pref.edit();
        editor.putString(str_otp, name);
        editor.apply();
    }

    public void setfname(String name) {
        editor = pref.edit();
        editor.putString(str_fname, name);
        editor.apply();
    }

    public void setlname(String name) {
        editor = pref.edit();
        editor.putString(str_lname, name);
        editor.apply();
    }

    public void setprofile(String name) {
        editor = pref.edit();
        editor.putString(str_profile, name);
        editor.apply();
    }

    public void setpos(String name) {
        editor = pref.edit();
        editor.putString(str_pos, name);
        editor.apply();

    }

    public void setStr_pos_latest(String name) {
        editor = pref.edit();
        editor.putString(str_pos_latest, name);
        editor.apply();

    }

    public void setStr_pos_top(String name) {
        editor = pref.edit();
        editor.putString(str_pos_top, name);
        editor.apply();

    }

    public void setStr_latest(String name) {
        editor = pref.edit();
        editor.putString(str_latest, name);
        editor.apply();

    }

    public void setStr_top(String name) {
        editor = pref.edit();
        editor.putString(str_top, name);
        editor.apply();

    }

    public void setStr_Bucket_url(String name) {
        editor = pref.edit();
        editor.putString(str_Bucket_url, name);
        editor.apply();

    }

    public void setStr_bucketname(String name) {
        editor = pref.edit();
        editor.putString(str_bucketname, name);
        editor.apply();

    }

    public void setStr_poolid(String name) {
        editor = pref.edit();
        editor.putString(str_poolid, name);
        editor.apply();

    }

    public void setStr_poolregion(String name) {
        editor = pref.edit();
        editor.putString(str_poolregion, name);
        editor.apply();

    }

    public void setStr_bucketregion(String name) {
        editor = pref.edit();
        editor.putString(str_bucketregion, name);
        editor.apply();

    }

    public void setStr_postid(String name) {
        editor = pref.edit();
        editor.putString(str_postid, name);
        editor.apply();

    }

    public void setStr_post_uid(String name) {
        editor = pref.edit();
        editor.putString(str_post_uid, name);
        editor.apply();

    }


    // *******************************************************************************************
    // *******************************************************************************************
    // *******************************************************************************************
    // code to clear all preferences
    // *******************************************************************************************
    // *******************************************************************************************
    // *******************************************************************************************

    public void clear_pref() {
        pref.edit().clear().apply();
    }
}

