package com.shadypines.radio.view.activity

import com.shadypines.radio.SyadApplication
import com.shadypines.radio.view.authentication.forgot.AdsView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Ads_presenter(val forgotView: AdsView) {

        fun Get_Ads() {
            SyadApplication.getScheduleApiClient().adsData("https://shadypinesradio.herokuapp.com/api/ads/").enqueue(object : Callback<ads_response> {
                override fun onResponse(call: Call<ads_response>, adsResponse: Response<ads_response>) {
                    try {
                        if (adsResponse.isSuccessful) {
                            val responseBody = adsResponse.body()
                            if (responseBody != null) {
                                if (responseBody.isSuccess) {
                                    forgotView.onAdsSuccess(responseBody.adsdata.adsimage, responseBody.adsdata.adsLink, responseBody.adsdata.Is_Show_Ads, responseBody.adsdata.Show_Ads_Everytime)
                                } else {
                                    forgotView.onAdsError(responseBody.message)
                                }
                            } else {
                                forgotView.onAdsError("Something went wrong! Try again!")
                            }

                        } else {
                            forgotView.onAdsError("Something went wrong! Try again!")
                        }


                    } catch (e: NullPointerException) {
                        forgotView.onAdsError("Something went wrong! Try again!")
                    }
                }

                override fun onFailure(call: Call<ads_response>, t: Throwable) {
                    forgotView.onAdsError("Something went wrong! Check your internet!")
                }
            })
        }

    }

