package com.example.market_ceo.http.request;

import android.content.Context;
import android.util.Log;

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

public class RequestLogin extends HttpApiRequest<JSONObject> {

    //아이디로그인
    private String mb_id;
    private String mb_pw;

    //휴대폰 로그인일때만 1로
    private String mb_hp;
    private String type; //휴대폰 로그인일때만 1

    private String auto_login;
    private String os_type;
    private String device_uuid;
    private String fcm_token;

    public RequestLogin(Context context) {
        super(context);
    }

    public String getMb_id() {
        return mb_id;
    }

    public void setMb_id(String mb_id) {
        this.mb_id = mb_id;
    }

    public String getMb_pw() {
        return mb_pw;
    }

    public void setMb_pw(String mb_pw) {
        this.mb_pw = mb_pw;
    }

    public String getMb_hp() {
        return mb_hp;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setMb_hp(String mb_hp) {
        this.mb_hp = mb_hp;
    }

    public String getAuto_login() {
        return auto_login;
    }

    public void setAuto_login(String auto_login) {
        this.auto_login = auto_login;
    }

    public String getOs_type() {
        return os_type;
    }

    public void setOs_type(String os_type) {
        this.os_type = os_type;
    }

    public String getDevice_uuid() {
        return device_uuid;
    }

    public void setDevice_uuid(String device_uuid) {
        this.device_uuid = device_uuid;
    }

    public String getFcm_token() {
        return fcm_token;
    }

    public void setFcm_token(String fcm_token) {
        this.fcm_token = fcm_token;
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
        return Constant.HTTP_API_BASE_URL + "cmk006_login.php";
    }

    @Override
    public Map<String, Object> getParameters() {
        return null;
    }

    @Override
    public HttpContent getBody() throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("return_type", "json");
        params.put("mb_id", mb_id);
        params.put("mb_pass", mb_pw);
        params.put("mb_hp", mb_hp);
        params.put("type", type);
        params.put("auto_login", auto_login);
        params.put("os_type", os_type);
        params.put("device_uuid", device_uuid);
        params.put("fcm_token", fcm_token);
        params = MemberManager.getInstance().setAccessInfo(params);
        params = MemberManager.getInstance().setCommonParamater(params);

        Log.e("haeun","params: "+params);
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