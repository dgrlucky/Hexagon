package com.android.hexa.error;

import android.app.Activity;
import android.os.Bundle;

import com.android.hexa.error.internal.ErrorHandleSubscriber;
import com.android.hexa.error.internal.ErrorHandleSubscriberOfFlowable;
import com.android.hexa.error.internal.RetryWithDelay;
import com.android.hexa.error.internal.RetryWithDelayOfFlowable;

import io.reactivex.Flowable;
import io.reactivex.Observable;

public class SampleActivity extends Activity {
    private final String TAG = getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RxErrorHandler rxErrorHandler = ((App) getApplicationContext()).getRxErrorHandler();
        Observable
                .error(new Exception("Error"))
                .retryWhen(new RetryWithDelay(3, 2))//retry(http connect timeout)
                .subscribe(new ErrorHandleSubscriber<Object>(rxErrorHandler) {
                    @Override
                    public void onNext(Object o) {

                    }
                });

        Flowable //Backpressure
                .error(new Exception("Error"))
                .retryWhen(new RetryWithDelayOfFlowable(3, 2))//retry(http connect timeout)
                .subscribe(new ErrorHandleSubscriberOfFlowable<Object>(rxErrorHandler) {
                    @Override
                    public void onNext(Object o) {

                    }
                });

    }
}
