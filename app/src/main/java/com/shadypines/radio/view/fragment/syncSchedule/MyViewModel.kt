package com.shadypines.radio.view.fragment.syncSchedule

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shadypines.radio.model.RadioResponse
import com.shadypines.radio.model.schecule.Schedule

class MyViewModel : ViewModel() {

    private val _scheduleLiveData = MutableLiveData<Resource<Schedule>>()

    val scheduleLiveData: LiveData<Resource<Schedule>> get() = _scheduleLiveData

    private val _radioResponseLiveData = MutableLiveData<RadioResponse>()
    val radioResponseLiveData: LiveData<RadioResponse> get() = _radioResponseLiveData

    private val _isUserLoggedOut = MutableLiveData<Boolean>()
    val isUserLoggedOut: LiveData<Boolean> get() = _isUserLoggedOut

    fun userLoggedOut() {
        _isUserLoggedOut.postValue(true)
    }

    fun resetUserLoggedOutStatus() {
        _isUserLoggedOut.postValue(false)
    }

    fun postScheduleData(resource: Resource<Schedule>) {
        _scheduleLiveData.postValue(resource)
    }
    fun postRadioResponseData(radioResponse: RadioResponse) {
        _radioResponseLiveData.postValue(radioResponse)
    }
}
