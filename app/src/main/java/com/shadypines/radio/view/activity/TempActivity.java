package com.shadypines.radio.view.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.NotificationManager;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.PowerManager;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.facebook.FacebookSdk;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.ui.PlayerNotificationManager;
import com.shadypines.radio.R;
import com.shadypines.radio.SharedPrefUtils;
import com.shadypines.radio.model.RadioResponse;
import com.shadypines.radio.model.schecule.Day;
import com.shadypines.radio.player.NewPlayService;
import com.shadypines.radio.player.PlayerHolder;
import com.shadypines.radio.presenter.HomePresenterImpl;
import com.shadypines.radio.view.authentication.forgot.AdsView;
import com.shadypines.radio.view.authentication.social.SocialActivity;
import com.shadypines.radio.view.fragment.ChatFragment;
import com.shadypines.radio.view.fragment.CommonFragment;
import com.shadypines.radio.view.fragment.HomeFragment;
import com.shadypines.radio.view.fragment.syncSchedule.GetSchedulePresenter;
import com.shadypines.radio.view.fragment.syncSchedule.MyViewModel;
import com.shadypines.radio.view.tab_adapter.MyPagerAdapter;
import com.shadypines.radio.view.viewinteractors.HomeView;
import com.shadypines.utils.Constants;
import com.shadypines.utils.ReviewUtils;
import com.shadypines.utils.TimeUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import me.leolin.shortcutbadger.ShortcutBadger;
import me.relex.circleindicator.CircleIndicator;
import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;


