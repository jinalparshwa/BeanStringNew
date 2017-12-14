package com.beanstringnew.Json_model;

import android.util.Log;

import com.beanstringnew.Model.Model_Paying_order_list;
import com.beanstringnew.Model.Model_Product;
import com.beanstringnew.Model.Model_Shipping;
import com.beanstringnew.Model.Model_mycart;
import com.beanstringnew.Model.Model_notification;
import com.beanstringnew.Model.Model_profile;
import com.beanstringnew.Model.Model_search;
import com.beanstringnew.Model.Model_user;
import com.beanstringnew.Model.Model_view_comment;
import com.beanstringnew.Shared.Pref_Master;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.beanstringnew.Activity.Add_photo_activity.Select_value;
import static com.beanstringnew.Activity.Add_photo_activity.videocount;

/**
 * Created by Admin on 8/30/2016.
 */
public class JSON {

    public static String add_json(ArrayList<Model_user> array_user, Pref_Master pref, String array_nm) {
        JSONObject jobj_data = new JSONObject();
        JSONObject jobj_main = new JSONObject();

        try {
            if (array_nm.equals("registration")) {
                for (int i = 0; i < array_user.size(); i++) {
                    Model_user model = array_user.get(i);
                    jobj_main.put("fname", model.getFname());
                    jobj_main.put("lname", model.getLname());
                    jobj_main.put("email", model.getEmail());
                    jobj_main.put("mobile", model.getMobile());
                    jobj_main.put("dob", model.getDate());
                    // jobj_main.put("college", model.getCname());
                    jobj_main.put("city", model.getCity());
                    jobj_main.put("state", model.getState());
                    jobj_main.put("propic", model.getPic());
                    jobj_main.put("password", model.getPassword());
                    jobj_main.put("deviceid", model.getDeviceid());
                    jobj_main.put("devicetoken", model.getDevicetoken());
                    jobj_main.put("devicetype", "0");

                }

            } else if (array_nm.equals("login")) {
                for (int i = 0; i < array_user.size(); i++) {
                    Model_user model = array_user.get(i);
                    jobj_main.put("email", model.getEmail());
                    jobj_main.put("password", model.getPassword());
                    jobj_main.put("deviceid", model.getDeviceid());
                    jobj_main.put("devicetoken", model.getDevicetoken());
                    jobj_main.put("devicetype", "0");
                }


            } else if (array_nm.equals("loginwithsocial")) {
                for (int i = 0; i < array_user.size(); i++) {
                    Model_user model = array_user.get(i);
                    jobj_main.put("username", model.getFname());
                    jobj_main.put("id", model.getUserid());
                    jobj_main.put("email", model.getEmail());
                    jobj_main.put("status", model.getStatus());
                    jobj_main.put("deviceid", model.getDeviceid());
                    jobj_main.put("devicetoken", model.getDevicetoken());
                    jobj_main.put("devicetype", "0");
                }


            } else if (array_nm.equals("upload")) {
                for (int i = 0; i < array_user.size(); i++) {
                    Model_user model = array_user.get(i);


                    if (videocount == 1) {
                        jobj_main.put("description", model.getPost());
                        jobj_main.put("photo", "");
                        jobj_main.put("video", "" + model.getVideo_upload());
                        jobj_main.put("id", pref.getStr_Userid());
                    } else if (Select_value == 1) {

                        jobj_main.put("description", model.getPost());
                        jobj_main.put("photo", "" + model.getImage_upload());
                        jobj_main.put("video", "");
                        jobj_main.put("id", pref.getStr_Userid());
                    } else if (Select_value == 2) {
                        jobj_main.put("description", model.getPost());
                        jobj_main.put("photo", "" + model.getImage_upload());
                        jobj_main.put("video", "");
                        jobj_main.put("id", pref.getStr_Userid());
                    } else {
                        jobj_main.put("description", model.getPost());
                        jobj_main.put("photo", "");
                        jobj_main.put("video", "");
                        jobj_main.put("id", pref.getStr_Userid());
                    }

//                    jobj_main.put("photo", (Add_photo_fragment.select == 0) ? model.getImage_upload() : "");
//                    jobj_main.put("video", (Add_photo_fragment.select == 1) ? model.getVideo_upload() : "");

                }


//            }
// else if (array_nm.equals("upload")) {
//                for (int i = 0; i < array_user.size(); i++) {
//                    Model_user model = array_user.get(i);
//
//
//                    if (Add_photo_fragment.select == 3) {
//                        jobj_main.put("description", model.getPost());
//                        jobj_main.put("photo", "");
//                        jobj_main.put("video", "");
//                        jobj_main.put("id", pref.getStr_Userid());
//
//                    } else if (Add_photo_fragment.select == 0) {
//                        jobj_main.put("description", model.getPost());
//                        jobj_main.put("photo", "" + model.getImage_upload());
//                        jobj_main.put("video", "");
//                        jobj_main.put("id", pref.getStr_Userid());
//                    } else if (Add_photo_fragment.select == 1) {
//                        jobj_main.put("description", model.getPost());
//                        jobj_main.put("photo", "");
//                        jobj_main.put("video", "" + model.getVideo_upload());
//                        jobj_main.put("id", pref.getStr_Userid());
//                    }
//
////                    jobj_main.put("photo", (Add_photo_fragment.select == 0) ? model.getImage_upload() : "");
////                    jobj_main.put("video", (Add_photo_fragment.select == 1) ? model.getVideo_upload() : "");
//
//                }
//
//
            } else if (array_nm.equals("userfollowingpost")) {

                for (int i = 0; i < array_user.size(); i++) {
                    Model_user model = array_user.get(i);
                    jobj_main.put("userid", pref.getStr_Userid());
                    jobj_main.put("limit", model.getLimit());
                }

            } else if (array_nm.equals("followerslist")) {
                jobj_main.put("id", pref.getStr_Userid());

            } else if (array_nm.equals("followinglist")) {
                jobj_main.put("id", pref.getStr_Userid());


            } else if (array_nm.equals("profilefollowerslist")) {
                for (int i = 0; i < array_user.size(); i++) {
                    Model_user model = array_user.get(i);
                    jobj_main.put("currentuserid", pref.getStr_Userid());
                    jobj_main.put("profileuserid", model.getUserid());

                }


            } else if (array_nm.equals("profilefollowinglist")) {
                for (int i = 0; i < array_user.size(); i++) {
                    Model_user model = array_user.get(i);
                    jobj_main.put("currentuserid", pref.getStr_Userid());
                    jobj_main.put("profileuserid", model.getUserid());

                }


            } else if (array_nm.equals("changepass")) {
                for (int i = 0; i < array_user.size(); i++) {
                    Model_user model = array_user.get(i);
                    jobj_main.put("userid", pref.getStr_Userid());
                    jobj_main.put("password", model.getPassword());
                }


            } else if (array_nm.equals("getmyprofile")) {
                jobj_main.put("id", pref.getStr_Userid());

            } else if (array_nm.equals("editprofile")) {
                for (int i = 0; i < array_user.size(); i++) {
                    Model_user model = array_user.get(i);
                    jobj_main.put("id", pref.getStr_Userid());
                    jobj_main.put("fname", model.getFname());
                    jobj_main.put("lname", model.getLname());
                    jobj_main.put("email", model.getEmail());
                    jobj_main.put("mobile", model.getMobile());
                    jobj_main.put("dob", model.getDate());
                    jobj_main.put("college", model.getCname());
                    jobj_main.put("city", model.getCity());
                    jobj_main.put("state", model.getState());
                    jobj_main.put("propic", model.getPic());
                }


            } else if (array_nm.equals("deletepropic")) {
                for (int i = 0; i < array_user.size(); i++) {
                    Model_user model = array_user.get(i);
                    jobj_main.put("id", pref.getStr_Userid());
                    jobj_main.put("url", model.getPic());
                }
            } else if (array_nm.equals("serch")) {
                for (int i = 0; i < array_user.size(); i++) {
                    Model_user model = array_user.get(i);
                    jobj_main.put("name", model.getFname());
                    jobj_main.put("id", pref.getStr_Userid());
                }
            } else if (array_nm.equals("highestbeanearner")) {
                jobj_main.put("id", pref.getStr_Userid());

            } else if (array_nm.equals("userlatestpost")) {
                for (int i = 0; i < array_user.size(); i++) {
                    Model_user model = array_user.get(i);
                    jobj_main.put("userid", pref.getStr_Userid());
                    jobj_main.put("postid", model.getPost_id());
                    jobj_main.put("limit", model.getLimit());
                }

            } else if (array_nm.equals("usertoppost")) {
                for (int i = 0; i < array_user.size(); i++) {
                    Model_user model = array_user.get(i);
                    jobj_main.put("userid", pref.getStr_Userid());
                    jobj_main.put("limit", model.getLimit());
                    jobj_main.put("postid", model.getPost_id());
                }

            } else if (array_nm.equals("mybeans")) {
                jobj_main.put("userid", pref.getStr_Userid());
            } else if (array_nm.equals("beanssearch")) {
                for (int i = 0; i < array_user.size(); i++) {
                    Model_user model = array_user.get(i);
                    jobj_main.put("userid", pref.getStr_Userid());
                    jobj_main.put("sdate", model.getStartdate());
                    jobj_main.put("edate", model.getEnddate());
                }
            } else if (array_nm.equals("forgotpassword")) {
                for (int i = 0; i < array_user.size(); i++) {
                    Model_user model = array_user.get(i);
                    jobj_main.put("number", model.getMobile());

                }
            } else if (array_nm.equals("checkoldpassword")) {
                for (int i = 0; i < array_user.size(); i++) {
                    Model_user model = array_user.get(i);
                    jobj_main.put("userid", pref.getStr_Userid());
                    jobj_main.put("oldpassword", model.getSsuerid());

                }
            } else if (array_nm.equals("sendotp")) {
                for (int i = 0; i < array_user.size(); i++) {
                    Model_user model = array_user.get(i);
                    jobj_main.put("number", model.getMobile());
                    jobj_main.put("email", model.getEmail());

                }
            } else if (array_nm.equals("deletepost")) {
                for (int i = 0; i < array_user.size(); i++) {
                    Model_user model = array_user.get(i);
                    jobj_main.put("postid", model.getPostid());

                }

            } else if (array_nm.equals("deletecomment")) {
                for (int i = 0; i < array_user.size(); i++) {
                    Model_user model = array_user.get(i);
                    jobj_main.put("commentid", model.getCommentid());

                }

            } else if (array_nm.equals("viewpost")) {
                for (int i = 0; i < array_user.size(); i++) {
                    Model_user model = array_user.get(i);
                    jobj_main.put("userid", pref.getStr_Userid());
                    jobj_main.put("postid", model.getPost_id());

                }

            } else if (array_nm.equals("viewadvertisement")) {
                for (int i = 0; i < array_user.size(); i++) {
                    Model_user model = array_user.get(i);
                    jobj_main.put("userid", pref.getStr_Userid());
                    jobj_main.put("adid", model.getPost_id());

                }

            }
            JSONArray jarray_main = new JSONArray();
            jarray_main.put(jobj_main);

            jobj_data.put(array_nm, jarray_main);

        } catch (
                JSONException e
                )

        {
            e.printStackTrace();
        }

        Log.i("Json request", jobj_data.toString().

                replaceAll("\\\\", "")

        );

        return jobj_data.toString().

                replaceAll("\\\\", "");
    }

