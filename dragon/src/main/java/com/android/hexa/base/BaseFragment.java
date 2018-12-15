package com.android.hexa.base;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.hexa.delegate.IFragment;
import com.android.hexa.di.component.AppComponent;
import com.android.hexa.config.cache.Cache;
import com.android.hexa.config.cache.CacheType;
import com.android.hexa.lifecycle.FragmentLifecycleable;
import com.android.hexa.util.HexaUtil;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.Subject;

/**
 * 因为 Java 只能单继承, 所以如果要用到需要继承特定 @{@link Fragment} 的三方库, 那你就需要自己自定义 @{@link Fragment}
 * 继承于这个特定的 @{@link Fragment}, 然后再按照 {@link BaseFragment} 的格式, 将代码复制过去, 记住一定要实现{@link IFragment}
 */
public abstract class BaseFragment<DB extends ViewDataBinding, VM extends BaseViewModel> extends
        Fragment implements IFragment, FragmentLifecycleable {

    protected final String TAG = this.getClass().getSimpleName();

    protected DB mBinding;
    protected VM mViewModel;
    protected BaseActivity mActivity;
    protected boolean isFirst = true;//首次加载
    protected boolean isVisible = false;//是否可见

    private Cache<String, Object> mCache;
    @Inject
    protected ViewModelProvider.Factory mViewModelFactory;
    private final BehaviorSubject<Lifecycle.Event> mLifecycleSubject = BehaviorSubject.create();

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public synchronized Cache<String, Object> provideCache() {
        if (mCache == null) {
            mCache = HexaUtil.obtainAppComponentFromContext(getActivity()).cacheFactory().build(CacheType.FRAGMENT_CACHE);
        }
        return mCache;
    }

    @NonNull
    @Override
    public final Subject<Lifecycle.Event> provideLifecycleSubject() {
        return mLifecycleSubject;
    }

    public BaseFragment() {
        //必须确保在 Fragment 实例化时 setArguments()
        setArguments(new Bundle());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BaseActivity) {
            BaseActivity activity = (BaseActivity) context;
            this.mActivity = activity;
            activity.onFragmentAttached();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, initView(savedInstanceState), container, false);
        initData(savedInstanceState);
        if (mViewModel != null) {
            getLifecycle().addObserver(mViewModel);
        }
        //可见，并且是首次加载
        if (isVisible && isFirst) {
            onFragmentVisibleChange(true);
        }
        return mBinding.getRoot();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isVisible = isVisibleToUser;
        //可见，并且首次加载时才调用
        onFragmentVisibleChange(isVisible & isFirst);
    }

    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {

    }

    /**
     * 当前 Fragment 可见状态发生变化时会回调该方法。
     * 如果当前 Fragment 是第一次加载，等待 onCreateView 后才会回调该方法，
     * 其它情况回调时机跟 {@link #setUserVisibleHint(boolean)}一致
     * 在该回调方法中你可以做一些加载数据操作，甚至是控件的操作.
     *
     * @param isVisible true  不可见 -> 可见
     *                  false 可见  -> 不可见
     */
    protected void onFragmentVisibleChange(boolean isVisible) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mViewModelFactory = null;
        //移除LifecycleObserver
        if (mViewModel != null) {
            getLifecycle().removeObserver(mViewModel);
        }
        this.mViewModel = null;
        this.mBinding.unbind();
    }

    public interface Callback {

        void onFragmentAttached();

        void onFragmentDetached(String tag);
    }

}
