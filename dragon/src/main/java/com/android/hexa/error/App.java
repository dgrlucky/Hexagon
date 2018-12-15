package com.android.hexa.error;

import android.app.Application;
import android.content.Context;
import android.net.ParseException;
import android.util.Log;

import com.android.hexa.error.listener.ResponseErrorListener;

import org.json.JSONException;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

public class App extends Application {
    private final String TAG = getClass().getSimpleName();
    private RxErrorHandler mRxErrorHandler;

    @Override
    public void onCreate() {
        super.onCreate();
        //Initialization
        mRxErrorHandler = RxErrorHandler
                .builder()
                .with(this)
                .responseErrorListener(new ResponseErrorListener() {
                    @Override
                    public void handleResponseError(Context context, Throwable t) {
                        if (t instanceof UnknownHostException) {
                            //do something ...
                        } else if (t instanceof SocketTimeoutException) {
                            //do something ...
                        } else if (t instanceof ParseException || t instanceof JSONException) {
                            //do something ...
                        } else {
                            //handle other Exception ...
                        }
                        Log.w(TAG, "Error handle");
                    }
                }).build();
    }

    public RxErrorHandler getRxErrorHandler() {
        return mRxErrorHandler;
    }
}