    public static String add_view(ArrayList<Model_view_comment> array_user, Pref_Master pref, String array_nm) {
        JSONObject jobj_data = new JSONObject();
        JSONObject jobj_main = new JSONObject();

        try {

            if (array_nm.equals("getnotificationscreen")) {
                for (int i = 0; i < array_user.size(); i++) {
                    Model_view_comment model = array_user.get(i);
                    jobj_main.put("postid", model.getPostid());
                }
            }


            JSONArray jarray_main = new JSONArray();
            jarray_main.put(jobj_main);

            jobj_data.put(array_nm, jarray_main);

        } catch (
                JSONException e
                )

        {
            e.printStackTrace();
        }

        Log.i("Json request", jobj_data.toString().

                replaceAll("\\\\", "")

        );

        return jobj_data.toString().

                replaceAll("\\\\", "");
    }

    public static String add_jsonn(ArrayList<Model_search> array_user, Pref_Master pref, String array_nm) {
        JSONObject jobj_data = new JSONObject();
        JSONObject jobj_main = new JSONObject();

        try {
            if (array_nm.equals("serch")) {
                for (int i = 0; i < array_user.size(); i++) {
                    Model_search model = array_user.get(i);
                    jobj_main.put("name", model.getFname());
                    jobj_main.put("id", pref.getStr_Userid());
                }

            }

            JSONArray jarray_main = new JSONArray();
            jarray_main.put(jobj_main);

            jobj_data.put(array_nm, jarray_main);

        } catch (
                JSONException e
                )

        {
            e.printStackTrace();
        }

        Log.i("Json request", jobj_data.toString().

                replaceAll("\\\\", "")

        );

        return jobj_data.toString().

                replaceAll("\\\\", "");
    }


