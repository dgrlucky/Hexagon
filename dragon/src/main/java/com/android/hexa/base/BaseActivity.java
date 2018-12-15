package com.android.hexa.base;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import com.android.hexa.delegate.IActivity;
import com.android.hexa.di.component.AppComponent;
import com.android.hexa.config.cache.Cache;
import com.android.hexa.config.cache.CacheType;
import com.android.hexa.lifecycle.ActivityLifecycleable;
import com.android.hexa.util.HexaUtil;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.Subject;


/**
 * 因为 Java 只能单继承, 所以如果要用到需要继承特定 {@link Activity} 的三方库, 那你就需要自己自定义 {@link Activity}
 * 继承于这个特定的 {@link Activity}, 然后再按照 {@link BaseActivity} 的格式, 将代码复制过去, 记住一定要实现{@link IActivity}
 */
public abstract class BaseActivity<DB extends ViewDataBinding, VM extends BaseViewModel> extends
        AppCompatActivity implements IActivity, BaseFragment.Callback, ActivityLifecycleable {

    protected final String TAG = this.getClass().getSimpleName();
    private final BehaviorSubject<Lifecycle.Event> mLifecycleSubject = BehaviorSubject.create();
    private Cache<String, Object> mCache;

    protected DB mBinding;
    protected VM mViewModel;
    @Inject
    protected ViewModelProvider.Factory mViewModelFactory;

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public synchronized Cache<String, Object> provideCache() {
        if (mCache == null) {
            mCache = HexaUtil.obtainAppComponentFromContext(this).cacheFactory().build(CacheType.ACTIVITY_CACHE);
        }
        return mCache;
    }

    @NonNull
    @Override
    public final Subject<Lifecycle.Event> provideLifecycleSubject() {
        return mLifecycleSubject;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置DataBinding
        mBinding = DataBindingUtil.setContentView(this, initView(savedInstanceState));
        initData(savedInstanceState);
        if (mViewModel != null) {
            getLifecycle().addObserver(mViewModel);
        }
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {

    }

    @Override
    public void onFragmentAttached() {

    }

    @Override
    public void onFragmentDetached(String tag) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.mViewModelFactory = null;
        //移除LifecycleObserver
        if (mViewModel != null) {
            getLifecycle().removeObserver(mViewModel);
        }
        this.mViewModel = null;
        this.mBinding.unbind();
        this.mBinding = null;
    }


    /**
     * 这个Activity是否会使用Fragment,框架会根据这个属性判断是否注册{@link FragmentManager.FragmentLifecycleCallbacks}
     * 如果返回false,那意味着这个Activity不需要绑定Fragment,那你再在这个Activity中绑定继承于 {@link BaseFragment}
     * 的Fragment将不起任何作用
     *
     * @return
     */
    @Override
    public boolean useFragment() {
        return true;
    }


    public BaseViewModel generateViewModel() {
        Class modelClass;
        Type type = getClass().getGenericSuperclass();
        if (type instanceof ParameterizedType) {
            modelClass = (Class) ((ParameterizedType) type).getActualTypeArguments()[1];
        } else {
            //如果没有指定泛型参数，则默认使用BaseViewModel
            modelClass = BaseViewModel.class;
        }
        return (VM) createViewModel(this, modelClass);
    }

    /**
     * 创建ViewModel
     *
     * @param cls
     * @param <T>
     * @return
     */
    public <T extends androidx.lifecycle.ViewModel> T createViewModel(FragmentActivity activity, Class<T> cls) {
        return ViewModelProviders.of(activity).get(cls);
    }

    @TargetApi(Build.VERSION_CODES.M)
    public boolean hasPermission(String permission) {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M ||
                checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
    }
}