public class TempActivity extends BaseActivity
        implements HomeView, View.OnClickListener, AdsView {

    private boolean isFirstTimeInit = true;
    private static String imageName;
    String message = "";
    int pushTitle_count;
    PlayerNotificationManager playerNotificationManager;
    private Dialog dialog;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog adsdialog;

    public static HomeFragment homeFragment;
    public static ChatFragment chatFragment;
    CircleIndicator pageIndicatorView;
    public NewPlayService serviceBinder;
    public PlayerHolder playerServiceBinder;
    private DrawerLayout mDrawerLayout;
    public static Bitmap bitmap;
    //    public static HomeFragment homeFragment;
    public RelativeLayout rl_menu, rl_loading, rl_refresh, rl_play_pause, ll_review, ll_profile;
    TextView tv_title;
    ImageView iv_logo, progrees, iv_play_pause, ivImages, ads_image, backgroundIv;
    TextView adsButton;
    RelativeLayout ll_instagram, ll_youtube, ll_facebook,
            sprshow, ll_my_account, ll_buy, ll_become, ll_chat, ll_event, tv_contact, ll_donate,
            tv_sendsong, ll_who, tv_shady, top_bar;
    RelativeLayout ll_shady, tv_logout, rv_changePassword;
    CommonFragment commonFragment;
    public String loadUrl = "";
    HomePresenterImpl presenter;
    public static NotificationManager mNotificationManager;
    public boolean firstTime = false;
    public boolean homeScreen = false;
    boolean isFirstTImeApp = true;
    public MediaPlayer mPlayer;
    public ConstraintLayout constraint_free_play;

   // Compress compressedFile;
    // ProgressDialogUtils.Companion.on().showProgressDialog(this);

    // LinearLayout tabLayout;
    FrameLayout containerLayout;
    ConstraintLayout constraintLayout;
    ViewPager viewPager;
    //    RadioTabAdapter radioTabAdapter;
    MyPagerAdapter myPagerAdapter;
    //    List<Fragment> fragmentList = new ArrayList<Fragment>();
    GetSchedulePresenter getSchedulePresenter;
    Ads_presenter ads_presenter;

    private LayoutInflater layoutInflater;

    private TextView textView_count_notification;
    public static boolean isAppActive = true;

    private GifImageView gifImageView;
    private MyViewModel viewModel;

    private static final int REQUEST_CODE_POST_NOTIFICATIONS = 123;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp);
        isAppActive = true;
        //   homeFragment = HomeFragment.getInstance();
        rl_menu = findViewById(R.id.rl_menu);
        viewModel = new ViewModelProvider(this).get(MyViewModel.class);
        getSchedulePresenter = new GetSchedulePresenter(viewModel);

        ads_presenter = new Ads_presenter(this);

        //ask for the notification permission
        requestNotificationPermission();
        //end of the ask for the notification permission
        checkBatteryOptimize();

        init();

        FacebookSdk.fullyInitialize();
        // printHashKey();
        //show review every 3 times
        if (SharedPrefUtils.INSTANCE.readInt(Constants.Default.OPEN_COUNT) != 3) {
            //increase the count
            SharedPrefUtils.INSTANCE.write(Constants.Default.OPEN_COUNT, SharedPrefUtils.INSTANCE.readInt(
                    Constants.Default.OPEN_COUNT) + 1);
        } else {
            SharedPrefUtils.INSTANCE.write(Constants.Default.OPEN_COUNT, 0);
            //show is every 3 times the app open
            ReviewUtils.Companion.askForReview(this);
        }
    }

    public void checkBatteryOptimize() {
        String packageName = getPackageName();
        PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        // Check if the API level is at least 23
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!powerManager.isIgnoringBatteryOptimizations(packageName)) {
                // Optional: Show an explanation dialog or a toast message.
                Toast.makeText(this, "Please allow battery optimization exception for smoother app experience.", Toast.LENGTH_LONG).show();
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                intent.setData(Uri.parse("package:" + packageName));
                startActivity(intent);
            }
        }  // Handle the case for devices with API level lower than 23
        // In such cases, you may not need to check for battery optimizations
        // as the restrictions introduced in API level 23 don't apply.

    }

    private void requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.POST_NOTIFICATIONS},
                        REQUEST_CODE_POST_NOTIFICATIONS);
            }  // Permission already granted
            // You can proceed with posting notifications

        }  // Older OS versions don't require runtime permission for notifications
        // You can proceed with posting notifications

    }

    private void initViewPager() {
        containerLayout.setVisibility(View.GONE);
//        radioTabAdapter = new RadioTabAdapter(getSupportFragmentManager(), fragmentList);
        myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(myPagerAdapter);
        viewPager.setOffscreenPageLimit(3);
         pageIndicatorView.setVisibility(View.VISIBLE);
         pageIndicatorView.setViewPager(viewPager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                HomeFragment.btn_arrow.setVisibility(View.GONE);
            }

            //top_bar
            @Override
            public void onPageSelected(int position) {
                try {
                    View view = TempActivity.this.getCurrentFocus();
                    //If no view currently has focus, create a new one, just so we can grab a window token from it
                    if (view == null) {
                        view = new View(TempActivity.this);
                    }
                    InputMethodManager imm =
                            (InputMethodManager) TempActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                } catch (Exception e) {

                }
                if (position == 0) {
                    pageIndicatorView.setVisibility(View.VISIBLE);

                    ll_shady.setBackgroundResource(R.drawable.drawable_selected);
                    ll_my_account.setBackgroundResource(R.drawable.drawable_unselected);
                    ll_event.setBackgroundResource(R.drawable.drawable_unselected);
                    ll_review.setBackgroundResource(R.drawable.drawable_unselected);
                    ll_donate.setBackgroundResource(R.drawable.drawable_unselected);
                    ll_instagram.setBackgroundResource(R.drawable.drawable_unselected);
                    ll_youtube.setBackgroundResource(R.drawable.drawable_unselected);
                    ll_facebook.setBackgroundResource(R.drawable.drawable_unselected);
                    sprshow.setBackgroundResource(R.drawable.drawable_unselected);
                    ll_buy.setBackgroundResource(R.drawable.drawable_unselected);
                    ll_become.setBackgroundResource(R.drawable.drawable_unselected);
                    ll_chat.setBackgroundResource(R.drawable.drawable_unselected);
                    tv_contact.setBackgroundResource(R.drawable.drawable_unselected);
                    tv_sendsong.setBackgroundResource(R.drawable.drawable_unselected);
                    ll_who.setBackgroundResource(R.drawable.drawable_unselected);
                    tv_shady.setBackgroundResource(R.drawable.drawable_unselected);
                    // rv_changePassword.setBackgroundResource(R.drawable.drawable_unselected);
                    mDrawerLayout.closeDrawer(GravityCompat.START);
                    tv_title.setText("Shady Pines Radio");
                    rl_play_pause.setVisibility(View.INVISIBLE);
                    //unlock the drawer
                    mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);

                } else if (position == 1) {
                    pageIndicatorView.setVisibility(View.VISIBLE);

                    sprshow.setBackgroundResource(R.drawable.drawable_selected);
                    ll_my_account.setBackgroundResource(R.drawable.drawable_unselected);
                    ll_event.setBackgroundResource(R.drawable.drawable_unselected);
                    ll_review.setBackgroundResource(R.drawable.drawable_unselected);
                    ll_donate.setBackgroundResource(R.drawable.drawable_unselected);
                    ll_instagram.setBackgroundResource(R.drawable.drawable_unselected);
                    ll_youtube.setBackgroundResource(R.drawable.drawable_unselected);
                    ll_facebook.setBackgroundResource(R.drawable.drawable_unselected);
                    ll_shady.setBackgroundResource(R.drawable.drawable_unselected);
                    ll_buy.setBackgroundResource(R.drawable.drawable_unselected);
                    ll_become.setBackgroundResource(R.drawable.drawable_unselected);
                    ll_chat.setBackgroundResource(R.drawable.drawable_unselected);
                    tv_contact.setBackgroundResource(R.drawable.drawable_unselected);
                    tv_sendsong.setBackgroundResource(R.drawable.drawable_unselected);
                    ll_who.setBackgroundResource(R.drawable.drawable_unselected);
                    tv_shady.setBackgroundResource(R.drawable.drawable_unselected);
                    //  rv_changePassword.setBackgroundResource(R.drawable.drawable_unselected);
                    rl_refresh.setVisibility(View.VISIBLE);
                    rl_play_pause.setVisibility(View.VISIBLE);
                    tv_title.setText("SPR Show Schedule");
                    mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                    if (viewPager.getAdapter() != null) {
                        viewPager.getAdapter().notifyDataSetChanged();
                    }
                    // Toast.makeText(getApplicationContext(), "SPR Show Schedule - 1", Toast.LENGTH_SHORT).show();

                } else if (position == 2) {

//                    homeScreen = true;

                    mDrawerLayout.closeDrawer(GravityCompat.START);
                    ll_chat.setBackgroundResource(R.drawable.drawable_selected);
                    ll_my_account.setBackgroundResource(R.drawable.drawable_unselected);
                    ll_event.setBackgroundResource(R.drawable.drawable_unselected);
                    ll_review.setBackgroundResource(R.drawable.drawable_unselected);
                    ll_instagram.setBackgroundResource(R.drawable.drawable_unselected);
                    ll_youtube.setBackgroundResource(R.drawable.drawable_unselected);
                    ll_facebook.setBackgroundResource(R.drawable.drawable_unselected);
                    ll_shady.setBackgroundResource(R.drawable.drawable_unselected);
                    sprshow.setBackgroundResource(R.drawable.drawable_unselected);
                    ll_buy.setBackgroundResource(R.drawable.drawable_unselected);
                    ll_become.setBackgroundResource(R.drawable.drawable_unselected);
                    tv_contact.setBackgroundResource(R.drawable.drawable_unselected);
                    tv_sendsong.setBackgroundResource(R.drawable.drawable_unselected);
                    ll_who.setBackgroundResource(R.drawable.drawable_unselected);
                    tv_shady.setBackgroundResource(R.drawable.drawable_unselected);
                    ll_donate.setBackgroundResource(R.drawable.drawable_unselected);
                    //ll_profile.setBackgroundResource(R.drawable.drawable_unselected);
                    //rv_changePassword.setBackgroundResource(R.drawable.drawable_unselected);
                    rl_refresh.setVisibility(View.VISIBLE);

                    rl_play_pause.setVisibility(View.VISIBLE);
                    pageIndicatorView.setVisibility(View.GONE);
                    tv_title.setText("Chat");

                    //loadUrl ="https://www.shadypinesradio.com/chat-app";
                    loadUrl = "https://www6.cbox.ws/box/?boxid=848135&boxtag=S3UNz0";
                    // loadWebView();

                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void init() {
        commonFragment = new CommonFragment();
        Transformation<Bitmap> circleCrop = new CircleCrop();
        textView_count_notification = (TextView) findViewById(R.id.text_count_notification);
        gifImageView = findViewById(R.id.gif_image);
        backgroundIv = findViewById(R.id.backgroundIv);
        top_bar = findViewById(R.id.top_bar);
        pageIndicatorView = findViewById(R.id.pageIndicatorView);
        containerLayout = findViewById(R.id.container);
        constraintLayout = findViewById(R.id.constraint_free_play);
//        constraintLayout.setVisibility(View.GONE);
        //tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        rl_refresh = findViewById(R.id.rl_refresh);
        iv_play_pause = findViewById(R.id.iv_play_pause);
        ll_review = findViewById(R.id.ll_review);
        rl_play_pause = findViewById(R.id.rl_play_pause);
        rl_loading = findViewById(R.id.rl_loading);
        ll_instagram = findViewById(R.id.ll_instagram);
        ll_youtube = findViewById(R.id.ll_youtube);
        ll_facebook = findViewById(R.id.ll_facebook);

        ll_donate = findViewById(R.id.ll_donate);
        ll_shady = findViewById(R.id.ll_shady);
        ll_buy = findViewById(R.id.ll_buy);
        ll_become = findViewById(R.id.ll_become);
        ll_chat = findViewById(R.id.ll_chat);


        ll_event = findViewById(R.id.tv_events);
        tv_contact = findViewById(R.id.tv_contact);
        tv_title = findViewById(R.id.tv_title);
        tv_sendsong = findViewById(R.id.tv_sendsong);
        ll_who = findViewById(R.id.ll_who);
        tv_shady = findViewById(R.id.tv_shady);
        sprshow = findViewById(R.id.sprshow);
        ll_my_account = findViewById(R.id.my_account);
        iv_logo = findViewById(R.id.iv_logo);
        progrees = findViewById(R.id.progrees);
        rl_play_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NotificationValue();
            }
        });


        Glide.with(TempActivity.this)
                .load(R.drawable.ic_dummy)
                .apply(new RequestOptions().circleCrop()).into(iv_logo);

        playInService();
        listener();
        presenter = new HomePresenterImpl(this);

        initViewPager();

        getSchedulePresenter.pollForSchedule(TimeUtils.Companion.getCurrentDayName());

        updateUI();

   /*     if (isFirstTimeInit) {
            // This will run only on the first call to init()
            new Handler().postDelayed(() -> updateUI(),2000);
            isFirstTimeInit = false;  // set the flag to false after the first run
        } else {
            new Handler().postDelayed(() -> updateUI(), 2500);
        }
*/
        mDrawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
            }

            @Override
            public void onDrawerOpened(View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {
                /**
                 * todo: its really important part here..
                 */
//                HomeFragment.btn_arrow.setVisibility(View.GONE);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                //Set your new fragment here
                if (homeScreen) {
                    containerLayout.setVisibility(View.GONE);

                } else {
                    //todo: must essential.
                    loadWebView();
                }
            }
        });
        ads_presenter.Get_Ads();
    }


    public void getSchedule(String day) {
        getSchedulePresenter.getScheduleRx(day);
    }


    private void playInService() {
        Intent intent1 = new Intent(TempActivity.this, NewPlayService.class);
        bindService(intent1, connection, BIND_AUTO_CREATE);
        // Start the service without using startForegroundService
        startService(new Intent(TempActivity.this, NewPlayService.class).setAction(Intent.ACTION_DEFAULT));
    }


    private void updateUI() {
        new Handler().postDelayed(() -> rl_loading.setVisibility(View.GONE), 3000);
        top_bar.setVisibility(View.VISIBLE);
        homeScreen = true;
        presenter.onRadioValue();
    }


    ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d("newPlayerTag=", "onServiceConnected");
            if (service instanceof NewPlayService.LocalBinder) {
                serviceBinder = ((NewPlayService.LocalBinder) service).getInstance();
                playerServiceBinder = serviceBinder.getPlayerHolder();
                playerServiceBinder.getAudioFocusPlayer().addListener(new Player.Listener() {
                    @Override
                    public void onIsPlayingChanged(boolean isPlaying) {
                        if (isPlaying) {
                            if (homeFragment.iv_play != null) {
                                homeFragment.iv_play.setImageResource(R.drawable.pause12);
                            }
                            iv_play_pause.setImageResource(R.drawable.ic_pause_white);
                        } else {
                            if (homeFragment.iv_play != null) {
                                homeFragment.iv_play.setImageResource(R.drawable.play12);
                            }
                            iv_play_pause.setImageResource(R.drawable.ic_play_white);
                        }
                    }
                });
                if (isFirstTImeApp) {
                    isFirstTImeApp = false;
                    playerServiceBinder.start();
                }
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d("newPlayerTag=", "onServiceDisconnected");

        }
    };

    public void onrefersh() {
        playerServiceBinder.start();
//        playInService();
        presenter.onRadioValue();
        getSchedule(TimeUtils.Companion.getCurrentDayName());
        reloadMedia();
    }

    private void reloadMedia() {
        if (playerServiceBinder.getAudioFocusPlayer().isPlaying()) {
            playerServiceBinder.getAudioFocusPlayer().pause();
            if (homeFragment != null) {
                homeFragment.iv_play.setImageResource(R.drawable.ic_play);
            }
            iv_play_pause.setImageResource(R.drawable.ic_play_white);
        }
        playerServiceBinder.getAudioFocusPlayer().pause();
        playerServiceBinder.getAudioFocusPlayer().play();
        if (homeFragment != null) {
            homeFragment.iv_play.setImageResource(R.drawable.ic_pause);
        }
        iv_play_pause.setImageResource(R.drawable.ic_pause_white);
    }

    public void playMedia() {
        if (!playerServiceBinder.getAudioFocusPlayer().isPlaying()) {
            playerServiceBinder.getAudioFocusPlayer().play();
        }
    }

    public void NotificationValue() {
        if (playerServiceBinder.getAudioFocusPlayer().isPlaying()) {
            playerServiceBinder.getAudioFocusPlayer().pause();
            homeFragment.iv_play.setImageResource(R.drawable.ic_play);
            iv_play_pause.setImageResource(R.drawable.ic_play_white);
        } else {
            if (!playerServiceBinder.getAudioFocusPlayer().isPlaying()) {
                playerServiceBinder.start();
            }
            homeFragment.iv_play.setImageResource(R.drawable.ic_pause);
            iv_play_pause.setImageResource(R.drawable.ic_pause_white);
        }
    }

    private void listener() {
        rl_menu.setOnClickListener(this);
        rl_refresh.setOnClickListener(this);
        rl_loading.setOnClickListener(this);
        ll_instagram.setOnClickListener(this);
        ll_youtube.setOnClickListener(this);
        ll_facebook.setOnClickListener(this);
        ll_shady.setOnClickListener(this);
        ll_buy.setOnClickListener(this);
        ll_become.setOnClickListener(this);
        ll_chat.setOnClickListener(this);
        tv_contact.setOnClickListener(this);
        sprshow.setOnClickListener(this);
        tv_sendsong.setOnClickListener(this);
        ll_who.setOnClickListener(this);
        tv_shady.setOnClickListener(this);
        ll_donate.setOnClickListener(this);
        ll_review.setOnClickListener(this);
        ll_event.setOnClickListener(this);
        ll_my_account.setOnClickListener(this);
    }


    private void Count_pushNotification() {


        pushTitle_count = 0;
        int badgeCount = 1;
        //for 1.1.4+

        String url = "https://shadypinesradio.herokuapp.com/api/my-shows/";

        String email = SharedPrefUtils.INSTANCE.readString("email");

        StringRequest stringRequest1 = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    // Toast.makeText(HomeActivity.this, "dd:"+response, Toast.LENGTH_SHORT).show();

                    JSONObject jsonObMain = new JSONObject(response);

                    message = jsonObMain.getString("message");

                    if (message.contains("Date get Successful.")) {

                        JSONArray jsonArray = jsonObMain.getJSONArray("data");

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String pushTitle = jsonObject.getString("pushTitle");
                            Log.d("pushtitletest", pushTitle);
                            if (pushTitle.isEmpty()) {
                                Log.d("pushtitletestok", pushTitle);
                                pushTitle_count++;
                            }
                        }
                        textView_count_notification.setText("" + pushTitle_count);
                        ShortcutBadger.applyCount(TempActivity.this, pushTitle_count);
                        // Toast.makeText(HomeActivity.this, message, Toast.LENGTH_SHORT).show();
                    } else {

                        textView_count_notification.setText("" + 0);
                        Toast.makeText(TempActivity.this, message, Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                /// Toast.makeText(Login_Activity.this, "error:"+error.getMessage(), Toast.LENGTH_LONG).show();
                netWorkError(error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> hashMap = new HashMap<String, String>();
                hashMap.put("email", email);
                return hashMap;
            }
        };
        ;

        RequestQueue requestQueue = Volley.newRequestQueue(TempActivity.this);
        requestQueue.add(stringRequest1);

    }

    private void loadWebView() {
       /* Fragment fr=getSupportFragmentManager().findFragmentById(R.id.container);
        String fragmentName = fr.getClass().getSimpleName();*/
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                commonFragment = new CommonFragment();
                changeFragment(commonFragment);
            }
        }, 200);


    }

    private void changeFragment(Fragment fragment) {
        if (homeScreen) {
            rl_play_pause.setVisibility(View.GONE);
        } else {
            rl_play_pause.setVisibility(View.VISIBLE);
        }
        if (fragment instanceof CommonFragment) {
            //tabLayout.setVisibility(View.GONE);
            containerLayout.setVisibility(View.VISIBLE);
        } else {
            // tabLayout.setVisibility(View.VISIBLE);
            containerLayout.setVisibility(View.GONE);
            //containerLayout.setVisibility(View.VISIBLE);
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.container, fragment)
                .commitAllowingStateLoss();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            /**
             * for navigation drawer click listener..
             */

            case R.id.ll_chat:
                mDrawerLayout.closeDrawer(GravityCompat.START);
                ll_chat.setBackgroundResource(R.drawable.drawable_selected);
                ll_my_account.setBackgroundResource(R.drawable.drawable_unselected);
                ll_event.setBackgroundResource(R.drawable.drawable_unselected);
                ll_review.setBackgroundResource(R.drawable.drawable_unselected);
                ll_instagram.setBackgroundResource(R.drawable.drawable_unselected);
                ll_youtube.setBackgroundResource(R.drawable.drawable_unselected);
                ll_facebook.setBackgroundResource(R.drawable.drawable_unselected);
                ll_shady.setBackgroundResource(R.drawable.drawable_unselected);
                sprshow.setBackgroundResource(R.drawable.drawable_unselected);
                ll_buy.setBackgroundResource(R.drawable.drawable_unselected);
                ll_become.setBackgroundResource(R.drawable.drawable_unselected);
                tv_contact.setBackgroundResource(R.drawable.drawable_unselected);
                tv_sendsong.setBackgroundResource(R.drawable.drawable_unselected);
                ll_who.setBackgroundResource(R.drawable.drawable_unselected);
                tv_shady.setBackgroundResource(R.drawable.drawable_unselected);
                ll_donate.setBackgroundResource(R.drawable.drawable_unselected);


                rl_refresh.setVisibility(View.VISIBLE);
                tv_title.setText("Chat");
                viewPager.setCurrentItem(2);

                break;


            case R.id.ll_review:
                //15
                ll_review.setBackgroundResource(R.drawable.drawable_selected);
                ll_my_account.setBackgroundResource(R.drawable.drawable_unselected);
                ll_donate.setBackgroundResource(R.drawable.drawable_unselected);
                ll_event.setBackgroundResource(R.drawable.drawable_unselected);
                ll_instagram.setBackgroundResource(R.drawable.drawable_unselected);
                ll_youtube.setBackgroundResource(R.drawable.drawable_unselected);
                ll_facebook.setBackgroundResource(R.drawable.drawable_unselected);
                ll_shady.setBackgroundResource(R.drawable.drawable_unselected);
                sprshow.setBackgroundResource(R.drawable.drawable_unselected);
                ll_buy.setBackgroundResource(R.drawable.drawable_unselected);
                ll_become.setBackgroundResource(R.drawable.drawable_unselected);
                ll_chat.setBackgroundResource(R.drawable.drawable_unselected);
                tv_contact.setBackgroundResource(R.drawable.drawable_unselected);
                tv_sendsong.setBackgroundResource(R.drawable.drawable_unselected);
                ll_who.setBackgroundResource(R.drawable.drawable_unselected);
                tv_shady.setBackgroundResource(R.drawable.drawable_unselected);

                mDrawerLayout.closeDrawer(GravityCompat.START);
                rl_refresh.setVisibility(View.VISIBLE);
                //open play store
                final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
                break;


            case R.id.ll_donate:
                homeScreen = false;
                mDrawerLayout.closeDrawer(GravityCompat.START);

                ll_donate.setBackgroundResource(R.drawable.drawable_selected);
                ll_my_account.setBackgroundResource(R.drawable.drawable_unselected);
                constraint_free_play = view.findViewById(R.id.constraint_free_play);
                ll_event.setBackgroundResource(R.drawable.drawable_unselected);
                ll_review.setBackgroundResource(R.drawable.drawable_unselected);
                ll_instagram.setBackgroundResource(R.drawable.drawable_unselected);
                ll_youtube.setBackgroundResource(R.drawable.drawable_unselected);
                ll_facebook.setBackgroundResource(R.drawable.drawable_unselected);
                ll_shady.setBackgroundResource(R.drawable.drawable_unselected);
                sprshow.setBackgroundResource(R.drawable.drawable_unselected);
                ll_buy.setBackgroundResource(R.drawable.drawable_unselected);
                ll_become.setBackgroundResource(R.drawable.drawable_unselected);
                ll_chat.setBackgroundResource(R.drawable.drawable_unselected);
                tv_contact.setBackgroundResource(R.drawable.drawable_unselected);
                tv_sendsong.setBackgroundResource(R.drawable.drawable_unselected);
                ll_who.setBackgroundResource(R.drawable.drawable_unselected);
                tv_shady.setBackgroundResource(R.drawable.drawable_unselected);


                loadUrl = "https://shadypinesradio.com/donate-app";
                tv_title.setText("Donate");
                rl_refresh.setVisibility(View.VISIBLE);
                break;

            case R.id.rl_menu:
                try {
                    homeFragment.pullToRefresh.setRefreshing(false);
                } catch (Exception e) {
                    e.printStackTrace();
                }


                try {
                    View view1 = TempActivity.this.getCurrentFocus();
                    //If no view currently has focus, create a new one, just so we can grab a window token from it
                    if (view1 == null) {
                        view1 = new View(TempActivity.this);
                    }
                    InputMethodManager imm =
                            (InputMethodManager) TempActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view1.getWindowToken(), 0);
                } catch (Exception e) {

                }

                mDrawerLayout.openDrawer(GravityCompat.START);
                break;

            case R.id.ll_instagram:
                homeScreen = false;
                mDrawerLayout.closeDrawer(GravityCompat.START);

                ll_instagram.setBackgroundResource(R.drawable.drawable_selected);
                ll_my_account.setBackgroundResource(R.drawable.drawable_unselected);
                ll_event.setBackgroundResource(R.drawable.drawable_unselected);
                ll_review.setBackgroundResource(R.drawable.drawable_unselected);
                ll_donate.setBackgroundResource(R.drawable.drawable_unselected);
                ll_youtube.setBackgroundResource(R.drawable.drawable_unselected);
                ll_facebook.setBackgroundResource(R.drawable.drawable_unselected);
                ll_shady.setBackgroundResource(R.drawable.drawable_unselected);
                sprshow.setBackgroundResource(R.drawable.drawable_unselected);
                ll_buy.setBackgroundResource(R.drawable.drawable_unselected);
                ll_become.setBackgroundResource(R.drawable.drawable_unselected);
                ll_chat.setBackgroundResource(R.drawable.drawable_unselected);
                tv_contact.setBackgroundResource(R.drawable.drawable_unselected);
                tv_sendsong.setBackgroundResource(R.drawable.drawable_unselected);
                ll_who.setBackgroundResource(R.drawable.drawable_unselected);
                tv_shady.setBackgroundResource(R.drawable.drawable_unselected);

                loadUrl = "https://www.instagram.com/shadypinesradio/";
                tv_title.setText("Instagram");
                rl_refresh.setVisibility(View.VISIBLE);

                break;


            case R.id.ll_youtube:
                homeScreen = false;
                ll_youtube.setBackgroundResource(R.drawable.drawable_selected);
                ll_my_account.setBackgroundResource(R.drawable.drawable_unselected);
                ll_review.setBackgroundResource(R.drawable.drawable_unselected);
                ll_event.setBackgroundResource(R.drawable.drawable_unselected);
                ll_donate.setBackgroundResource(R.drawable.drawable_unselected);
                ll_instagram.setBackgroundResource(R.drawable.drawable_unselected);
                ll_facebook.setBackgroundResource(R.drawable.drawable_unselected);
                ll_shady.setBackgroundResource(R.drawable.drawable_unselected);
                sprshow.setBackgroundResource(R.drawable.drawable_unselected);
                ll_buy.setBackgroundResource(R.drawable.drawable_unselected);
                ll_become.setBackgroundResource(R.drawable.drawable_unselected);
                ll_chat.setBackgroundResource(R.drawable.drawable_unselected);
                tv_contact.setBackgroundResource(R.drawable.drawable_unselected);
                tv_sendsong.setBackgroundResource(R.drawable.drawable_unselected);
                ll_who.setBackgroundResource(R.drawable.drawable_unselected);
                tv_shady.setBackgroundResource(R.drawable.drawable_unselected);

                rl_refresh.setVisibility(View.GONE);
                mDrawerLayout.closeDrawer(GravityCompat.START);
                loadUrl = "http://m.youtube.com/shadypinesmedia";
                tv_title.setText("YouTube");
                rl_refresh.setVisibility(View.VISIBLE);

                break;

            case R.id.ll_facebook:
                homeScreen = false;
                ll_facebook.setBackgroundResource(R.drawable.drawable_selected);
                ll_my_account.setBackgroundResource(R.drawable.drawable_unselected);
                ll_review.setBackgroundResource(R.drawable.drawable_unselected);
                ll_event.setBackgroundResource(R.drawable.drawable_unselected);
                ll_instagram.setBackgroundResource(R.drawable.drawable_unselected);
                ll_youtube.setBackgroundResource(R.drawable.drawable_unselected);
                ll_shady.setBackgroundResource(R.drawable.drawable_unselected);
                sprshow.setBackgroundResource(R.drawable.drawable_unselected);
                ll_buy.setBackgroundResource(R.drawable.drawable_unselected);
                ll_become.setBackgroundResource(R.drawable.drawable_unselected);
                ll_chat.setBackgroundResource(R.drawable.drawable_unselected);
                tv_contact.setBackgroundResource(R.drawable.drawable_unselected);
                tv_sendsong.setBackgroundResource(R.drawable.drawable_unselected);
                ll_who.setBackgroundResource(R.drawable.drawable_unselected);
                tv_shady.setBackgroundResource(R.drawable.drawable_unselected);
                ll_donate.setBackgroundResource(R.drawable.drawable_unselected);


                mDrawerLayout.closeDrawer(GravityCompat.START);

                loadUrl = "https://www.facebook.com/shadypinesradio";
                tv_title.setText("Facebook");
                rl_refresh.setVisibility(View.VISIBLE);
                break;

            case R.id.ll_shady:
                homeScreen = true;
                ll_shady.setBackgroundResource(R.drawable.drawable_selected);
                ll_my_account.setBackgroundResource(R.drawable.drawable_unselected);
                ll_review.setBackgroundResource(R.drawable.drawable_unselected);
                ll_event.setBackgroundResource(R.drawable.drawable_unselected);
                ll_donate.setBackgroundResource(R.drawable.drawable_unselected);
                ll_instagram.setBackgroundResource(R.drawable.drawable_unselected);
                ll_youtube.setBackgroundResource(R.drawable.drawable_unselected);
                ll_facebook.setBackgroundResource(R.drawable.drawable_unselected);
                sprshow.setBackgroundResource(R.drawable.drawable_unselected);
                ll_buy.setBackgroundResource(R.drawable.drawable_unselected);
                ll_become.setBackgroundResource(R.drawable.drawable_unselected);
                ll_chat.setBackgroundResource(R.drawable.drawable_unselected);
                tv_contact.setBackgroundResource(R.drawable.drawable_unselected);
                tv_sendsong.setBackgroundResource(R.drawable.drawable_unselected);
                ll_who.setBackgroundResource(R.drawable.drawable_unselected);
                tv_shady.setBackgroundResource(R.drawable.drawable_unselected);
                mDrawerLayout.closeDrawer(GravityCompat.START);
                tv_title.setText("Shady Pines Radio");
                if (homeScreen) {
                    rl_play_pause.setVisibility(View.GONE);
                } else {
                    rl_play_pause.setVisibility(View.VISIBLE);
                }
                mDrawerLayout.closeDrawer(GravityCompat.START);
                viewPager.setCurrentItem(0);
                break;

            case R.id.sprshow:
                homeScreen = true;
                mDrawerLayout.closeDrawer(GravityCompat.START);
                ll_my_account.setBackgroundResource(R.drawable.drawable_unselected);
                ll_review.setBackgroundResource(R.drawable.drawable_unselected);
                ll_donate.setBackgroundResource(R.drawable.drawable_unselected);
                ll_instagram.setBackgroundResource(R.drawable.drawable_unselected);
                ll_event.setBackgroundResource(R.drawable.drawable_unselected);
                ll_youtube.setBackgroundResource(R.drawable.drawable_unselected);
                ll_facebook.setBackgroundResource(R.drawable.drawable_unselected);
                ll_shady.setBackgroundResource(R.drawable.drawable_unselected);
                sprshow.setBackgroundResource(R.drawable.drawable_selected);
                ll_buy.setBackgroundResource(R.drawable.drawable_unselected);
                ll_become.setBackgroundResource(R.drawable.drawable_unselected);
                ll_chat.setBackgroundResource(R.drawable.drawable_unselected);
                tv_contact.setBackgroundResource(R.drawable.drawable_unselected);
                tv_sendsong.setBackgroundResource(R.drawable.drawable_unselected);
                ll_who.setBackgroundResource(R.drawable.drawable_unselected);
                tv_shady.setBackgroundResource(R.drawable.drawable_unselected);

                rl_refresh.setVisibility(View.VISIBLE);
                tv_title.setText("SPR Show Schedule");

                viewPager.setCurrentItem(1);

                break;


            case R.id.my_account:
                homeScreen = true;

                try {
                    mDrawerLayout.closeDrawer(GravityCompat.START);
                    if (SharedPrefUtils.INSTANCE.readString(Constants.Preferences.EMAIL) == null) {
                        ll_my_account.setBackgroundResource(R.drawable.drawable_selected);
                        ll_review.setBackgroundResource(R.drawable.drawable_unselected);
                        ll_donate.setBackgroundResource(R.drawable.drawable_unselected);
                        ll_instagram.setBackgroundResource(R.drawable.drawable_unselected);
                        ll_event.setBackgroundResource(R.drawable.drawable_unselected);
                        ll_youtube.setBackgroundResource(R.drawable.drawable_unselected);
                        ll_facebook.setBackgroundResource(R.drawable.drawable_unselected);
                        ll_shady.setBackgroundResource(R.drawable.drawable_unselected);
                        sprshow.setBackgroundResource(R.drawable.drawable_unselected);
                        ll_buy.setBackgroundResource(R.drawable.drawable_unselected);
                        ll_become.setBackgroundResource(R.drawable.drawable_unselected);
                        ll_chat.setBackgroundResource(R.drawable.drawable_unselected);
                        tv_contact.setBackgroundResource(R.drawable.drawable_unselected);
                        tv_sendsong.setBackgroundResource(R.drawable.drawable_unselected);
                        ll_who.setBackgroundResource(R.drawable.drawable_unselected);
                        tv_shady.setBackgroundResource(R.drawable.drawable_unselected);
                        rl_refresh.setVisibility(View.VISIBLE);


                        dialog = new Dialog(this);
                        dialog.setContentView(R.layout.custom_dialog_sign_in);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.custom_dialog_background));
                        }
                        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        dialog.setCancelable(false); //Optional

                        Button signInDialog = dialog.findViewById(R.id.btn_okay);
                        Button cancelDialog = dialog.findViewById(R.id.btn_cancel);

                        signInDialog.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startActivity(new Intent(getApplicationContext(), SocialActivity.class));
                                dialog.dismiss();
                            }
                        });

                        cancelDialog.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });

                        dialog.show();
                    } else {

                        ll_my_account.setBackgroundResource(R.drawable.drawable_selected);
                        ll_review.setBackgroundResource(R.drawable.drawable_unselected);
                        ll_donate.setBackgroundResource(R.drawable.drawable_unselected);
                        ll_instagram.setBackgroundResource(R.drawable.drawable_unselected);
                        ll_event.setBackgroundResource(R.drawable.drawable_unselected);
                        ll_youtube.setBackgroundResource(R.drawable.drawable_unselected);
                        ll_facebook.setBackgroundResource(R.drawable.drawable_unselected);
                        ll_shady.setBackgroundResource(R.drawable.drawable_unselected);
                        sprshow.setBackgroundResource(R.drawable.drawable_unselected);
                        ll_buy.setBackgroundResource(R.drawable.drawable_unselected);
                        ll_become.setBackgroundResource(R.drawable.drawable_unselected);
                        ll_chat.setBackgroundResource(R.drawable.drawable_unselected);
                        tv_contact.setBackgroundResource(R.drawable.drawable_unselected);
                        tv_sendsong.setBackgroundResource(R.drawable.drawable_unselected);
                        ll_who.setBackgroundResource(R.drawable.drawable_unselected);
                        tv_shady.setBackgroundResource(R.drawable.drawable_unselected);
                        rl_refresh.setVisibility(View.VISIBLE);


                        dialog = new Dialog(this);

                        dialog.setContentView(R.layout.account_alert);
                        try {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.custom_dialog_background));
                            }
                        } catch (Exception dk) {
                            Log.d("d", dk.toString());
                        }
                        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        dialog.setCancelable(false); //Optional

                        TextView signOut = dialog.findViewById(R.id.dialog_btn_signOut);
                        TextView btnCancell = dialog.findViewById(R.id.dialog_btn_cancel);
                        TextView setting = dialog.findViewById(R.id.text_setting);
                        TextView deshbord = dialog.findViewById(R.id.text_dashbord);

                        int Status = 0;
                        try {
                            Status = SharedPrefUtils.INSTANCE.readInt("status");
                        } catch (Exception e) {
                            Log.d("d", e.toString());
                        }

                        if (Status == 2) {

                            deshbord.setVisibility(View.VISIBLE);

                        } else {

                            deshbord.setVisibility(View.GONE);
                        }


                        deshbord.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                Intent intent = new Intent(TempActivity.this, Deshbord_MainActivity.class);
                                startActivity(intent);

                            }
                        });

                        setting.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                Intent intent = new Intent(TempActivity.this, Setting_Activity.class);
                                startActivity(intent);

                            }
                        });

