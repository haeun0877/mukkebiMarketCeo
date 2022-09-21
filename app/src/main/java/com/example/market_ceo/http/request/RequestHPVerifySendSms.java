package com.example.market_ceo.http.request;

import android.content.Context;
import android.util.Log;

import com.example.market_ceo.constant.Constant;
import com.example.market_ceo.http.HttpApiRequest;
import com.example.market_ceo.http.Response;
import com.example.market_ceo.member.MemberManager;
import com.google.api.client.http.HttpContent;
import com.google.api.client.http.HttpResponse;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RequestHPVerifySendSms extends HttpApiRequest<JSONObject> {

    private String mb_hp;
    private String type; //tpye:1인 경우 / 등록된 휴대폰의 인증번호를 받는 경우(회원가입인 경우 아님X), 나머지는 빈값

    public RequestHPVerifySendSms(Context context) {
        super(context);
    }

    public String getMb_hp() {
        return mb_hp;
    }

    public void setMb_hp(String mb_hp) {
        this.mb_hp = mb_hp;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
        //return super.getUrl();
        return Constant.HTTP_API_BASE_URL + "cmk004_hp_send_certify.php";
    }

    @Override
    public Map<String, Object> getParameters() {
        Map<String, Object> params = new HashMap<>();
        params.put("return_type", "json");
        params.put("mb_hp", mb_hp);
        params.put("type", type);
        params = MemberManager.getInstance().setAccessInfo(params);
        params = MemberManager.getInstance().setCommonParamater(params);
        return params;
    }

    @Override
    public HttpContent getBody() throws Exception {
        return null;
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