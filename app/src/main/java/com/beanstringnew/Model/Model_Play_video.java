package com.beanstringnew.Model;

import java.io.Serializable;

/**
 * Created by admin on 1/5/2017.
 */

public class Model_Play_video implements Serializable {
    String postid;
    String postuserid;
    String description;
    String status;
    String url;
    String userid;
    String del_valu;

    public String getDel_valu() {
        return del_valu;
    }

    public void setDel_valu(String del_valu) {
        this.del_valu = del_valu;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPostid() {
        return postid;
    }

    public void setPostid(String postid) {
        this.postid = postid;
    }

    public String getPostuserid() {
        return postuserid;
    }

    public void setPostuserid(String postuserid) {
        this.postuserid = postuserid;
    }



    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