//                        setting.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//
//                                Intent intent=new Intent(TempActivity.this,Setting_Activity.class);
//                                startActivity(intent);
//
//                            }
//                        });


                        signOut.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                viewModel.userLoggedOut();

                                // homeFragment.updateSubscribeButtonUI();

                                SharedPrefUtils.INSTANCE.delete(Constants.Preferences.EMAIL);
                                Toast.makeText(getApplicationContext(), "Signed out successfully!", Toast.LENGTH_LONG).show();
                                dialog.dismiss();
                                SharedPrefUtils.INSTANCE.write("isEmail", false);
                                SharedPrefUtils.INSTANCE.write("status", 0);
                                SharedPrefUtils.INSTANCE.write("isAdmin", false);
                                textView_count_notification.setVisibility(View.GONE);
                                new Timer().schedule(new TimerTask() {
                                    @Override
                                    public void run() {
                                        runOnUiThread(() -> {
                                            Constants.Default.Companion.setCAME_FROM_LOGIN_Schedule(true);
                                        });

                                    }
                                }, 3000);

                            }
                        });

                        btnCancell.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });

                        dialog.show();
                    }
                } catch (Exception e) {
                    try {
                        ll_my_account.setBackgroundResource(R.drawable.drawable_selected);
                        ll_review.setBackgroundResource(R.drawable.drawable_unselected);
                        ll_donate.setBackgroundResource(R.drawable.drawable_unselected);
                        ll_instagram.setBackgroundResource(R.drawable.drawable_unselected);
                        ll_event.setBackgroundResource(R.drawable.drawable_unselected);
                        ll_youtube.setBackgroundResource(R.drawable.drawable_unselected);
                        ll_facebook.setBackgroundResource(R.drawable.drawable_unselected);
                        ll_shady.setBackgroundResource(R.drawable.drawable_unselected);
                        sprshow.setBackgroundResource(R.drawable.drawable_unselected);
                        ll_buy.setBackgroundResource(R.drawable.drawable_unselected);
                        ll_become.setBackgroundResource(R.drawable.drawable_unselected);
                        ll_chat.setBackgroundResource(R.drawable.drawable_unselected);
                        tv_contact.setBackgroundResource(R.drawable.drawable_unselected);
                        tv_sendsong.setBackgroundResource(R.drawable.drawable_unselected);
                        ll_who.setBackgroundResource(R.drawable.drawable_unselected);
                        tv_shady.setBackgroundResource(R.drawable.drawable_unselected);
                        rl_refresh.setVisibility(View.VISIBLE);
                    } catch (Exception d) {
                        Log.d("d", d.toString());
                    }


                    dialog = new Dialog(this);
                    dialog.setContentView(R.layout.custom_dialog_sign_in);
                    try {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.custom_dialog_background));
                        }
                    } catch (Exception l) {
                        Log.d("d", l.toString());
                    }
                    dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    dialog.setCancelable(false); //Optional

                    Button signInDialog = dialog.findViewById(R.id.btn_okay);
                    Button cancelDialog = dialog.findViewById(R.id.btn_cancel);

                    signInDialog.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(getApplicationContext(), SocialActivity.class));
                            dialog.dismiss();
                        }
                    });

                    cancelDialog.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //Toast.makeText(HomeActivity.this, "cancelDialog", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    });
                    try {
                        dialog.show();
                    } catch (Exception di) {
                        Log.d("d", di.toString());
                    }
                }

                break;

            case R.id.ll_buy:
                homeScreen = false;
                mDrawerLayout.closeDrawer(GravityCompat.START);
                ll_my_account.setBackgroundResource(R.drawable.drawable_unselected);
                ll_review.setBackgroundResource(R.drawable.drawable_unselected);
                ll_donate.setBackgroundResource(R.drawable.drawable_unselected);
                ll_event.setBackgroundResource(R.drawable.drawable_unselected);
                ll_instagram.setBackgroundResource(R.drawable.drawable_unselected);
                ll_youtube.setBackgroundResource(R.drawable.drawable_unselected);
                ll_facebook.setBackgroundResource(R.drawable.drawable_unselected);
                ll_shady.setBackgroundResource(R.drawable.drawable_unselected);
                sprshow.setBackgroundResource(R.drawable.drawable_unselected);
                ll_buy.setBackgroundResource(R.drawable.drawable_selected);
                ll_become.setBackgroundResource(R.drawable.drawable_unselected);
                ll_chat.setBackgroundResource(R.drawable.drawable_unselected);
                tv_contact.setBackgroundResource(R.drawable.drawable_unselected);
                tv_sendsong.setBackgroundResource(R.drawable.drawable_unselected);
                ll_who.setBackgroundResource(R.drawable.drawable_unselected);
                tv_shady.setBackgroundResource(R.drawable.drawable_unselected);
                /*todo: profile..*/
                //ll_profile.setBackgroundResource(R.drawable.drawable_unselected);
                // rv_changePassword.setBackgroundResource(R.drawable.drawable_unselected);

                rl_refresh.setVisibility(View.VISIBLE);
                tv_title.setText("Merch");

                loadUrl = "https://www.shadypinesradio.com/merch-app";
