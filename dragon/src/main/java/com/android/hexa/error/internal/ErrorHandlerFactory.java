package com.android.hexa.error.internal;

import android.content.Context;

import com.android.hexa.error.listener.ResponseErrorListener;

public class ErrorHandlerFactory {
    public final String TAG = this.getClass().getSimpleName();
    private Context mContext;
    private ResponseErrorListener mResponseErrorListener;

    public ErrorHandlerFactory(Context mContext, ResponseErrorListener mResponseErrorListener) {
        this.mResponseErrorListener = mResponseErrorListener;
        this.mContext = mContext;
    }

    /**
     * 处理错误
     *
     * @param throwable
     */
    public void handleError(Throwable throwable) {
        mResponseErrorListener.handleResponseError(mContext, throwable);
    }
}
