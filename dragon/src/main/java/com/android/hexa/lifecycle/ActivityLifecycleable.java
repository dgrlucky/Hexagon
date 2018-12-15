package com.android.hexa.lifecycle;

import android.app.Activity;

import androidx.lifecycle.Lifecycle;

/**
 * 让 {@link Activity} 实现此接口,即可正常使用 {@link }
 */
public interface ActivityLifecycleable extends Lifecycleable<Lifecycle.Event> {
}
