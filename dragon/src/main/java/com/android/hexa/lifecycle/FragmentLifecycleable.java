package com.android.hexa.lifecycle;


import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;

/**
 * 让 {@link Fragment} 实现此接口,即可正常使用 {@link}
 */
public interface FragmentLifecycleable extends Lifecycleable<Lifecycle.Event> {

}
