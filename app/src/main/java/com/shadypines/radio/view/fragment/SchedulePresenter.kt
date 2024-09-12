package com.shadypines.radio.view.fragment

import android.util.Log
import com.onesignal.OneSignal
import com.shadypines.utils.Constants
import com.shadypines.radio.SharedPrefUtils
import com.shadypines.radio.SyadApplication
import com.shadypines.radio.model.BaseResponse
import com.shadypines.radio.model.schecule.Schedule
import com.shadypines.radio.view.fragment.syncSchedule.MyViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.TimeUnit

class SchedulePresenter(val scheduleView: ScheduleView) {

    // todo: continue google sign in..
    fun socialSignIn(email: String, id: String, name: String, day: String) {
        SyadApplication.getScheduleApiClient().socialSignInt(id, email, name, OneSignal
                .getDeviceState()?.userId).enqueue(object :
                Callback<BaseResponse> {
            override fun onResponse(call: Call<BaseResponse>, response: Response<BaseResponse>) {
                if (response.isSuccessful) {
                    val res = response.body()
                    if (res != null) {
                        if (res.success) {
                            scheduleView.onSignInSuccess(email, day)
                        } else {
                            scheduleView.onSignInError(res.message)
                        }
                    } else {
                        scheduleView.onSignInError("Server error!")
                    }
                } else {
                    scheduleView.onSignInError("Server error!")
                }
            }

            override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                scheduleView.onSignInError("Something went wrong! Check your internet!")
            }

        })
    }


    /**
     * done..
     */
    fun signIn(email: String, password: String, day: String) {
        SyadApplication.getScheduleApiClient().loginUser(email, password).enqueue(object :
                Callback<BaseResponse> {
            override fun onResponse(call: Call<BaseResponse>, response: Response<BaseResponse>) {
                if (response.isSuccessful) {
                    val res = response.body()
                    if (res != null) {
                        if (res.success) {
                            scheduleView.onSignInSuccess(email, day)
                        } else {
                            scheduleView.onSignInError(res.message)
                        }
                    } else {
                        scheduleView.onSignInError("Server error!")
                    }
                } else {
                    scheduleView.onSignInError("Server error!")
                }
            }

            override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                scheduleView.onSignInError("Something went wrong! Check your internet!")
            }

        })
    }




    /**
     * done..
     */
    fun registerUser(firstName: String,  email: String, password: String,
                     confirmPassword: String, isSendEmail: Boolean) {

        SyadApplication.getScheduleApiClient().registerUser(
                firstName, email, password, confirmPassword, isSendEmail.toString()
                .capitalize(), OneSignal.getDeviceState()?.userId

        ).enqueue(object : Callback<BaseResponse> {
            override fun onResponse(call: Call<BaseResponse>, response: Response<BaseResponse>) {
                if (response.isSuccessful) {
                    val res = response.body()
                    if (res != null) {
                        if (res.success) {
                            scheduleView.onSignUpSuccess()
                        } else {
                            scheduleView.onSignUPError(res.message)
                        }
                    } else {
                        scheduleView.onSignUPError("Server error!")
                    }
                } else {
                    scheduleView.onSignUPError("Server error!")
                }
            }

            override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                scheduleView.onSignUPError("Something went wrong! Check your internet!")
            }

        })
    }

    /**
     * done..
     */
    fun resetPassword(email: String) {
        SyadApplication.getScheduleApiClient().resetPassword("https://shadypinesradio.herokuapp" +
                ".com/reset-link/", email).enqueue(object : Callback<BaseResponse> {
            override fun onResponse(call: Call<BaseResponse>, response: Response<BaseResponse>) {
                if (response.isSuccessful) {
                    val res = response.body()
                    if (res != null) {
                        if (res.success) {
                            scheduleView.onResetPasswordSuccess()
                        } else {
                            scheduleView.onResetPasswordError(res.message)
                        }
                    } else {
                        scheduleView.onResetPasswordError("Server error!")
                    }
                } else {
                    scheduleView.onResetPasswordError("Server error!")
                }
            }

            override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                scheduleView.onResetPasswordError("Something went wrong! Check your internet!")
            }
        })
    }

    fun subscribeToShow(showId: String, day: String) {
        SyadApplication.getScheduleApiClient().subscribeToShow(showId, SharedPrefUtils.readString
        (Constants.Preferences.EMAIL)).enqueue(object
            : Callback<BaseResponse> {
            override fun onResponse(call: Call<BaseResponse>, response: Response<BaseResponse>) {
                if (response.isSuccessful) {
                    val response = response.body()
                    if (response != null) {
                        if (response.isSubscribed) {

                            scheduleView.onSubscribeSuccess(showId,day)
                        } else {
                            scheduleView.onSubscribeError("Unsubscribed successfully!", day)
                        }
                    } else {
                        scheduleView.onSubscribeError("Can't subscribe now, Try again!", day)
                    }
                } else {
                    scheduleView.onSubscribeError("Can't subscribe now, Try again!", day)
                }
            }

            override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                scheduleView.onSubscribeError("Can't subscribe now, Try again!", day)
            }
        })
    }
}