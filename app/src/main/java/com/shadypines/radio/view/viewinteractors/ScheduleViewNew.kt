package com.shadypines.radio.view.viewinteractors

/**
 * Created by Android Dev on 27-Oct-21 Oct, 2021
 */
interface ScheduleViewNew {
    fun onSubscribeSuccess(day: String)
    fun onSubscribeError(message: String, day: String)
}