//                loadWebView();
                break;

            case R.id.ll_become:
                homeScreen = false;
                mDrawerLayout.closeDrawer(GravityCompat.START);
                ll_become.setBackgroundResource(R.drawable.drawable_selected);
                ll_my_account.setBackgroundResource(R.drawable.drawable_unselected);
                ll_event.setBackgroundResource(R.drawable.drawable_unselected);
                ll_review.setBackgroundResource(R.drawable.drawable_unselected);
                ll_donate.setBackgroundResource(R.drawable.drawable_unselected);
                ll_instagram.setBackgroundResource(R.drawable.drawable_unselected);
                ll_youtube.setBackgroundResource(R.drawable.drawable_unselected);
                ll_facebook.setBackgroundResource(R.drawable.drawable_unselected);
                ll_shady.setBackgroundResource(R.drawable.drawable_unselected);
                sprshow.setBackgroundResource(R.drawable.drawable_unselected);
                ll_buy.setBackgroundResource(R.drawable.drawable_unselected);
                ll_chat.setBackgroundResource(R.drawable.drawable_unselected);
                tv_contact.setBackgroundResource(R.drawable.drawable_unselected);
                tv_sendsong.setBackgroundResource(R.drawable.drawable_unselected);
                ll_who.setBackgroundResource(R.drawable.drawable_unselected);
                tv_shady.setBackgroundResource(R.drawable.drawable_unselected);
                /*todo: profile..*/
                //ll_profile.setBackgroundResource(R.drawable.drawable_unselected);
                //rv_changePassword.setBackgroundResource(R.drawable.drawable_unselected);


                rl_refresh.setVisibility(View.VISIBLE);
                tv_title.setText("Become a Patron");

                loadUrl = "https://www.patreon.com/shadypinesradio";

