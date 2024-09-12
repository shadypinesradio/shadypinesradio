package com.shadypines.radio.view.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.google.gson.Gson;
import com.shadypines.radio.R;
import com.shadypines.radio.SharedPrefUtils;
import com.shadypines.radio.model.RadioResponse;
import com.shadypines.radio.model.schecule.Day;
import com.shadypines.radio.model.schecule.Schedule;
import com.shadypines.radio.utils.AppSettings;
import com.shadypines.radio.view.ShowDetailsDialog;
import com.shadypines.radio.view.activity.TempActivity;
import com.shadypines.radio.view.authentication.social.SocialActivity;
import com.shadypines.radio.view.fragment.syncSchedule.MyViewModel;
import com.shadypines.utils.Constants;
import com.shadypines.utils.ProgressDialogUtils;
import com.shadypines.utils.TimeUtils;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class HomeFragment extends Fragment implements View.OnClickListener, ScheduleView {


    SchedulePresenter schedulePresenter;
    private MyViewModel viewModel;
    View view;
    private Schedule schedule;
    public ImageView iv_images;
    ConstraintLayout constraintLayout;
    public static ImageView iv_play, btn_arrow;
    public TextView tv_title, tv_albume, tv_free_play, tv_subscribes;
    public ConstraintLayout constraint_free_play;
    public RelativeLayout rl_play;
    public SwipeRefreshLayout pullToRefresh;
    TempActivity homeActivity;

 /*   public static HomeFragment homeFragment = null;

    public static HomeFragment getInstance(){
        if (homeFragment == null){
            homeFragment =new HomeFragment();
        }
        return homeFragment;
    }
*/
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getActivity() instanceof TempActivity) {
            homeActivity = (TempActivity) getActivity();
        } else {
            throw new RuntimeException("HomeFragment must be hosted by TempActivity");
        }
        schedulePresenter = new SchedulePresenter(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home_layout, container, false);
        init();
        viewModel = new ViewModelProvider(requireActivity()).get(MyViewModel.class);
        viewModel.getScheduleLiveData().observe(getViewLifecycleOwner(), resource -> {
            if (resource != null) {
                if (resource.getData() != null) {
                    // Update UI with new schedule data
                    if (resource.getDay() != null) {
                        onGetScheduleSuccessImp(resource.getData(), resource.getDay());
                    }
                } else if (resource.getError() != null) {
                    // Handle error and update UI accordingly
                    onGetScheduleErrorImp();
                }
            }
        });

        // Observe logout event
        viewModel.isUserLoggedOut().observe(getViewLifecycleOwner(), isLoggedOut -> {
            if (isLoggedOut != null && isLoggedOut) {
                updateSubscribeButtonUI();
                // Reset the flag in ViewModel to avoid unwanted UI updates
                viewModel.resetUserLoggedOutStatus();
            }
        });

        viewModel.getRadioResponseLiveData().observe(getViewLifecycleOwner(), this::updateRadioUI);


        try {
            if (SharedPrefUtils.INSTANCE.readString(Constants.Preferences.EMAIL) != null) {
                Gson gson = new Gson();
                String json = SharedPrefUtils.INSTANCE.readString("runnigKey");
                Day obj = gson.fromJson(json, Day.class);
                if (obj.getShows().get(0).getShowName().equals("Free Play")) {
                    tv_subscribes.setVisibility(View.GONE);
                } else {
                    tv_subscribes.setVisibility(View.VISIBLE);
                    if (obj.isSubscribe()) {
                        tv_subscribes.setBackgroundResource(R.drawable.bg_sub_btn_selected);
                        tv_subscribes.setText("Subscribed");
                    } else {
                        tv_subscribes.setText("Subscribe");
                    }
                }
            }
        } catch (Exception ex) {
            tv_subscribes.setVisibility(View.VISIBLE);
        }
        return view;
    }

    private String currentTitle = "";
    private String currentAlbum = "";
    private String currentImageUrl = "";

    private void updateRadioUI(RadioResponse radioResponse) {
        try {
            String currentTrackTitle = radioResponse.getCurrentTrack().getTitle();
            String imageUrl = radioResponse.getCurrentTrack().getArtworkUrlLarge();
            String[] value = currentTrackTitle.split("-");
            String album = value[0];
            String title = value.length > 1 ? value[1] : "";

            // Always set the text
            tv_title.setText(title);
            AppSettings.title = title;
            tv_albume.setText(album);
            AppSettings.album = album;
            AppSettings.imageurl = imageUrl;

            if (!title.equals(currentTitle) || !album.equals(currentAlbum)) {
                currentTitle = title;
                currentAlbum = album;
                // Additional UI updates here if necessary when title or album changes
            }

            if (!imageUrl.equals(currentImageUrl)) {
                // Load image only when the URL is different
                Glide.with(this).load(imageUrl).into(iv_images);
                currentImageUrl = imageUrl;

                Glide.with(this)
                        .asBitmap()
                        .transform(new CenterCrop(), new GranularRoundedCorners(45, 45, 0, 0))
                        .load(imageUrl)
                        .listener(new RequestListener<Bitmap>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model,
                                                        Target<Bitmap> target, boolean isFirstResource) {
                                Log.e("GLIDE_ERROR", "Failed to load image", e);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Bitmap resource, Object model,
                                                           Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                                return false;
                            }
                        })
                        .into(new CustomTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                resource.compress(Bitmap.CompressFormat.JPEG, 20, stream);
                                byte[] byteArray = stream.toByteArray();
                                // Handle the bitmap as necessary...
                                TempActivity.bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                                if (getActivity() instanceof TempActivity && ((TempActivity)getActivity()).serviceBinder != null) {
                                    Log.d("newPlayerTag =", "serviceBinder");
                                    ((TempActivity)getActivity()).serviceBinder.setNotification();
                                }
                            }

                            @Override
                            public void onLoadCleared(@Nullable Drawable placeholder) {
                            }
                        });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
      //  counter();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void init() {
        homeActivity = ((TempActivity) getActivity());
        //videoView = view.findViewById(R.id.video_view);
        iv_images = view.findViewById(R.id.iv_image);
        constraint_free_play =   view.findViewById(R.id.constraint_free_play);
        iv_play = view.findViewById(R.id.iv_play);
        btn_arrow = view.findViewById(R.id.btn_arrow);
        tv_title = view.findViewById(R.id.tv_title);
        tv_free_play = view.findViewById(R.id.text_free_play);
        tv_title.setSelected(true);
        tv_albume = view.findViewById(R.id.tv_albume);
        tv_albume.setSelected(true);
        rl_play = view.findViewById(R.id.rl_play);
        pullToRefresh = view.findViewById(R.id.pullToRefresh);
        tv_subscribes = view.findViewById(R.id.txt_subscribed);
        constraint_free_play.setVisibility(View.GONE);

        pullToRefresh.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                btn_arrow.setVisibility(View.VISIBLE);
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        btn_arrow.setVisibility(View.GONE);
                    }
                }, 1500);
            }

            return true;
        });
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                homeActivity.onrefersh();
            }
        });
        listener();
        //setListener();
    }

    private void listener() {
        rl_play.setOnClickListener(this);
        tv_free_play.setOnClickListener(this);
        tv_subscribes.setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_play:
                try {
                    homeActivity.NotificationValue();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            //web view
            /**
             * showing the popup code...
             */
            case R.id.text_free_play:
                //Toast.makeText(requireActivity(), "free play", Toast.LENGTH_SHORT).show();
                try {

                    /**
                     * here is the Gson ...
                     */
                    Gson gson = new Gson();
                    String json = SharedPrefUtils.INSTANCE.readString("runnigKey");
                    Day obj = gson.fromJson(json, Day.class);

                    ShowDetailsDialog showDetailsDialog = new ShowDetailsDialog();
                    Bundle bundle = new Bundle();
                    /**
                     * get link here by url....
                     * if the Free Play then not showing url..
                     * if not the Free play then dialog link showing...
                     */
                    bundle.putString("URL", obj.getShows().get(0).getUrl());
                    if (obj.getShows().get(0).getShowName().equals("Free Play")) {

                    } else {
                        showDetailsDialog.setArguments(bundle);
                        showDetailsDialog.show(getActivity().getSupportFragmentManager(), "URL");
                    }

                } catch (IllegalStateException ignored) {
                    Log.d("Home","excep="+ignored);
                }

                break;

            /**
             * todo: text subscribe button...!
             */
            case R.id.txt_subscribed:
                Gson gson = new Gson();
                String json = SharedPrefUtils.INSTANCE.readString("runnigKey");
                Day obj = gson.fromJson(json, Day.class);
                // Toast.makeText(requireActivity(), "subscribe.", Toast.LENGTH_SHORT).show();
                /**
                 * when email null then go to sign in or social activity
                 * if email given then subscribed show
                 */
                try {
                    if (SharedPrefUtils.INSTANCE.readString(Constants.Preferences.EMAIL) == null) {
                        //activity.showLoginDialog();
                        /**
                         * subscribe to sign in activity....
                         */
                        homeActivity.startActivity(new Intent(requireActivity(), SocialActivity.class));

                    } else {
                        /**
                         * subscribe button click...
                         */
                        ProgressDialogUtils.Companion.on().showProgressDialog(requireActivity());
                        if (schedulePresenter != null) {
                            //todo: neww
                            schedulePresenter.subscribeToShow(String.valueOf(obj.getId()),
                                    obj.getDayName().substring(0, 3).toUpperCase());
                        }
                    }
                }catch (Exception e) {
                    homeActivity.startActivity(new Intent(requireActivity(), SocialActivity.class));
                }

                break;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        try {
           if (homeActivity.playerServiceBinder.getAudioFocusPlayer().isPlaying()) {
               iv_play.setImageResource(R.drawable.ic_pause);
           } else {
               iv_play.setBackgroundResource(R.drawable.ic_play);
           }
       }catch (Exception e){
           Log.d("Home","excep="+e);
       }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        viewModel.getScheduleLiveData().removeObservers(getViewLifecycleOwner());
    }

    @Override
    public void onResume() {
        Gson gson = new Gson();
        String json = SharedPrefUtils.INSTANCE.readString("runnigKey");
        Day obj = gson.fromJson(json, Day.class);

        try {
            if (homeActivity.playerServiceBinder.getAudioFocusPlayer().isPlaying()) {
                iv_play.setImageResource(R.drawable.ic_pause);
            } else {
                iv_play.setBackgroundResource(R.drawable.ic_play);
            } }catch (Exception e){
            Log.d("Home","excep="+e);
        }

        try {
                if (obj.getShows().get(0).getShowName().equals("Free Play")) {
                    tv_subscribes.setVisibility(View.GONE);
                } else {
                    tv_subscribes.setVisibility(View.VISIBLE);
                }
        } catch (Exception e) {
            Log.d("Home","excep="+e);
        }

        super.onResume();
    }


    public void updateSubscribeButtonUI(){
        try {
            getActivity().runOnUiThread(() -> {
                tv_subscribes.setBackgroundResource(R.drawable.bg_sub_btn_unselected);
                tv_subscribes.setText("Subscribe");
            });
        }catch (Exception exception){
            exception.printStackTrace();
        }
    }

    /**
     * todo: onSubscribe to show..
     *
     * @param day
     */
    @Override
    public void onSubscribeSuccess(@NonNull String id, @NotNull String day) {
        //ProgressDialogUtils.Companion.on().hideProgressDialog();
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Constants.Default.Companion.setCAME_FROM_LOGIN_Schedule(true);
                       // ScheduleFragment.Companion.getInstance().onResume();
                        tv_subscribes.setBackgroundResource(R.drawable.bg_sub_btn_selected);
                        tv_subscribes.setText("Subscribed");
                    }
                });

            }
        },200);
        if(getActivity() instanceof TempActivity){
            ((TempActivity) getActivity()).getSchedule(day);
        }
       // schedulePresenter.getScheduleRx(day);
    }



    @Override
    public void onSubscribeError(@NotNull String message, @NotNull String day) {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Constants.Default.Companion.setCAME_FROM_LOGIN_Schedule(true);
                        //ScheduleFragment.Companion.getInstance().onResume();
                        tv_subscribes.setBackgroundResource(R.drawable.bg_sub_btn_unselected);
                        tv_subscribes.setText("Subscribe");
                    }
                });

            }
        },200);
        if(getActivity() instanceof TempActivity){
            ((TempActivity) getActivity()).getSchedule(day);
        }
    }



    public void onGetScheduleSuccessImp(@NotNull Schedule schedule, @NotNull String day) {
        try {
            ProgressDialogUtils.Companion.on().hideProgressDialog();
            this.schedule = schedule;
            List<Day> days = TimeUtils.Companion.selectDay(TimeUtils.Companion.getCurrentDayName().toUpperCase(), schedule);
            int isRunning = 0;
            String showDay = "";
            for (Day d : days) {
                if (d.getDayName().equals("Monday")) {
                    showDay = Constants.Days.MON;
                } else if (d.getDayName().equals("Tuesday")) {
                    showDay = Constants.Days.TUE;
                }else if (d.getDayName().equals("Wednesday")) {
                    showDay = Constants.Days.WED;
                }else if (d.getDayName().equals("Thursday")) {
                    showDay = Constants.Days.THU;
                }else if (d.getDayName().equals("Friday")) {
                    showDay = Constants.Days.FRI;
                }else if (d.getDayName().equals("Saturday")) {
                    showDay = Constants.Days.SAT;
                }else {
                    showDay = Constants.Days.SUN;
                }
                if (isShowRunning(d, showDay)) {
                    Gson gson = new Gson();
                    String gsonOne = gson.toJson(d);
                    isRunning = 1;
                    SharedPrefUtils.INSTANCE.write("runnigKey", gsonOne);
                    if (tv_free_play != null) {
                        tv_free_play.setText(d.getShows().get(0).getShowName());
                    }
                    if (d.isSubscribe()){
                        Log.e("HOME_SHOW_RUNNING", "TRUE");
                        tv_subscribes.setBackgroundResource(R.drawable.bg_sub_btn_selected);
                        tv_subscribes.setText("Subscribed");
                    }else{
                        Log.e("HOME_SHOW_RUNNING", "FALSE");
                    }
                }
            }
            if (isRunning == 0) {
                constraint_free_play.setVisibility(View.GONE);
            } else {
                constraint_free_play.setVisibility(View.VISIBLE);
            }

          //  counter();
        }catch (Exception e){
            Log.d("schudleexe",e.toString());
        }

    }

    private boolean isShowRunning( Day d,String day) {
        if (TimeUtils.Companion.getCurrentDayName().toUpperCase().equals(day)) {
            return TimeUtils.Companion.isConstrainedByTime(TimeUtils.Companion.getTimeFromTimeStamp(
                    d.getStartTimeStamp()
            ), TimeUtils.Companion.getTimeFromTimeStamp(d.getEndTimeStamp()));
        }
        return false;
    }


    public void onGetScheduleErrorImp() {
        try {
            ProgressDialogUtils.Companion.on().hideProgressDialog();
        }catch (Exception e) {
            Log.d("schduleError",e.toString());
        }
    }

    @Override
    public void onSignInSuccess(@NotNull String email, @NotNull String day) {

    }

    @Override
    public void onSignInError(@NotNull String message) {

    }

    @Override
    public void onSignUpSuccess() {

    }

    @Override
    public void onSignUPError(@NotNull String message) {

    }

    @Override
    public void onResetPasswordSuccess() {

    }

    @Override
    public void onResetPasswordError(@NotNull String message) {

    }




}