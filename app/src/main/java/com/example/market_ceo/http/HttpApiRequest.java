package com.example.market_ceo.http;

import android.content.Context;

import com.example.market_ceo.constant.Constant;
import com.google.api.client.http.ByteArrayContent;
import com.google.api.client.http.HttpContent;
import com.google.api.client.http.HttpResponse;

import org.json.JSONObject;

import java.net.URLDecoder;

public abstract class HttpApiRequest<T> extends Request<T> {

    public HttpApiRequest(Context context) {
        super(context);
    }

    public abstract HttpContent getBody() throws Exception;


    @Override
    public String getUrl() {
        return Constant.HTTP_API_BASE_URL;
    }

    @Override
    public String getMethod() {
        return "POST";
    }

    @Override
    public String getContentType() {
        return Constant.MIME_TYPE_JSON;
    }

    protected HttpContent createBody(String method, Object data) throws Exception {

        JSONObject body = (JSONObject) data;
        return new ByteArrayContent(Constant.MIME_TYPE_JSON, body.toString().getBytes("UTF-8"));
    }

    protected Object parseJSONRespone(HttpResponse response) throws Exception {
        String resultsss = response.parseAsString();

        try {
            //resultsss = URLDecoder.decode(resultsss, "utf-8");
            resultsss = resultsss;
        }
        catch(Exception e){
            e.printStackTrace();
        }
//		if (BuildConfig.DEBUG)
//			Log.e("xxx", "parseJSONRespone:" + resultsss);

        JSONObject json = null;
        String serverError = "";
        json = new JSONObject(resultsss);

        return json;//json.opt("d");
    }

    protected Object parseJSONResponeNoDecode(HttpResponse response) throws Exception {
        String resultsss = response.parseAsString();

//		if (BuildConfig.DEBUG)
//			Log.e("xxx", "parseJSONRespone:" + resultsss);

        JSONObject json = null;
        String serverError = "";
        json = new JSONObject(resultsss);
        serverError = json.optString("result");

        try {
            serverError = URLDecoder.decode(serverError, "utf-8");
        }
        catch (Exception e){
            e.printStackTrace();
        }

        if(serverError != null && !serverError.equals("ok")) {
            //
            //
        }
        return json;//json.opt("d");
    }

    protected static String checkStringValue(String value) {
        if(value == null || value.equalsIgnoreCase("null"))
            return "";
        return value;
    }
}