//                loadWebView();
                break;


            /* todo: doing the code*/


            case R.id.tv_events:
                homeScreen = false;
                mDrawerLayout.closeDrawer(GravityCompat.START);
                ll_event.setBackgroundResource(R.drawable.drawable_selected);
                ll_my_account.setBackgroundResource(R.drawable.drawable_unselected);
                ll_chat.setBackgroundResource(R.drawable.drawable_unselected);
                ll_review.setBackgroundResource(R.drawable.drawable_unselected);
                ll_instagram.setBackgroundResource(R.drawable.drawable_unselected);
                ll_youtube.setBackgroundResource(R.drawable.drawable_unselected);
                ll_facebook.setBackgroundResource(R.drawable.drawable_unselected);
                ll_shady.setBackgroundResource(R.drawable.drawable_unselected);
                sprshow.setBackgroundResource(R.drawable.drawable_unselected);
                ll_buy.setBackgroundResource(R.drawable.drawable_unselected);
                ll_become.setBackgroundResource(R.drawable.drawable_unselected);
                tv_contact.setBackgroundResource(R.drawable.drawable_unselected);
                tv_sendsong.setBackgroundResource(R.drawable.drawable_unselected);
                ll_who.setBackgroundResource(R.drawable.drawable_unselected);
                tv_shady.setBackgroundResource(R.drawable.drawable_unselected);
                ll_donate.setBackgroundResource(R.drawable.drawable_unselected);

                /*todo: profile..*/
                //ll_profile.setBackgroundResource(R.drawable.drawable_unselected);
                // rv_changePassword.setBackgroundResource(R.drawable.drawable_unselected);


                rl_refresh.setVisibility(View.VISIBLE);
                tv_title.setText("Events");

                //loadUrl ="https://www.shadypinesradio.com/chat-app";
                loadUrl = "https://www.shadypinesradio.com/events-app";

