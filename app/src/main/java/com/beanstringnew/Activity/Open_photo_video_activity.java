package com.beanstringnew.Activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.beanstringnew.Adapter.Adapter_comment;
import com.beanstringnew.Controller.Acitvity_comunator;
import com.beanstringnew.Controller.Activity_indicator;
import com.beanstringnew.Controller.Configr;
import com.beanstringnew.Controller.Connection;
import com.beanstringnew.Controller.DialogBox;
import com.beanstringnew.Json_model.JSON;
import com.beanstringnew.Model.Model_Play_video;
import com.beanstringnew.Model.Model_profile;
import com.beanstringnew.Model.Model_user;
import com.beanstringnew.Model.Model_view_comment;
import com.beanstringnew.Myapplication;
import com.beanstringnew.R;
import com.beanstringnew.Shared.Pref_Master;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.firebase.crash.FirebaseCrash;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Open_photo_video_activity extends AppCompatActivity implements Thread.UncaughtExceptionHandler {

    Pref_Master pref;
    Context context = this;
    ImageView back_button;
    Activity_indicator obj_dialog;
    String postid;
    ListView comment_list;
    ArrayList<Model_view_comment> array = new ArrayList<>();
    ArrayList<Model_user> array1 = new ArrayList<>();
    ArrayList<Model_view_comment> array_comment = new ArrayList<>();
    ImageView profilepic;
    TextView list_fname, list_lname, time, list_post, tot_beans;
    ImageView list_upload, displayimage;
    Adapter_comment adapter;
    EditText edit_comment;
    ImageView send_post;
    ImageView bottom_pro;
    String comment;
    String res_message;
    String post_user_id;
    static ArrayList<Model_profile> array_liist = new ArrayList<>();
    LinearLayout ll_beans;
    RelativeLayout rr_lat;
    TextView tott_beans;
    String Userid;
    ImageView iv_play;
    String del_value = "";
    ImageView img_delete;
    PopupMenu menu_delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.open_photo_video_activity);
        pref = new Pref_Master(context);


        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            postid = extras.getString("postid");
            del_value = extras.getString("del_value");

        }

        obj_dialog = new Activity_indicator(context);
        obj_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));

        HTTP_API_get_comment_Call();

        back_button = (ImageView) findViewById(R.id.back_button);
        profilepic = (ImageView) findViewById(R.id.profilepic);
        list_fname = (TextView) findViewById(R.id.list_fname);
        list_lname = (TextView) findViewById(R.id.list_lname);
        time = (TextView) findViewById(R.id.time);
        list_post = (TextView) findViewById(R.id.list_post);
        list_upload = (ImageView) findViewById(R.id.list_upload);
        displayimage = (ImageView) findViewById(R.id.displayimage);
        tot_beans = (TextView) findViewById(R.id.tot_beans);
        edit_comment = (EditText) findViewById(R.id.edit_comment);
        send_post = (ImageView) findViewById(R.id.send_post);
        bottom_pro = (ImageView) findViewById(R.id.bottom_pro);
        ll_beans = (LinearLayout) findViewById(R.id.ll_beans);
        rr_lat = (RelativeLayout) findViewById(R.id.rr_lat);
        tott_beans = (TextView) findViewById(R.id.tott_beans);
        iv_play = (ImageView) findViewById(R.id.iv_play);
        img_delete = (ImageView) findViewById(R.id.img_delete);

        if (del_value.equals("1")) {
            img_delete.setVisibility(View.VISIBLE);
        } else {
            img_delete.setVisibility(View.INVISIBLE);
        }
        img_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                menu_delete.show();
            }
        });

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if (pref.getStr_profile().equals("")) {
            bottom_pro.setImageResource(R.drawable.default_imggg);
        } else {
            Glide.with(context).load(pref.getStr_profile()).into(bottom_pro);
        }

        menu_delete = new PopupMenu(context, img_delete);
        createPopup();


        send_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edit_comment.getText().toString().equals("")) {
                    edit_comment.setError("Enter Comment");
                } else {
                    comment = edit_comment.getText().toString();
                    HTTP_API_post_comment_Call();
                    Acitvity_comunator.beans_Count(context, obj_dialog, pref, array_liist);
                }
            }
        });
        comment_list = (ListView) findViewById(R.id.comment_list);
        adapter = new Adapter_comment(context, array_comment);
        comment_list.setAdapter(adapter);
        listview(comment_list);
        final Model_view_comment model = new Model_view_comment();
        model.setPostid(postid);
        array.add(model);
        notification_data();


        iv_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Model_Play_video model_play_video = new Model_Play_video();
                model_play_video.setDescription(array.get(0).getPost());
                model_play_video.setPostid(array.get(0).getPostid());
                model_play_video.setPostuserid(array.get(0).getUserid());
                model_play_video.setStatus(array.get(0).getStatus());
                model_play_video.setUrl(array.get(0).getUrl());
                model_play_video.setDel_valu("0");
                Intent i = new Intent(context, Play_video_activity.class);
                i.putExtra("url", model_play_video);
                context.startActivity(i);
            }
        });

        list_fname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, Other_Profile_activity.class);
                i.putExtra("id", array.get(0).getUserid());
                context.startActivity(i);
            }
        });
        list_lname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, Other_Profile_activity.class);
                i.putExtra("id", array.get(0).getUserid());
                context.startActivity(i);
            }
        });
        Myapplication.getInstance().trackScreenView("Open Photo Video Screen");
    }

    public void createPopup() {
        menu_delete = new PopupMenu(context, img_delete);
        menu_delete.getMenu().add(Menu.NONE, 0, Menu.NONE, "Delete");
        menu_delete.setOnMenuItemClickListener(onClick_State_popupmenu);
    }

    PopupMenu.OnMenuItemClickListener onClick_State_popupmenu = new PopupMenu.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            LayoutInflater li = LayoutInflater.from(context);
            View v = li.inflate(R.layout.alert_popup, null);
            final android.support.v7.app.AlertDialog alert = new android.support.v7.app.AlertDialog.Builder(context).setView(v).show();
            alert.setCancelable(false);
            alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            TextView ok = (TextView) v.findViewById(R.id.con_ok);
            TextView cancel = (TextView) v.findViewById(R.id.con_cancel);
            TextView txt = (TextView) v.findViewById(R.id.txt);
            txt.setText("Are you sure you want to Delete this post?");

            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Delete_post(postid);
                    alert.dismiss();
                }
            });
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alert.dismiss();
                }
            });


            return true;
        }
    };

    public void Delete_post(String postid) {

        ArrayList<Model_user> array1 = new ArrayList<>();
        Model_user model = new Model_user();
        model.setPostid(postid);
        array1.add(model);

        // obj_dialog.show();
        String url = Configr.app_url + "deletepost";
        String json = "";

        json = JSON.add_json(array1, pref, "deletepost");

        HashMap<String, String> param = new HashMap<>();
        param.put("data", json);


        Response.Listener<String> lis_pat = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response", response);
                // obj_dialog.dismiss();

                String toastMsg = "";
                toastMsg = "";


                try {
                    JSONObject jobj = new JSONObject(response);
                    String res_flag = jobj.getString("status");
                    String res_msg = jobj.getString("message");
                    String otp = "", userid = "";

                    if (res_flag.equals("200")) {
                       // Toast.makeText(context, res_msg, Toast.LENGTH_SHORT).show();
                        finish();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        };


        Response.ErrorListener lis_error = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //  signup.setClickable(true);
                Toast.makeText(context, "" + Configr.Message, Toast.LENGTH_SHORT).show();
            }
        };
        Connection.postconnection(url, param, Configr.getHeaderParam(), context, lis_pat, lis_error);
    }


    public void notification_data() {

        obj_dialog.show();

        String url = Configr.app_url + "getnotificationscreen";

        String json = "";

        json = JSON.add_view(array, pref, "getnotificationscreen");

        HashMap<String, String> param = new HashMap<>();
        param.put("data", json);

        Response.Listener<String> lis_pat = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response", response);
                obj_dialog.dismiss();

                String toastMsg = "";
                toastMsg = "";


                try {
                    JSONObject jobj = new JSONObject(response);
                    String res_flag = jobj.getString("status");
                    String res_msg = jobj.getString("message");


                    if (res_flag.equals("200")) {
                        JSONObject jarray = new JSONObject(jobj.getString("data"));

                        JSONArray jarray1 = new JSONArray(jarray.getString("post"));
                        JSONArray jarray2 = new JSONArray(jarray.getString("comment"));

                        array.clear();
                        array_comment.clear();

                        for (int i = 0; i < jarray1.length(); i++) {
                            Model_view_comment model = new Model_view_comment();
                            JSONObject jobj1 = jarray1.getJSONObject(i);
                            model.setPostid(jobj1.getString("postid"));
                            model.setFname(jobj1.getString("fname"));
                            model.setLname(jobj1.getString("lname"));
                            model.setPost(jobj1.getString("description"));
                            model.setTime(jobj1.getString("time"));
                            model.setUserid(jobj1.getString("userid"));
                            Log.e("Jinal_Url", jobj1.getString("url"));
                            model.setUrl(jobj1.getString("url"));
                            Userid = jobj1.getString("userid");


                            if (jobj1.getString("propic").equals("")) {
                                profilepic.setImageResource(R.drawable.default_imggg);
                            } else {
                                Glide.with(context).load(jobj1.getString("propic")).into(profilepic);
                            }

                            //model.setThumb(jobj1.getString("thumb"));
                            model.setTot_beans(jobj1.getString("beans"));
                            model.setStatus(jobj1.getString("status"));


                            if (model.getStatus().equals("0")) {
                                iv_play.setVisibility(View.GONE);
                                Glide.with(context).load(jobj1.getString("url")).centerCrop().into(displayimage);

                            } else if (model.getStatus().equals("1")) {
                                iv_play.setVisibility(View.VISIBLE);
                                Glide.with(context).load(jobj1.getString("thumb")).centerCrop().into(displayimage);
                            } else if (jobj1.getString("status").equals("2")) {
                                rr_lat.setVisibility(View.GONE);
                                ll_beans.setVisibility(View.VISIBLE);
                                tott_beans.setText(model.getTot_beans());
                            } else {
                                rr_lat.setVisibility(View.VISIBLE);
                                ll_beans.setVisibility(View.GONE);

                            }
                            array.add(model);


                            list_fname.setText(jobj1.getString("fname"));
                            list_lname.setText(jobj1.getString("lname"));
                            list_post.setText(jobj1.getString("description"));
                            time.setText(jobj1.getString("time") + "ago");
                            //Glide.with(context).load(jobj1.getString("propic")).into(profilepic);


                            // Glide.with(context).load(jobj1.getString("thumb")).centerCrop().into(displayimage);
                            tot_beans.setText(jobj1.getString("beans"));
                        }

                        for (int i = 0; i < jarray2.length(); i++) {
                            Model_view_comment model = new Model_view_comment();
                            JSONObject jobj1 = jarray2.getJSONObject(i);
                            model.setUserid(jobj1.getString("userid"));
                            model.setComment(jobj1.getString("comment"));
                            model.setFname(jobj1.getString("comment_fname"));
                            model.setLname(jobj1.getString("comment_lname"));
                            model.setPro_pic(jobj1.getString("comment_propic"));
                            model.setTime(jobj1.getString("commenttime"));
                            array_comment.add(model);

                        }
                        Log.e("sizeeee", String.valueOf(array_comment.size()));
                        adapter.notifyDataSetChanged();
                        listview(comment_list);
                    } else {
                        DialogBox.alert_popup(context, res_msg);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }


        };

        Response.ErrorListener lis_error = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //  signup.setClickable(true);
                obj_dialog.dismiss();
                Toast.makeText(context, "" + Configr.Message, Toast.LENGTH_SHORT).show();
            }
        };
        Connection.postconnection(url, param, Configr.getHeaderParam(), context, lis_pat, lis_error);

    }


    public static void listview(ListView list) {
        int height = 0;
        for (int i = 0; i < list.getCount(); i++) {
            View childView = list.getAdapter().getView(i, null, list);
            childView.measure(
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            height += childView.getMeasuredHeight();
        }
        height += list.getDividerHeight() * list.getCount();
        list.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                height));
        Log.e("Listview height", " : " + height);
    }


    private void HTTP_API_post_comment_Call() {


        obj_dialog.show();
        res_message = "Server Error...";

        final String url = Configr.app_url + "postcomment";

        JSONObject jobj_row = new JSONObject();
        JSONObject jobj_loginuser = new JSONObject();
        try {

            jobj_row.put("postid", postid);
            jobj_row.put("userid", pref.getStr_Userid());
            jobj_row.put("comment", comment);

            //Log.e("posttttusserrr", post_user_id);
            jobj_row.put("postuserid", Userid);


            JSONArray jarray_loginuser = new JSONArray();
            jarray_loginuser.put(jobj_row);
            jobj_loginuser.put("postcomment", jarray_loginuser);

        } catch (Exception e) {
            e.printStackTrace();
        }


        Map<String, String> params = new HashMap<>();
        params.put("data", "" + jobj_loginuser.toString().replaceAll("\\\\", ""));

        Response.Listener<String> lis_res = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response", " : " + response);
                obj_dialog.dismiss();
                try {
                    JSONObject jobj1 = new JSONObject(response);
                    Log.e("status code", ":" + jobj1.getInt("status"));
                    HTTP_API_get_comment_Call();
                    edit_comment.setText("");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
              //  Toast.makeText(context, "Successful post comment", Toast.LENGTH_SHORT).show();
            }
        };
        Response.ErrorListener lis_error = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("response error", "" + error);
                obj_dialog.dismiss();
                Toast.makeText(context, "" + Configr.Message, Toast.LENGTH_SHORT).show();
            }
        };
        Connection.postconnection(url, params, Configr.getHeaderParam(), context, lis_res, lis_error);
    }


    private void HTTP_API_get_comment_Call() {

        obj_dialog.show();
        res_message = "Server Error...";

        final String url = Configr.app_url + "getcomment";

        JSONObject jobj_row = new JSONObject();
        JSONObject jobj_loginuser = new JSONObject();
        try {

            jobj_row.put("postid", postid);
            jobj_row.put("userid", pref.getStr_Userid());

            JSONArray jarray_loginuser = new JSONArray();
            jarray_loginuser.put(jobj_row);
            jobj_loginuser.put("getcomment", jarray_loginuser);

        } catch (Exception e) {
            e.printStackTrace();
        }
        Map<String, String> params = new HashMap<>();
        params.put("data", "" + jobj_loginuser.toString().replaceAll("\\\\", ""));

        Response.Listener<String> lis_res = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response", " : " + response);
                obj_dialog.dismiss();
                try {
                    JSONObject jobj1 = new JSONObject(response);

                    if (jobj1.getInt("status") == 200) {
                        JSONArray jsonArray = jobj1.getJSONArray("data");

                        array_comment.clear();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            Model_view_comment model = new Model_view_comment();
                            JSONObject jobj2 = jsonArray.getJSONObject(i);

                            Log.e("Userrr_id", jobj2.getString("userid"));
                            model.setUserid(jobj2.getString("userid"));
                            model.setComment(jobj2.getString("comment"));
                            model.setTime(jobj2.getString("time"));
                            model.setFname(jobj2.getString("fname"));
                            model.setLname(jobj2.getString("lname"));
                            model.setPro_pic(jobj2.getString("propic"));

                            array_comment.add(model);
                        }
                        adapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        Response.ErrorListener lis_error = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("response error", "" + error);
                obj_dialog.dismiss();
                Toast.makeText(context, "" + Configr.Message, Toast.LENGTH_SHORT).show();
            }
        };
        Connection.postconnection(url, params, Configr.getHeaderParam(), context, lis_res, lis_error);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        FirebaseCrash.report(e);
    }
}
