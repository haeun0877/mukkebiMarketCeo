package com.example.market_ceo.http.request;

import android.content.Context;

import com.example.market_ceo.constant.Constant;
import com.example.market_ceo.http.HttpApiRequest;
import com.example.market_ceo.http.Response;
import com.example.market_ceo.member.MemberManager;
import com.google.api.client.http.HttpContent;
import com.google.api.client.http.HttpResponse;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RequestFindId extends HttpApiRequest<JSONObject> {

    private String mb_name;
    private String mb_email;


    public RequestFindId(Context context) {
        super(context);
    }

    public String getMb_name() {
        return mb_name;
    }

    public void setMb_name(String mb_name) {
        this.mb_name = mb_name;
    }

    public String getMb_email() {
        return mb_email;
    }

    public void setMb_email(String mb_email) {
        this.mb_email = mb_email;
    }

    @Override
    public String getMethod() {
        return "GET";
    }

    @Override
    public String getMediaType() {
        return null;
    }

    @Override
    public String getUrl() {
        return Constant.HTTP_API_BASE_URL + "cmk011_find_id.php";
    }

    @Override
    public Map<String, Object> getParameters() {
        Map<String, Object> params = new HashMap<>();
        params.put("return_type", "json");
        params.put("mb_name", mb_name);
        params.put("mb_email", mb_email);
        params = MemberManager.getInstance().setAccessInfo(params);
        params = MemberManager.getInstance().setCommonParamater(params);
        return params;
    }

    @Override
    public HttpContent getBody() throws Exception
    {
        return null;
    }

    @Override
    public Response<JSONObject> parseResponse(Context context, HttpResponse response) throws Exception {
        final JSONObject json = (JSONObject)parseJSONRespone(response);
        return Response.success(json);
    }

    @Override
    protected void saveResponseData(Response<JSONObject> response) {
        super.saveResponseData(response);
    }
}

