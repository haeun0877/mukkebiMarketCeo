package com.example.market_ceo.http;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;

import com.example.market_ceo.constant.Define;
import com.example.market_ceo.dialog.LoadingDialog;
import com.google.api.client.http.HttpContent;
import com.google.api.client.http.HttpResponse;

import java.lang.ref.WeakReference;
import java.util.Locale;
import java.util.Map;

public abstract class Request<T> {
    protected final WeakReference<Object> mOwnerRef;
    protected boolean mCancelled;

    protected Response.OnHttpResponseBeforeListener mOnHttpResponseBeforeListener;
    protected Response.OnHttpResponseAfterListener<T> mOnHttpResponseAfterListener;
    protected Response.OnHttpResponseListener<T> mOnHttpResponseListener;
    protected Response.OnHttpResponseErrorListener mOnHttpResponseErrorListener;

    public abstract String getUrl();

    public abstract String getMethod();

    public abstract String getMediaType();

    public abstract String getContentType();

    public abstract Response<T> parseResponse(Context context, HttpResponse response) throws Exception;

    protected void saveResponseData(Response<T> response) {
    }

    ;

    public Request(Activity activity) {
        mOwnerRef = new WeakReference<Object>(activity);
    }


    public Request(Context context) {
        mOwnerRef = new WeakReference<Object>(context != null ? context.getApplicationContext() : context);
    }

    public Map<String, Object> getParameters() {
        return null;
    }

    public HttpContent getBody() throws Exception {
        return null;
    }

    public void cancel() {
        mCancelled = true;
    }

    public boolean isCancelled() {
        return mCancelled;
    }

    public Request<T> setOnHttpResponseBeforeListener(Response.OnHttpResponseBeforeListener listener) {
        mOnHttpResponseBeforeListener = listener;
        return this;
    }

    public Request<T> setOnHttpResponseAfterListener(Response.OnHttpResponseAfterListener<T> listener) {
        mOnHttpResponseAfterListener = listener;
        return this;
    }

    public Request<T> setOnHttpResponseListener(Response.OnHttpResponseListener<T> listener) {
        mOnHttpResponseListener = listener;
        return this;
    }

    public Request<T> setOnHttpResponseErrorListener(Response.OnHttpResponseErrorListener listener) {
        mOnHttpResponseErrorListener = listener;
        return this;
    }

    public void send(Context context) {
        if (Define.m_bIsShowDialog && LoadingDialog.isAvail()) {
            LoadingDialog.showProgress();
        }

        HttpClient.getInstance(context).sendAsync(this);
    }

    protected Context getContext() {
        if (mOwnerRef != null) {
            Object obj = mOwnerRef.get();
            if (obj != null) {
                if (obj instanceof Activity) {
                    return ((Activity) obj).getApplicationContext();
                } else if (obj instanceof Context) {
                    return (Context) obj;
                }
            }
        }
        return null;
    }

    protected void fireOnResponseBefore() throws HttpError {
        if (mOnHttpResponseBeforeListener != null)
            mOnHttpResponseBeforeListener.onHttpResponseBefore(this);
    }

    protected void fireOnResponseAfter(Response<T> response) throws HttpError {
        if (response != null && response.isSuccess()) {
            if (mOnHttpResponseAfterListener != null)
                mOnHttpResponseAfterListener.onHttpResponseAfter(this, response.getResult());
        }
    }

    protected void fireOnResponse(Response<T> response) {
        if (response.isSuccess()) {
            if (mOnHttpResponseListener != null)
                mOnHttpResponseListener.onHttpResponse(this, response.getResult());
        } else {
            if (mOnHttpResponseErrorListener != null)
                mOnHttpResponseErrorListener.onHttpResponseError(this, response.getError());
        }

        if (Define.m_bIsShowDialog && LoadingDialog.isAvail()) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    LoadingDialog.dismissProgress();
                }
            }, 500);
        }
    }

    protected static void checkContentType(String contentType, HttpResponse response) throws Exception {
        if (!response.getContentType().toLowerCase(Locale.US).startsWith(contentType.toLowerCase(Locale.US)))
            throw new Exception("Content-Type(" + response.getContentType() + ") is not " + contentType);
    }
}
