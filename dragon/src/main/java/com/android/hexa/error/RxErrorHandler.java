package com.android.hexa.error;

import android.content.Context;

import com.android.hexa.error.internal.ErrorHandlerFactory;
import com.android.hexa.error.listener.ResponseErrorListener;

public class RxErrorHandler {
    public final String TAG = this.getClass().getSimpleName();
    private ErrorHandlerFactory mHandlerFactory;

    private RxErrorHandler(Builder builder) {
        this.mHandlerFactory = builder.errorHandlerFactory;
    }

    public static Builder builder() {
        return new Builder();
    }

    public ErrorHandlerFactory getHandlerFactory() {
        return mHandlerFactory;
    }

    public static final class Builder {
        private Context context;
        private ResponseErrorListener mResponseErrorListener;
        private ErrorHandlerFactory errorHandlerFactory;

        private Builder() {
        }

        public Builder with(Context context) {
            if (context == null)
                throw new NullPointerException("Context cannot be null");
            this.context = context;
            return this;
        }

        public Builder responseErrorListener(ResponseErrorListener responseErrorListener) {
            if (responseErrorListener == null)
                throw new NullPointerException("responseErrorListener cannot be null");
            this.mResponseErrorListener = responseErrorListener;
            return this;
        }

        public RxErrorHandler build() {
            if (context == null)
                throw new IllegalStateException("Context is required");

            if (mResponseErrorListener == null)
                throw new IllegalStateException("ResponseErrorListener is required");

            this.errorHandlerFactory = new ErrorHandlerFactory(context, mResponseErrorListener);

            return new RxErrorHandler(this);
        }
    }


}
