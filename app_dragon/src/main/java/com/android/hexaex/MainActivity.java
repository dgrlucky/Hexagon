package com.android.hexaex;

import android.os.Bundle;

import com.android.hexa.base.BaseActivity;
import com.android.hexaex.ui.main.MainFragment;

import androidx.annotation.Nullable;

public class MainActivity extends BaseActivity {

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.main_activity;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, MainFragment.newInstance())
                    .commitNow();
        }

    }
}
