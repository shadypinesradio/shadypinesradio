package com.shadypines.radio.view.viewinteractors;
import androidx.annotation.StringRes;

public interface BaseView {

     void showProgress(String msg);

     void hideProgress();

    void onSuccess(String s);

    void onFailure(String msg);

    void onError(String message);

    void onError(@StringRes int resId);

    void showMessage(String message);

    void showMessage(@StringRes int resId);

    boolean isNetworkConnected();

    void showServerError(int response);

    void showToast(String msg);

}
