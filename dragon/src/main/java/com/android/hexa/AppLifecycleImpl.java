package com.android.hexa;

import android.app.Application;
import android.content.Context;

import com.android.hexa.lifecycle.AppLifecycles;
import com.android.hexa.util.HexaUtil;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import androidx.annotation.NonNull;
import androidx.multidex.MultiDex;


/**
 * 展示 {@link AppLifecycles} 的用法
 */
public class AppLifecycleImpl implements AppLifecycles {

    @Override
    public void attachBaseContext(@NonNull Context base) {
          MultiDex.install(base);  //这里比 onCreate 先执行,常用于 MultiDex 初始化,插件化框架的初始化
    }

    @Override
    public void onCreate(@NonNull Application application) {
        if (LeakCanary.isInAnalyzerProcess(application)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        //leakCanary内存泄露检查
        HexaUtil.obtainAppComponentFromContext(application).extras().put(RefWatcher.class.getName(), BuildConfig.DEBUG ? LeakCanary.install(application) : RefWatcher.DISABLED);
    }

    @Override
    public void onTerminate(@NonNull Application application) {

    }
}