    public static String add(ArrayList<Model_profile> array_user, Pref_Master pref, String array_nm) {
        JSONObject jobj_data = new JSONObject();
        JSONObject jobj_main = new JSONObject();

        try {
            if (array_nm.equals("beanscount")) {
                jobj_main.put("id", pref.getStr_Userid());

            } else if (array_nm.equals("getprofile")) {

                for (int i = 0; i < array_user.size(); i++) {

                    Log.e("row ", " : " + array_user.get(i).getUserid());
                    Model_profile model = array_user.get(i);

                    Log.e("jinal_id", model.getUserid());
                    jobj_main.put("id", model.getUserid());

                    Log.e("current_user_id", pref.getStr_Userid());
                    jobj_main.put("currentuserid", pref.getStr_Userid());
                }

            } else if (array_nm.equals("share")) {
                for (int i = 0; i < array_user.size(); i++) {
                    Model_profile model = array_user.get(i);
                    jobj_main.put("userid", pref.getStr_Userid());
                    jobj_main.put("postid", model.getUserid());
                    jobj_main.put("post_uid", model.getProfile());
                    jobj_main.put("media", model.getStatus());
                }


            } else if (array_nm.equals("checkshare")) {
                for (int i = 0; i < array_user.size(); i++) {
                    Model_profile model = array_user.get(i);
                    jobj_main.put("userid", pref.getStr_Userid());
                    jobj_main.put("postid", model.getPostid());
                    jobj_main.put("post_uid", model.getPost_userid());
                    jobj_main.put("media", model.getStatus());
                }

            } else if (array_nm.equals("checkshareadvertisement")) {
                for (int i = 0; i < array_user.size(); i++) {
                    Model_profile model = array_user.get(i);
                    jobj_main.put("id", model.getPostid());
                    jobj_main.put("userid", pref.getStr_Userid());
                    jobj_main.put("media", model.getStatus());
                }

            } else if (array_nm.equals("shareadvertisement")) {
                for (int i = 0; i < array_user.size(); i++) {
                    Model_profile model = array_user.get(i);
                    jobj_main.put("id", model.getPostid());
                    jobj_main.put("userid", pref.getStr_Userid());
                    jobj_main.put("media", model.getStatus());
                }

            } else if (array_nm.equals("profilelatestpost")) {
                for (int i = 0; i < array_user.size(); i++) {
                    Model_profile model = array_user.get(i);
                    jobj_main.put("id", model.getUserid());
                    jobj_main.put("limit", model.getLimit());
                    jobj_main.put("myid", pref.getStr_Userid());
                }

            } else if (array_nm.equals("profiletoppost")) {
                for (int i = 0; i < array_user.size(); i++) {
                    Model_profile model = array_user.get(i);
                    jobj_main.put("id", model.getUserid());
                    jobj_main.put("limit", model.getLimit());
                    jobj_main.put("myid", pref.getStr_Userid());
                }

            } else if (array_nm.equals("signout")) {
                jobj_main.put("userid", pref.getStr_Userid());


            } else if (array_nm.equals("postslider")) {
                for (int i = 0; i < array_user.size(); i++) {
                    Model_profile model = array_user.get(i);
                    jobj_main.put("userid", model.getUserid());
                    jobj_main.put("postid", model.getPostid());
                }


            } else if (array_nm.equals("reportpost")) {
                for (int i = 0; i < array_user.size(); i++) {
                    Model_profile model = array_user.get(i);
                    jobj_main.put("userid", pref.getStr_Userid());
                    jobj_main.put("postuserid", model.getUserid());
                    jobj_main.put("postid", model.getPostid());
                }


            } else if (array_nm.equals("cms")) {
                for (int i = 0; i < array_user.size(); i++) {
                    Model_profile model = array_user.get(i);
                    jobj_main.put("cmsid", model.getStatus());
                }


            } else if (array_nm.equals("fbinvitebeans")) {
                jobj_main.put("id", pref.getStr_Userid());
            }

            JSONArray jarray_main = new JSONArray();
            jarray_main.put(jobj_main);

            jobj_data.put(array_nm, jarray_main);

        } catch (
                JSONException e
                )

        {
            e.printStackTrace();
        }

        Log.i("Json request", jobj_data.toString().

                replaceAll("\\\\", "")

        );

        return jobj_data.toString().

                replaceAll("\\\\", "");

    }