//                loadWebView();
                break;


            case R.id.tv_contact:
                homeScreen = false;
                mDrawerLayout.closeDrawer(GravityCompat.START);
                tv_contact.setBackgroundResource(R.drawable.drawable_selected);
                ll_my_account.setBackgroundResource(R.drawable.drawable_unselected);
                ll_event.setBackgroundResource(R.drawable.drawable_unselected);
                ll_instagram.setBackgroundResource(R.drawable.drawable_unselected);
                ll_youtube.setBackgroundResource(R.drawable.drawable_unselected);
                ll_facebook.setBackgroundResource(R.drawable.drawable_unselected);
                ll_shady.setBackgroundResource(R.drawable.drawable_unselected);
                sprshow.setBackgroundResource(R.drawable.drawable_unselected);
                ll_buy.setBackgroundResource(R.drawable.drawable_unselected);
                ll_become.setBackgroundResource(R.drawable.drawable_unselected);
                ll_chat.setBackgroundResource(R.drawable.drawable_unselected);
                tv_sendsong.setBackgroundResource(R.drawable.drawable_unselected);
                ll_who.setBackgroundResource(R.drawable.drawable_unselected);
                tv_shady.setBackgroundResource(R.drawable.drawable_unselected);
                ll_donate.setBackgroundResource(R.drawable.drawable_unselected);
                ll_review.setBackgroundResource(R.drawable.drawable_unselected);
                /*todo: profile..*/
                //ll_profile.setBackgroundResource(R.drawable.drawable_unselected);
                // rv_changePassword.setBackgroundResource(R.drawable.drawable_unselected);

                rl_refresh.setVisibility(View.VISIBLE);
                tv_title.setText("Contact Us");

                loadUrl = "https://www.shadypinesradio.com/contact";

