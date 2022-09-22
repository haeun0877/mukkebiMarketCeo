package com.example.market_ceo.http.request;

import android.content.Context;

import com.example.market_ceo.constant.Constant;
import com.example.market_ceo.http.HttpApiRequest;
import com.example.market_ceo.http.Response;
import com.example.market_ceo.member.MemberManager;
import com.google.api.client.http.HttpContent;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.UrlEncodedContent;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RequestLeave extends HttpApiRequest<JSONObject> {

    private String mb_out_reason;
    private String mb_pass;


    public RequestLeave(Context context) {
        super(context);
    }

    public String getMb_out_reason() {
        return mb_out_reason;
    }

    public void setMb_out_reason(String mb_out_reason) {
        this.mb_out_reason = mb_out_reason;
    }

    public String getMb_pass() {
        return mb_pass;
    }

    public void setMb_pass(String mb_pass) {
        this.mb_pass = mb_pass;
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
        //return super.getUrl();
        return Constant.HTTP_API_BASE_URL + "cmk012_member_out.php";
    }

    @Override
    public Map<String, Object> getParameters() {
        return null;
    }

    @Override
    public HttpContent getBody() throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("return_type", "json");
        params.put("mb_out_reason", mb_out_reason);
        params.put("mb_pass", mb_pass);
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
