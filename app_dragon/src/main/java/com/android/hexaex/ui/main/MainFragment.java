package com.android.hexaex.ui.main;

import android.os.Bundle;

import com.android.hexa.base.BaseFragment;
import com.android.hexaex.R;
import com.android.hexaex.databinding.MainFragmentBinding;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

public class MainFragment extends BaseFragment<MainFragmentBinding, MainViewModel> {

    private MainViewModel mViewModel;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.main_fragment;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        mViewModel = ViewModelProviders.of(this,mViewModelFactory).get(MainViewModel.class);
        mViewModel.random.setValue("begin");
        mBinding.setViewModel(mViewModel);
        mBinding.setLifecycleOwner(this);
        mBinding.executePendingBindings();
    }

}