    public static String addd(ArrayList<Model_mycart> array_user, Pref_Master pref, String array_nm) {
        JSONObject jobj_data = new JSONObject();
        JSONObject jobj_main = new JSONObject();

        try {
            if (array_nm.equals("getmycart")) {
                jobj_main.put("userid", pref.getStr_Userid());

            } else if (array_nm.equals("removetocart")) {
                for (int i = 0; i < array_user.size(); i++) {
                    Model_mycart model = array_user.get(i);
                    jobj_main.put("cartid", model.getCartid());

                }

            }

            JSONArray jarray_main = new JSONArray();
            jarray_main.put(jobj_main);

            jobj_data.put(array_nm, jarray_main);

        } catch (
                JSONException e
                )

        {
            e.printStackTrace();
        }

        Log.i("Json request", jobj_data.toString().

                replaceAll("\\\\", "")

        );

        return jobj_data.toString().

                replaceAll("\\\\", "");
    }

    public static String add_jsonnn(ArrayList<Model_Shipping> array_user, Pref_Master pref, String array_nm) {
        JSONObject jobj_data = new JSONObject();
        JSONObject jobj_main = new JSONObject();

        try {
            if (array_nm.equals("addshippingaddress")) {
                for (int i = 0; i < array_user.size(); i++) {
                    Model_Shipping model = array_user.get(i);
                    jobj_main.put("fname", model.getSh_fname());
                    jobj_main.put("lname", model.getSh_lname());
                    jobj_main.put("mobile", model.getSh_number());
                    jobj_main.put("address", model.getSh_address());
                    jobj_main.put("city", model.getSh_city());
                    jobj_main.put("state", model.getSh_state());
                    jobj_main.put("id", model.getId());
                    jobj_main.put("userid", pref.getStr_Userid());
                    jobj_main.put("country", model.getCountry());
                    jobj_main.put("zipcode", model.getSh_postal());

                }

            } else if (array_nm.equals("getshippingaddress")) {
                jobj_main.put("userid", pref.getStr_Userid());
            }

            JSONArray jarray_main = new JSONArray();
            jarray_main.put(jobj_main);

            jobj_data.put(array_nm, jarray_main);

        } catch (
                JSONException e
                )

        {
            e.printStackTrace();
        }

        Log.i("Json request", jobj_data.toString().

                replaceAll("\\\\", "")

        );

        return jobj_data.toString().

                replaceAll("\\\\", "");
    }

