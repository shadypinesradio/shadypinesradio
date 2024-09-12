package com.shadypines.radio.view.authentication.signin

import android.provider.Settings.Global.getString
import android.provider.Settings.Secure.getString
import android.provider.Settings.System.getString
import android.util.Log
import androidx.core.content.res.TypedArrayUtils.getString
import com.google.android.gms.common.util.DataUtils
import com.onesignal.OneSignal
import com.shadypines.radio.R
import com.shadypines.radio.SharedPrefUtils
import com.shadypines.radio.SyadApplication
import com.shadypines.radio.model.BaseResponse
import com.shadypines.radio.view.authentication.signin.model.SignInResponse
import com.shadypines.utils.Constants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


/**
 * Created by Android Dev on 04-Oct-21 Oct, 2021
 */
class SignInPresenter(val signInView: SignInView) {

    fun signIn(email: String, password: String) {
       // SharedPrefUtils.write(Constants.Preferences.EMAIL, email)
        SyadApplication.getScheduleApiClient().loginUserData(email, password, OneSignal
                .getDeviceState()?.userId)
                    .enqueue(object : Callback<SignInResponse> {
                    override fun onResponse(call: Call<SignInResponse>,
                                            signInResponse: Response<SignInResponse>) {
                        try {
                            if (signInResponse.isSuccessful) {
                                val responseBody = signInResponse.body()
                                if (responseBody?.isSuccess!!) {

                                     SharedPrefUtils.write("email",responseBody.email())
                                     SharedPrefUtils.write("isAdmin",responseBody.isAdmin())
                                     SharedPrefUtils.write("status",responseBody.status())
                                    if(responseBody.username()!=null)
                                        SharedPrefUtils.write("username",responseBody.username())
                                    else
                                        SharedPrefUtils.write("username","")
                                     SharedPrefUtils.write("isEmail",true)

                                   // SharedPrefUtils.write("email",email)
                                    //SharedPrefUtils.write
                                    signInView.onSignInSuccess()
                                    /*SharedPrefUtils.write(Constants.PreferenceKeys.ACCESS_TOKEN, "Bearer " + responseBody.data.access)
                                    SharedPrefUtils.write(Constants.PreferenceKeys.IS_LOGGED_IN, true)*/
                                } else {
                                    signInView.onSignInError(responseBody.message)
                                }
                            } else {
                                signInView.onSignInError("Something went wrong! Try again!")
                            }


                        } catch (e: NullPointerException) {
                            signInView.onSignInError("Something went wrong! Try again!")
                        }

                    }

                    override fun onFailure(call: Call<SignInResponse>, t: Throwable) {
                        signInView.onSignInError("Something went wrong! Check your internet!")
                    }

                })

    }

    fun socialGoogleSignIn(email: String, id: String, name: String, day: String) {
        SyadApplication.getScheduleApiClient().getSocialGoogleSignIn(id, email, name, OneSignal
                .getDeviceState()?.userId).enqueue(object :
                Callback<SignInResponse> {
            override fun onResponse(call: Call<SignInResponse>, response: Response<SignInResponse>) {
                if (response.isSuccessful) {
                    SharedPrefUtils.write("isAdmin",false)
                    val res = response.body()
                    if (res != null) {
                        if (res?.isSuccess!!) {
                            SharedPrefUtils.write("email",email)
                            SharedPrefUtils.write("status",res.status())
                            signInView.onGoogleSignInSuccess(email, day)
                        } else {
                            Log.d("signinerror", response.message())
                            signInView.onGoogleSignInError(res.message)
                        }
                    } else {
                        signInView.onGoogleSignInError("Server error!")
                    }
                } else {
                    signInView.onGoogleSignInError("Server error!")
                }
            }

            override fun onFailure(call: Call<SignInResponse>, t: Throwable) {
                signInView.onGoogleSignInError("Something went wrong! Check your internet!")
            }

        })
    }
}