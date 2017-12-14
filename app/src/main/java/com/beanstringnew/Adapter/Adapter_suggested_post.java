package com.beanstringnew.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.beanstringnew.Activity.Play_video_activity;
import com.beanstringnew.Activity.Sponsored_Photo_activity;
import com.beanstringnew.Activity.Sponsored_video_activity;
import com.beanstringnew.Controller.Activity_indicator;
import com.beanstringnew.Model.Model_Play_video;
import com.beanstringnew.Model.Model_Suggest;
import com.beanstringnew.Model.Model_user;
import com.beanstringnew.R;
import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Admin on 11/23/2017.
 */

public class Adapter_suggested_post extends RecyclerView.Adapter<Adapter_suggested_post.MyView> {
    ArrayList<Model_Suggest> array_suggest = new ArrayList<>();

    Context context;
    MediaController mc;
    Activity_indicator obj_dialog;
    CountDownTimer timer;
    public static int videoduration = 0;


    public Adapter_suggested_post(Context context, ArrayList<Model_Suggest> array_suggest) {
        this.context = context;
        this.array_suggest = array_suggest;
        obj_dialog = new Activity_indicator(context);
        obj_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));

    }

    public class MyView extends RecyclerView.ViewHolder {

        ImageView sugge_image;
        TextView sugge_text;
        ImageView iv_play;


        public MyView(View itemView) {
            super(itemView);

            sugge_image = (ImageView) itemView.findViewById(R.id.sugge_image);
            sugge_text = (TextView) itemView.findViewById(R.id.sugge_text);
            iv_play = (ImageView) itemView.findViewById(R.id.iv_play);


        }
    }

    @Override
    public Adapter_suggested_post.MyView onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_suggested_post, parent, false);


        return new MyView(itemView);
    }

    @Override
    public void onBindViewHolder(final Adapter_suggested_post.MyView holder, final int position) {
        final Model_Suggest model = array_suggest.get(position);

        if (model.getStatus().equals("1")) {
            holder.iv_play.setVisibility(View.GONE);
        } else {
            holder.iv_play.setVisibility(View.VISIBLE);
        }

        Glide.with(context).load(model.getImage()).into(holder.sugge_image);
        holder.sugge_text.setText(model.getName());
        Log.e("Video", "" + model.getVideo());

        holder.iv_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                Model_Play_video model_play_video = new Model_Play_video();
                model_play_video.setPostid(model.getAdv_id());
                model_play_video.setUrl(model.getVideo());
                Intent i = new Intent(context, Sponsored_video_activity.class);
                i.putExtra("url", model_play_video);
                context.startActivity(i);

            }
        });

        holder.sugge_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Model_Play_video model_play_video = new Model_Play_video();
                model_play_video.setPostid(model.getAdv_id());
                model_play_video.setUrl(model.getImage());
                Intent i = new Intent(context, Sponsored_Photo_activity.class);
                i.putExtra("url", model_play_video);
                context.startActivity(i);
            }
        });


    }

    @Override
    public int getItemCount() {
        return array_suggest.size();
    }
}
