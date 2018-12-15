package com.android.hexa.delegate;

import android.app.Activity;
import android.os.Bundle;

import com.android.hexa.util.HexaUtil;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


/**
 * {@link ActivityDelegate} 默认实现类
 */
public class ActivityDelegateImpl implements ActivityDelegate {

    private Activity mActivity;
    private IActivity iActivity;

    public ActivityDelegateImpl(@NonNull Activity activity) {
        this.mActivity = activity;
        this.iActivity = (IActivity) activity;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        //这里提供 AppComponent 对象给 BaseActivity 的子类, 用于 Dagger2 的依赖注入
        iActivity.setupActivityComponent(HexaUtil.obtainAppComponentFromContext(mActivity));
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {

    }

    @Override
    public void onDestroy() {
        this.iActivity = null;
        this.mActivity = null;
    }
}