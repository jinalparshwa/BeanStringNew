package com.beanstringnew.Model;

/**
 * Created by Nirav on 10/29/2016.
 */

public class Model_top_post {
    String Userid;
    String postid;
    String fname;
    String lname;
    String description;
    String time;
    String propic;
    String url;
    String Thumb;
    String Status;
    String last_comment_profilepic;
    String last_comment;
    String Last_comment_time;

    public String getLast_comment_time() {
        return Last_comment_time;
    }

    public void setLast_comment_time(String last_comment_time) {
        Last_comment_time = last_comment_time;
    }

    public String getUserid() {
        return Userid;
    }

    public void setUserid(String userid) {
        Userid = userid;
    }

    public String getLast_comment_profilepic() {
        return last_comment_profilepic;
    }

    public void setLast_comment_profilepic(String last_comment_profilepic) {
        this.last_comment_profilepic = last_comment_profilepic;
    }

    public String getLast_comment() {
        return last_comment;
    }

    public void setLast_comment(String last_comment) {
        this.last_comment = last_comment;
    }

    public String getPostid() {
        return postid;
    }

    public void setPostid(String postid) {
        this.postid = postid;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
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

    public String getPropic() {
        return propic;
    }

    public void setPropic(String propic) {
        this.propic = propic;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getThumb() {
        return Thumb;
    }

    public void setThumb(String thumb) {
        Thumb = thumb;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

}
