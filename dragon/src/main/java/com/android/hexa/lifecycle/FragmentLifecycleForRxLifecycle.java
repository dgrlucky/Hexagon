package com.android.hexa.lifecycle;

import android.os.Bundle;

import javax.inject.Inject;
import javax.inject.Singleton;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import io.reactivex.subjects.Subject;

/**
 * 配合 {@link FragmentLifecycleable} 使用,使 {@link Fragment} 具有 {@link} 的特性
 */
@Singleton
public class FragmentLifecycleForRxLifecycle extends FragmentManager.FragmentLifecycleCallbacks {

    @Inject
    public FragmentLifecycleForRxLifecycle() {
    }

    @Override
    public void onFragmentCreated(FragmentManager fm, Fragment f, Bundle savedInstanceState) {
        if (f instanceof FragmentLifecycleable) {
            obtainSubject(f).onNext(Lifecycle.Event.ON_CREATE);
        }
    }

    @Override
    public void onFragmentStarted(FragmentManager fm, Fragment f) {
        if (f instanceof FragmentLifecycleable) {
            obtainSubject(f).onNext(Lifecycle.Event.ON_START);
        }
    }

    @Override
    public void onFragmentResumed(FragmentManager fm, Fragment f) {
        if (f instanceof FragmentLifecycleable) {
            obtainSubject(f).onNext(Lifecycle.Event.ON_RESUME);
        }
    }

    @Override
    public void onFragmentPaused(FragmentManager fm, Fragment f) {
        if (f instanceof FragmentLifecycleable) {
            obtainSubject(f).onNext(Lifecycle.Event.ON_PAUSE);
        }
    }

    @Override
    public void onFragmentStopped(FragmentManager fm, Fragment f) {
        if (f instanceof FragmentLifecycleable) {
            obtainSubject(f).onNext(Lifecycle.Event.ON_STOP);
        }
    }

    @Override
    public void onFragmentDestroyed(FragmentManager fm, Fragment f) {
        if (f instanceof FragmentLifecycleable) {
            obtainSubject(f).onNext(Lifecycle.Event.ON_DESTROY);
        }
    }


    private Subject<Lifecycle.Event> obtainSubject(Fragment fragment) {
        return ((FragmentLifecycleable) fragment).provideLifecycleSubject();
    }
}
