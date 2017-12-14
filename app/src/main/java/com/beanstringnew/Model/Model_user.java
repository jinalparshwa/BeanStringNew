package com.beanstringnew.Model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by admin on 10/1/2016.
 */

public class Model_user implements Serializable {
    String Userid;
    String fname;
    String lname;
    String Email;
    String Mobile;
    String Date;
    String Cname;
    String City;
    String State;
    String Pic;
    String Password;
    String Post;
    String Image_upload;
    String Video_upload;
    String days;
    String Minutes;
    String Seconds;
    String Hours;
    String url;
    String thumb;
    String status;
    String description;
    String time;
    String last_comment_fname;
    String last_comment_lname;
    String last_comment_profilepic;
    String last_comment;
    String last_comment_time;
    String post_id;
    String limit;
    String beans_profile;
    String beans_comment;
    String beans_post;
    String Startdate;
    String enddate;
    String deviceid;
    String devicetoken;
    String Total_beans;
    String Postid;
    String Postuserid;
    String Post_userid;
    String sstatus;
    String sfname;
    String slname;
    String ssuerid;
    String comment_cunt;
    String share_cunt;
    String following;
    String follower;
    String commentid;
    String like;
    String mylike;
    String posturl;

    public String getPosturl() {
        return posturl;
    }

    public void setPosturl(String posturl) {
        this.posturl = posturl;
    }

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

    public String getCommentid() {
        return commentid;
    }

    public void setCommentid(String commentid) {
        this.commentid = commentid;
    }

    public String getFollowing() {
        return following;
    }

    public void setFollowing(String following) {
        this.following = following;
    }

    public String getFollower() {
        return follower;
    }

    public void setFollower(String follower) {
        this.follower = follower;
    }

    public String getComment_cunt() {
        return comment_cunt;
    }

    public void setComment_cunt(String comment_cunt) {
        this.comment_cunt = comment_cunt;
    }

    public String getShare_cunt() {
        return share_cunt;
    }

    public void setShare_cunt(String share_cunt) {
        this.share_cunt = share_cunt;
    }

    ArrayList<Model_Comment> Comments;

    public String getSsuerid() {
        return ssuerid;
    }

    public void setSsuerid(String ssuerid) {
        this.ssuerid = ssuerid;
    }

    public String getSstatus() {
        return sstatus;
    }

    public void setSstatus(String sstatus) {
        this.sstatus = sstatus;
    }

    public String getSfname() {
        return sfname;
    }

    public void setSfname(String sfname) {
        this.sfname = sfname;
    }

    public String getSlname() {
        return slname;
    }

    public void setSlname(String slname) {
        this.slname = slname;
    }

    public String getPost_userid() {
        return Post_userid;
    }

    public void setPost_userid(String post_userid) {
        Post_userid = post_userid;
    }

    public String getPostid() {
        return Postid;
    }

    public void setPostid(String postid) {
        Postid = postid;
    }

    public String getPostuserid() {
        return Postuserid;
    }

    public void setPostuserid(String postuserid) {
        Postuserid = postuserid;
    }

    public String getTotal_beans() {
        return Total_beans;
    }

    public void setTotal_beans(String total_beans) {
        Total_beans = total_beans;
    }

    public String getDeviceid() {
        return deviceid;
    }

    public void setDeviceid(String deviceid) {
        this.deviceid = deviceid;
    }

    public String getDevicetoken() {
        return devicetoken;
    }

    public void setDevicetoken(String devicetoken) {
        this.devicetoken = devicetoken;
    }

    public String getStartdate() {
        return Startdate;
    }

    public void setStartdate(String startdate) {
        Startdate = startdate;
    }

    public String getEnddate() {
        return enddate;
    }

    public void setEnddate(String enddate) {
        this.enddate = enddate;
    }

    public String getBeans_post() {
        return beans_post;
    }

    public void setBeans_post(String beans_post) {
        this.beans_post = beans_post;
    }

    public String getBeans_profile() {
        return beans_profile;
    }

    public void setBeans_profile(String beans_profile) {
        this.beans_profile = beans_profile;
    }

    public String getBeans_comment() {
        return beans_comment;
    }

    public void setBeans_comment(String beans_comment) {
        this.beans_comment = beans_comment;
    }

    public String getLimit() {
        return limit;
    }

    public void setLimit(String limit) {
        this.limit = limit;
    }

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    public String getLast_comment() {
        return last_comment;
    }

    public void setLast_comment(String last_comment) {
        this.last_comment = last_comment;
    }

    public String getLast_comment_time() {
        return last_comment_time;
    }

    public void setLast_comment_time(String last_comment_time) {
        this.last_comment_time = last_comment_time;
    }

    public String getLast_comment_fname() {
        return last_comment_fname;
    }

    public void setLast_comment_fname(String last_comment_fname) {
        this.last_comment_fname = last_comment_fname;
    }

    public String getLast_comment_lname() {
        return last_comment_lname;
    }

    public void setLast_comment_lname(String last_comment_lname) {
        this.last_comment_lname = last_comment_lname;
    }

    public String getLast_comment_profilepic() {
        return last_comment_profilepic;
    }

    public void setLast_comment_profilepic(String last_comment_profilepic) {
        this.last_comment_profilepic = last_comment_profilepic;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

//    public String getProfilepic() {
//        return profilepic;
//    }
//
//    public void setProfilepic(String profilepic) {
//        this.profilepic = profilepic;
//    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public String getMinutes() {
        return Minutes;
    }

    public void setMinutes(String minutes) {
        Minutes = minutes;
    }

    public String getSeconds() {
        return Seconds;
    }

    public void setSeconds(String seconds) {
        Seconds = seconds;
    }

    public String getHours() {
        return Hours;
    }

    public void setHours(String hours) {
        Hours = hours;
    }

    public String getImage_upload() {
        return Image_upload;
    }

    public void setImage_upload(String image_upload) {
        Image_upload = image_upload;
    }

    public String getVideo_upload() {
        return Video_upload;
    }

    public void setVideo_upload(String video_upload) {
        Video_upload = video_upload;
    }

    public String getPost() {
        return Post;
    }

    public void setPost(String post) {
        Post = post;
    }

    public String getPic() {
        return Pic;
    }

    public void setPic(String pic) {
        Pic = pic;
    }

    public String getUserid() {
        return Userid;
    }

    public void setUserid(String userid) {
        Userid = userid;
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

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getCname() {
        return Cname;
    }

    public void setCname(String cname) {
        Cname = cname;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }


    public ArrayList<Model_Comment> getComments() {
        return Comments;
    }

    public void setComments(ArrayList<Model_Comment> comments) {
        Comments = comments;
    }


}
