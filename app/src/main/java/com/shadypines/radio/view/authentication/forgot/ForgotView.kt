package com.shadypines.radio.view.authentication.forgot

/**
 * Created by Android Dev on 12-Oct-21 Oct, 2021
 */
interface ForgotView {
    fun onForgotSuccess()
    fun onForgotError(message: String)
}

interface AdsView {
    fun onAdsSuccess(image:String, adsLink: String, isShowAds: Boolean, isShowAdsEverytime: Boolean)
    fun onAdsError(message: String)
}