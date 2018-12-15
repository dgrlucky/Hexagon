package com.android.hexa.error.listener;

import android.content.Context;

public interface ResponseErrorListener {
    void handleResponseError(Context context, Throwable t);

    ResponseErrorListener EMPTY = new ResponseErrorListener() {
        @Override
        public void handleResponseError(Context context, Throwable t) {

        }
    };
}
