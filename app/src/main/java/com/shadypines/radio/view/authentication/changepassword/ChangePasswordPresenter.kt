package com.shadypines.radio.view.authentication.changepassword

import com.shadypines.radio.SharedPrefUtils
import com.shadypines.radio.SyadApplication
import com.shadypines.radio.view.authentication.changepassword.model.ChangePasswordResponse
import com.shadypines.utils.Constants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.NullPointerException

/**
 * Created by Android Dev on 13-Oct-21 Oct, 2021
 */
class ChangePasswordPresenter(val changePasswordView: ChangePasswordView) {

    fun changePassword(confirmPassword: String, newPassword: String) {

        SyadApplication.getScheduleApiClient()
                .changePasswordData(SharedPrefUtils.readString(Constants.Preferences.EMAIL),
                        confirmPassword, newPassword)
                .enqueue(object : Callback<ChangePasswordResponse> {
                    override fun onResponse(call: Call<ChangePasswordResponse>,
                                            changePasswordResponse: Response<ChangePasswordResponse>) {

                        try {
                            if (changePasswordResponse.isSuccessful) {
                                val responseBody = changePasswordResponse.body()
                                if (responseBody?.isSuccess!!) {
                                    changePasswordView.onChangePasswordSuccess()
                                } else {
                                    changePasswordView.onChangePasswordError("Something went wrong! Try again !")
                                }

                            } else {
                                changePasswordView.onChangePasswordError("Something went wrong! Try again !")
                            }

                        } catch (e: NullPointerException) {
                            changePasswordView.onChangePasswordError("Something wen wrong! Try again!")
                        }
                    }

                    override fun onFailure(call: Call<ChangePasswordResponse>, t: Throwable) {
                        changePasswordView.onChangePasswordError("Something went wrong! Check your internet!")
                    }

                })


    }

}