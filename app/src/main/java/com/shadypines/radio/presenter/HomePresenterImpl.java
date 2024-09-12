package com.shadypines.radio.presenter;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.shadypines.radio.SyadApplication;
import com.shadypines.radio.model.CurrentTrack;
import com.shadypines.radio.model.RadioProgramResponse;
import com.shadypines.radio.model.RadioResponse;
import com.shadypines.radio.view.viewinteractors.HomeView;
import com.shadypines.utils.ProgressDialogUtils;

import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomePresenterImpl {

    private final CompositeDisposable disposables = new CompositeDisposable();
    HomeView mView;

    public HomePresenterImpl(HomeView mView) {
        this.mView = mView;
    }
/*    public void onRadioValue() {
//        mView.showProgress("Please wait.....");
        SyadApplication.getRetroApiClient().getRadio().enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                try {
                    assert response.body() != null;
                    String res = response.body().string();
                    JSONObject jsonArray = new JSONObject(res);
                    RadioResponse obj = new Gson().fromJson(jsonArray.toString(), RadioResponse.class);
                    Log.e("RadioData", obj.toString());
                    */

    /**
     * codding attach show progress
     *//*
//                        mView.hideProgress();
                    mView.onRadioValue(obj);

                    Log.e("value",res);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mView.hideProgress();
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                mView.hideProgress();
            }
        });


    }*/
    public void onRadioValue() {
        Disposable disposable = Observable.concat(
                        Observable.timer(0, TimeUnit.SECONDS),
                        Observable.interval(5, TimeUnit.SECONDS))
                .flatMapSingle(time -> SyadApplication.getRetroApiClient().getRadio())
                .distinctUntilChanged() // Make sure to only proceed if the data has changed
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(responseBody -> {
                    // onSuccess
                    try {
                        String res = responseBody.string();
                        Log.e("RadioData =", res);
                        JSONObject jsonArray = new JSONObject(res);
                        RadioProgramResponse radioProgram = new Gson().fromJson(jsonArray.toString(), RadioProgramResponse.class);
                        CurrentTrack currentTrack = new CurrentTrack();
                        currentTrack.setStartTime("2024-01-06 12:30");
                        currentTrack.setArtworkUrl(radioProgram.getAdsLink());
                        currentTrack.setArtworkUrlLarge(radioProgram.getAdsLink());
                        currentTrack.setTitle(radioProgram.getDescription() + " - " + radioProgram.getTitle());

                        // Creating a RadioResponse object
                        RadioResponse radioResponse = new RadioResponse();
                        radioResponse.setCurrentTrack(currentTrack);
                        radioResponse.setLogoUrl(radioProgram.getAdsLink());
                        mView.onRadioValue(radioResponse);
                    } catch (Exception e) {
                        e.printStackTrace();
                        mView.hideProgress();
                    }
                }, throwable -> {
                    // onError
                    throwable.printStackTrace();
                    mView.hideProgress();
                });

        disposables.add(disposable);
    }

    public void clearHomeDisposeAble(){
        disposables.clear();
    }

}
