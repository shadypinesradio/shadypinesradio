package com.shadypines.radio.view.authentication.forgot

import com.shadypines.radio.SyadApplication
import com.shadypines.radio.view.authentication.forgot.model.ForgotResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by Android Dev on 12-Oct-21 Oct, 2021
 */
class ForgotPresenter(val forgotView: ForgotView) {

    fun forgotResetPassword(email: String) {
        SyadApplication.getScheduleApiClient().resetPasswordData("https://shadypinesradio.herokuapp" +
                ".com/reset-link/", email).enqueue(object : Callback<ForgotResponse> {
            override fun onResponse(call: Call<ForgotResponse>, forgotResponse: Response<ForgotResponse>) {
                try {
                    if (forgotResponse.isSuccessful) {
                        val responseBody = forgotResponse.body()
                        if (responseBody != null) {
                            if (responseBody.isSuccess) {
                                forgotView.onForgotSuccess()
                            } else {
                                forgotView.onForgotError(responseBody.message)
                            }
                        } else {
                            forgotView.onForgotError("Something went wrong! Try again!")
                        }

                    } else {
                        forgotView.onForgotError("Something went wrong! Try again!")
                    }


                } catch (e: NullPointerException) {
                    forgotView.onForgotError("Something went wrong! Try again!")
                }
            }

            override fun onFailure(call: Call<ForgotResponse>, t: Throwable) {
                forgotView.onForgotError("Something went wrong! Check your internet!")
            }
        })
    }

}

