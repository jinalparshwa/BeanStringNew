package com.beanstringnew.Model;

import java.io.Serializable;

/**
 * Created by Abc on 10/25/2016.
 */

public class Model_upload_image_and_video implements Serializable {

    String thumb;
    String url;
    String status;
    String tot_beans;
    String time;
    String postid;
    String description;
    String userid;
    String postuserid;

    public String getPostuserid() {
        return postuserid;
    }

    public void setPostuserid(String postuserid) {
        this.postuserid = postuserid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getPostid() {
        return postid;
    }

    public void setPostid(String postid) {
        this.postid = postid;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTot_beans() {
        return tot_beans;
    }

    public void setTot_beans(String tot_beans) {
        this.tot_beans = tot_beans;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
