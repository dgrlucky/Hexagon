package com.android.hexa.di.component;

import android.app.Application;
import android.content.Context;

import com.android.hexa.delegate.AppDelegate;
import com.android.hexa.di.module.AppModule;
import com.android.hexa.di.module.GlobalConfigModule;
import com.android.hexa.error.RxErrorHandler;
import com.android.hexa.config.ConfigModule;
import com.android.hexa.config.cache.Cache;
import com.android.hexa.util.HexaUtil;
import com.google.gson.Gson;

import java.io.File;
import java.util.concurrent.ExecutorService;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;

/**
 * 可通过 {@link HexaUtil#obtainAppComponentFromContext(Context)} 拿到此接口的实现类
 * 拥有此接口的实现类即可调用对应的方法拿到 Dagger 提供的对应实例
 */
@Singleton
@Component(modules = {AppModule.class, GlobalConfigModule.class})
public interface AppComponent {

    Application application();

    /**
     * RxJava 错误处理管理类
     *
     * @return {@link RxErrorHandler}
     */
    RxErrorHandler rxErrorHandler();

    /**
     * Json 序列化库
     *
     * @return {@link Gson}
     */
    Gson gson();

    /**
     * 缓存文件根目录 (RxCache 和 Glide 的缓存都已经作为子文件夹放在这个根目录下), 应该将所有缓存都统一放到这个根目录下
     * 便于管理和清理, 可在 {@link ConfigModule#applyOptions(Context, GlobalConfigModule.Builder)} 种配置
     *
     * @return {@link File}
     */
    File cacheFile();

    /**
     * 用来存取一些整个 AppInject 公用的数据, 切勿大量存放大容量数据, 这里的存放的数据和 {@link Application} 的生命周期一致
     *
     * @return {@link Cache}
     */
    Cache<String, Object> extras();

    /**
     * 用于创建框架所需缓存对象的工厂
     *
     * @return {@link Cache.Factory}
     */
    Cache.Factory cacheFactory();

    /**
     * 返回一个全局公用的线程池,适用于大多数异步需求。
     * 避免多个线程池创建带来的资源消耗。
     *
     * @return {@link ExecutorService}
     */
    ExecutorService executorService();

    void inject(AppDelegate delegate);

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(Application application);

        Builder globalConfigModule(GlobalConfigModule globalConfigModule);

        AppComponent build();
    }
}
