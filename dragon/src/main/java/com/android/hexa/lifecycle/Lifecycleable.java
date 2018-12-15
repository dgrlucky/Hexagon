package com.android.hexa.lifecycle;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import io.reactivex.subjects.Subject;

/**
 * 让 {@link Activity}/{@link Fragment} 实现此接口,即可正常使用
 * 无需再继承提供的 Activity/Fragment ,扩展性极强
 */
public interface Lifecycleable<E> {
    @NonNull
    Subject<Lifecycle.Event> provideLifecycleSubject();
}
