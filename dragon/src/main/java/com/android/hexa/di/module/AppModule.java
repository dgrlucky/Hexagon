package com.android.hexa.di.module;

import android.app.Application;
import android.content.Context;

import com.android.hexa.lifecycle.ActivityLifecycle;
import com.android.hexa.config.AppManager;
import com.android.hexa.lifecycle.ActivityLifecycleForArchLifecycle;
import com.android.hexa.lifecycle.FragmentLifecycle;
import com.android.hexa.config.IRepositoryManager;
import com.android.hexa.config.RepositoryManager;
import com.android.hexa.config.cache.Cache;
import com.android.hexa.config.cache.CacheType;
import com.android.hexa.config.ViewModelFactory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Named;
import javax.inject.Singleton;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import io.rx_cache2.internal.RxCache;

/**
 * 提供一些框架必须的实例的 {@link Module}
 */
@Module
public abstract class AppModule {

    @Singleton
    @Provides
    static Gson provideGson(Application application, @Nullable GsonConfiguration configuration) {
        GsonBuilder builder = new GsonBuilder();
        if (configuration != null)
            configuration.configGson(application, builder);
        return builder.create();
    }

    /**
     * 之前 {@link AppManager} 使用 Dagger 保证单例, 只能使用访问
     * 现在直接将 AppManager 独立为单例类, 可以直接通过静态方法 {@link AppManager#getAppManager()} 访问, 更加方便
     * 但为了不影响之前使用,获取 {@link AppManager} 的项目, 所以暂时保留这种访问方式
     *
     * @param application
     * @return
     */
    @Singleton
    @Provides
    static AppManager provideAppManager(Application application) {
        return AppManager.getAppManager().init(application);
    }

    @Binds
    abstract IRepositoryManager bindRepositoryManager(RepositoryManager repositoryManager);

    @Singleton
    @Provides
    @SuppressWarnings("unchecked")
    static Cache<String, Object> provideExtras(Cache.Factory cacheFactory) {
        return cacheFactory.build(CacheType.EXTRAS);
    }

    @Binds
    @Named("ActivityLifecycle")
    abstract Application.ActivityLifecycleCallbacks bindActivityLifecycle(ActivityLifecycle
                                                                                      activityLifecycle);

    @Binds
    @Named("ActivityLifecycleForArchLifecycle")
    abstract Application.ActivityLifecycleCallbacks bindActivityLifecycleForRxLifecycle
            (ActivityLifecycleForArchLifecycle activityLifecycleForArchLifecycle);

    @Binds
    abstract FragmentManager.FragmentLifecycleCallbacks bindFragmentLifecycle(FragmentLifecycle
                                                                                          fragmentLifecycle);

    @Singleton
    @Provides
    static List<FragmentManager.FragmentLifecycleCallbacks> provideFragmentLifecycles() {
        return new ArrayList<>();
    }

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelFactory factory);

    public interface GsonConfiguration {
        void configGson(Context context, GsonBuilder builder);
    }

    public interface RxCacheConfiguration {
        /**
         * 若想自定义 RxCache 的缓存文件夹或者解析方式, 如改成 fastjson
         * 请 {@code return rxCacheBuilder.persistence(cacheDirectory, new FastJsonSpeaker());},
         * 否则请 {@code return null;}
         *
         * @param context
         * @param builder
         * @return {@link RxCache}
         */
        RxCache configRxCache(Context context, RxCache.Builder builder);
    }
}
