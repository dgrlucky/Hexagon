package com.android.hexa.config;

import android.app.Application;
import android.content.Context;

import com.android.hexa.config.cache.Cache;
import com.android.hexa.config.cache.CacheType;
import com.android.hexa.util.Preconditions;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Lazy;
import io.rx_cache2.internal.RxCache;

/**
 * 用来管理网络请求层,以及数据缓存层,以后可能添加数据库请求层
 * 提供给ViewModel层必要的 Api 做数据处理
 */
@Singleton
public class RepositoryManager implements IRepositoryManager {

    @Inject
    Lazy<RxCache> mRxCache;
    @Inject
    Application mApplication;
    @Inject
    Cache.Factory mCachefactory;
    private Cache<String, Object> mRetrofitServiceCache;
    private Cache<String, Object> mCacheServiceCache;

    @Inject
    public RepositoryManager() {
    }


    /**
     * 根据传入的 Class 获取对应的 RxCache service
     *
     * @param cacheClass Cache class
     * @param <T>        Cache class
     * @return Cache
     */
    @Override
    public synchronized <T> T obtainCacheService(Class<T> cacheClass) {
        if (mCacheServiceCache == null) {
            mCacheServiceCache = mCachefactory.build(CacheType.CACHE_SERVICE_CACHE);
        }
        Preconditions.checkNotNull(mCacheServiceCache, "Cannot return null from a Cache.Factory#build(int) method");
        T cacheService = (T) mCacheServiceCache.get(cacheClass.getCanonicalName());
        if (cacheService == null) {
            cacheService = mRxCache.get().using(cacheClass);
            mCacheServiceCache.put(cacheClass.getCanonicalName(), cacheService);
        }
        return cacheService;
    }

    /**
     * 清理所有缓存
     */
    @Override
    public void clearAllCache() {
        mRxCache.get().evictAll().subscribe();
    }

    @Override
    public Context getContext() {
        return mApplication;
    }
}
