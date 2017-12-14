package com.beanstringnew.Model;

/**
 * Created by admin on 11/19/2016.
 */

public class Model_Comment {

    String comment;
    String comment_fname;
    String comment_lname;
    String comment_propic;
    String commenttime;
    String userid;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getComment_fname() {
        return comment_fname;
    }

    public void setComment_fname(String comment_fname) {
        this.comment_fname = comment_fname;
    }

    public String getComment_lname() {
        return comment_lname;
    }

    public void setComment_lname(String comment_lname) {
        this.comment_lname = comment_lname;
    }

    public String getComment_propic() {
        return comment_propic;
    }

    public void setComment_propic(String comment_propic) {
        this.comment_propic = comment_propic;
    }

    public String getCommenttime() {
        return commenttime;
    }

    public void setCommenttime(String commenttime) {
        this.commenttime = commenttime;
    }
}
