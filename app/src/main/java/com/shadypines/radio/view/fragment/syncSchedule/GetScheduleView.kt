package com.shadypines.radio.view.fragment.syncSchedule

import com.shadypines.radio.model.schecule.Schedule

interface GetScheduleView {
    fun onGetScheduleSuccess(schedule: Schedule, day:String)
    fun onGetScheduleError()
}