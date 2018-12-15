package com.android.hexa.di.module;

import android.app.Application;

import com.android.hexa.error.RxErrorHandler;
import com.android.hexa.error.listener.ResponseErrorListener;
import com.android.hexa.config.cache.Cache;
import com.android.hexa.config.cache.CacheType;
import com.android.hexa.config.cache.IntelligentCache;
import com.android.hexa.config.cache.LruCache;
import com.android.hexa.util.HexaUtil;
import com.android.hexa.util.DataHelper;
import com.google.gson.Gson;

import java.io.File;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.inject.Named;
import javax.inject.Singleton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import dagger.Module;
import dagger.Provides;
import io.rx_cache2.internal.RxCache;
import io.victoralbertos.jolyglot.GsonSpeaker;

/**
 * 框架独创的建造者模式 {@link Module},可向框架中注入外部配置的自定义参数
 */
@Module
public class GlobalConfigModule {
    private File mCacheFile;
    private Cache.Factory mCacheFactory;
    private ExecutorService mExecutorService;
    private ResponseErrorListener mErrorListener;
    private AppModule.GsonConfiguration mGsonConfiguration;
    private AppModule.RxCacheConfiguration mRxCacheConfiguration;

    private GlobalConfigModule(Builder builder) {
        this.mErrorListener = builder.responseErrorListener;
        this.mCacheFile = builder.cacheFile;
        this.mRxCacheConfiguration = builder.rxCacheConfiguration;
        this.mGsonConfiguration = builder.gsonConfiguration;
        this.mCacheFactory = builder.cacheFactory;
        this.mExecutorService = builder.executorService;
    }

    public static Builder builder() {
        return new Builder();
    }


    /**
     * 提供缓存文件
     */
    @Singleton
    @Provides
    File provideCacheFile(Application application) {
        return mCacheFile == null ? DataHelper.getCacheFile(application) : mCacheFile;
    }

    /**
     * 提供 {@link RxCache}
     *
     * @param application
     * @param configuration
     * @param cacheDirectory cacheDirectory RxCache缓存路径
     * @return {@link RxCache}
     */
    @Singleton
    @Provides
    static RxCache provideRxCache(Application application, @Nullable AppModule.RxCacheConfiguration
            configuration,
                                  @Named("RxCacheDirectory") File cacheDirectory, Gson gson) {
        RxCache.Builder builder = new RxCache.Builder();
        RxCache rxCache = null;
        if (configuration != null) {
            rxCache = configuration.configRxCache(application, builder);
        }
        if (rxCache != null)
            return rxCache;
        return builder.persistence(cacheDirectory, new GsonSpeaker(gson));
    }

    /**
     * 需要单独给 {@link RxCache} 提供缓存路径
     *
     * @param cacheDir
     * @return {@link File}
     */
    @Singleton
    @Provides
    @Named("RxCacheDirectory")
    static File provideRxCacheDirectory(File cacheDir) {
        File cacheDirectory = new File(cacheDir, "RxCache");
        return DataHelper.makeDirs(cacheDirectory);
    }

    /**
     * 提供处理 RxJava 错误的管理器
     *
     * @param application
     * @param listener
     * @return {@link RxErrorHandler}
     */
    @Singleton
    @Provides
    static RxErrorHandler ProvideRxErrorHandler(Application application, ResponseErrorListener listener) {
        return RxErrorHandler
                .builder()
                .with(application)
                .responseErrorListener(listener)
                .build();
    }

    /**
     * 提供处理 RxJava 错误的管理器的回调
     *
     * @return
     */
    @Singleton
    @Provides
    ResponseErrorListener provideResponseErrorListener() {
        return mErrorListener == null ? ResponseErrorListener.EMPTY : mErrorListener;
    }

    @Singleton
    @Provides
    @Nullable
    AppModule.RxCacheConfiguration provideRxCacheConfiguration() {
        return mRxCacheConfiguration;
    }

    @Singleton
    @Provides
    @Nullable
    AppModule.GsonConfiguration provideGsonConfiguration() {
        return mGsonConfiguration;
    }

    @Singleton
    @Provides
    Cache.Factory provideCacheFactory(Application application) {
        return mCacheFactory == null ? new Cache.Factory() {
            @NonNull
            @Override
            public Cache build(CacheType type) {
                //若想自定义 LruCache 的 size, 或者不想使用 LruCache, 想使用自己自定义的策略
                //使用 GlobalConfigModule.Builder#cacheFactory() 即可扩展
                switch (type.getCacheTypeId()){
                    //Activity、Fragment 以及 Extras 使用 IntelligentCache (具有 LruCache 和 可永久存储数据的 Map)
                    case CacheType.EXTRAS_TYPE_ID:
                    case CacheType.ACTIVITY_CACHE_TYPE_ID:
                    case CacheType.FRAGMENT_CACHE_TYPE_ID:
                        return new IntelligentCache(type.calculateCacheSize(application));
                    //其余使用 LruCache (当达到最大容量时可根据 LRU 算法抛弃不合规数据)
                    default:
                        return new LruCache(type.calculateCacheSize(application));
                }
            }
        } : mCacheFactory;
    }

    /**
     * 返回一个全局公用的线程池,适用于大多数异步需求。
     * 避免多个线程池创建带来的资源消耗。
     *
     * @return {@link Executor}
     */
    @Singleton
    @Provides
    ExecutorService provideExecutorService() {
        return mExecutorService == null ? new ThreadPoolExecutor(0, Integer.MAX_VALUE, 60, TimeUnit.SECONDS,
                new SynchronousQueue<>(), HexaUtil.threadFactory("Hexa Executor", false)) : mExecutorService;
    }


    public static final class Builder {
        private ResponseErrorListener responseErrorListener;
        private File cacheFile;
        private AppModule.RxCacheConfiguration rxCacheConfiguration;
        private AppModule.GsonConfiguration gsonConfiguration;
        private Cache.Factory cacheFactory;
        private ExecutorService executorService;

        private Builder() {
        }

        public Builder responseErrorListener(ResponseErrorListener listener) {//处理所有RxJava的onError逻辑
            this.responseErrorListener = listener;
            return this;
        }


        public Builder cacheFile(File cacheFile) {
            this.cacheFile = cacheFile;
            return this;
        }

        public Builder rxCacheConfiguration(AppModule.RxCacheConfiguration rxCacheConfiguration) {
            this.rxCacheConfiguration = rxCacheConfiguration;
            return this;
        }

        public Builder gsonConfiguration(AppModule.GsonConfiguration gsonConfiguration) {
            this.gsonConfiguration = gsonConfiguration;
            return this;
        }

        public Builder cacheFactory(Cache.Factory cacheFactory) {
            this.cacheFactory = cacheFactory;
            return this;
        }

        public Builder executorService(ExecutorService executorService) {
            this.executorService = executorService;
            return this;
        }

        public GlobalConfigModule build() {
            return new GlobalConfigModule(this);
        }


    }

}
