package com.shadypines.radio.view.fragment

import com.shadypines.radio.model.schecule.Schedule

interface ScheduleView {
    fun onSubscribeSuccess(showId: String,day: String)
    fun onSubscribeError(message:String, day: String)
    fun onSignInSuccess(email:String, day: String)
    fun onSignInError(message: String)
    fun onSignUpSuccess()
    fun onSignUPError(message: String)
    fun onResetPasswordSuccess()
    fun onResetPasswordError(message: String)

}