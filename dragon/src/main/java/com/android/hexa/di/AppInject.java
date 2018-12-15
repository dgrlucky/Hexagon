package com.android.hexa.di;


import com.android.hexa.di.component.AppComponent;

import androidx.annotation.NonNull;


/**
 * 框架要求框架中的每个 {@link android.app.Application} 都需要实现此类, 以满足规范
 *
 */
public interface AppInject {
    @NonNull
    AppComponent getAppComponent();
}