//                loadWebView();
                break;

            case R.id.tv_sendsong:
                homeScreen = false;
                mDrawerLayout.closeDrawer(GravityCompat.START);

                tv_sendsong.setBackgroundResource(R.drawable.drawable_selected);
                ll_my_account.setBackgroundResource(R.drawable.drawable_unselected);
                ll_event.setBackgroundResource(R.drawable.drawable_unselected);
                ll_review.setBackgroundResource(R.drawable.drawable_unselected);
                ll_instagram.setBackgroundResource(R.drawable.drawable_unselected);
                ll_youtube.setBackgroundResource(R.drawable.drawable_unselected);
                ll_facebook.setBackgroundResource(R.drawable.drawable_unselected);
                ll_shady.setBackgroundResource(R.drawable.drawable_unselected);
                sprshow.setBackgroundResource(R.drawable.drawable_unselected);
                ll_buy.setBackgroundResource(R.drawable.drawable_unselected);
                ll_become.setBackgroundResource(R.drawable.drawable_unselected);
                ll_chat.setBackgroundResource(R.drawable.drawable_unselected);
                tv_contact.setBackgroundResource(R.drawable.drawable_unselected);
                ll_who.setBackgroundResource(R.drawable.drawable_unselected);
                tv_shady.setBackgroundResource(R.drawable.drawable_unselected);
                ll_donate.setBackgroundResource(R.drawable.drawable_unselected);
                /*todo: profile..*/
                //ll_profile.setBackgroundResource(R.drawable.drawable_unselected);
                //rv_changePassword.setBackgroundResource(R.drawable.drawable_unselected);


                rl_refresh.setVisibility(View.VISIBLE);
                tv_title.setText("Send Us A Song");

                loadUrl = "https://www.shadypinesradio.com/sendusasong-app";
//                loadWebView();

                break;

            case R.id.ll_who:
                homeScreen = false;
                ll_who.setBackgroundResource(R.drawable.drawable_selected);
                ll_instagram.setBackgroundResource(R.drawable.drawable_unselected);
                ll_my_account.setBackgroundResource(R.drawable.drawable_unselected);
                ll_event.setBackgroundResource(R.drawable.drawable_unselected);
                ll_youtube.setBackgroundResource(R.drawable.drawable_unselected);
                ll_facebook.setBackgroundResource(R.drawable.drawable_unselected);
                ll_shady.setBackgroundResource(R.drawable.drawable_unselected);
                sprshow.setBackgroundResource(R.drawable.drawable_unselected);
                ll_buy.setBackgroundResource(R.drawable.drawable_unselected);
                ll_become.setBackgroundResource(R.drawable.drawable_unselected);
                ll_chat.setBackgroundResource(R.drawable.drawable_unselected);
                tv_contact.setBackgroundResource(R.drawable.drawable_unselected);
                tv_sendsong.setBackgroundResource(R.drawable.drawable_unselected);
                tv_shady.setBackgroundResource(R.drawable.drawable_unselected);
                ll_donate.setBackgroundResource(R.drawable.drawable_unselected);
                ll_review.setBackgroundResource(R.drawable.drawable_unselected);

                /*todo: profile..*/
                //ll_profile.setBackgroundResource(R.drawable.drawable_unselected);
                //rv_changePassword.setBackgroundResource(R.drawable.drawable_unselected);

                rl_refresh.setVisibility(View.VISIBLE);
                tv_title.setText("About Us");
                mDrawerLayout.closeDrawer(GravityCompat.START);
                loadUrl = "https://www.shadypinesradio.com/aboutus-app";
//                loadWebView();
                break;

            case R.id.tv_shady:
                mDrawerLayout.closeDrawer(GravityCompat.START);
                rl_refresh.setVisibility(View.GONE);
                homeScreen = false;

                tv_shady.setBackgroundResource(R.drawable.drawable_selected);
                ll_my_account.setBackgroundResource(R.drawable.drawable_unselected);
                ll_instagram.setBackgroundResource(R.drawable.drawable_unselected);
                ll_event.setBackgroundResource(R.drawable.drawable_unselected);
                ll_youtube.setBackgroundResource(R.drawable.drawable_unselected);
                ll_facebook.setBackgroundResource(R.drawable.drawable_unselected);
                ll_shady.setBackgroundResource(R.drawable.drawable_unselected);
                sprshow.setBackgroundResource(R.drawable.drawable_unselected);
                ll_buy.setBackgroundResource(R.drawable.drawable_unselected);
                ll_become.setBackgroundResource(R.drawable.drawable_unselected);
                ll_chat.setBackgroundResource(R.drawable.drawable_unselected);
                tv_contact.setBackgroundResource(R.drawable.drawable_unselected);
                tv_sendsong.setBackgroundResource(R.drawable.drawable_unselected);
                ll_who.setBackgroundResource(R.drawable.drawable_unselected);
                ll_donate.setBackgroundResource(R.drawable.drawable_unselected);
                ll_review.setBackgroundResource(R.drawable.drawable_unselected);
                /*todo: profile..*/
                //ll_profile.setBackgroundResource(R.drawable.drawable_unselected);
                // rv_changePassword.setBackgroundResource(R.drawable.drawable_unselected);

                tv_title.setText("Shady Pines Media");
                rl_refresh.setVisibility(View.VISIBLE);
                loadUrl = "https://www.shadypinesmedia.com";
