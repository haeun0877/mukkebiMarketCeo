package com.example.market_ceo.http.request;

import android.content.Context;
import android.text.TextUtils;

import com.example.market_ceo.constant.Constant;
import com.example.market_ceo.data.ShareData;
import com.example.market_ceo.http.HttpApiRequest;
import com.example.market_ceo.http.Response;
import com.example.market_ceo.member.MemberManager;
import com.google.api.client.http.HttpContent;
import com.google.api.client.http.HttpResponse;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RequestUserInfo extends HttpApiRequest<JSONObject> {

    private String fcm_token;
    private String access_id;
    private String access_token;
    private String uuid;
    private String version;

    public RequestUserInfo(Context context) {
        super(context);
    }

    public String getFcm_token() {
        return fcm_token;
    }

    public void setFcm_token(String fcm_token) {
        this.fcm_token = fcm_token;
    }

    public String getAccess_id() {
        return access_id;
    }

    public void setAccess_id(String access_id) {
        this.access_id = access_id;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
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
        return Constant.HTTP_API_BASE_URL + "cmk008_get_member_info.php";
    }

    @Override
    public Map<String, Object> getParameters() {
        Map<String, Object> params = new HashMap<>();
        params.put("return_type", "json");
        params.put("fcm_token", fcm_token);
        if (!TextUtils.isEmpty(uuid)) {
            params.put("uuid", uuid);
        }
        if (!TextUtils.isEmpty(version)) {
            params.put("version", version);
        }
        if (ShareData.shared() != null) {
            params = MemberManager.getInstance().setAccessInfo(params);
        } else {
            params.put("AccessID", access_id);
            params.put("AccessToken", access_token);
        }
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
        //Gson gson = new Gson();
        //UserInfo info = gson.fromJson(json.toString(), UserInfo.class);
        //return Response.success(info);
        return Response.success(json);
    }

    @Override
    protected void saveResponseData(Response<JSONObject> response) {
        super.saveResponseData(response);
    }
}