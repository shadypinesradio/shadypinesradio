package com.shadypines.radio.view.fragment.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.shadypines.radio.R;
import com.shadypines.radio.SharedPrefUtils;
import com.shadypines.radio.model.schecule.Day;
import com.shadypines.radio.view.ShowDetailsDialog;
import com.shadypines.radio.view.activity.SUbscribersListActivity;
import com.shadypines.radio.view.authentication.social.SocialActivity;
import com.shadypines.radio.view.fragment.ScheduleFragment;
import com.shadypines.radio.view.fragment.SchedulePresenter;
import com.shadypines.utils.Constants;
import com.shadypines.utils.ProgressDialogUtils;
import com.shadypines.utils.TimeUtils;

import java.util.List;

import pl.droidsonroids.gif.GifImageView;


public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ScheduleHolder> {

    List<Day> list;
    ScheduleFragment activity;
    SchedulePresenter schedulePresenter;
    String day;
    FragmentManager fragmentManager;
    private Context context;



    public ScheduleAdapter(List<Day> list, ScheduleFragment activity,
                           SchedulePresenter schedulePresenter, FragmentManager fragmentManager) {
        this.list = list;
        this.activity = activity;
        this.schedulePresenter = schedulePresenter;
        this.fragmentManager = fragmentManager;
    //    setHasStableIds(true);  // Here

    }

/*
    @Override
    public long getItemId(int position) {
        return list.get(position).getId(); // Assuming each Day item has a unique ID.
    }
*/


    @NonNull
    @Override
    public ScheduleHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_second_schedule,
                parent, false);

        return new ScheduleHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ScheduleHolder holder, @SuppressLint("RecyclerView") int position) {

        /**
         * changing the subscribe color with text..
         */
        Log.e("SCHEULDE_SUBSCRIBE", list.get(position).getShows().get(0).getShowName() + " = " + String.valueOf(list.get(position).isSubscribe()));


        if (list.get(position).isSubscribe()) {
            holder.txtSubscribe.setBackgroundResource(R.drawable.bg_sub_btn_selected);
            holder.txtSubscribe.setText("Subscribed");
        } else {
            holder.txtSubscribe.setText("Subscribe");
            holder.txtSubscribe.setBackgroundResource(R.drawable.bg_sub_btn_unselected);
        }

        holder.txtTitle.setText(list.get(position).getShows().get(0).getShowName());
        holder.textDjName.setText("w/ " + list.get(position).getShows().get(0).getDjName());
        holder.subscribeCountTxt.setText(String.format("%s",list.get(position).getShows().get(0).getSubscribers()));

        if (list.get(position).getShows().get(0).getShowName().equals("Free Play")) {
            holder.txtSubscribe.setVisibility(View.GONE);
        } else {
            holder.txtSubscribe.setVisibility(View.VISIBLE);
        }

        boolean admin= SharedPrefUtils.INSTANCE.readBoolean("isAdmin");
        holder.subscribeCount.setVisibility(View.VISIBLE);

        holder.txtDesc.setText(list.get(position).getShows().get(0).getShowDescription());
        if (list.get(position).getEndTime().equals("00:00:00")){
            String endTime = "12:00 AM";
            holder.txtTime.setText(String.format("%s - %s PT",TimeUtils.Companion.getTimeFromTimeStamp(list.get(position).getStartTimeStamp()),endTime));
        }else{
            holder.txtTime.setText(String.format("%s - %s PT",
                    TimeUtils.Companion.getTimeFromTimeStamp(list.get(position).getStartTimeStamp()),
                    TimeUtils.Companion.getTimeFromTimeStamp(list.get(position).getEndTimeStamp())));
        }

        Glide.with(activity).load(list.get(position).getShows().get(0).getImage()).into(holder.imgSchedule);

        holder.subscribeCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                //Toast.makeText(context, "hello", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(context, SUbscribersListActivity.class);
                intent.putExtra("id",String.format("%s",list.get(position).getShows().get(0).getId()));

                context.startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        });

        holder.txtSubscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Step 1: Optimistic UI Update
                // Step 1: Optimistic UI Update
                holder.txtSubscribe.setClickable(false);  // Disable further clicks

                if ("Subscribe".equals(holder.txtSubscribe.getText().toString())) {
                    holder.txtSubscribe.setBackgroundResource(R.drawable.bg_sub_btn_selected);
                    holder.txtSubscribe.setText("Subscribed");
                } else {
                    holder.txtSubscribe.setText("Subscribe");
                    holder.txtSubscribe.setBackgroundResource(R.drawable.bg_sub_btn_unselected);
                }

                if (SharedPrefUtils.INSTANCE.readString(Constants.Preferences.EMAIL) == null) {
                    activity.startActivity(new Intent(activity.getContext(), SocialActivity.class));
                } else {
                    //subscribe to show
                    ProgressDialogUtils.Companion.on().showProgressDialog(activity.requireContext());
                    if (schedulePresenter != null) {
                        schedulePresenter.subscribeToShow(String.valueOf(list.get(position).getId()),
                                list.get(position).getDayName().substring(0, 3).toUpperCase());
                    }
                }
            }
        });


        /**
         * when show click then show it otherwise gone....
         */
        if (isShowRunning(position)) {
            holder.gif_active.setVisibility(View.VISIBLE);
            Gson gson = new Gson();
            String gsonOne = gson.toJson(list.get(position));
            SharedPrefUtils.INSTANCE.write("runnigKey", gsonOne);
        /*    if (homeFragment.tv_free_play != null) {
                homeFragment.tv_free_play.setText(list.get(position).getShows().get(0).getShowName());
            }*/
        } else {
            holder.gif_active.setVisibility(View.GONE);
        }
        /**
         * click able show the pop up url....
         */
        holder.clickAbleView.setOnClickListener(
                v -> {
                    if (list.get(position).getShows().get(0).getUrl() != null) {
                        //todo : show WebView dialog
                        try {
                            ShowDetailsDialog showDetailsDialog = new ShowDetailsDialog();
                            Bundle bundle = new Bundle();
                            bundle.putString("URL", list.get(position).getShows().get(0).getUrl());
                            showDetailsDialog.setArguments(bundle);
                            showDetailsDialog.show(fragmentManager, "URL");
                        } catch (IllegalStateException ignored) {

                        }
                    }
                }
        );

        holder.txtSubscribe.setClickable(true);

    }

    private boolean isShowRunning(int position) {
        if (TimeUtils.Companion.getCurrentDayName().toUpperCase().equals(day)) {
            return TimeUtils.Companion.isConstrainedByTime(TimeUtils.Companion.getTimeFromTimeStamp(
                    list.get(position).getStartTimeStamp()
            ), TimeUtils.Companion.getTimeFromTimeStamp(list.get(position).getEndTimeStamp()));
        }
        return false;
    }


    public void updateList(List<Day> list, String day, SchedulePresenter schedulePresenter) {
        this.list.clear();
        this.list.addAll(list);
        this.day = day;
        this.schedulePresenter = schedulePresenter;
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ScheduleHolder extends RecyclerView.ViewHolder {
        ImageView imgSchedule;
        GifImageView gif_active;
        TextView txtTitle, txtTime, txtDesc, txtSubscribe, textDjName, subscribeCountTxt;
        LinearLayout subscribeCount;
        View clickAbleView;

        public ScheduleHolder(@NonNull View itemView) {
            super(itemView);
            gif_active = itemView.findViewById(R.id.gif_active);
            imgSchedule = itemView.findViewById(R.id.image_view_schedule);
            // title..
            subscribeCount = itemView.findViewById(R.id.subscribe_count);
            subscribeCountTxt = itemView.findViewById(R.id.subscribe_count_txt);
            txtTitle = itemView.findViewById(R.id.txt_title);
            txtTime = itemView.findViewById(R.id.txt_time);
            txtDesc = itemView.findViewById(R.id.txt_desc);
            clickAbleView = itemView.findViewById(R.id.click_able_view);
            txtSubscribe = itemView.findViewById(R.id.txt_subscribe);
            textDjName = itemView.findViewById(R.id.text_dj_name);
            // homeFragment.tv_free_play = itemView.findViewById(R.id.text_free_play);
        }
    }
}
