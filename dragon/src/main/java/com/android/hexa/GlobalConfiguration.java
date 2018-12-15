package com.android.hexa;

import android.app.Application;
import android.content.Context;

import com.android.hexa.config.ConfigModule;
import com.android.hexa.di.module.GlobalConfigModule;
import com.android.hexa.lifecycle.AppLifecycles;
import com.android.hexa.util.HexaUtil;
import com.squareup.leakcanary.RefWatcher;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import timber.log.Timber;


/**
 * ================================================
 * CommonSDK 的 GlobalConfiguration 含有有每个组件都可公用的配置信息, 每个组件的 AndroidManifest 都应该声明此 ConfigModule
 * ================================================
 */
public class GlobalConfiguration implements ConfigModule {

    @Override
    public void applyOptions(Context context, GlobalConfigModule.Builder builder) {

    }

    @Override
    public void injectAppLifecycle(Context context, List<AppLifecycles> lifecycles) {
        // AppDelegate.Lifecycle 的所有方法都会在基类Application对应的生命周期中被调用,所以在对应的方法中可以扩展一些自己需要的逻辑
        lifecycles.add(new AppLifecycles() {
            @Override
            public void attachBaseContext(@NonNull Context base) {
            }

            @Override
            public void onCreate(@NonNull Application application) {
                if (BuildConfig.DEBUG) {//Timber日志打印
                    Timber.plant(new Timber.DebugTree());
//                    RetrofitUrlManager.getInstance().setDebug(true);
                }
            }

            @Override
            public void onTerminate(@NonNull Application application) {

            }
        });
        // AppLifecycles 的所有方法都会在基类 Application 的对应的生命周期中被调用,所以在对应的方法中可以扩展一些自己需要的逻辑
        // 可以根据不同的逻辑添加多个实现类
        lifecycles.add(new AppLifecycleImpl());
    }

    @Override
    public void injectActivityLifecycle(Context context, List<Application.ActivityLifecycleCallbacks> lifecycles) {
        lifecycles.add(new ActivityLifecycleCallbacksImpl());
    }

    @Override
    public void injectFragmentLifecycle(Context context, List<FragmentManager.FragmentLifecycleCallbacks> lifecycles) {
        lifecycles.add(new FragmentLifecycleCallbacksImpl());
        lifecycles.add(new FragmentManager.FragmentLifecycleCallbacks() {
            @Override
            public void onFragmentDestroyed(FragmentManager fm, Fragment f) {
                ((RefWatcher) HexaUtil
                        .obtainAppComponentFromContext(f.getActivity())
                        .extras()
                        .get(RefWatcher.class.getName()))
                        .watch(f);
            }
        });
    }
}