//                loadWebView();
                break;

            case R.id.rl_refresh:

                onrefersh();

                break;

            case R.id.rl_loading:

                break;

        }
    }


    public String SHARED_PREFS = "sharedPrefs";
    private String KEY = "adsImage";

    public void saveData(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY, imageName);
        editor.apply();
    }

    public String loadData(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        String text = sharedPreferences.getString(KEY, "");
        return text;
    }

    @Override
    public void onAdsSuccess(@NonNull String image, @NonNull String adsLink, boolean isShowAds, boolean isShowAdsEverytime) {
        imageName = image;
        Log.d("localValue", loadData(this).toString());
        Log.d("localValue", String.valueOf(isShowAdsEverytime));
        Log.d("localValue", String.valueOf(isShowAds));

        if (isShowAds) {
            if (!loadData(this).toString().equals(image.toString()) || (loadData(this) == null) || isShowAdsEverytime) {
                createAdsDialog();
                saveData(this);
                Glide.with(this)
                        .load(image)
                        .transform(new CenterCrop(), new GranularRoundedCorners(45, 45, 0, 0))
                        .into(ads_image);
                final Handler handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //Do something after 100ms
                        ads_image.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                adsdialog.dismiss();
                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(adsLink));
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        });
                        adsdialog.show();
                    }
                }, 4000);

            }
        }

    }

    public void createAdsDialog() {
        dialogBuilder = new AlertDialog.Builder(this);
        final View adsPopupView = getLayoutInflater().inflate(R.layout.ads_popup, null);
        ads_image = (ImageView) adsPopupView.findViewById(R.id.ads_img);
        adsButton = (TextView) adsPopupView.findViewById(R.id.cancel_btn);
        dialogBuilder.setView(adsPopupView);
        adsdialog = dialogBuilder.create();
        adsdialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));


        adsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adsdialog.dismiss();
            }
        });
    }

    @Override
    public void onAdsError(@NonNull String message) {

    }

/*
    @Override
    public void onGetScheduleSuccess(@NonNull Schedule schedule, @NonNull String day) {
        Log.d("sksohel","sksohelworking");
        List<Day> days = TimeUtils.Companion.selectDay(day,schedule);
        int isRunning = 0;
        for (Day d:days){
            if (isShowRunning(d,day)){
                Gson gson = new Gson();
                String gsonOne = gson.toJson(d);
                isRunning = 1;
                SharedPrefUtils.INSTANCE.write("runnigKey", gsonOne);
                if (homeFragment.tv_free_play != null) {
                    homeFragment.tv_free_play.setText(d.getShows().get(0).getShowName());
                }
            }
            Log.d("iloveutest12","test case");

            System.out.print("iloveutest");
            System.out.print(d.getShows().get(0).getShowName());
        }
        if (isRunning == 0) {
            SharedPreferences mySPrefs = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = mySPrefs.edit();
            editor.remove("runnigKey");
            editor.apply();
            //tabLayout.setVisibility(View.GONE);
//            constraint_free_play.setVisibility(View.GONE);
//                    constraintLayout.setVisibility(View.INVISIBLE);


        } else {
            // tabLayout.setVisibility(View.VISIBLE);
//            constraint_free_play.setVisibility(View.VISIBLE);
            //containerLayout.setVisibility(View.VISIBLE);
        }
    }
*/

    private boolean isShowRunning(Day d, String day) {
        if (TimeUtils.Companion.getCurrentDayName().toUpperCase().equals(day)) {
            return TimeUtils.Companion.isConstrainedByTime(TimeUtils.Companion.getTimeFromTimeStamp(
                    d.getStartTimeStamp()
            ), TimeUtils.Companion.getTimeFromTimeStamp(d.getEndTimeStamp()));
        }
        return false;
    }

    @Override
    public void onRadioValue(RadioResponse radioResponse) {
        Log.e("MK_TEST", radioResponse.getCurrentTrack().toString());
        viewModel.postRadioResponseData(radioResponse);
       /* runOnUiThread(() -> {
            try {
                //Show image
                homeFragment.tv_title.setText(AppSettings.title);
                homeFragment.tv_albume.setText(AppSettings.album);
//                chatFragment.binding.textMarqueChatFm.setText(AppSettings.title + " - " + AppSettings.album + "    ");
                Glide.with(this).load(AppSettings.imageurl).into(homeFragment.iv_images);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        String currentTrackTitle = radioResponse.getCurrentTrack().getTitle();
        AppSettings.imageurl = radioResponse.getCurrentTrack().getArtworkUrlLarge();
        String value[] = currentTrackTitle.split("-");
        if (!AppSettings.album.equals(value[0])) {
            AppSettings.album = value[0];
            if (value.length > 1)
                AppSettings.title = value[1];
        } else {
            return;  // If album is same as before, no need to re-fetch the image
        }
        Glide.with(TempActivity.this)
                .asBitmap()
                .transform(new CenterCrop(), new GranularRoundedCorners(45, 45, 0, 0))
                .load(AppSettings.imageurl)
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
                        resource.compress(Bitmap.CompressFormat.JPEG, 20, stream);  // 20 is
                        // the quality, which means 80% compression
                        byte[] byteArray = stream.toByteArray();
                        bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                        if (serviceBinder != null) {
                            Log.d("newPlayerTag =", "serviceBinder");
                            serviceBinder.setNotification();
                        }
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });*/
    }


    public void fnLoadInnerPage() {
        CommonFragment commonFragmentv = new CommonFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.container, commonFragmentv)
                .commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isAppActive = false;
        if (playerServiceBinder != null) {
            playerServiceBinder.stop();
            playerServiceBinder.release();
        }
        presenter.clearHomeDisposeAble();
        getSchedulePresenter.clear();
        stopService(new Intent(TempActivity.this, NewPlayService.class));
        serviceBinder = null;
        playerServiceBinder = null;

    }

    @Override
    protected void onPause() {
        super.onPause();
        GifDrawable gifDrawable = (GifDrawable) gifImageView.getDrawable();
        if (gifDrawable != null) {
            gifDrawable.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Constants.Default.Companion.getCAME_FROM_LOGIN_Schedule()) {
            Constants.Default.Companion.setCAME_FROM_LOGIN_Schedule(false);
            getSchedule(TimeUtils.Companion.getCurrentDayName().toUpperCase(Locale.getDefault()));
        }
        // Prioritize GIF playback
        GifDrawable gifDrawable = (GifDrawable) gifImageView.getDrawable();
        if (gifDrawable != null) {
            gifDrawable.start();
        }
        // Use a handler or background thread for potentially long-running tasks
        new Handler().postDelayed(() -> {
            //  presenter.onRadioValue();
            int status = SharedPrefUtils.INSTANCE.readInt("status");
            String Emali = SharedPrefUtils.INSTANCE.readString("email");
            if (status == 2 && (!Emali.isEmpty())) {
                textView_count_notification.setVisibility(View.VISIBLE);
                Count_pushNotification();
            } else {
                textView_count_notification.setVisibility(View.GONE);
                textView_count_notification.setText("" + 0);
            }
        }, 100);  // Delay of 100ms
    }


    private void netWorkError(VolleyError error) {

        if (error instanceof NetworkError) {
            Toast.makeText(TempActivity.this, "No internet connection", Toast.LENGTH_SHORT).show();
        } else if (error instanceof ServerError) {
            Toast.makeText(TempActivity.this, "Our server is busy please try again later", Toast.LENGTH_SHORT).show();
        } else if (error instanceof AuthFailureError) {
            Toast.makeText(TempActivity.this, "AuthFailure Error please try again later", Toast.LENGTH_SHORT).show();
        } else if (error instanceof ParseError) {
            Toast.makeText(TempActivity.this, "Parse Error please try again later", Toast.LENGTH_SHORT).show();
        } else if (error instanceof NoConnectionError) {
            Toast.makeText(TempActivity.this, "No connection", Toast.LENGTH_SHORT).show();
        } else if (error instanceof TimeoutError) {
            Toast.makeText(TempActivity.this, "Server time out please try again later", Toast.LENGTH_SHORT).show();
        }
        error.printStackTrace();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_POST_NOTIFICATIONS) {
            // Permission granted
            // You can proceed with posting notifications
            // Permission denied
            // Handle the feature without notification permission
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            //  ChatFragment.Companion.getInstance().onActivityResult(requestCode, resultCode, data);
        } catch (Exception e) {

        }
    }


}