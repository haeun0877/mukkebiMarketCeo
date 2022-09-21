package com.example.market_ceo.http;

@SuppressWarnings("serial")
public class HttpError extends Exception {
    private int mStatusCode;

    public HttpError() {
        super();
    }

    public HttpError(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public HttpError(String detailMessage) {
        super(detailMessage);
    }

    public HttpError(Throwable throwable) {
        super(throwable);
    }

    public int getStatusCode() {
        return mStatusCode;
    }
}
