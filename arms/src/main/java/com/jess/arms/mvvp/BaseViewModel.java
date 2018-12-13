package com.jess.arms.mvvp;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.OnLifecycleEvent;
import android.support.annotation.NonNull;

public class BaseViewModel extends AndroidViewModel implements LifecycleObserver {

    public BaseViewModel(@NonNull Application application) {
        super(application);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_ANY)
    void onAny(LifecycleOwner owner, Lifecycle.Event event){}

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    void onCreate(){}

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    void onStart(){}

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    void onStop(){}

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    void onResume(){}

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    void onPause(){}

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    void onDestroy(){}
}
