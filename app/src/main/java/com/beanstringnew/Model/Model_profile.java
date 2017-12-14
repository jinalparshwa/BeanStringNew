package com.beanstringnew.Model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by admin on 10/27/2016.
 */

public class Model_profile implements Serializable{
    String Beans;
    String City;
    String College;
    String Following;
    String Follower;
    String fname;
    String lname;
    String Profile;
    String username;
    String userid;
    String status;
    String highest;
    String noti_count;
    String limit;
    String url;
    String thumb;
    String description;
    String postid;
    String tot_beans;
    String comment_count;
    String share_count;
    String time;
    String post_userid;
    String like;
    String mylike;

    public String getLike() {
        return like;
    }

    public void setLike(String like) {
        this.like = like;
    }

    public String getMylike() {
        return mylike;
    }

    public void setMylike(String mylike) {
        this.mylike = mylike;
    }

    public String getPost_userid() {
        return post_userid;
    }

    public void setPost_userid(String post_userid) {
        this.post_userid = post_userid;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }


    ArrayList<Model_Comment> Comments;

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
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

    public String getTot_beans() {
        return tot_beans;
    }

    public void setTot_beans(String tot_beans) {
        this.tot_beans = tot_beans;
    }

    public String getComment_count() {
        return comment_count;
    }

    public void setComment_count(String comment_count) {
        this.comment_count = comment_count;
    }

    public String getShare_count() {
        return share_count;
    }

    public void setShare_count(String share_count) {
        this.share_count = share_count;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLimit() {
        return limit;
    }

    public void setLimit(String limit) {
        this.limit = limit;
    }

    public String getNoti_count() {
        return noti_count;
    }

    public void setNoti_count(String noti_count) {
        this.noti_count = noti_count;
    }

    public String getHighest() {
        return highest;
    }

    public void setHighest(String highest) {
        this.highest = highest;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getProfile() {
        return Profile;
    }

    public void setProfile(String profile) {
        Profile = profile;
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

    public String getBeans() {
        return Beans;
    }

    public void setBeans(String beans) {
        Beans = beans;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getCollege() {
        return College;
    }

    public void setCollege(String college) {
        College = college;
    }

    public String getFollowing() {
        return Following;
    }

    public void setFollowing(String following) {
        Following = following;
    }

    public String getFollower() {
        return Follower;
    }

    public void setFollower(String follower) {
        Follower = follower;
    }

    public ArrayList<Model_Comment> getComments() {
        return Comments;
    }

    public void setComments(ArrayList<Model_Comment> comments) {
        Comments = comments;
    }
}
