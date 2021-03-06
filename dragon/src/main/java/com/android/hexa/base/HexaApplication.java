package com.android.hexa.base;

import android.app.Application;
import android.content.Context;

import com.android.hexa.delegate.AppDelegate;
import com.android.hexa.di.AppInject;
import com.android.hexa.lifecycle.AppLifecycles;
import com.android.hexa.di.component.AppComponent;
import com.android.hexa.util.HexaUtil;
import com.android.hexa.util.Preconditions;

import androidx.annotation.NonNull;


/**
 * Hexa 是一个整合了大量主流开源项目的 Android MVVM 快速搭建框架, 其中包含 Dagger2、Retrofit、RxJava 以及
 * RxCache 等 Rx 系三方库, 并且提供 UI 自适应方案, 本框架将它们结合起来, 并全部使用 Dagger2 管理
 * 并提供给开发者使用, 使用本框架开发您的项目, 就意味着您已经拥有一个 MVVM + Dagger2 + Retrofit + RxJava 项目
 *
 */
public class HexaApplication extends Application implements AppInject {
    private AppLifecycles mAppDelegate;

    /**
     * 这里会在 {@link HexaApplication#onCreate} 之前被调用,可以做一些较早的初始化
     * 常用于 MultiDex 以及插件化框架的初始化
     *
     * @param base
     */
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        if (mAppDelegate == null) {
            this.mAppDelegate = new AppDelegate(base);
        }
        this.mAppDelegate.attachBaseContext(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (mAppDelegate != null) {
            this.mAppDelegate.onCreate(this);
        }
    }

    /**
     * 在模拟环境中程序终止时会被调用
     */
    @Override
    public void onTerminate() {
        super.onTerminate();
        if (mAppDelegate != null) {
            this.mAppDelegate.onTerminate(this);
        }
    }

    /**
     * 将 {@link AppComponent} 返回出去, 供其它地方使用, {@link AppComponent} 接口中声明的方法所返回的实例, 在 {@link #getAppComponent()} 拿到对象后都可以直接使用
     *
     * @see HexaUtil#obtainAppComponentFromContext(Context) 可直接获取 {@link AppComponent}
     * @return AppComponent
     */
    @NonNull
    @Override
    public AppComponent getAppComponent() {
        Preconditions.checkNotNull(mAppDelegate, "%s cannot be null", AppDelegate.class.getName());
        Preconditions.checkState(mAppDelegate instanceof AppInject, "%s must be implements %s", mAppDelegate.getClass().getName(), AppInject.class.getName());
        return ((AppInject) mAppDelegate).getAppComponent();
    }

}
