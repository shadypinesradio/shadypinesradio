package com.shadypines.radio.view.fragment.syncSchedule

import android.util.Log
import com.shadypines.radio.SharedPrefUtils
import com.shadypines.radio.SyadApplication
import com.shadypines.radio.model.schecule.Schedule
import com.shadypines.utils.Constants
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.TimeUnit


class GetSchedulePresenter (private val viewModel: MyViewModel) {
    private val disposables = CompositeDisposable()
    fun getScheduleRx(day: String) {
        val email = SharedPrefUtils.readString(Constants.Preferences.EMAIL) ?: ""
        val disposeAble = SyadApplication.getScheduleApiClient().getScheduleRx(email)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ schedule ->
                    // onSuccess
                    //scheduleView.onGetScheduleSuccess(schedule, day)
                    if (schedule.success) {
                        viewModel.postScheduleData(Resource.success(schedule, day))
                        Log.e("GetScheduleRx", schedule.toString())
                    } else {
                        val throwable = Throwable("Can't retrieve schedule")
                        viewModel.postScheduleData(Resource.error(throwable))
                    }

                }, { throwable ->
                    // onError
                    val throwable = Throwable("Can't retrieve schedule")
                    viewModel.postScheduleData(Resource.error(throwable))            })
        disposables.add(disposeAble)
    }


    fun pollForSchedule(day: String) {
        val email = SharedPrefUtils.readString(Constants.Preferences.EMAIL) ?: ""
        val disposeAble =  Observable.interval(0, 15, TimeUnit.SECONDS) // every 5 seconds
                .flatMapSingle { SyadApplication.getScheduleApiClient().getScheduleRx(email) }
                .distinctUntilChanged()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ schedule ->
                    // onSuccess
                    if (schedule.success) {
                        viewModel.postScheduleData(Resource.success(schedule, day))
                        Log.e("PollScheduleRx", schedule.toString())
                    } else {
                        val throwable = Throwable("Can't retrieve schedule")
                        viewModel.postScheduleData(Resource.error(throwable))
                    }
                    //scheduleView.onGetScheduleSuccess(schedule, day)
                }, { throwable ->
                    // onError
                    val throwable = Throwable("Can't retrieve schedule")
                    viewModel.postScheduleData(Resource.error(throwable))
                })
        disposables.add(disposeAble)
    }

    fun getSchedule(day: String) {
        SyadApplication.getScheduleApiClient().getSchedule(
                if (SharedPrefUtils.readString(
                                Constants.Preferences.EMAIL
                        ) != null) SharedPrefUtils.readString(Constants.Preferences.EMAIL)
                else "")
                .enqueue(object : Callback<Schedule> {
                    override fun onResponse(call: Call<Schedule>, response: Response<Schedule>) {
                        if (response.isSuccessful) {
                            Log.d("sksohel","sksohel12")
                            val schedule = response.body()
                            if (schedule != null) {
                                    if (schedule.success) {
                                        viewModel.postScheduleData(Resource.success(schedule, day))
                                    } else {
                                        val throwable = Throwable("Can't retrieve schedule")
                                        viewModel.postScheduleData(Resource.error(throwable))
                                    }
                            } else {
                                val throwable = Throwable("Can't retrieve schedule")
                                viewModel.postScheduleData(Resource.error(throwable))
                            }
                        } else {
                            val throwable = Throwable("Can't retrieve schedule")
                            viewModel.postScheduleData(Resource.error(throwable))
                        }
                    }
                    override fun onFailure(call: Call<Schedule>, t: Throwable) {
                        try {
                            val throwable = Throwable("Can't retrieve schedule")
                            viewModel.postScheduleData(Resource.error(throwable))
                        }catch (_:Exception){

                        }

                    }

                })
    }



    fun clear() {
        disposables.clear()
    }

}