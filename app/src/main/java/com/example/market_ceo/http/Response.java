package com.example.market_ceo.http;

public class Response<T> {
    protected T mResult;
    protected HttpError mError;

    public boolean isSuccess() {
        return mError == null;
    }

    public T getResult() {
        return mResult;
    }

    public HttpError getError() {
        return mError;
    }

    public static <T> Response<T> success(T result) {
        Response<T> response = new Response<T>();
        response.mResult = result;
        return response;
    }

    public static <T> Response<T> error(HttpError error) {
        Response<T> response = new Response<T>();
        response.mError = error;
        return response;
    }

    public static interface OnHttpResponseBeforeListener {
        public void onHttpResponseBefore(Request<?> request) throws HttpError;
    }

    public static interface OnHttpResponseAfterListener<T> {
        public void onHttpResponseAfter(Request<?> request, T response) throws HttpError;
    }

    public static interface OnHttpResponseListener<T> {
        public void onHttpResponse(Request<?> request, T response);
    }

    public static interface OnHttpResponseErrorListener {
        public void onHttpResponseError(Request<?> request, HttpError error);
    }
}
