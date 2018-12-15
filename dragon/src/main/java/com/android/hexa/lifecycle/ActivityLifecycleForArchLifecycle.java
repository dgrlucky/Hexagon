package com.android.hexa.lifecycle;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import com.android.hexa.base.BaseActivity;

import javax.inject.Inject;
import javax.inject.Singleton;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Lifecycle;
import dagger.Lazy;
import io.reactivex.subjects.Subject;

/**
 * 配合 {@link ActivityLifecycleable} 使用,使 {@link Activity} 具有 {@link } 的特性
 */
@Singleton
public class ActivityLifecycleForArchLifecycle implements Application.ActivityLifecycleCallbacks {

    @Inject
    Lazy<FragmentLifecycleForRxLifecycle> mFragmentLifecycle;

    @Inject
    public ActivityLifecycleForArchLifecycle() {
    }

    /**
     * 通过桥梁对象 {@code BehaviorSubject<ActivityEvent> mLifecycleSubject}
     * 在每个 Activity 的生命周期中发出对应的生命周期事件
     */
    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        if (activity instanceof ActivityLifecycleable) {
            obtainSubject(activity).onNext(Lifecycle.Event.ON_CREATE);
            if (activity instanceof FragmentActivity) {
                ((FragmentActivity) activity).getSupportFragmentManager().registerFragmentLifecycleCallbacks(mFragmentLifecycle.get(), true);
            }
        }
    }

    @Override
    public void onActivityStarted(Activity activity) {
        if (activity instanceof ActivityLifecycleable) {
            obtainSubject(activity).onNext(Lifecycle.Event.ON_START);
        }
    }

    @Override
    public void onActivityResumed(Activity activity) {
        if (activity instanceof ActivityLifecycleable) {
            obtainSubject(activity).onNext(Lifecycle.Event.ON_RESUME);
        }
    }

    @Override
    public void onActivityPaused(Activity activity) {
        if (activity instanceof ActivityLifecycleable) {
            obtainSubject(activity).onNext(Lifecycle.Event.ON_PAUSE);
        }
    }

    @Override
    public void onActivityStopped(Activity activity) {
        if (activity instanceof ActivityLifecycleable) {
            obtainSubject(activity).onNext(Lifecycle.Event.ON_STOP);
        }
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        if (activity instanceof ActivityLifecycleable) {
            obtainSubject(activity).onNext(Lifecycle.Event.ON_DESTROY);
        }
    }

    /**
     * 从 {@link BaseActivity} 中获得桥梁对象 {@code BehaviorSubject<ActivityEvent> mLifecycleSubject}
     *
     * @see <a href="https://mcxiaoke.gitbooks.io/rxdocs/content/Subject.html">BehaviorSubject 官方中文文档</a>
     */
    private Subject<Lifecycle.Event> obtainSubject(Activity activity) {
        return ((ActivityLifecycleable) activity).provideLifecycleSubject();
    }
}