    public static String add_json_order(ArrayList<Model_Paying_order_list> array_user, Pref_Master pref, String array_nm) {
        JSONObject jobj_data = new JSONObject();
        JSONObject jobj_main = new JSONObject();

        try {
            if (array_nm.equals("orderhistory")) {
                jobj_main.put("userid", pref.getStr_Userid());

            }


            JSONArray jarray_main = new JSONArray();
            jarray_main.put(jobj_main);

            jobj_data.put(array_nm, jarray_main);

        } catch (
                JSONException e
                )

        {
            e.printStackTrace();
        }

        Log.i("Json request", jobj_data.toString().

                replaceAll("\\\\", "")

        );

        return jobj_data.toString().

                replaceAll("\\\\", "");
    }

    public static String json_product(ArrayList<Model_Product> array_user, Pref_Master pref, String array_nm) {
        JSONObject jobj_data = new JSONObject();
        JSONObject jobj_main = new JSONObject();

        try {
            if (array_nm.equals("getproductdetail")) {
                for (int i = 0; i < array_user.size(); i++) {
                    Model_Product model = array_user.get(i);
                    jobj_main.put("id", model.getId());
                }

            } else if (array_nm.equals("addtocart")) {
                for (int i = 0; i < array_user.size(); i++) {
                    Model_Product model = array_user.get(i);
                    jobj_main.put("userid", pref.getStr_Userid());
                    jobj_main.put("productid", model.getId());
                    jobj_main.put("catid", model.getCatid());
                    jobj_main.put("qty", model.getQty());
                    //  jobj_main.put("price", model.getPrice());
                    jobj_main.put("beans", model.getBeans());
//                    jobj_main.put("color", model.getColorid());
//                    jobj_main.put("size", model.getSizeid());
                }

            } else if (array_nm.equals("cartcount")) {
                jobj_main.put("userid", pref.getStr_Userid());


            } else if (array_nm.equals("checkout")) {
                jobj_main.put("userid", pref.getStr_Userid());


            } else if (array_nm.equals("searchproduct")) {
                for (int i = 0; i < array_user.size(); i++) {
                    Model_Product model = array_user.get(i);
                    jobj_main.put("name", model.getName());
                }


            } else if (array_nm.equals("beansspend")) {
                jobj_main.put("userid", pref.getStr_Userid());


            } else if (array_nm.equals("getinnerads")) {
                for (int i = 0; i < array_user.size(); i++) {
                    Model_Product model = array_user.get(i);
                    jobj_main.put("status", model.getId());
                }


            } else if (array_nm.equals("beansspenddetail")) {
                for (int i = 0; i < array_user.size(); i++) {
                    Model_Product model = array_user.get(i);
                    jobj_main.put("id", model.getId());
                }


            }
            JSONArray jarray_main = new JSONArray();
            jarray_main.put(jobj_main);

            jobj_data.put(array_nm, jarray_main);

        } catch (
                JSONException e
                )

        {
            e.printStackTrace();
        }

        Log.i("Json request", jobj_data.toString().

                replaceAll("\\\\", "")

        );

        return jobj_data.toString().

                replaceAll("\\\\", "");
    }

    public static String json_notification(ArrayList<Model_notification> array_user, Pref_Master pref, String array_nm) {
        JSONObject jobj_data = new JSONObject();
        JSONObject jobj_main = new JSONObject();

        try {
            if (array_nm.equals("getnotification")) {
                for (int i = 0; i < array_user.size(); i++) {
                    Model_notification model = array_user.get(i);
                    jobj_main.put("userid", pref.getStr_Userid());
                    jobj_main.put("limit", model.getLimit());
                }


            } else if (array_nm.equals("readnotification")) {
                for (int i = 0; i < array_user.size(); i++) {
                    Model_notification model = array_user.get(i);
                    jobj_main.put("notificationid", model.getNoti_id());
                }


            }

            JSONArray jarray_main = new JSONArray();
            jarray_main.put(jobj_main);

            jobj_data.put(array_nm, jarray_main);

        } catch (
                JSONException e
                )

        {
            e.printStackTrace();
        }

        Log.i("Json request", jobj_data.toString().

                replaceAll("\\\\", "")

        );

        return jobj_data.toString().

                replaceAll("\\\\", "");
    }

}
