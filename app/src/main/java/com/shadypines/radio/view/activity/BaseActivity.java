package com.shadypines.radio.view.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.shadypines.radio.R;
import com.shadypines.radio.utils.ConnectivityChangeReceiver;
import com.shadypines.radio.utils.NetworkUtils;
import com.shadypines.radio.view.viewinteractors.BaseView;


/**
 * Created by root on 12/2/18.
 */

public abstract class BaseActivity extends AppCompatActivity implements BaseView {

    ConnectivityChangeReceiver receiver;
    public static ProgressDialog mProgress;
    private int Fragmentcount = 0;
    private boolean doubleBackToExitPressedOnce = false;

    boolean locationInPolygon = false;
    Double polygonLat, polygonLong;

    @Override
    public void onStart() {
        super.onStart();
        // EventBus.getDefault().register(this);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (receiver != null) {
            unregisterReceiver(receiver);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerNetworkReceiver();
    }

    @Override
    public void onStop() {
        // EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        if (mProgress != null && mProgress.isShowing()) {
            mProgress.cancel();
        }
        super.onDestroy();
    }

    private void registerNetworkReceiver() {
        if (receiver == null) {
            receiver = new ConnectivityChangeReceiver(this, new Handler());
        }
        registerReceiver(receiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }


    @Override
    public void showProgress(String msg) {
//        mProgress = DialogUtils.showLoadingDialog(this);
    }

    @Override
    public void hideProgress() {
        try {
            mProgress.cancel();
        }catch (Exception ignored){

        }

    }

    @Override
    public void onSuccess(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFailure(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private void showSnackBar(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
      //  Utils.getCustomSnackBar(getWindow().getDecorView(), message);
    }

    @Override
    public void onBackPressed() {
        Fragmentcount = getSupportFragmentManager().getBackStackEntryCount();
        if (Fragmentcount <= 1) {
            if (Fragmentcount != 0) {
                FragmentManager.BackStackEntry backEntry = getSupportFragmentManager().getBackStackEntryAt(Fragmentcount - 1);
                String tag = backEntry.getName();
                Fragment fragment = getSupportFragmentManager().findFragmentByTag(tag);
//                if (!(fragment instanceof HomeFragment)) {
//                    getSupportFragmentManager().beginTransaction().add(R.id.content_frame, new HomeFragment()).commit();
//                } else if (fragment instanceof ConfirmationFragment) {
//                    getSupportFragmentManager().beginTransaction().add(R.id.content_frame, new HomeFragment()).commit();
//                } else {
                    super.onBackPressed();
//                }
            } else {
                if (doubleBackToExitPressedOnce) {
                    super.onBackPressed();
                    return;
                }

                this.doubleBackToExitPressedOnce = true;
                Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        doubleBackToExitPressedOnce = false;
                    }
                }, 2000);
            }
        } else {
            FragmentManager.BackStackEntry backEntry = getSupportFragmentManager().getBackStackEntryAt(Fragmentcount - 1);
            String tag = backEntry.getName();
            Fragment fragment = getSupportFragmentManager().findFragmentByTag(tag);
//            if (fragment instanceof ConfirmationFragment) {
//                getSupportFragmentManager().beginTransaction().add(R.id.content_frame, new HomeFragment()).commit();
//            } else {
                getSupportFragmentManager().popBackStack();
//            }
        }
    }

    @Override
    public void onError(String message) {
        if (message != null) {
            showSnackBar(message);
        } else {
            showSnackBar(getString(R.string.error));
        }
    }

    @Override
    public void onError(@StringRes int resId) {
        onError(getString(resId));
    }

    @Override
    public void showMessage(String message) {
        if (message != null) {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, getString(R.string.error), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void showMessage(@StringRes int resId) {
        showMessage(getString(resId));
    }

    @Override
    public boolean isNetworkConnected() {
        return NetworkUtils.checkInternet(getApplicationContext());
    }


    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }

    @Override
    public void showServerError(int response) {
        switch (response) {
            case 404:
                showMessage(R.string.server_not_found);
                break;
            case 500:
                showMessage(R.string.server_broken);
                break;
            default:
                showMessage(R.string.error_unknown);
                break;
        }
    }


    @Override
    public void showToast(String msg) {
        Toast.makeText(getApplicationContext(),msg, Toast.LENGTH_SHORT).show();
    }


}
