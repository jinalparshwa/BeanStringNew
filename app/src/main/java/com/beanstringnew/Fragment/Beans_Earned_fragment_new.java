package com.beanstringnew.Fragment;


import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.beanstringnew.Controller.Activity_indicator;
import com.beanstringnew.Controller.Configr;
import com.beanstringnew.Controller.Connection;
import com.beanstringnew.Controller.DialogBox;
import com.beanstringnew.Json_model.JSON;
import com.beanstringnew.Model.Model_profile;
import com.beanstringnew.Model.Model_user;
import com.beanstringnew.Myapplication;
import com.beanstringnew.R;
import com.beanstringnew.Shared.Pref_Master;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static com.beanstringnew.Fragment.Grid_Beans_earned_fragment.adapter_grid;
import static com.beanstringnew.Fragment.Grid_Beans_earned_fragment.array;
import static com.beanstringnew.Fragment.Grid_Beans_earned_fragment.img_grid;
import static com.beanstringnew.Fragment.Grid_Beans_earned_fragment.notext1;
import static com.beanstringnew.Fragment.List_Beans_earned_fragment.adapter;
import static com.beanstringnew.Fragment.List_Beans_earned_fragment.array1;
import static com.beanstringnew.Fragment.List_Beans_earned_fragment.notext;

/**
 * Created by Abc on 10/24/2016.
 */


public class Beans_Earned_fragment_new extends Fragment implements View.OnClickListener {

