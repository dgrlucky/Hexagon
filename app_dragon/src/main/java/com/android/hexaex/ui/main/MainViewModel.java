package com.android.hexaex.ui.main;


import android.app.Application;
import android.view.View;

import com.android.hexa.base.BaseViewModel;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;


public class MainViewModel extends BaseViewModel {

    public MutableLiveData<String> random = new MutableLiveData<>();

    public MainViewModel(@NonNull Application application) {
        super(application);
    }

    public void randomClick(View v) {
        random.setValue(String.valueOf(Math.random()));
    }
}
