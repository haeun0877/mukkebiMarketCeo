package com.example.market_ceo.http.request;

import android.content.Context;

import com.example.market_ceo.constant.Constant;
import com.example.market_ceo.data.ShareData;
import com.example.market_ceo.http.HttpApiRequest;
import com.example.market_ceo.http.Response;
import com.example.market_ceo.member.MemberManager;
import com.google.api.client.http.HttpContent;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.UrlEncodedContent;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RequestLogout extends HttpApiRequest<JSONObject> {

    public RequestLogout(Context context) {
        super(context);
    }


    @Override
    public String getMethod() {
        return "POST";
    }

    @Override
    public String getMediaType() {
        return null;
    }

    @Override
    public String getUrl() {
        return Constant.HTTP_API_BASE_URL + "cmk007_logout.php";
    }

    @Override
    public Map<String, Object> getParameters() {
        return null;
    }

    @Override
    public HttpContent getBody() throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("return_type", "json");
        params.put("os_type", "A");
        params.put("device_uuid", ShareData.shared().getPfData(Constant.PREF_UUID));
        params = MemberManager.getInstance().setAccessInfo(params);
        params = MemberManager.getInstance().setCommonParamater(params);
        return new UrlEncodedContent(params);
    }

    @Override
    public Response<JSONObject> parseResponse(Context context, HttpResponse response) throws Exception {
        final JSONObject json = (JSONObject) parseJSONRespone(response);
        return Response.success(json);
    }

    @Override
    protected void saveResponseData(Response<JSONObject> response) {
        super.saveResponseData(response);
    }
}