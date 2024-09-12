package com.shadypines.radio.view.authentication.signin

/**
 * Created by Android Dev on 04-Oct-21 Oct, 2021
 */
interface SignInView {
    fun onSignInSuccess()
    fun onSignInError(message: String)
    fun onGoogleSignInSuccess(email: String, day: String)
    fun onGoogleSignInError(message: String)


}