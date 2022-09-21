package com.example.market_ceo.http;

import android.content.Context;
import android.util.Log;

import com.example.market_ceo.BuildConfig;
import com.example.market_ceo.constant.Define;
import com.example.market_ceo.utils.AndroidVersionHelper;
import com.example.market_ceo.utils.Utils;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpContent;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.apache.ApacheHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;

import com.example.market_ceo.utils.Task;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class HttpClient {

    private static volatile HttpClient sInstance = null;
    public static HttpClient getInstance(Context context) {
        if(sInstance == null) {
            synchronized(HttpClient.class) {
                if(sInstance == null) {
                    sInstance = new HttpClient(context);
                }
            }
        }
        return sInstance;
    }

    private static final String TAG = HttpClient.class.getSimpleName();

    private final Context mContext;
    private final HttpTransport mHttpTansport;
    private final HttpRequestFactory mHttpRequestFactory;
    private final String mUser;
    private static List<String> mCookies;


    private HttpClient(Context context) {
        java.util.logging.Logger.getLogger(HttpTransport.class.getName()).setLevel(BuildConfig.DEBUG ? java.util.logging.Level.ALL : java.util.logging.Level.SEVERE);

        mContext = context.getApplicationContext();
        mUser = mContext.getPackageName() + "/" + Utils.getVersionCode(mContext);
        if(AndroidVersionHelper.isOverGingerbread()) {
            final NetHttpTransport.Builder builder = new NetHttpTransport.Builder();
            try {
                builder.doNotValidateCertificate();
            } catch(Exception e) {
                e.printStackTrace();
            }
            mHttpTansport = builder.build();
        } else {
            final ApacheHttpTransport.Builder builder = new ApacheHttpTransport.Builder();
            try {
                builder.doNotValidateCertificate();
            } catch(Exception e) {
                e.printStackTrace();
            }
            mHttpTansport = builder.build();
        }
        mHttpRequestFactory = mHttpTansport.createRequestFactory(new HttpRequestInitializer() {
            @Override
            public void initialize(HttpRequest request) throws IOException {
                request.setSuppressUserAgentSuffix(true);
            }
        });
    }

    public static String getCookeis()
    {
        String steCookies = null;
        if(mCookies != null)
        {
            if(mCookies.size() > 0)
            {
                steCookies = mCookies.get(0).toString();
            }
        }
        return steCookies;
    }

    public HttpRequestFactory getRequestFactory() {
        return mHttpRequestFactory;
    }

    public void sendAsync(Request<?> request) {
        new RequestTask(request).execute();
    }

    public Response<?> send(Request<?> request) {
        if(Utils.isUiThread())
            throw new RuntimeException("call in not ui thread.");

        try {
            return new RequestTask(request).onExecute();
        } catch(Exception e) {
            return Response.error(new HttpError(e));
        }
    }

    private class RequestTask extends Task<Response<?>> {
        private Request<?> mRequest;
        public RequestTask(Request<?> request) {
            mRequest = request;
            setPriority(0);
        }

        private HttpRequest createRequest() throws Exception
        {
            final GenericUrl url = new GenericUrl(mRequest.getUrl());
            final String method = mRequest.getMethod();
            final String mediaType = mRequest.getMediaType();
            final Map<String, Object> parameters = mRequest.getParameters();
            final HttpContent content = mRequest.getBody();

            if(parameters != null)
            {
                for(Map.Entry<String, Object> entry : parameters.entrySet())
                {
                    url.set(entry.getKey(), entry.getValue());
                }
            }

            final HttpRequest httpRequest = mHttpRequestFactory.buildRequest(method, url, content);
            httpRequest.getHeaders().set("Host", url.getHost());
            if (mCookies != null)
            {
                httpRequest.getHeaders().set("Cookie", mCookies);
            }
            return httpRequest;
        }

        private void fireOnResponseBefore() throws HttpError  {
            mRequest.fireOnResponseBefore();
        }

        @SuppressWarnings({ "rawtypes", "unchecked" })
        private void fireOnResponseAfter(Response response) throws HttpError {
            mRequest.fireOnResponseAfter(response);
        }

        @SuppressWarnings({ "rawtypes", "unchecked" })
        private void fireOnResponse(Response response) {
            mRequest.fireOnResponse(response);
        }

        @Override
        protected Response<?> onExecute() {
            try {
                fireOnResponseBefore();

                final HttpRequest httpRequest = createRequest();
                final HttpResponse httpResponse = httpRequest.execute();

                try {
                    if (mRequest.getUrl().endsWith("_login.php"))
                    {
                        mCookies = (List<String>) httpResponse.getHeaders().get("Set-Cookie");
                        if (BuildConfig.DEBUG) {
                            Log.d(Define.TAG, "mCookies = " + mCookies.toString());
                        }
                    }
                    else if (mRequest.getUrl().endsWith("_logout.php"))
                        mCookies = null;
                }
                catch(Exception e){
                    e.printStackTrace();
                }

                final Response<?> response = mRequest.parseResponse(mContext, httpResponse);
                fireOnResponseAfter(response);
                return response;
            } catch(HttpError e) {
                Log.i(TAG, e.getMessage(), e);
                return Response.error(e);
            } catch(Exception e) {
                Log.e(TAG, e.getMessage());
                return Response.error(new HttpError(""));
            }
        }

        @Override
        protected void onPostExecute(Response<?> result) {
            super.onPostExecute(result);
            if(isCancelled() || mRequest.isCancelled())
                return;

            fireOnResponse(result);
        }
    }
}