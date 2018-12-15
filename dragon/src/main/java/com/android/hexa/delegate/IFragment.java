package com.android.hexa.delegate;

import android.os.Bundle;

import com.android.hexa.base.BaseFragment;
import com.android.hexa.di.component.AppComponent;
import com.android.hexa.config.cache.Cache;
import com.android.hexa.config.cache.LruCache;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


/**
 * 框架要求框架中的每个 {@link Fragment} 都需要实现此类,以满足规范
 * @see BaseFragment
 */
public interface IFragment {

    /**
     * 提供在 {@link Fragment} 生命周期内的缓存容器, 可向此 {@link Fragment} 存取一些必要的数据
     * 此缓存容器和 {@link Fragment} 的生命周期绑定, 如果 {@link Fragment} 在屏幕旋转或者配置更改的情况下
     * 重新创建, 那此缓存容器中的数据也会被清空, 如果你想避免此种情况请使用
     *
     * @return like {@link LruCache}
     */
    @NonNull
    Cache<String, Object> provideCache();

    /**
     * 提供 AppComponent (提供所有的单例对象) 给实现类, 进行 Component 依赖
     *
     * @param appComponent
     */
    void setupFragmentComponent(@NonNull AppComponent appComponent);

    /**
     * 初始化 View
     *
     * @param savedInstanceState
     * @return
     */
    int initView(@Nullable Bundle savedInstanceState);

    /**
     * 初始化数据
     *
     * @param savedInstanceState
     */
    void initData(@Nullable Bundle savedInstanceState);

}
