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

public class RequestMydeliveryList extends HttpApiRequest<JSONObject> {

    private String mb_id;
    private String mb_idx;

    public RequestMydeliveryList(Context context) {
        super(context);
    }

    public String getMb_id() {
        return mb_id;
    }
    public void setMb_id(String mb_id) {
        this.mb_id = mb_id;
    }

    public String getMb_idx() {
        return mb_idx;
    }
    public void setMb_idx(String mb_idx) {
        this.mb_idx = mb_idx;
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
        return Constant.HTTP_API_BASE_URL + "cmk101_shop_delivery_list.php";
    }

    @Override
    public HttpContent getBody() throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("return_type", "json");
        params.put("mb_idx", mb_idx);
        params.put("mb_id", mb_id);
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