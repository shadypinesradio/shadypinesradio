package com.shadypines.radio.view.authentication.signup

import com.onesignal.OneSignal
import com.shadypines.radio.SyadApplication
import com.shadypines.radio.view.authentication.signup.model.SignUpResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.Callback as Callback

/**
 * Created by Android Dev on 07-Oct-21 Oct, 2021
 */
class SignUpPresenter(val signUpView: SignUpView) {

    fun signUp(
        firstName: String, lastName: String, email: String, password: String,
        confirmPassword: String, isSendEmail: String, username: String
    ) {
        SyadApplication.getScheduleApiClient().signUpUserData(
            firstName, lastName,
            email, password, confirmPassword, isSendEmail,
            OneSignal.getDeviceState()?.userId,username
        )
            .enqueue(object : Callback<SignUpResponse> {
                override fun onResponse(
                    call: Call<SignUpResponse>,
                    signUpResponse: Response<SignUpResponse>
                ) {

                    try {
                        if (signUpResponse.isSuccessful) {
                            val responseBody = signUpResponse.body()
                            if (responseBody?.isSuccess!!) {
                                signUpView.onSignUpSuccess()
                            } else {
                                signUpView.onSignUpError(responseBody.message)
                            }
                        } else {
                            signUpView.onSignUpError("Something went wrong! Try again!")
                        }
                    } catch (e: NullPointerException) {
                        signUpView.onSignUpError("Something went wrong! Try again!")
                    }
                }

                override fun onFailure(call: Call<SignUpResponse>, t: Throwable) {
                    signUpView.onSignUpError("Something went wrong! Check your internet!")
                }

            })
    }

}