    Pref_Master pref;
    Context context;
    Activity_indicator obj_dialog;
    //    ArrayList<Model_user> array = new ArrayList<>();
//    ArrayList<Model_user> array1 = new ArrayList<>();
    EditText startdate, enddate;
    Button datesearch;
    TextView beancount;
    TextView start, end;
    ArrayList<Model_user> Array_user = new ArrayList<>();
    ArrayList<Model_profile> Array_userr = new ArrayList<>();
    TextView from;
    TextView to;
    TabLayout tabLayout;
    private int[] tabIcons = {
            R.drawable.grid_small,
            R.drawable.list,
    };

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.beans_earned_layout_new, container, false);
        context = getActivity();
        pref = new Pref_Master(context);
        obj_dialog = new Activity_indicator(context);
        obj_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));

        ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        createTabIcons();

        startdate = (EditText) view.findViewById(R.id.startdate);
        enddate = (EditText) view.findViewById(R.id.enddate);
        datesearch = (Button) view.findViewById(R.id.datesearch);
        beancount = (TextView) view.findViewById(R.id.beancount);

        startdate.setFocusableInTouchMode(false);
        startdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDateTimeField(startdate);
            }
        });

        enddate.setFocusableInTouchMode(false);
        enddate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDateTimeField(enddate);
            }
        });
        datesearch.setOnClickListener(this);
        start = (TextView) view.findViewById(R.id.start);
        end = (TextView) view.findViewById(R.id.end);
        from = (TextView) view.findViewById(R.id.from);
        to = (TextView) view.findViewById(R.id.to);


        beans_Count();
        Beans_earned();
        Myapplication.getInstance().trackScreenView("Beans Earned Screen");

        return view;
    }

    private void createTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new Grid_Beans_earned_fragment(), "");
        adapter.addFragment(new List_Beans_earned_fragment(), "");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    public void Beans_earned() {

        obj_dialog.show();

        String url = Configr.app_url + "mybeans";
        String json = "";

        json = JSON.add_json(array, pref, "mybeans");

        HashMap<String, String> param = new HashMap<>();
        param.put("data", json);
        Log.e("jinal", ":" + param.toString());

        Response.Listener<String> lis_pat = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response", response);
                obj_dialog.dismiss();
                try {
                    JSONObject jobj = new JSONObject(response);
                    String res_flag = jobj.getString("status");
                    String res_msg = jobj.getString("message");
                    if (res_flag.equals("200")) {


                        JSONObject job = new JSONObject(jobj.getString("data"));

                        beancount.setText(job.getString("beans"));


                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        };

        Response.ErrorListener lis_error = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                obj_dialog.dismiss();
                Toast.makeText(context, "" + Configr.Message, Toast.LENGTH_SHORT).show();
            }
        };
        Connection.postconnection(url, param, Configr.getHeaderParam(), context, lis_pat, lis_error);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.datesearch:
                Validate();
                break;
        }
    }

    private void Validate() {
        if (startdate.getText().toString().equals("")) {
            startdate.setError("Choose Start date");

        } else if (enddate.getText().toString().equals("")) {
            enddate.setError("Choose end date");
            startdate.setError(null);
        } else {
            enddate.setError(null);
            Array_user.clear();
            Model_user user = new Model_user();
            user.setStartdate(startdate.getText().toString().trim());
            user.setEnddate(enddate.getText().toString().trim());
            Array_user.add(user);
            search_date();

//            if (mContext instanceof StoryActivity) {
//                ((StoryActivity) mContext).setProgress();
//            }

            // search_date();


        }
    }


    private void setDateTimeField(final EditText et) {
        final SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Calendar newCalendar = Calendar.getInstance();

        DatePickerDialog fromDatePickerDialog = new DatePickerDialog(context,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        // TODO Auto-generated method stub
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, monthOfYear, dayOfMonth);
                        et.setText(dateFormatter.format(newDate.getTime()));
                    }
                }, newCalendar.get(Calendar.YEAR),
                newCalendar.get(Calendar.MONTH),
                newCalendar.get(Calendar.DAY_OF_MONTH));
        fromDatePickerDialog.show();
        fromDatePickerDialog.getDatePicker().setMaxDate(newCalendar.getTimeInMillis());
    }

    @Override
    public void onResume() {
        //Beans_earned();
        startdate.setText("");
        enddate.setText("");
//        beancount.setText("");
        from.setText("");
        start.setText("");
        to.setText("");
        end.setText("");
        super.onResume();
        Myapplication.getInstance().trackScreenView("Beans Earned Screen");
    }

    public void beans_Count() {

//        obj_dialog.show();

        String url = Configr.app_url + "beanscount";

        String json = "";

        json = JSON.add(Array_userr, pref, "beanscount");

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
                        JSONArray jarray = new JSONArray(jobj.getString("data"));
                        Array_userr.clear();

                        for (int i = 0; i < jarray.length(); i++) {
                            Model_profile model = new Model_profile();
                            JSONObject jobj1 = jarray.getJSONObject(i);

                            model.setBeans(jobj1.getString("beans"));

                            Array_userr.add(model);


                        }

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
                // obj_dialog.dismiss();
                Toast.makeText(context, "" + Configr.Message, Toast.LENGTH_SHORT).show();
            }
        };
        Connection.postconnection(url, param, Configr.getHeaderParam(), context, lis_pat, lis_error);

    }

    public void search_date() {

        obj_dialog.show();


        String url = Configr.app_url + "beanssearch";
        String json = "";

        json = JSON.add_json(Array_user, pref, "beanssearch");

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


                        JSONObject job = new JSONObject(jobj.getString("data"));


                        JSONArray jarray2 = new JSONArray(job.getString("post"));
                        JSONArray jarray1 = new JSONArray(job.getString("comment"));

                        from.setVisibility(View.VISIBLE);
                        to.setVisibility(View.VISIBLE);

                        array.clear();
                        array1.clear();


                        for (int i = 0; i < jarray2.length(); i++) {
                            Model_user model = new Model_user();
                            JSONObject jobj1 = jarray2.getJSONObject(i);
                            model.setUrl(jobj1.getString("url"));
                            model.setThumb(jobj1.getString("thumb"));
                            model.setTotal_beans(jobj1.getString("beans"));
                            model.setStatus(jobj1.getString("status"));
                            model.setPostid(jobj1.getString("postid"));
                            model.setPost_userid(jobj1.getString("postuserid"));
                            array.add(model);
                        }
                        start.setText(startdate.getText().toString());
                        end.setText(enddate.getText().toString());
                        beancount.setText(job.getString("beans"));

                        for (int i = 0; i < jarray1.length(); i++) {
                            Model_user model = new Model_user();
                            JSONObject jobj1 = jarray1.getJSONObject(i);
                            model.setCommentid(jobj1.getString("commentid"));
                            model.setBeans_comment(jobj1.getString("comment"));
                            model.setBeans_profile(jobj1.getString("propic"));
                            model.setTotal_beans(jobj1.getString("beans"));
                            model.setPostid(jobj1.getString("postid"));
                            model.setPostuserid(jobj1.getString("postuserid"));
                            model.setLast_comment_time(jobj1.getString("time"));
                            array1.add(model);
                        }


                        Log.e("arraylist_photo", ":" + array1.size());

                        adapter_grid.notifyDataSetChanged();
                        if (array.size() == 0) {
                            notext1.setVisibility(View.VISIBLE);
                            img_grid.setVisibility(View.GONE);
                        } else {
                            notext1.setVisibility(View.GONE);
                            img_grid.setVisibility(View.VISIBLE);
                        }

                        adapter.notifyDataSetChanged();
                        if (array1.size() == 0) {
                            notext.setVisibility(View.VISIBLE);
                            img_grid.setVisibility(View.GONE);
                        } else {
                            notext.setVisibility(View.GONE);
                            img_grid.setVisibility(View.VISIBLE);
                        }

                    } else {
                        if (array.size() == 0) {
                            notext1.setVisibility(View.VISIBLE);
                            img_grid.setVisibility(View.GONE);
                        } else {
                            notext1.setVisibility(View.GONE);
                            img_grid.setVisibility(View.VISIBLE);
                        }

                        if (array1.size() == 0) {
                            notext.setVisibility(View.VISIBLE);
                            img_grid.setVisibility(View.GONE);
                        } else {
                            notext.setVisibility(View.GONE);
                            img_grid.setVisibility(View.VISIBLE);
                        }

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
}
