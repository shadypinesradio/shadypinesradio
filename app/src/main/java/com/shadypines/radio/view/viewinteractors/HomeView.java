package com.shadypines.radio.view.viewinteractors;

import com.shadypines.radio.model.RadioResponse;

public interface HomeView extends BaseView{
    void onRadioValue(RadioResponse radioResponse);
}
